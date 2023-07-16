package net.ticket.domain.pagination;


import lombok.Getter;

@Getter
public class PageAndSortingObject {
    private int size;
    private int page;
    private String sortingField;
    private SortingOrder sortingOrder;

    public PageAndSortingObject(int size, int page, String sortingField, SortingOrder sortingOrder) {
        this.size = size;
        this.page = page;
        this.sortingField = sortingField;
        this.sortingOrder = sortingOrder;
    }

    public static PageAndSortingObject createObject(int size, int page, String sortingField, SortingOrder sortingOrder) {
        return new PageAndSortingObject(size, page, sortingField, sortingOrder);
    }

    public enum SortingOrder {
        ASC(),
        DESC();
    }
}
