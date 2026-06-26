package com.nouresmat.book.feedback;

import com.nouresmat.book.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback,Integer> {
    Page<Feedback> findByBook(Book book, Pageable pageable);
}
