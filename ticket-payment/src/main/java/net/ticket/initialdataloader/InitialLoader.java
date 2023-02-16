package net.ticket.initialdataloader;

import java.util.List;
import java.util.Optional;

public interface InitialLoader <T> {
    Optional<List<T>> loadInitialDataFromJson();
}
