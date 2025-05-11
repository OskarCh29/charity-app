# Charity fundraising event application

Application for managing collection boxes during fundraising events.

## Table of Contents

- [Requirements](#requirements)
- [Functionalities](#funcionalities)
- [Installation and configuration](#installation-and-configuration)
- [Documentation](#documentation)
- [Technologies](#technologies)
- [Testing](#testing)


## Requirements
- Java 21
- Spring boot 3.4.4 +
- Maven 3.9.4 +
- Postman - for testing purposes (Optional)

## Functionalities
Based on the requirements application gives access to the following functionalities:
- Creating a new fundraising event with duplicates validation. Each new fundraising event for a charity organization
is created with an empty charity account with chosen currency
- Box management:
   - Create a new empty fundraising event box (not assigned to any charity foundation)
   - List all available boxes—includes information if empty and if assigned
   - Register / Unregister box. Based on the requirements assign or unregister box to / from fundraising event
   - Remove box (deleting) in case it is damaged
   - Donate money inside the box in required currency and amount
   - Transfer money from a particular box to a charity bank account (including base account currency and exchanging other based on exchanging rates)
- Displaying a financial report with all fundraising events as a bank account balance

## Installation and configuration
1. Clone app repository to your local machine <br>
```shell
https://github.com/OskarCh29/charity-app.git
```

2. Application is integrated with `FreeCurrencyAPI` to provide real time exchange rates when transferring money between box and accounts.
Ensure that you provide free api-key.
3. After getting your api-key, navigate to the `application.yaml` file located in the `resources` folder. By the moment, key is provided as environmental viable from IDE.
- For safety, purpose provides api-key as environmental variable from IDE or as system variable. 
- Name of the environmental variable - `EXCHANGE_KEY`
4. Application uses flyway migration and H2 in a memory database. Application is prepared to work with basic currencies provided in `V2__add_currency.sql` file located in `resources/db/migration` folder. For deleting, inserting currencies modify this file.
5. When finished configuration runs the application with your IDE or with command window and maven:
```bash
mvn spring-boot:run
```
* Be aware that the application has an own validation system which prevents input invalid currencies. Migration file is determinate which currencies are valid for app environment.
## Documentation

The Functionality of the application is exposed on the following endpoints:<br>
 - `GET - /api/financial-report` - Generate a financial report
 - `POST - /api/event` - Create new fundraising event
 - `GET - /api/box` - Generate a available box status
 - `POST - /api/box` - Register (create) new box
 - `DELETE - /api/box/{boxId}` - Unregister (delete) a box
 - `PUT - /api/box/{boxId}/assign` - Register box to particular fundraising event
 - `PUT - /api/box/{boxId}/unregister` - Unregister (remove from fundraising event) a box
 - `PUT - /api/box/{boxId}/donate` - Donate a box
 - `POST - /api/box/{boxId}/transfer` - Transfer money from box to a fundraising account

Further information about a request type and fields are exposed in the documentation.
Documentation is exposed on `http://localhost:8080/api-docs` provided with swagger / openAPI functionalities after running application.

Response / Request are presented in JSON format as example below<br>

Response example for a financial report:
````JSON
[
  {
    "charityName": "WOSP",
    "balance": 932036.41,
    "currency": "PLN"
  },
  {
    "charityName": "#4Child",
    "balance": 220188.85,
    "currency": "EUR"
  }
]
````
Error response example:
````JSON
{
  "code": 404,
  "errorMessage": "Cannot generate financial report - no fundraising event found"
}
````

Donate request example:
````JSON
{
"amount" : "220000",
"currency" : "EUR"
}
````
## Technologies
- Java: The primary programming language for building backend logic
- Spring boot: A framework used to develop the RESTful application
- Maven: Used for dependency management and project build automation
- H2 database: In memory database for storing records
- Hibernate: Object-Relation-Mapping for relational databases
- Flyway: Responsible for database migrations

## Testing
The Application has almost 100% code coverage, according to a jacoco report.<br>
For testing use the following command:
````bash
mvn clean test
````
The project uses checkstyle validation. If there are any style error visit `target/checkstyle-result.xml` for more information and corrections.
Application also provides jacoco `.html` report. To generate report application cannot have any style validation.<br>
To generate report use the following command:
````bash
mvn clean verify
````
At the moment coverage is almost 100% as follows:
![Image](https://github.com/user-attachments/assets/e82c740e-82e7-44b3-8fa2-424dde1b4eb3)