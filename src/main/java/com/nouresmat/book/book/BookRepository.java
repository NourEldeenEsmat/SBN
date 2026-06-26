package com.nouresmat.book.book;

import com.nouresmat.book.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("""
            select b from Book b
            where b.archived = false
            and b.shareable = true
            and b.owner.id != :user_id
            """)
    Page<Book> findAllDisplayableBooks(Pageable pageable,@Param("user_id") Integer id);

    Page<Book> findAllByOwner(Pageable pageable, User user);
}
