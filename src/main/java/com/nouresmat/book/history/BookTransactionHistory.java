package com.nouresmat.book.history;

import com.nouresmat.book.book.Book;
import com.nouresmat.book.common.BaseEntity;
import com.nouresmat.book.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class BookTransactionHistory extends BaseEntity {

    private boolean returned;
    private boolean returnApproved;
    @ManyToOne
    private User user;
    @ManyToOne
    private Book book;
}
