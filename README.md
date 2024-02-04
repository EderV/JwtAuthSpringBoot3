# Authorization with JWT in Spring Framework 6

This project aims to perform user authorization using JsonWebToken using Spring Boot 3 & Spring Security 6

## How to run ▶️
* Run `docker-compose up -d` on root project
* Create a database named `testauthdb`
* Create all the tables inside the database using the querys in `<project_root>/SQL_tables_querys.txt` file
* Run `./mvnw spring-boot:run` on root project

## Features 🔬
### General
* Spring Security 6. There is a big change in security filter chain between SS5 and SS6
* Docker compose file for the database
* Uses relational database to store user related data
  * Users table
  * Roles table
  * Access token table
  * Refresh token table
* Tokens expiration is configurable in application.yml file
* Simple API
### Endpoints
* Public
  * POST `/api/auth/register`
    * body:
    ```
    {
        "enmail":string,
        "username":string,
        "password":string
    }
    ```
  * POST `/api/auth/login`
      * body:
    ```
    {
        "username":string,
        "password":string
    }
    ```
  * POST `/api/auth/token`
      * body:
    ```
    {
        "userId":number,
        "accesssToken":string,
        "refreshToken":string
    }
    ```
* Authenticated user with any role
  * GET `/api/test`
    * Response: `Hello <username>`
* Authenticated user with ADMIN role
  * GET `/api/test/role`
    * Response: `Test OK`