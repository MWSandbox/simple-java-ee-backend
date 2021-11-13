# Simple Java REST Backend <!-- omit in toc -->

This project contains a simple Java REST backend connected to a database that can be used to test your architecture setup (not production ready!). Docker & Docker-Compose are being used to make the installation easy. This project can be used as it is to test two-tier-architectures (app + db), but can be extended to a three-tier-architecture by providing a frontend that is connected to the backend via REST API. A simple frontend to play around is currently in development and will be released in a different repository.

![Architecture Diagram](./docs/resources/architecture-diagram.png)

1. [Tech-Stack](#tech-stack)
2. [Developer Instructions](#developer-instructions)
   1. [Prerequisites](#prerequisites)
   2. [How to get started](#how-to-get-started)
   3. [Useful commands](#useful-commands)

## Tech-Stack

| Category             | Technologies           |
| :------------------- | :--------------------- |
| Build Tool           | Maven                  |
| Programming Language | Java                   |
| Database             | PostgreSQL             |
| Appserver            | Payara Full            |
| Containerization     | Docker, Docker-Compose |

## Developer Instructions

### Prerequisites
- Docker & Docker-Compose installed
- Java & Maven installed

### How to get started
1. Run `mvn clean install -P redeploy`. This will trigger the following process:
   1. Run `docker-compose down -d` to remove existing containers (app + db) and the docker network
   2. Build new docker image `com.mdevoc/simple-java-rest-backend` and tag it with the current version number. The docker image is based on the Payara Full server images and adds the WAR file to the server. Previously built images with this tag will be removed.
   3. Use `docker-compose up -d` to run new containers
      1. Setup a network so appserver and database container can communicate
      2. Run appserver based on previously built image
      3. Runn database base on official PostgreSQL image
2. Open Postman and import the collection from `docs/resources/postman-collection.json`
3. Call `PUT /schema` to generate the database schema based on the Java entity definitions
4. Create your first counter by calling `POST /counter` (provide the counter json in the request body)
5. You can open your browser and call `http:localhost:8080/simple-java-rest-backend/rest/counter` to view the result in your webbrowser (GET against all counter objects)
6. Manipulate the counter objects using Postman

### Useful commands

| Task                            | Command                                                                                                                           |
| :------------------------------ | :-------------------------------------------------------------------------------------------------------------------------------- |
| Build Docker Image              | `mvn clean install`                                                                                                               |
| Build Docker Image, Clean & Run | `mvn clean install -P redeploy`                                                                                                   |
| Cleanup containers & network    | `mvn clean -P shutdown`                                                                                                           |
| Run containers                  | `mvn install -P run`                                                                                                              |
| Start Postgres DB manually      | `docker run --name postgres -e POSTGRES_USER=[user] -e POSTGRES_PASSWORD=[password] -e POSTGRES_DB=[db] -p 5432:5432 -d postgres` |
| View logfile from appserver     | `docker logs --tail=[number of lines] [container name]`                                                                           |
