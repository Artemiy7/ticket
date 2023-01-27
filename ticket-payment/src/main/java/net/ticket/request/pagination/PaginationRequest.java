package net.ticket.request.pagination;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PaginationRequest {
    private short size = 1;
    private short resultOrder;
    private SortingOrder sortingOrder = SortingOrder.ASC;

    public enum SortingOrder {
        ASC(),
        DESC();
    }
}
