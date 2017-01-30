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
* [Dependencies](#dependencies)
* [Local Installation](#local-installation)
  + [Application](#application)
  + [Health Check](#health-check)
* [Testing the Service](#testing-the-service)
  + [Get Minimum Fleet Engineers](#get-minimum-fleet-engineers)
  + [Random Test](#random-test)
* [Calculations](#calculations)
* [License](#license)

<a name="dependencies"></a>
## Dependencies
This project was built using **Dropwizard 1.0.5** and **Java 1.0.8** on
**Mac OS 10.12.2**.

**Apache Maven (3.3.9)** is required to build and package
the application.

<a name="local-installation"></a>
## Local Installation

<a name="application"></a>
### Application
1. Run `mvn clean install` to build the application
2. Start application with `java -jar target/fleetmanager-0.0.1.jar server FleetManager.yml`
3. To check that the application is running enter url `http://localhost:8080`
  * Will return a 404 HTTP Response
4. Full API documentation can be found in target/apidocs

<a name="health-check"></a>
### Health Check
To see the application's health enter url `http://localhost:8081/healthcheck`

<a name="testing-the-service"></a>
## Testing the Service

<a name="get-minimum-fleet-engineers"></a>
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
```javascript
$.ajax({
    url: "/fleet&scooters=[15,10]&fmCapacity=12&feCapacity=5",
    dataType: "json",
    type : "GET",
    success : function(r) {
        console.log(r);
    }
});
```

<a name="random-test"></a>
### Random Test
* URL: **/fleet/test**
* Description: Generates a test case to check the calculations of the webservice.
* Method: `GET`
* Params: None
* Success Response: `{ "fleetEngineers": 3 }`
* Error Response: HTTP 400
* Sample call:
```javascript
$.ajax({
    url: "/fleet/test",
    dataType: "json",
    type : "GET",
    success : function(r) {
        console.log(r);
    }
});
```

<a name="calculations"></a>
## Calculations
The method is as follows:
* Select the best district for the FM to go to
  * For each district D, calculate the FE's required to cover that district, if the FM chooses it
  * Calculate the wastage in each case
    * (how many extra units could one of the FE's attended?)
    * (how many extra units could the FM have attended?)
  * Order the districts by wastage, and take those districts with the least wastage
* Calculate the minimum FE's required for each of the min wastage districts
  * Loop over the other districts &amp; calculate num FE's needed to cover
  * Add totals together, plus any FE's that are required to help the FM on their district
* Choose the FM district that results the minimum total FE's required
* Return num FE's required.

<a name="license"></a>
## License
[MIT](http://link.com)
