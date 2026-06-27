package com.nouresmat.book.book;

import com.nouresmat.book.common.PageResponse;
import com.nouresmat.book.exception.OperationNotPermittedException;
import com.nouresmat.book.file.FileStorageService;
import com.nouresmat.book.history.BookHistoryResponse;
import com.nouresmat.book.history.BookTransactionHistory;
import com.nouresmat.book.history.HistoryRepo;
import com.nouresmat.book.user.User;
import com.nouresmat.book.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final UserRepository userRepository;
    private final HistoryRepo historyRepo;
    private final FileStorageService fileStorageService;

    public Integer save(BookRequest bookRequest, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Book book = bookMapper.toBook(bookRequest);
        book.setOwner(user);
        bookRepository.save(book);
        return bookRepository.save(book).getId();
    }

    public BookResponse findById(Integer id) {

        return bookRepository.findById(id).map(bookMapper::toBookResponse)
                .orElseThrow(() -> new RuntimeException("No Book With the ID:" + id));
    }

    public PageResponse<BookResponse> getAllBooks(int page, int size, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Book> books = bookRepository.findAllDisplayableBooks(pageable, user.getId());
        List<BookResponse> bookResponses = books.stream().map(bookMapper::toBookResponse).toList();
        return new PageResponse<>(
                bookResponses,
                books.getSize(),
                page,
                books.getTotalElements(),
                books.getTotalPages(),
                books.isFirst(),
                books.isLast()
        );
    }

    public PageResponse<BookResponse> getBooksByOwner(int page, int size, Authentication connectedUser) {

        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate"));
        User owner = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("owner not found"));

        Page<Book> books = bookRepository.findAllByOwner(pageable, owner);
        List<BookResponse> ownerBooks = books.stream().map(bookMapper::toBookResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                ownerBooks, size, page, books.getTotalElements(),
                books.getTotalPages(), books.isFirst(), books.isLast()
        );

    }

    public PageResponse<BookHistoryResponse> getBorrowedBooks
            (int page, int size , Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("createdDate").descending());
        Page<BookTransactionHistory> borrowedBooks = historyRepo.findAllByUser(pageable, user);
        List<BookHistoryResponse> bookHistoryResponses = borrowedBooks.stream()
                .map(bookMapper::toBorrowedBooks).toList();
        return new PageResponse<>(
                bookHistoryResponses,
                size, page, borrowedBooks.getTotalElements(),
                borrowedBooks.getTotalPages(), borrowedBooks.isFirst(),
                borrowedBooks.isLast()
        );
    }

    public PageResponse<BookHistoryResponse> getReturnedBooks(int page, int size,
                                                              Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BookTransactionHistory> returnedBooks = historyRepo.findReturnedBooks(pageable, user.getId());
        List<BookHistoryResponse> bookHistoryResponses = returnedBooks.stream()
                .map(bookMapper::toBorrowedBooks).toList();
        return PageResponse.<BookHistoryResponse>builder()
                .size(size)
                .page(page).content(bookHistoryResponses)
                .totalElements(returnedBooks.getTotalElements())
                .totalPages(returnedBooks.getTotalPages())
                .isFirst(returnedBooks.isFirst())
                .isLast(returnedBooks.isLast()).build();
    }

    public Integer updateShareable(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        if (!user.getId().equals(book.getOwner().getId())) {
            throw new OperationNotPermittedException("only Owner Can change shareable status ! ");
        }
        book.setShareable(!book.isShareable());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer updateArchived(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        if (!user.getId().equals(book.getOwner().getId())) {
            throw new OperationNotPermittedException("only Owner Can change archived status ! ");
        }
        book.setArchived(!book.isArchived());
        bookRepository.save(book);
        return book.getId();
    }

    public Integer borrowBook(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("this book can not be borrowed -archived / not shareable-");
        }
        if (user.getId().equals(book.getOwner().getId())) {
            throw new OperationNotPermittedException("Owner Can not borrow his book ! ");
        }
        final boolean isAlreadyBorrowed = historyRepo.isAlreadyBorrowedByUser(id, user.getId());
        if (isAlreadyBorrowed) {
            throw new OperationNotPermittedException("you cannot borrow the same book two times ! ");
        }
        BookTransactionHistory history = BookTransactionHistory.builder()
                .book(book)
                .returnApproved(false)
                .returned(false)
                .user(user)
                .build();
        BookTransactionHistory bookTransactionHistory = historyRepo.save(history);
        return bookTransactionHistory.getId();
    }

    public Integer returnBook(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book not found"));

        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("this book can not be borrowed -archived / not shareable-");
        }
        if (user.getId().equals(book.getOwner().getId())) {
            throw new OperationNotPermittedException("Owner Can not borrow / return  his book ! ");
        }
        BookTransactionHistory bookToReturn = historyRepo.findByBookAndUser(book, user)
                .orElseThrow(() -> new OperationNotPermittedException("you cant return not borrowed book"));
        if (bookToReturn.isReturned()){
            throw new OperationNotPermittedException("you Can not return  returned book ! ");
        }
        bookToReturn.setReturned(true);
        historyRepo.save(bookToReturn);
        return bookToReturn.getId();
    }

    public Integer approveReturnBook(Integer id, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        if (book.isArchived() || !book.isShareable()) {
            throw new OperationNotPermittedException("this book can not be borrowed -archived / not shareable-");
        }
        if (user.getId().equals(book.getOwner().getId())) {
            throw new OperationNotPermittedException("Owner Can not borrow / return  his book ! ");
        }
        BookTransactionHistory bookToReturn = historyRepo.findByBookAndUser(book, user)
                .orElseThrow(() -> new OperationNotPermittedException("you cant return not borrowed book"));
        if (!bookToReturn.isReturned()) {
            throw new OperationNotPermittedException("book is not returned yet");
        }
        bookToReturn.setReturnApproved(true);
        historyRepo.save(bookToReturn);
        return bookToReturn.getId();
    }

    public void uploadCover(MultipartFile file, Integer id, Authentication connectedUser) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book not found"));
        User user = (User) connectedUser.getPrincipal();
        var bookCover = fileStorageService.saveFile(file, user.getId());
        book.setBookCover(bookCover);
        bookRepository.save(book);
    }
}
