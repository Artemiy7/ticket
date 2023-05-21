package net.ticket.service.ticketorder;

import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.ticketexception.bank.BankServerError;
import net.ticket.ticketexception.bank.InvalidBankAccount;
import net.ticket.ticketexception.bank.NoSuchBankAccount;
import net.ticket.ticketexception.bank.NotEnoughAmountForPayment;
import net.ticket.ticketexception.occasion.NoSuchOccasionException;
import net.ticket.ticketexception.occasion.NoSuchOccasionSeatException;
import net.ticket.ticketexception.occasion.OccasionSeatIsBookedException;
import net.ticket.ticketexception.ticketorder.NoSuchTicketOrderEntityException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.NoResultException;

public interface TicketOrderService {
    long createTicket(TicketOrderDto ticketOrderDto) throws NoResultException, NoSuchOccasionException,
            OccasionSeatIsBookedException, NoSuchBankAccount, BankServerError, NotEnoughAmountForPayment, NoSuchOccasionSeatException, InvalidBankAccount;

    ResponseEntity<byte[]> generatePdf(long ticketOrderId) throws NoSuchTicketOrderEntityException, NullPointerException, HttpServerErrorException;
}
