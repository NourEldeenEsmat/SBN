package com.nouresmat.book.book;

import com.nouresmat.book.file.FileUtils;
import com.nouresmat.book.history.BookHistoryResponse;
import com.nouresmat.book.history.BookTransactionHistory;
import org.springframework.stereotype.Service;

@Service
public class BookMapper {

    public Book toBook(BookRequest bookRequest) {
        return Book.builder()
                .authorName(bookRequest.authorName())
                .isbn(bookRequest.isbn())
                .title(bookRequest.title())
                .synopsis(bookRequest.synopsis())
                .shareable(bookRequest.shareable())
                .archived(false).build();
    }

    public BookResponse toBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .authorName(book.getAuthorName())
                .synopsis(book.getSynopsis())
                .isbn(book.getIsbn())
                .rate(book.getRate())
                .shareable(book.isShareable())
                .archived(book.isArchived())
                .owner(book.getOwner().fullName())
                .cover(FileUtils.getFileFromLocation(book.getBookCover()))
                .build();
    }

    public BookHistoryResponse toBorrowedBooks(BookTransactionHistory history) {
        return BookHistoryResponse.builder()
                .sbin(history.getBook().getIsbn())
                .returned(history.isReturned())
                .returnApproved(history.isReturnApproved())
                .authorName(history.getBook().getAuthorName())
                .title(history.getBook().getTitle())
                .rate(history.getBook().getRate())
                .id(history.getBook().getId())
                .build();
    }
}
