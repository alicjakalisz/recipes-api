# Recipes API
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [API Contract](#api-contract)
* [Setup](#setup)

## General info
This project is a Spring API that represents classic REST API with **unit tests** and **integration test**. It stores and retrieves data about recipes from a database.

A client is able to get all recipes available in database, find a recipe by its id, update and delete a recipe.

## Public Endpoint Example
This Rest API has been deployed in AWS in an EC2 instance and using a MySQL RDS database.

```http://ec2-18-130-4-145.eu-west-2.compute.amazonaws.com:8080/weather/london```
## Technologies/Main Libraries
Project is created with:

* Maven
* Maven Profiles for local and production deployments
* Java 1.11
* Spring Boot
* Json version: 20210307
* Junit
* Gson version: 2.8.2
* MySQL db
* H2 db for testing  
* Mockito
* AWS: EC2, RDS and Security Groups

## API contract

Get all recipes available in the database:
###  HTTP GET /recipes
#### EXAMPLE OF HTTP 200 response:
```
[
{
"id": 1,
"name": "Spaguetti Bolognese",
"cuisine": "Italy",
"steps": "steps described here",
"ingredients": "tomatoes, olive oil, cheese, mince meat"
}
]
```


Get a recipe by id:
### HTTP GET /recipes/{id}
#### EXAMPLE OF HTTP 200 response:
```
{
    "id": 1,
    "name": "Spaguetti Bolognese",
    "cuisine": "Italy",
    "steps": "steps described here",
    "ingredients": "tomatoes, olive oil, cheese, mince meat"
}
```


Add a recipe to the database:
### HTTP POST /recipes

*BODY*:
```
{
   "name" : "Spaguetti Bolognese",
   "cuisine" : "Italy",
   "steps" : "steps described here",
   "ingredients" : "tomatoes, olive oil, cheese, mince meat"
}
```

#### EXAMPLE OF HTTP 201 response:

```
{
    "id": 1,
    "name": "Spaguetti Bolognese",
    "cuisine": "Italy",
    "steps": "steps described here",
    "ingredients": "tomatoes, olive oil, cheese, mince meat"
}
```

Update the chosen recipe:
### HTTP PUT /recipes{id}
*BODY*:
```
{
   "name" : "Spaguetti Bolognese",
   "cuisine" : "Italy",
   "steps" : "CHANGED steps described here",
   "ingredients" : "tomatoes, olive oil, cheese, mince meat"
}
```


#### EXAMPLE OF HTTP 200 response:

```
{
    "id": 1,
    "name": "Spaguetti Bolognese",
    "cuisine": "Italy",
    "steps": "CHANGED steps described here",
    "ingredients": "tomatoes, olive oil, cheese, mince meat"
}
```
Replace the chosen recipe:
### HTTP PATCH /recipes{id}

*BODY*:
```
{
   "name" : "Pizza",
   "cuisine" : "Italy",
   "steps" : "CHANGED steps described here",
   "ingredients" : "tomatoes, olive oil, cheese, mince meat"
}
```
#### EXAMPLE OF HTTP 200 response:
```
{
    "id": 1,
    "name": "Pizza",
    "cuisine": "Italy",
    "steps": "CHANGED steps described here",
    "ingredients": "tomatoes, olive oil, cheese, mince meat"
}
```
Delete the chosen recipe:
### HTTP DELETE /recipes{id}
#### EXAMPLE OF HTTP 200 response:
```
{
   "name" : "Spaguetti Bolognese",
   "cuisine" : "Italy",
   "steps" : "steps described here",
   "ingredients" : "tomatoes, olive oil, cheese, mince meat"
}
```
##### HTTP 204 (No Content)

## Setup
To run this project locally:
* H2 is created in memory by Spring (not required to do anything). In case you want a different db, add the driver in the pom.xml.
* Modify application.properties.dev with your Database credentials.
* Run it from IntellIJ or from command line:
  `mvn clean install -P dev`

## Deployment to Production
* Connect to remote db from your computer and create there a database "recipes".
* Modify application.properties.prod with the correct database connection of AWS RDS .
* Generate the jar for production:   
  
    `mvn clean install -P prod`
* Connect to Remote machine:
  
  `ssh -i ${location_pem}  ec2-user@ec2-18-130-4-145.eu-west-2.compute.amazonaws.com`

* Upload jar to remote machine:
  
  `scp -i ${location_pem} ${WORKSPACE_PATH}/recipes-api/target/recipes-api-0.0.1-SNAPSHOT.jar ec2-user@ec2-18-130-4-145.eu-west-2.compute.amazonaws.com:/home/ec2-user
  recipes-api-0.0.1-SNAPSHOT.jar`
  
* Run spring boot app in a remote machine:

  A.The process will be automatically killed after ending connection with EC2

  `java -jar recipes_api-0.0.1-SNAPSHOT.jar > output.txt &`
    * to kill it manually:

      `ps`

      `ps | grep java`

      `kill -9 process ref`

  B. Keep the process as a daemon (even after ending connection):
    * create a screen named recipes and run the program

      `screen -dmS recipes java -jar recipes_api-0.0.1-SNAPSHOT.jar > restaurants.log &
      `
    * to kill the process, list all the running processes and kill the screen recipes:

      `screen -ls
      `  
      `screen -X -S recipes quit`

* Ready to use GET endpoints





