package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class PetstoreSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("https://petstore.swagger.io/v2")
    .acceptHeader("application/json")

  val scn = scenario("Petstore – Inventory")
    .exec(
      http("Get Inventory")
        .get("/store/inventory")
        .check(status.is(200))
    )
    .pause(1)

  setUp(
    scn.inject(
      atOnceUsers(10),
      rampUsers(40).during(20)
    )
  ).protocols(httpProtocol)
   .assertions(
     global.successfulRequests.percent.gte(99),
     global.responseTime.mean.lt(800)
   )
}
