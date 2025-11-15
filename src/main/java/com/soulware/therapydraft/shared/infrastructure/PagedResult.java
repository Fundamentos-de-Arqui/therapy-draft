package com.soulware.therapydraft.shared.infrastructure;

import java.util.List;

public class PagedResult<T> {

    private List<T> items;
    private long totalItems;
    private int totalPages;
    private int page;
    private int size;

    public PagedResult(List<T> items, long totalItems, int page, int size) {
        this.items = items;
        this.totalItems = totalItems;
        this.page = page;
        this.size = size;
        this.totalPages = (int) Math.ceil((double) totalItems / size);
    }

    public List<T> getItems() { return items; }
    public long getTotalItems() { return totalItems; }
    public int getTotalPages() { return totalPages; }
    public int getPage() { return page; }
    public int getSize() { return size; }
}
