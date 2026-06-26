package com.nouresmat.book.history;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookHistoryResponse {
    private Integer id;
    private String title;
    private String authorName;
    private String sbin;
    private double rate;
    private boolean returned;
    private boolean returnApproved;
}
