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
    * Body:
    ```
    {
        "enmail":string,
        "username":string,
        "password":string
    }
    ```
    * Response
      * `200` if user is new
      * `400` if body is null
      * `401` if user exists
  * POST `/api/auth/login`
      * Body:
    ```
    {
        "username":string,
        "password":string
    }
    ```
    * Response
      * `200` if user and password are correct
        * Body: `{"userId":number,"accesssToken":string,"refreshToken":string}`
      * `400` if body is null
      * `401` if user and password are not correct
  * POST `/api/auth/token`
      * Body:
    ```
    {
        "userId":number,
        "accesssToken":string,
        "refreshToken":string
    }
    ```
    * Response
      * `200` if userid and refresh token is correct and not expired
        * Body: `{"userId":number,"accesssToken":string,"refreshToken":string}`
      * `400` if body is null
      * `401` if refresh token is expired
* Authenticated user with any role
  * GET `/api/test`
    * Response: `Hello <username>`
* Authenticated user with ADMIN role
  * GET `/api/test/role`
    * Response: `Test OK`