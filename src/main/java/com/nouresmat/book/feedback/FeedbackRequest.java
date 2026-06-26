package com.nouresmat.book.feedback;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequest {
    @Positive(message = "200")
    @Min(value = 0,message = "201")
    @Max(value = 5, message = "202")
    private double note;
    @NotEmpty(message = "203")
    @NotBlank(message = "203")
    @NotNull(message = "203")
    private String comment;
    @NotNull(message = "204")
    private int bookId;
}
