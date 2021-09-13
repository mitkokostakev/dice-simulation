# Dice Simulation service

### Requirement

Assignment 1: Create a Spring Boot application
Create a REST endpoint to execute a dice distribution simulation:
1. Roll 3 pieces of 6-sided dice a total of 100 times.

a. For every roll sum the rolled number from the dice (the result will be between 3 and 18).
b. Count how many times each total has been rolled.
c. Return this as a JSON structure.

2. Make the number of dice, the sides of the dice and the total number of rolls configurable through query
   parameters.

3. Add input validation:
   a. The number of dice and the total number of rolls must be at least 1.
   b. The sides of a dice must be at least 4.

Assignment 2: Store the result of the simulation from Assignment 1 in a database
Create a REST endpoint that can query the stored data:
1. Return the total number of simulations and total rolls made, grouped by all existing dice number–dice side
   combinations.

a. Eg. if there were two calls to the REST endpoint for 3 pieces of 6 sided dice, once with a total number of
rolls of 100 and once with a total number of rolls of 200, then there were a total of 2 simulations, with a
total of 300 rolls for this combination.

2. For a given dice number–dice side combination, return the relative distribution, compared to the total rolls, for all
   the simulations.
   a. In case of a total of 300 rolls, if the sum „3” was rolled 4 times, that would be 1.33%.
   b. If the sum „4” was rolled 3 times, that would be 1%.
   c. If the total „5” was rolled 11 times, that would be 3.66%. Etc...

i.e.
GET search by : numberOfDices = 3, sidesOfDice = 6

Response :
    {
    "totalRolls": 300,
    "totalSimulations": 2
    "relativeDistributions" :[
        {
        "sum": 3,
        "relativeDistribution": 1.33
        },
        {
        "sum": 4,
        "relativeDistribution": 1
        },
        {
        "sum": 5,
        "relativeDistribution": 3.66
        }
    ]
    }

### Solution : 

### Create a microservice to maintain all data related to rolling dice simulations providing a REST API

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
