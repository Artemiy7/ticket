package net.ticket.transformer.ticketorder.customer;

import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.domain.entity.ticketorder.CustomerTicketEntity;
import net.ticket.transformer.Transformer;
import org.springframework.stereotype.Component;

@Component
public class CustomerEntityToCustomerDtoTransformer implements Transformer<CustomerTicketEntity, CustomerTicketDto> {
    @Override
    public CustomerTicketDto transform(CustomerTicketEntity customerTicketEntity) {
        return CustomerTicketDto.builder()
                                .country(customerTicketEntity.getCountry())
                                .firstName(customerTicketEntity.getFirstName())
                                .lastName(customerTicketEntity.getLastName())
                                .seat(customerTicketEntity.getOccasionSeatEntity().getSeat())
                                .amount(customerTicketEntity.getAmount())
                                .seatPlaceType(customerTicketEntity.getOccasionSeatEntity().getSeatPlaceType())
                                .customerTicketId(customerTicketEntity.getCustomerTicketId())
                                .build();
    }
}
