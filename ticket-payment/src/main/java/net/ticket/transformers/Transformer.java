package net.ticket.transformers;

public interface Transformer<From, To> {
    To transform(From from);
}
