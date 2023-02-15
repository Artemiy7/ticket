package net.ticket.transformer.ticketorder;

import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.entity.ticketorder.TicketOrderEntity;
import net.ticket.transformer.ticketorder.customer.CustomerDtoTransformerToCustomerEntity;
import net.ticket.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TicketOrderDtoToTicketOrderEntityTransformer implements Transformer<TicketOrderDto, TicketOrderEntity> {
    private final CustomerDtoTransformerToCustomerEntity customerDtoTransformerToCustomerEntity;
    @Autowired
    public TicketOrderDtoToTicketOrderEntityTransformer(CustomerDtoTransformerToCustomerEntity customerDtoTransformerToCustomerEntity) {
        this.customerDtoTransformerToCustomerEntity = customerDtoTransformerToCustomerEntity;
    }

    @Override
    public TicketOrderEntity transform(TicketOrderDto orderDto) {
        TicketOrderEntity ticketOrder = TicketOrderEntity.builder()
                                                         .bankAccount(orderDto.getBankAccount())
                                                         .ticketType(orderDto.getTicketType())
                                                         .currency(orderDto.getCurrency())
                                                         .build();

        ticketOrder.setCustomersEntitySet(orderDto.getCustomerTicketDto().stream()
                                                                         .map(customerDtoTransformerToCustomerEntity::transform)
                                                                         .collect(Collectors.toSet()));
        ticketOrder.getCustomersEntitySet().forEach(customerTicketEntity -> customerTicketEntity.setTicketOrderEntity(ticketOrder));
        return ticketOrder;
    }
}
