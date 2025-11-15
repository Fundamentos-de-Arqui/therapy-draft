package com.soulware.therapydraft.interfaces.rest.resources;

import java.util.List;

public record PagedResponseResource<T>(
        List<T> items,
        long totalItems,
        int totalPages,
        int page,
        int size
) {}
