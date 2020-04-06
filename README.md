# Morbis Football Management System

[![pipeline status](https://gitlab.com/Zshoham/morbis/badges/master/pipeline.svg)](https://gitlab.com/Zshoham/morbis/-/commits/master)

[![codecov](https://codecov.io/gl/Zshoham/Morbis/branch/master/graph/badge.svg?token=9Rm5sKYMsQ)](https://codecov.io/gl/Zshoham/Morbis)


Authors: Shoham Zarfati, Yuval Khoramian, Hod Twito, Ron Zeidman, Haim Reyes

> This project is the assignment in the course - Information Systems Engineering-Project Preparation

### Project Structure:

* doc - the doc directory contains all the project documentation, specification and 
architecture.

* src - under com.morbis contains all the java source files separated into model, service
and presentation.

### Dependencies

The major project dependencies are jdk11 and gradle (6+ is preferred), 
in addition there are dependencies that are managed by gradle
those include:
 - Spring Boot
 - Spring Data JPA
 - Spring WebFlux (Netty)
 - lombok
 - h2 (debug database)
 
 ### Building From Sources
 
 Gradle makes the build process extremely easy to build the project 
 run `./gradlew build`, to run the test use `./gradlew test`
 for additional tasks run `./gradlew tasks`
 
 > Note that the generated jar can be found in build/libs