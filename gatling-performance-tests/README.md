#  Gatling Performance Evaluation Suite – **Swagger Petstore**

> **Scientific-grade load-testing harness** engineered with [Gatling](https://gatling.io) 3.9.5 (stand-alone bundle) to produce **repeatable, statistically significant** latency and throughput measurements for the public Swagger Petstore REST interface.

---

## 1   Abstract
This repository provides an **experiment-oriented performance testbed** that exercises critical Petstore workflows under synthetic concurrency.

**Goals**

1. **Quantify capacity** of selected endpoints with well-defined KPIs (response-time percentiles, throughput, error rate).  
2. **Offer a reproducible methodology** (CI/CD ready, deterministic feeders, fixed random seeds).

---

## 2   Technical Stack

| Layer        | Technology             | Rationale                                                                          |
|--------------|------------------------|------------------------------------------------------------------------------------|
| Load Engine  | **Gatling 3.9.5**      | Asynchronous, event-driven, high-fidelity HTTP sampler with Scala DSL.             |
| Runtime      | **OpenJDK 17** (Temurin) | LTS JVM; stable GC behaviour across OSes.                                          |
| Protocol     | **HTTP 1.1 + JSON**     | Matches real-world client traffic; default Petstore dialect.                       |
| Statistics   | Gatling Metrics + Highcharts | Built-in percentiles, histograms & error breakdown for post-hoc analysis.          |

---

## 3   Experimental Design

### 3.1 Endpoints Under Test

| Group           | Method(s)                  | Path                                        | Purpose                              |
|-----------------|----------------------------|---------------------------------------------|--------------------------------------|
| **Inventory**   | `GET`                     | `/store/inventory`                          | Baseline read-only call (cacheable). |
| **Pet CRUD**    | `POST · GET · PUT · DELETE` | `/pet`, `/pet/{id}`                         | Full entity lifecycle (unique IDs).  |
| **Status Search** | `GET`                   | `/pet/findByStatus?status={s}`              | List fan-out to stress serialization.|
| **Image Upload**| `POST`                    | `/pet/{id}/uploadImage` (multipart)         | 50 KB payload transfer.              |
| **Order Flow**  | `POST · GET · DELETE`      | `/store/order`, `/store/order/{id}`         | Purchase simulation.                 |

### 3.2 Load Profile

| Phase   | Injector API              | VU  | Duration | Think-Time |
|---------|---------------------------|-----|----------|------------|
| Warm-up | `atOnceUsers(10)`         | 10  | —        | 1 s fixed  |
| Ramp    | `rampUsers(40).during(20)`| +40 | 20 s    | feeder-driven |
| Steady  | `constantUsersPerSec(5)`  | ≈5 VU/s | 30 s | Poisson (~1 s avg) |

### 3.3 Service-Level Objectives (SLO)

| KPI                        | Target             | Rationale                          |
|----------------------------|--------------------|------------------------------------|
| **Success Ratio**          | ≥ 99 %             | ≤ 1 % HTTP-level failures.          |
| **Mean Response Time**     | < 1200 ms          | Aligns with typical API P99 < 2 s. |
| **P95 Response Time**      | < 1500 ms          | Controls tail latency.             |
| **Throughput**             | ≥ 100 req/min      | Ensures generator isn’t bottleneck.|

> Assertions are embedded in Scala DSL → automatic pass/fail.

---

## 4   Repository Layout
```text
gatling-performance-tests/
├─ gatling/
│  └─ gatling-charts-highcharts-bundle-3.9.5/
│     ├─ bin/                    # gatling.sh, recorder.sh
│     ├─ user-files/
│     │   ├─ simulations/
│     │   │   ├─ PetstoreSimulation.scala        # inventory baseline
│     │   │   └─ PetstoreCRUDSimulation.scala    # full suite
│     │   └─ resources/images/sample.jpg         # 50 KB upload fixture
│     └─ results/   (git-ignored)
├─ .gitignore                  # excludes bundle & results
└─ README.md                   # this document
