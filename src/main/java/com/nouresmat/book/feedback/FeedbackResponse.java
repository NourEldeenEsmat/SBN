package com.nouresmat.book.feedback;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackResponse {
    private double note;
    private String comment;
    private boolean owen;
}
