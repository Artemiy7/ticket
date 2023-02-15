package net.ticket.transformer.ticketorder;

import net.ticket.dto.ticketorder.CustomerTicketDto;
import net.ticket.dto.ticketorder.TicketOrderDto;
import net.ticket.entity.occasion.OccasionEntity;
import net.ticket.entity.ticketorder.TicketOrderEntity;
import net.ticket.transformer.ticketorder.customer.CustomerEntityToCustomerDtoTransformer;
import net.ticket.transformer.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TicketOrderEntityToTicketOrderDtoTransformer implements Transformer<TicketOrderEntity, TicketOrderDto> {
    private final CustomerEntityToCustomerDtoTransformer customerEntityToCustomerDtoTransformer;
    private final String defaultCurrency;

    @Autowired
    public TicketOrderEntityToTicketOrderDtoTransformer(CustomerEntityToCustomerDtoTransformer customerEntityToCustomerDtoTransformer,
                                                  @Value("${service.default-currency}") String defaultCurrency) {
        this.customerEntityToCustomerDtoTransformer = customerEntityToCustomerDtoTransformer;
        this.defaultCurrency = defaultCurrency;
    }

    @Override
    public TicketOrderDto transform(TicketOrderEntity ticketOrderEntity) {
        Set<CustomerTicketDto> customerTicketDtoSet = ticketOrderEntity.getCustomersEntitySet()
                                                                       .stream()
                                                                       .map(customerEntityToCustomerDtoTransformer::transform)
                                                                       .collect(Collectors.toSet());

        OccasionEntity occasionEntity = ticketOrderEntity.getCustomersEntitySet()
                                                         .stream()
                                                         .findFirst().orElseThrow(() -> new NullPointerException("No OccasionSeat entity"))
                                                         .getOccasionSeatEntity()
                                                         .getOccasionEntity();

        return TicketOrderDto.builder()
                                                      .bankAccount(ticketOrderEntity.getBankAccount())
                                                      .currency(defaultCurrency)
                                                      .occasionAddress(occasionEntity.getOccasionAddress())
                                                      .occasionName(occasionEntity.getOccasionName())
                                                      .occasionDate(occasionEntity.getOccasionTime())
                                                      .ticketType(ticketOrderEntity.getTicketType())
                                                      .customerTicketDto(customerTicketDtoSet)
                                                      .build();
    }
}
