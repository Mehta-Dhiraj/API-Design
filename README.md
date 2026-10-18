# Weather API Project

Multi-module Maven project (Spring Boot 4.1, Java 17, PostgreSQL).

| Module | Purpose |
|---|---|
| `WeatherApiCommon` | Shared JPA entities (`Location`, `RealtimeWeather`, `HourlyWeather`, `HourlyWeatherId`) |
| `WeatherApiService` | REST APIs: locations, realtime weather, hourly forecast |

## Prerequisites

- Java 17 (check `.java-version`). Point `JAVA_HOME` at the intended JDK's explicit home path, e.g.
  `export JAVA_HOME=<path-to-intended-jdk>/Contents/Home` (list installed JDKs with `/usr/libexec/java_home -V`).
  Use an Intuit-approved OpenJDK distribution.
- Maven 3.6.3+
- PostgreSQL 12+ running on `localhost:5432` (steps below use a container)

## 1. Start PostgreSQL

If you already have a Postgres container/server on port 5432 (for example `postgres12` under Podman), skip to step 2.

```bash
podman run -d --name postgres12 -p 5432:5432 -e POSTGRES_PASSWORD=<admin-password> postgres:12
# or with Docker: docker run -d --name postgres12 -p 5432:5432 -e POSTGRES_PASSWORD=<admin-password> postgres:12
```

## 2. Create the database and user (one time)

Run these inside the container (replace `podman` with `docker` if needed). They only add a new database and role and
do not touch existing databases:

```bash
podman exec postgres12 psql -U postgres -c "CREATE ROLE weather_local LOGIN PASSWORD 'weather_local'"
podman exec postgres12 psql -U postgres -c "CREATE DATABASE weatherdb OWNER weather_local"
```

Verify the login:

```bash
PGPASSWORD=weather_local psql "host=localhost user=weather_local dbname=weatherdb gssencmode=disable" -c "select current_user"
```

These match `WeatherApiService/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/weatherdb
spring.datasource.username=weather_local
spring.datasource.password=weather_local
```

Tables (`locations`, `realtime_weather`, `weather_hourly`) are created automatically on first start
(`spring.jpa.hibernate.ddl-auto=update`).

## 3. Build

From the repo root (builds `WeatherApiCommon` first, then `WeatherApiService`):

```bash
mvn -DskipTests install
```

## 4. Run the service

Run from the **repo root**. The IP2Location database path is set in `application.properties`
(`ip2location.db-path=WeatherApiService/ip2locdb/IP2LOCATION-LITE-DB3.BIN`) and is relative to the working directory.

```bash
java -jar WeatherApiService/target/WeatherApiService-1.0.0.jar
```

The API listens on <http://localhost:8080>. In an IDE run configuration, set the working directory to the repo root.

## 5. Try the APIs

```bash
# Create a location
curl -i -X POST http://localhost:8080/v1/locations -H 'Content-Type: application/json' \
  -d '{"code":"DELHI_IN","city_name":"Delhi","region_name":"Delhi","country_code":"IN","country_name":"India","enabled":true}'

# Set hourly forecast for it
curl -X PUT http://localhost:8080/v1/hourly/DELHI_IN -H 'Content-Type: application/json' \
  -d '[{"hour_of_day":10,"temperature":30,"precipitation":20,"status":"Sunny"},{"hour_of_day":11,"temperature":32,"precipitation":10,"status":"Cloudy"}]'

# Read it back (X-Current-Hour is required; only hours after it are returned)
curl -i http://localhost:8080/v1/hourly/DELHI_IN -H 'X-Current-Hour: 9'

# Hourly forecast by caller IP (resolved with IP2Location)
curl -i http://localhost:8080/v1/hourly -H 'X-Current-Hour: 9'
```

| Endpoint | Description |
|---|---|
| `POST/GET/PUT /v1/locations`, `GET/DELETE /v1/locations/{code}` | Manage locations |
| `GET /v1/realtime`, `GET/PUT /v1/realtime/{locationCode}` | Realtime weather |
| `GET /v1/hourly`, `GET/PUT /v1/hourly/{locationCode}` | Hourly forecast (needs `X-Current-Hour` header on GET) |

## 6. Run the tests

The `*RepositoryTests` are `@DataJpaTest` tests that run against the real local database
(`replace = NONE`) and some expect existing rows (for example location `DELHI_IN` with hourly data). Start the
service once and run the `curl` calls in step 5 to create that data, then:

```bash
mvn test
```

The `*ControllerTest(s)` tests use mocks and need no database.

## Troubleshooting

- `password authentication failed`: re-check the role/password from step 2.
- `Connection refused` on 5432: the Postgres container is not running (`podman ps`).
- `FileNotFoundException: ip2locdb/IP2LOCATION-LITE-DB3.BIN`: the app was not started from the repo root; fix the
  working directory or set `ip2location.db-path` to the absolute path of the `.BIN` file.
- Port 8080 already in use: stop the other process, or run with `--server.port=8081`.
