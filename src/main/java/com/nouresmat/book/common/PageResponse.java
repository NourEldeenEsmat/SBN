package com.nouresmat.book.common;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;
    private int size;
    private int page;
    private long totalElements;
    private int totalPages;
    private boolean isFirst;
    private boolean isLast;
}
