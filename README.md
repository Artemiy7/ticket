SFG Beer Works - Brewery Client
#Ticket application to create pdf ticket with Qr-code from TicketOrder.
Spring Boot, Spring Cloud, Hibernate Jpa, Lombok, MySql.


ticket-payment:

script for manual creation:
ticket-payment/src/main/resources/createdatabase.sql

Endpoints:
![image](https://user-images.githubusercontent.com/83453822/215362672-ea8275e8-f65d-4c8d-9178-3bd01f3b059d.png)

Models:
![image](https://user-images.githubusercontent.com/83453822/215362706-991fdc49-fec8-44b1-b1f6-6757e429e7af.png)
![image](https://user-images.githubusercontent.com/83453822/215362733-1ef3357f-da74-47cf-b376-57e74bd49585.png)



pdf-generator:

Endpoints and Models:
![image](https://user-images.githubusercontent.com/83453822/215364006-91baeb2b-b56e-43eb-bf6f-a584665d80a1.png)



currency-exchange:

Endpoints and Models:
![image](https://user-images.githubusercontent.com/83453822/215364363-a69887ac-53a4-423f-a2ed-3dbe034a2e5b.png)




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

GET localhost:8084/ticket/PDF/3000000001

Pdf ticket example:

![image](https://user-images.githubusercontent.com/83453822/215364843-ccd59b68-43ab-443b-a734-fb157196122a.png)




