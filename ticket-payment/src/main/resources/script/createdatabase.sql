DROP DATABASE IF EXISTS ticketpayment;

CREATE DATABASE ticketpayment;

USE ticketpayment;

CREATE TABLE IF NOT EXISTS Occasion (
    OccasionId BIGINT PRIMARY KEY AUTO_INCREMENT,
    OccasionName VARCHAR(25) UNIQUE NOT NULL,
    OccasionTime DATETIME NOT NULL,
	NumberOfSeats INTEGER NOT NULL,
    InitialCost DOUBLE NOT NULL,
    TicketType VARCHAR(75) NOT NULL,
    IsActive BOOLEAN NOT NULL DEFAULT TRUE,
    OccasionAddress VARCHAR(170) NOT NULL)
;

ALTER TABLE Occasion AUTO_INCREMENT = 1000000001;

insert into Occasion(OccasionName, OccasionTime, NumberOfSeats, InitialCost, TicketType, IsActive,  OccasionAddress)
VALUES ('Concert Eric Clapton', '2023-11-22 22:10:00', 30, 10.00, 'CONCERT_CLUB', TRUE, 'Hard Rock Cafe London Picadilly Circus');


CREATE TABLE IF NOT EXISTS OccasionSeat (
    OccasionSeatId BIGINT PRIMARY KEY AUTO_INCREMENT,
    OccasionId BIGINT NOT NULL,
    Seat INTEGER NOT NULL,
    SeatPlaceType VARCHAR(20) NOT NULL,
    IsBooked BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (OccasionId) REFERENCES Occasion(OccasionId))
;

ALTER TABLE OccasionSeat AUTO_INCREMENT = 2000000001;

INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 1, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 2, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 3, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 4, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 5, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 6, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 7, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 8, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 9, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 10, 'CONCERT_CLUB_BALCONY', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 11, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 12, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 13, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 14, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 15, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 16, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 17, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 18, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 19, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 20, 'CONCERT_CLUB_VIP', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 21, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 22, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 23, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 24, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 25, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 26, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 27, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 28, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 29, 'CONCERT_CLUB_FAN', false);
INSERT INTO OccasionSeat(OccasionId, Seat, SeatPlaceType, IsBooked)
VALUES (1000000001, 30, 'CONCERT_CLUB_FAN', false);

CREATE TABLE IF NOT EXISTS TicketOrder (
    TicketOrderId BIGINT PRIMARY KEY AUTO_INCREMENT,
    BankAccount BIGINT NOT NULL,
    Currency VARCHAR(3) NOT NULL,
    IsPaid BOOLEAN NOT NULL DEFAULT FALSE,
    TicketType VARCHAR(30) NOT NULL)
;

ALTER TABLE TicketOrder AUTO_INCREMENT = 3000000001;

CREATE TABLE IF NOT EXISTS CustomerTicket (
    CustomerTicketId BIGINT PRIMARY KEY AUTO_INCREMENT,
	FirstName VARCHAR(25) NOT NULL,
	LastName VARCHAR(25) NOT NULL,
	Country VARCHAR(25) NOT NULL,
    Amount DECIMAL(15,2) NOT NULL,
    TicketOrderId BIGINT NOT NULL,
    OccasionSeatId BIGINT UNIQUE NOT NULL,
	FOREIGN KEY (TicketOrderId) REFERENCES TicketOrder (TicketOrderId),
	FOREIGN KEY (OccasionSeatId) REFERENCES OccasionSeat (OccasionSeatId)
);

ALTER TABLE CustomerTicket AUTO_INCREMENT = 4000000001;