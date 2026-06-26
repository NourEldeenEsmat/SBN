package com.nouresmat.book.feedback;

import com.nouresmat.book.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("feedbacks")
public class FeedbackController {
    final private FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<Integer> postFeedback(
            @Valid @RequestBody FeedbackRequest feedbackRequest,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(feedbackService.saveFeedback(feedbackRequest, connectedUser));
    }

    @GetMapping("book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> getFeedbacksByBookId(
            @PathVariable("book-id") Integer bookId
            , Authentication connectedUser
            , @RequestParam(name = "size", required = false, defaultValue = "10") int size
            , @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByBookId(bookId,
                connectedUser,size,page));
    }
}
