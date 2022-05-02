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
[http://localhost:8080/](http://localhost:8080/)


<a name="testing"></a>
## Testing
Testing Pending.

<a name="database"></a>
## Database
[MySQL](https://www.mysql.com/).  
![ER Diagram](erdiag.drawio.png?raw=true "ER Diagram")

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

Note: For now deployment is done using the jar file, which can be generated using the command:
```bash
mvn clean package -DskipTests
```
After successful completion of this command, the jar file can be found in the target folder.

Can be deployed to the EC2 instance after with steps:  
* Connect to the EC2 instance using ssh with command:  
```bash
ssh -i ~/.ssh/amcrestunity_backend.pem ubuntu@107.22.33.225
```

* Stop the current running server with command:  
```bash
sudo kill -9 $(sudo lsof -t -i:$PORT_NUMBER)
```
$PORT_NUMBER: 8080

* Copy the created jar file to the EC2 instance with command:  
```bash
scp -i /home/$USER/.ssh/amcrestunity_backend.pem /home/$USER/dev/amcrest_unity/target/unity-0.0.1-SNAPSHOT.jar ubuntu@107.22.33.225:/home/ubuntu/
```
$USER: system username

* Host the jar file with command:
```bash
nohup java -jar unity-0.0.1-SNAPSHOT.jar &
```

<a name="possible-improvements"></a>
## Possible improvements
* NA
