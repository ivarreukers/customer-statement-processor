
# Getting Started

Clone this repository

Run `mvn clean package` to package the application into a jar file

Run `java -jar target/customer-statement-processor-0.0.1-SNAPSHOT.jar` to start the application server.

Swagger documentation is available at http://localhost:8080/swagger-ui.html

The implemented actions are:

200 - status `SUCCESSFUL` - with valid transactions

200 - status `INVALID_BALANCE` - with transactions containing invalid endbalance

200 - status `DUPLICATE_REFERENCE` - with transactions containing non-unique references

200 - status `INVALID_BALANCE_DUPLICATE_REFERENCE` - if the transaction list contained both transactions with invalid enbalance and transactions with non-unique references. 

400 - status `BAD_REQUEST` - in case of invalid json or non-valid transaction fields

500 - status `INTERNAL_SERVER_ERROR` - in case of any other exception 


Sample requests are included in `src/test/resources/validate-transactions`

---

Unit tests are present. 

Integration test is present `nl.rabobank.ivarreukers.customerstatementprocesssor.web.StatementProcessorControllerIT`