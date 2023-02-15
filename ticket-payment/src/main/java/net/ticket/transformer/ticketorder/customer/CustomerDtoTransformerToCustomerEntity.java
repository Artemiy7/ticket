package net.ticket.transformer.ticketorder.customer;

import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.entity.ticketorder.CustomerTicketEntity;
import net.ticket.transformer.Transformer;
import org.springframework.stereotype.Component;

@Component
public class CustomerDtoTransformerToCustomerEntity implements Transformer<CustomerTicketDto, CustomerTicketEntity> {
    @Override
    public CustomerTicketEntity transform(CustomerTicketDto customerTicketDto) {
        return CustomerTicketEntity.builder()
                                   .country(customerTicketDto.getCountry())
                                   .firstName(customerTicketDto.getFirstName())
                                   .lastName(customerTicketDto.getLastName())
                                   .amount(customerTicketDto.getAmount())
                                   .occasionSeatEntity(customerTicketDto.getOccasionSeat())
                                   .build();
    }
}
