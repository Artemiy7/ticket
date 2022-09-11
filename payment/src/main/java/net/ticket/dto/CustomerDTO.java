package net.ticket.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.ticket.domains.Customer;
import net.ticket.domains.TicketOrder;


public class CustomerDTO {
    String firstName;
    String lastName;
    String country;

    public Customer toCustomer(TicketOrder ticketOrder) {
        return new Customer().setFirstName(this.firstName)
                             .setLastName(this.lastName)
                             .setCountry(this.country)
                             .setTicketOrder(ticketOrder);
    }

    @JsonCreator
    public CustomerDTO(@JsonProperty(value = "firstName", required = true) String firstName,
                       @JsonProperty(value = "lastName", required = true) String lastName,
                       @JsonProperty(value = "country", required = true) String country) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
    }
}
