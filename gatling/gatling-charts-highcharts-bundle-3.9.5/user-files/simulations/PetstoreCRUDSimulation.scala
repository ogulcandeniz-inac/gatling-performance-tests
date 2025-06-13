package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import java.util.UUID

class PetstoreCRUDSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://petstore.swagger.io/v2")
    .acceptHeader("application/json")

  val idFeeder = Iterator.continually(
    Map("petId" -> (UUID.randomUUID().getMostSignificantBits.abs % 1000000000L).toString)
  )
  val orderFeeder = Iterator.continually(
    Map("orderId" -> (UUID.randomUUID().getMostSignificantBits.abs % 100000L).toString)
  )

  val scn = scenario("Petstore – Extended CRUD, Status, Image & Order")
    .feed(idFeeder)
    // Create, Get, Update, Delete Pet (404’e izinli)
    .exec(http("Create Pet")
      .post("/pet")
      .body(StringBody("""{"id":#{petId},"name":"Pet-#{petId}","status":"available"}""")).asJson
      .check(status.is(200)))
    .pause(1)
    .exec(http("Get Pet")
      .get("/pet/#{petId}")
      .check(status.in(200, 404)))
    .pause(1)
    .exec(http("Update Pet")
      .put("/pet")
      .body(StringBody("""{"id":#{petId},"name":"Pet-#{petId}-Updated","status":"sold"}""")).asJson
      .check(status.is(200)))
    .pause(1)
    .exec(http("Delete Pet")
      .delete("/pet/#{petId}")
      .check(status.in(200, 204, 404)))
    .pause(1)

    // Find by Status
    .exec(http("Find Pets by Status")
      .get("/pet/findByStatus?status=sold")
      .check(status.is(200)))
    .pause(1)

    // Upload Pet Image (opsiyonel)
//  .exec(http("Upload Pet Image")
//    .post("/pet/#{petId}/uploadImage")
//    .bodyPart(RawFileBodyPart("file", "images/sample.jpg").fileName("sample.jpg").transferEncoding("binary"))
//    .asMultipartForm
//    .check(status.is(200)))
//  .pause(1)

    // Order Flow (Get/Delete de 404’e izinli)
    .feed(orderFeeder)
    .exec(http("Place Order")
      .post("/store/order")
      .body(StringBody(
        """{"id":#{orderId},"petId":#{petId},"quantity":1,
           "shipDate":"2025-06-14T00:00:00.000Z",
           "status":"placed","complete":true}"""
      )).asJson
      .check(status.is(200)))
    .pause(1)
    .exec(http("Get Order")
      .get("/store/order/#{orderId}")
      .check(status.in(200, 404)))
    .pause(1)
    .exec(http("Delete Order")
      .delete("/store/order/#{orderId}")
      .check(status.in(200, 204, 404)))

  setUp(
    scn.inject(atOnceUsers(5), rampUsers(20).during(30))
  ).protocols(httpProtocol)
   .assertions(
     global.successfulRequests.percent.gte(99),
     global.responseTime.mean.lt(1200)
   )
}
