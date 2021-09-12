# Dice Simulation service

### Microservice to maintain all data related to rolling dice simulations through a REST API

#### Build
To build project run the following command :
    
    gradle clean build

#### Dockerize
To run in Docker container do following steps : 
    
    docker build . -t dice-simulation
    docker run --name dice-simulation -d --restart unless-stopped -p 8080:8080 dice-simulation