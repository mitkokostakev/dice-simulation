# Dice Simulation service

### Microservice to maintain all data related to rolling dice simulations through a REST API

#### Build
To build project run the following command :
    
    gradle clean build

#### Dockerize
To run in Docker container do following steps : 
    
    docker build . -t dice-simulation
    docker run --name dice-simulation -d --restart unless-stopped -p 8080:8080 dice-simulation

#### REST API
REST Api specification could be accessed at http://localhost:8080/swagger-ui/index.html

To create a new Rolling Dice Simulation

    i.e. curl -iX POST "http://localhost:8080/api/v1/simulations?numberOfDices=3&numberOfRolls=100&sidesOfDice=6" -H  "accept: application/json"

