# Fleet Manager

A simple webservice to calculate the most efficient allocation of engineers
to a fleet of rent-able scooters in Berlin.

The application has been deployed using Heroku at: https://obscure-depths-17216.herokuapp.com.

Example URL:
```
https://obscure-depths-17216.herokuapp.com/fleet?scooters=[15,10]&fmCapacity=12&feCapacity=5
```

Example Response:
```
{"fleetEngineers":3}
```

## Contents

## Dependencies
This project was built using **Dropwizard 1.0.5** and **Java 1.0.8** on
**Mac OS 10.12.2**. **Apache Maven (3.3.9)** is required to build and package
the application.

## Installation

### Application
1. Run `mvn clean install` to build the application
2. Start application with `java -jar target/fleetmanager-0.0.1.jar server FleetManager.yml`
3. To check that the application is running enter url `http://localhost:8080`
  * Will return a 404 HTTP Response
4. Full API documentation can be found in target/apidocs

### Health Check
To see the application's health enter url `http://localhost:8081/healthcheck`

## Testing the Service

### Get Minimum Fleet Engineers
* URL: **/fleet**
* Method: `GET`
* Param (Required): scooters=string
  * Array representing Berlin districts &amp; number of scooters within each
  * Example: scooters=[15,10]
* Param (Required): fmCapacity=integer
  * Integer representing the number of scooters the Fleet Manager can service
  * Example: fmCapacity=12
* Param (Required): feCapacity=integer
  * Integer representing the number of scooters a Fleet Engineer can service
  * Example: feCapacity=5
* Success Response: `{ "fleetEngineers": 3 }`
* Error Response: HTTP 400
* Sample call:
```
$.ajax({
    url: "/fleet&scooters=[15,10]&fmCapacity=12&feCapacity=5",
    dataType: "json",
    type : "GET",
    success : function(r) {
        console.log(r);
    }
});
```

### Random Test
* URL: **/fleet/test**
* Description: Generates a test case to check the calculations of the webservice.
* Method: `GET`
* Params: None
* Success Response: `{ "fleetEngineers": 3 }`
* Error Response: HTTP 400
* Sample call:
```
$.ajax({
    url: "/fleet/test",
    dataType: "json",
    type : "GET",
    success : function(r) {
        console.log(r);
    }
});
```

## Calculations

## License
