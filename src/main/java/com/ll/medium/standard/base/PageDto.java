package com.ll.medium.standard.base;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
public class PageDto<T> {
    @NonNull
    private final long totalElementsCount;
    @NonNull
    private final long pageElementsCount;
    @NonNull
    private final long totalPagesCount;
    @NonNull
    private final int number;
    @NonNull
    private final List<T> content;
    @NonNull
    private final boolean hasPrevious;
    @NonNull
    private final boolean hasNext;

    public PageDto(Page<T> page) {
        this.totalElementsCount = page.getTotalElements();
        this.pageElementsCount = page.getNumberOfElements();
        this.number = page.getNumber();
        this.content = page.getContent();
        this.totalPagesCount = page.getTotalPages();
        this.hasPrevious = page.hasPrevious();
        this.hasNext = page.hasNext();
    }
}
