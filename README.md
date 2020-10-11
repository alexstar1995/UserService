# UserService

## 1. Configure the environment

a) Make sure you have Java 11 installed (from openjdk)
b) Make sure you have Docker installed
c) Make sure you have Maven installed
d) Make sure you have Git installed

## 2. Download the project

In a directory on your local filesystem run the command: git clone git@github.com:alexstar1995/UserService.git
  
## 3. Run the project
a) In the local directory where you cloned the Git repo from step 2, go to UserService directory
b) Run the command: mvn clean install -U && docker-compose up
c) Wait for 2-3 minutes, the last line in the log should contain "Started UserServiceApplication in ..."
d) Open your browser and go to http://localhost:8088/swagger-ui/index.html

## 4. Use the application

a) The Swagger-UI exposes the CRUD operations that UserService supports under 'user-controller'section
b) You can also see the model under the 'models'

## 5. More about the app
a) It uses Spring-Webflux application and a Postgres Database, that run in 2 separate docker containers. 
b) For the testing environment it uses an H2 database.
c) The database is being generated by a Liquibase script everytime the app starts.

## 6. Todo next
 a) Add Spring Security
 b) Improve the model and split the entities in multiple tables
 b) More unit tests
 c) Add external property configurations