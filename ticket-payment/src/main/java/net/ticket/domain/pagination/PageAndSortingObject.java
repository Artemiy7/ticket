package net.ticket.domain.pagination;


import lombok.Getter;

@Getter
public class PageAndSortingObject {
    private int size;
    private int page;
    private String sortField;
    private SortingOrder sortingOrder;

    public PageAndSortingObject(int size, int page, String sortField, SortingOrder sortingOrder) {
        this.size = size;
        this.page = page;
        this.sortField = sortField;
        this.sortingOrder = sortingOrder;
    }

    public static PageAndSortingObject createObject(int size, int page, String sortField, SortingOrder sortingOrder) {
        return new PageAndSortingObject(size, page, sortField, sortingOrder);
    }

    public enum SortingOrder {
        ASC(),
        DESC();
    }
}
