# opeanapi-test
This is openapi-test project.  This project hosts the Invoice Rest api that is defined in the `invoice-openapi.yaml` 
OpenAPI specification.

This project is written in Java using Spring WebFlux using Reactive programming.
This project requires a local instance of Postgres database `invoice` with the credentials
of username with `test` and password with `test` as defined in the run command below:

```
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081 \
    --POSTGRES_USERNAME=test \
    --POSTGRES_PASSWORD=test \
    --POSTGRES_DBNAME=invoice \
    --POSTGRES_SERVICE=localhost:5432
    --DB_SSLMODE=disable"
    
```