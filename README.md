# Dice Simulation service

### Microservice to maintain all data related to rolling dice simulations through a REST API

#### Prerequisites : 
To properly run the service please make sure that you have the following tools installed : 

    1.JDK 11 and above
    2.Gradle 6 and above
    3.Docker
    4.Docker-compose

#### Build
To build project run the following command :
    
    gradle clean build

#### Dockerize
When you have all the tools installed, next step would be to start all containers

    docker-compose up -d

#### REST API
REST Api specification could be accessed at http://localhost:8080/swagger-ui/index.html

To create a new Rolling Dice Simulation

    i.e. curl -iX POST "http://localhost:8080/api/v1/simulations?numberOfDices=3&numberOfRolls=100&sidesOfDice=6" -H  "accept: application/json"

