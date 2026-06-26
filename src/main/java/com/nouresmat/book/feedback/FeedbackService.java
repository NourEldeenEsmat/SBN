package com.nouresmat.book.feedback;

import com.nouresmat.book.book.Book;
import com.nouresmat.book.book.BookRepository;
import com.nouresmat.book.common.PageResponse;
import com.nouresmat.book.exception.OperationNotPermittedException;
import com.nouresmat.book.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
    final private FeedbackRepo feedbackRepo;
    final private FeedbackMapper feedbackMapper;
    private final BookRepository bookRepository;

    public Integer saveFeedback(FeedbackRequest feedbackRequest, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(feedbackRequest.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("this book can not be feedback -archived / not shareable-");
        }
        if (user.getId().equals(book.getOwner().getId())) {
            throw new OperationNotPermittedException("Owner Can not feedback his book ! ");
        }
        Feedback feedback = feedbackMapper.toFeedback(feedbackRequest);
        return feedbackRepo.save(feedback).getId();
    }

    public PageResponse<FeedbackResponse> getFeedbacksByBookId(Integer bookId, Authentication connectedUser,
                                                               int size, int page) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        Pageable pageable = PageRequest.of(size, page, Sort.by("note").descending());
        Page<Feedback> feedbacks = feedbackRepo.findByBook(book, pageable);
        List<FeedbackResponse> content = feedbacks.stream()
                .map(f -> feedbackMapper.toFeedbackResponse(f, user.getId())).toList();
        return PageResponse.<FeedbackResponse>builder()
                .size(feedbacks.getSize()).page(page).totalElements(feedbacks.getTotalElements())
                .totalPages(feedbacks.getTotalPages())
                .isFirst(feedbacks.isFirst()).isLast(feedbacks.isLast())
                .content(content)
                .build();
    }
}
