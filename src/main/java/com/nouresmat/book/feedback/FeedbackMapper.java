package com.nouresmat.book.feedback;

import com.nouresmat.book.book.Book;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class FeedbackMapper {
    public Feedback toFeedback(FeedbackRequest feedbackRequest){
        return Feedback.builder()
                .note(feedbackRequest.getNote())
                .comment(feedbackRequest.getComment())
                .book(Book.builder()
                        .id(feedbackRequest.getBookId())
                        .archived(false)
                        .shareable(true).build())
                .build();
    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Integer id) {
        return FeedbackResponse.builder().note(feedback.getNote())
                .comment(feedback.getComment())
                .owen(Objects.equals(id,feedback.getCreatedBy()))
                .build();
    }
}
