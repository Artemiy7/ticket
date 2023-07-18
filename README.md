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



![image](https://github.com/Artemiy7/ticket/assets/83453822/5a06393f-cd39-4a71-b294-4e7083902746)



<h4 align="left">Models:</h4>

![image](https://github.com/Artemiy7/ticket/assets/83453822/1803fa20-46c4-4977-a3bc-370caa8d0919)
![image](https://github.com/Artemiy7/ticket/assets/83453822/9b9c68bb-b950-49b7-a365-83ef3c159597)




Within selection, the api will calculate the ticket price according to the seat and date.


	GET localhost:8084/api/v1/occasions/1000000001


Result (reduced for readability):

![image](https://user-images.githubusercontent.com/83453822/224006378-3640a4b2-c8bb-4d23-84bd-b9cccba28987.png)




You can use filters and pagination to retrieve data you need:

	GET localhost:8084/api/v1/occasions?ticket_type=TRAIN_INTERCITY&ticket_type=CONCERT_STADION&occasion_seats_from=30&size=3&page=0&sorting_order=DESC&sorting_field=occasionId
	

Result:


![image](https://github.com/Artemiy7/ticket/assets/83453822/a739ad83-0140-4a14-99ed-9aee5bd7f9ae)



Fetch available filters:

	localhost:8084/api/v1/occasions/filters

Result:


![image](https://github.com/Artemiy7/ticket/assets/83453822/d43a11e1-f599-41a2-8ca6-27de15240875)



With selected Occasion you can create a pdf ticket with a qr-code. Default currency is "usd" but you can use another currency instead of "usd" for example "uah". In this case service will make a request to currency-exchange server and it will convert the currency you specified into "usd" according to the current exchange rate.
e.g.:

	POST localhost:8084/tickets


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

	GET localhost:8084/api/v1/tickets/PDF/3000000001
	
ticket-payment service will read TicketOrder from DB and will perform a request to pdf-generator to generate a pdf ticket for every Customer.

![image](https://user-images.githubusercontent.com/83453822/215364843-ccd59b68-43ab-443b-a734-fb157196122a.png)




<h3 align="left">pdf-generator:</h3>

<h4 align="left">Endpoints and Models:</h4>

![image](https://user-images.githubusercontent.com/83453822/224009913-ec0e07ce-810a-4d18-ab3c-105acbfb287d.png)




<h3 align="left">currency-exchange:</h3>

<h4 align="left">Endpoints and Models:</h4>

![image](https://user-images.githubusercontent.com/83453822/224015518-75a44bb6-bdee-4697-81ae-0f4a147d2f47.png)






