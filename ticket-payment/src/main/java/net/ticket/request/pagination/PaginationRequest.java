package net.ticket.request.pagination;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaginationRequest {
    private short size;
    private short resultOrder;
    private boolean withOutdated = false;
    private SortingOrder sortingOrder = SortingOrder.ASC;

    public enum SortingOrder {
        ASC(),
        DESC();
    }
}
