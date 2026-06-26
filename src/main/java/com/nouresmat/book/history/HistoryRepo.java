package com.nouresmat.book.history;

import com.nouresmat.book.book.Book;
import com.nouresmat.book.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HistoryRepo extends JpaRepository<BookTransactionHistory,Integer> {

    Page<BookTransactionHistory> findAllByUser(Pageable pageable, User user);
    @Query("""
            select b from
            BookTransactionHistory b
            where b.user.id = :user_id
            and b.returned = true
            """)
    Page<BookTransactionHistory> findReturnedBooks(Pageable pageable,@Param("user_id") Integer id);
    @Query("""
            select (count(*) > 0)
            from BookTransactionHistory h
            where h.book.id = :book_id
            and h.user.id = :user_id
            and h.returnApproved = false
            """)
    boolean isAlreadyBorrowedByUser(@Param("book_id") Integer id,@Param("user_id") Integer id1);

    Optional<BookTransactionHistory> findByBookAndUser(Book book, User user);
}
