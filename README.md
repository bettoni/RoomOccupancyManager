# Room Occupancy Manager
Simple application to optimize room occupancy based on room type and customer budget.

## Requirements
* JDK 11+

## Running this service
```bash
  >>./gradlew bootRun
```
## Running automated tests
```bash
  >>./gradlew clean test
```
## Testing manually
`run the service` and use a client (like curl)
```bash
  >>curl -X POST "http://localhost:8080/occupancy/optimize" -H  "accept: */*" -H  "Content-Type: application/json" -d "{\"customers\":[23,45,155,374,22,99,100,101,115,209],\"free_premium_rooms\":7,\"free_economy_rooms\":1}"
```
Or access Swagger-UI in http://localhost:8080/swagger-ui.html
  

