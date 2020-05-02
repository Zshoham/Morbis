# Morbis Football Management System

[![pipeline status](https://gitlab.com/Zshoham/morbis/badges/master/pipeline.svg)](https://gitlab.com/Zshoham/morbis/-/commits/master)

[![codecov](https://codecov.io/gl/Zshoham/Morbis/branch/master/graph/badge.svg?token=9Rm5sKYMsQ)](https://codecov.io/gl/Zshoham/Morbis)

[![Traceabillity Matrix](https://img.shields.io/badge/-traceability--matrix-informational)](https://docs.google.com/spreadsheets/d/16uEsQn1krlVqtcKuEiSGGFGlWQyg7g0BaGvJhJdVOWc/edit?ts=5e9c2063#gid=0)


Authors: Shoham Zarfati, Yuval Khoramian, Hod Twito, Ron Zeidman, Haim Reyes

> This project is the assignment in the course - Information Systems Engineering-Project Preparation

### Project Structure:

* doc - the doc directory contains all the project documentation, specification and 
architecture.

* src - under com.morbis contains all the java source files separated into model, service
and presentation.

* webapp - contains the web front-end of the application - a vue app, which compiles 
to static html and javascript files that are later placed in src/main/resources/static
to be served by the server. 

### Dependencies

The major project dependencies are jdk11 and gradle (6+ is preferred), 
in addition there are dependencies that are managed by gradle
those include:
 - Spring Boot
 - Spring Data JPA
 - Spring Web (Tomcat)
 - lombok
 - h2 (debug database)

 For the front-end the project uses npm for dependency managment
 and the application itself is written in vue.js and has the following
 major dependencies:
  - babel
  - cypress (for e2e testing)
  - jest (for unit testing)
  - vue raouter
 
### Building From Sources

* back-end: 
	Gradle makes the build process extremely easy to build the project 
	run `./gradlew build` (`./gradlew build` to generate a jar),
	to run the test use `./gradlew test`
	for additional tasks run `./gradlew tasks`
	> Note that the generated jar can be found in build/libs
	
* front-end:
	The build is managed by npm, you may run `npm run serve` for 
	a dev version of the app to launch, or `npm run build` to compile
	the vue app into static html and javascript files.

* packeging:
	There is a pack.py script inside the scripts folder that will automate 
	the build stages descrived above and also copy the static files generated 
	from the webapp into thier place inside the server. The script will generate
	a release folder with a jar file and a run.py script.
