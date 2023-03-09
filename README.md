<h2 align="left">Ticket app that creates pdf ticket with QR-code</h2>
<h4 align="left">! The project is not yet completed and still is in the development phase.
In the next releases, it is planned to decompose the ticket-payment service into occasion-service and ticket-order-service and add a message broker for implementing a saga pattern. I also plan to separate qr-code generation into a separate service and add functionality to read QR-codes.
In addition, I plan to launch the project on AWS and replace CircleCi to Jenkins. !</h4>

<h3 align="left">Languages and Tools: </h3>
Spring Boot, Spring Cloud, Hibernate Jpa, Lombok, MySql.
<p align="left"> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="79" height="79"/> </a> 
<a href="https://spring.io/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="70" height="70"/> </a>
<a href="https://postman.com" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/hibernate/hibernate-icon.svg" alt="postman" width="70" height="70"/> </a> 
<a href="https://postman.com" target="_blank" rel="noreferrer"> <img src="https://github.com/vscode-icons/vscode-icons/blob/master/icons/folder_type_maven.svg" alt="postman" width="70" height="70"/> </a> 
<a href="https://postman.com" target="_blank" rel="noreferrer"> <img src="https://upload.vectorlogo.zone/logos/mockito/images/36c60459-46b2-46dd-87b7-5ed157df95d4.svg" alt="postman" width="110" height="70"/> </a>  
<a href="https://www.mysql.com/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original-wordmark.svg" alt="mysql" width="89" height="89"/> </a> </p>


| Service | Port  |  CI  |
| :---:   | :---: | :---:   |
| ticket-payment | 8084   | [![CircleCI](https://dl.circleci.com/status-badge/img/gh/Artemiy7/ticket/tree/master.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/Artemiy7/ticket/tree/master)  |
| pdf-generator | 8083   |  |
| currency-exchange | 8082   |  |
| bank-simulator | 8081   |  |

Hibernate JPA was used instead of Spring Data JPA for learning purpose. Before app launch you must have JDK 11 and MySql server installed on your machine.

<h3 align="left">ticket-payment:</h3>


Execute to create Database:
 	
	mvn ticket-payment [liquibase:update]


<h4 align="left">Endpoints:</h4>

![image](https://user-images.githubusercontent.com/83453822/224001257-e77e40ce-e6cc-445f-82c8-27f2ba412e05.png)


<h4 align="left">Models:</h4>

![image](https://user-images.githubusercontent.com/83453822/224001698-54d0937e-bba6-4a50-bde0-5f18a6400fb3.png)
![image](https://user-images.githubusercontent.com/83453822/224001805-5c5a6149-f1c1-4cfc-8b03-0eec0cdaa00f.png)
![image](https://user-images.githubusercontent.com/83453822/224001961-e2c962ac-d52b-4c6f-ba15-86d2d2f4bded.png)




Select occasion:

	GET localhost:8084//api/v1/occasion/1000000001


Result (reduced for readability):

![image](https://user-images.githubusercontent.com/83453822/224006378-3640a4b2-c8bb-4d23-84bd-b9cccba28987.png)




You can use filters and pagination to retrieve data you need:

	GET localhost:8084/api/v1/occasion/filters?TICKET_TYPE=TRAIN_INTERCITY&TICKET_TYPE=CONCERT_STADION&NOT_BOOKED_SEATS_FROM=30
	
		
	{
  	    "size": 3,
  	    "resultOrder": 3,
  	    "withOutdated": false,
  	    "sortingOrder": "ASC"
	}

Result:


![image](https://user-images.githubusercontent.com/83453822/224006795-400074d2-309c-4c2f-8bf6-bb0c225114db.png)



Fetch available filters:

	localhost:8084/api/v1/occasion/fetchFilters

Result:

![image](https://user-images.githubusercontent.com/83453822/224007965-59175b58-8b30-4cbf-8d34-af13884d5d30.png)


	



With selected Occasion you can create a pdf ticket with a qr-code. Default currency is "usd" but you can use another currency instead of "usd" for example "uah". In this case service will make a request to config-server and it will convert the currency you specified into "usd" according to the current exchange rate.
e.g.:

	POST localhost:8084/ticket/save


	{
	    "customerTicketDto": [{
		 "firstName": "firstName2",
		 "lastName" : "lastName2",
		 "country"  : "Fiji", 
		 "seat" : 10,
		 "amount" : "51.00",
		 "seatPlaceType" : "CONCERT_CLUB_BALCONY"
		}],
	    "ticketType" : "CONCERT_CLUB",
	    "occasionAddress" : "Hard Rock Cafe London Picadilly Circus",
	    "occasionDate" : "2023-11-22 22:10",
	    "occasionName" : "Concert - Eric Clapton",
	    "bankAccount": 2548870,
	    "currency" : "usd"
	}

Api will return created TicketOrderId. It can be used generate pdf tiket with QR-code:

	GET localhost:8084/ticket/PDF/3000000001
	
ticket-paymnet service will read TicketOrder from DB and will perform a request to pdf-generator to generate a pdf ticket for every Customer.

![image](https://user-images.githubusercontent.com/83453822/215364843-ccd59b68-43ab-443b-a734-fb157196122a.png)




<h3 align="left">pdf-generator:</h3>

<h4 align="left">Endpoints and Models:</h4>
![image](https://user-images.githubusercontent.com/83453822/224009913-ec0e07ce-810a-4d18-ab3c-105acbfb287d.png)




<h3 align="left">currency-exchange:</h3>

<h4 align="left">Endpoints and Models:</h4>

![image](https://user-images.githubusercontent.com/83453822/215364363-a69887ac-53a4-423f-a2ed-3dbe034a2e5b.png)






