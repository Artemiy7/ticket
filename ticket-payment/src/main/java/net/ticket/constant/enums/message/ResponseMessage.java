package net.ticket.constant.enums.message;

public enum ResponseMessage {
    OCCASION_NOT_FOUND("No such occasion was found");

    String message;

    public String getMessage() {
        return message;
    }

    ResponseMessage(String message) {
        this.message = message;
    }
}
