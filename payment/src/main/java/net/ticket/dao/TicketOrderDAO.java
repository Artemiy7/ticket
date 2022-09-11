package net.ticket.dao;

import net.ticket.domains.TicketOrder;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TicketOrderDAO {
    @Autowired
    SessionFactory sessionFactory;

    public void saveTicketOrder(TicketOrder ticketOrder) {
        sessionFactory.getCurrentSession().save(ticketOrder);
    }
}
