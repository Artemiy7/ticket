package net.ticket.transformer;

public interface Transformer<From, To> {
    To transform(From from);
}
