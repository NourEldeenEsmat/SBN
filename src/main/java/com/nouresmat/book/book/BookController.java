package com.nouresmat.book.book;

import com.nouresmat.book.common.PageResponse;
import com.nouresmat.book.history.BookHistoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Integer> saveBook(
            @RequestBody @Valid BookRequest bookRequest,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService
                .save(bookRequest, connectedUser));
    }

    @GetMapping("{book-id}")
    public ResponseEntity<BookResponse> findBookById(@PathVariable("book-id") Integer id) {
        return ResponseEntity.ok(bookService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookResponse>> getAllBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getAllBooks(page, size, connectedUser));
    }

    @GetMapping("get_books_by_owner")
    public ResponseEntity<PageResponse<BookResponse>> getBooksByOwner(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getBooksByOwner(page, size, connectedUser));
    }

    @GetMapping("borrowed_books")
    public ResponseEntity<PageResponse<BookHistoryResponse>> getBorrowedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getBorrowedBooks(page, size, connectedUser));
    }

    @GetMapping("returned_books")
    public ResponseEntity<PageResponse<BookHistoryResponse>> getReturnedBooks(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService.getReturnedBooks(page, size, connectedUser));
    }

    @PatchMapping("shareable/{book-id}")
    public ResponseEntity<Integer> updateShareable(
            @PathVariable("book-id") Integer id,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService
                .updateShareable(id, connectedUser));
    }

    @PatchMapping("archived/{book-id}")
    public ResponseEntity<Integer> updateArchived(
            @PathVariable("book-id") Integer id,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(bookService
                .updateArchived(id, connectedUser));
    }

    @PostMapping("borrow/{book-id}")
    public ResponseEntity<Integer> borrowBook(
            @PathVariable("book-id") Integer id,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.borrowBook(id, connectedUser));
    }

    @PatchMapping("return/{book-id}")
    public ResponseEntity<Integer> returnBook(
            @PathVariable("book-id") Integer id,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.returnBook(id, connectedUser));
    }

    @PatchMapping("approveReturn/{book-id}")
    public ResponseEntity<Integer> approveReturnBook(
            @PathVariable("book-id") Integer id,
            Authentication connectedUser) {
        return ResponseEntity.ok(bookService.approveReturnBook(id, connectedUser));
    }

    @PostMapping(value = "cover/{book-id}")
    public ResponseEntity<?> updateCover(
            @PathVariable("book-id") Integer id,
            @RequestPart("file") MultipartFile file,
            Authentication connectedUser) {
        bookService.uploadCover(file, id, connectedUser);
        return ResponseEntity.accepted().build();
    }
}













