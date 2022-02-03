##### Table of Contents
* [Description](#description)  
* [Assumptions](#assumptions)  
* [Dependencies](#dependencies)  
* [Instructions to run](#instructions-to-run)  
* [Documentation](#documentation)  
* [Testing](#testing)  
* [Database](#database)  
* [Logging/Monitoring](#loggingmonitoring)  
* [Hosting](#hosting)  
* [Deployment](#deployment)  
* [Possible improvements](#possible-improvements)  

<a name="description"></a>
## Description
Amcrest Unity backend service based on spring boot.

<a name="assumptions"></a>
## Assumptions
* NA

<a name="dependencies"></a>
## Dependencies
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/)

<a name="instructions-to-run"></a>
## Instructions to run
1. Open project in any IDE as a maven project. And run the main file:
```bash
src/test/java/com/amcrest/unity/AmcrestApplicationTests.java
``` 

<a name="documentation"></a>
## Documentation
OpenAPI Specification Swagger documentation is at the default page.
[http://localhost:8080/]


<a name="testing"></a>
## Testing
Testing Pending.

<a name="database"></a>
## Database
[MySQL](https://www.mysql.com/).

<a name="loggingmonitoring"></a>
## Logging/Monitoring
[Lombok](https://projectlombok.org/) Slf4j logging is used for the ease of implementation.

<a name="hosting"></a>
## Hosting
This solution will be dockerized to provide easy hosting service.

<a name="deployment"></a>
## Deployment
Since, the service is a containerized solutions we can easily create a CI/CD pipeline using [Jenkins](https://www.jenkins.io/)
or [GitLab](https://docs.gitlab.com/ee/ci/) and create a dockerized image which then can further be deployed.

<a name="possible-improvements"></a>
## Possible improvements
* NA
