# Gatling Performance Tests – Petstore API

This project demonstrates how to use **Gatling** to perform a load test on the [Swagger Petstore API](https://petstore.swagger.io).  
It’s a minimal, production-ready simulation setup focused on the `/store/inventory` endpoint using Gatling's standalone bundle.

---

## Key Features
| Item | Details |
|------|---------|
| **Tooling** | Gatling 3.9.5 (self-contained bundle, no build tool required) |
| **JDK** | Temurin 17 (OpenJDK 17) |
| **Scenario** | 50 virtual users → `GET /store/inventory` |
| **Assertions** | ≥ 99 % success rate · ≤ 800 ms mean response time |
| **Report** | Automatic HTML report under `results/…/index.html` |

---




## Project Structure

```
gatling-performance-tests/
├── gatling/
│   └── gatling-charts-highcharts-bundle-3.9.5/
│       ├── bin/
│       │   └── gatling.sh
│       └── user-files/
│           └── simulations/
│               └── PetstoreSimulation.scala
├── .gitignore
└── README.md
```

---

## Installation & Setup

```bash
# Setup project folder
mkdir gatling-performance-tests && cd gatling-performance-tests

# Download Gatling
curl -L https://repo1.maven.org/maven2/io/gatling/highcharts/gatling-charts-highcharts-bundle/3.9.5/gatling-charts-highcharts-bundle-3.9.5-bundle.zip -o gatling.zip

# Extract files and cleanup
unzip gatling.zip -d gatling
rm gatling.zip

# Navigate to simulations folder
cd gatling/gatling-charts-highcharts-bundle-3.9.5/user-files/simulations

# Create simulation file
touch PetstoreSimulation.scala
```

---

## Running Tests

Execute the test:

```bash
cd ~/gatling-performance-tests/gatling/gatling-charts-highcharts-bundle-3.9.5/bin
./gatling.sh
```

Select `simulations.PetstoreSimulation` when prompted.

---

## Viewing Reports

Open HTML reports:

```bash
open ../results/*/index.html
```

---

## Additional Information

- [Gatling Documentation](https://gatling.io/docs/)
- [Swagger Petstore API](https://petstore.swagger.io/)
