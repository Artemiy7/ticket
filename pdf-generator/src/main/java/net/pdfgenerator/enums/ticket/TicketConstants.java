package net.pdfgenerator.enums.ticket;

public enum TicketConstants {
    HEAD_TEMPLATE("Ticket to %s: %s"),
    HEAD_TEMPLATE_WITH_LINE("%s - %s"),
    EMPTY_SPACE(" "),
    UNDERLINE("_"),
    SITE_AND_SUPPORT_PHONE("Site: %s; Support: %s"),
    SITE_TEMPLATE("Site: %s"),
    SUPPORT_PHONE("Support: %s"),
    CUSTOMER_NAME_HEADER("Customer name"),
    SEAT_HEADER("Seat"),
    COST_HEADER("Cost"),
    SEAT_TYPE_HEADER("Seat type"),
    DATETIME_HEADER("HH:mm dd.MM.yyyy");

    private String phrase;

    TicketConstants(String phrase) {
        this.phrase = phrase;
    }

    @Override
    public String toString() {
        return phrase;
    }
}
