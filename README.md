<h3 align="left">Ticket application to create pdf ticket with Qr-code from TicketOrder</h3>
<h3 align="left">Languages and Tools:</h3>

<h3 align="left">Spring Boot, Spring Cloud, Hibernate Jpa, Lombok, MySql.</h3>
<p align="left"> <a href="https://www.java.com" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/java/java-original.svg" alt="java" width="40" height="40"/> </a> 
<a href="https://spring.io/" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/springio/springio-icon.svg" alt="spring" width="40" height="40"/> </a>
<a href="https://postman.com" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/hibernate/hibernate-icon.svg" alt="postman" width="40" height="40"/> </a> 
<a href="https://www.mysql.com/" target="_blank" rel="noreferrer"> <img src="https://raw.githubusercontent.com/devicons/devicon/master/icons/mysql/mysql-original-wordmark.svg" alt="mysql" width="40" height="40"/> </a>
<a href="https://postman.com" target="_blank" rel="noreferrer"> <img src="https://www.vectorlogo.zone/logos/getpostman/getpostman-icon.svg" alt="postman" width="40" height="40"/> </a> </p>

| Service | Port  |
| :---:   | :---: |
| ticket-payment | 8084   |
| pdf-generator | 8083   |
| currency-exchange | 8082   |
| bank-simulator | 8081   |


<h3 align="left">! The project is not yet completed and still is in the development phase !</h3>

<h3 align="left">ticket-payment:</h3>

Before app launch execute:
 	
	mvn ticket-payment [liquibase:update]


<h4 align="left">Endpoints:</h4>

![image](https://user-images.githubusercontent.com/83453822/215362672-ea8275e8-f65d-4c8d-9178-3bd01f3b059d.png)


<h4 align="left">Models:</h4>

![image](https://user-images.githubusercontent.com/83453822/215362706-991fdc49-fec8-44b1-b1f6-6757e429e7af.png)
![image](https://user-images.githubusercontent.com/83453822/215362733-1ef3357f-da74-47cf-b376-57e74bd49585.png)



Get occasion:

	GET localhost:8084/occasion/getOccasionById/1000000001



Fetch available filters

	GET localhost:8084/fetchOccasionFilters



Filter occasion example:

	GET localhost:8084/occasion/filterOccasion?TICKET_TYPE=CONCERT_CLUB&TICKET_TYPE=CONCERT_STADION&NOT_BOOKED_SEATS_FROM=1000



Ticket-order creation example:

	POST localhost:8084/ticket/save


	{
	    "customerTicketDto": [{
		 "firstName": "firstName2",
		 "lastName" : "lastName2",
		 "country"  : "Ukraine", 
		 "seat" : 10,
		 "amount" : "51.00",
		 "seatPlaceType" : "CONCERT_CLUB_BALCONY"
		}],
		"ticketType" : "CONCERT_CLUB",
	    "occasionAddress" : "Hard Rock Cafe London Picadilly Circus",
	    "occasionDate" : "2023-11-22 22:10",
	    "occasionName" : "Concert Eric Clapton",
	    "bankAccount": 2548870,
	    "currency" : "uah"
	}



Get pdf tiket from ticket-order example:

	GET localhost:8084/ticket/PDF/3000000001

<h4 align="left">Generated result from TicketOrderDto where for every CustomerTicketDto printed pdf-ticket:</h4>


![image](https://user-images.githubusercontent.com/83453822/215364843-ccd59b68-43ab-443b-a734-fb157196122a.png)




<h3 align="left">pdf-generator:</h3>

<h4 align="left">Endpoints and Models:</h4>

![image](https://user-images.githubusercontent.com/83453822/215364006-91baeb2b-b56e-43eb-bf6f-a584665d80a1.png)




<h3 align="left">currency-exchange:</h3>

<h4 align="left">Endpoints and Models:</h4>

![image](https://user-images.githubusercontent.com/83453822/215364363-a69887ac-53a4-423f-a2ed-3dbe034a2e5b.png)






