package com.library.ReadFlow.controllers;

import com.library.ReadFlow.exceptions.BookException;
import com.library.ReadFlow.payload.dtos.BookDTO;
import com.library.ReadFlow.payload.request.BookSearchRequest;
import com.library.ReadFlow.payload.response.ApiResponse;
import com.library.ReadFlow.payload.response.PageResponse;
import com.library.ReadFlow.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long bookId){
        BookDTO book = bookService.getBookById(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByISBN(@PathVariable String isbn){
        BookDTO book = bookService.getBookByISBN(isbn);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable Long bookId ,
            @Valid @RequestBody BookDTO bookDTO){
        BookDTO updatedBook =
                bookService.updateBook(
                        bookId,
                        bookDTO
                );

        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<ApiResponse> deleteBook(
            @PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        ApiResponse response = new ApiResponse("Book deleted - soft delete",true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{bookId}/permanent")
    public ResponseEntity<ApiResponse> hardDeleteBook(
            @PathVariable("bookId") Long bookId) {
        bookService.hardDeleteBook(bookId);
        ApiResponse response = new ApiResponse("Book deleted Permanently",true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false,defaultValue = "false") Boolean availableOnly,
            @RequestParam(defaultValue = "true") boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ){
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setGenreId(genreId);
        searchRequest.setAvailableOnly(availableOnly);

        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSearchTerm(searchTerm);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<BookDTO> books = bookService.searchBookWithFilters(searchRequest);
        return ResponseEntity.ok(books);
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookDTO>> advancedSearch(
            @RequestBody BookSearchRequest searchRequest
            ){
        PageResponse<BookDTO> books = bookService.searchBookWithFilters(searchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @GetMapping("/stats")
    public ResponseEntity<BookStatsResponse> getBookStats(){
        long totalActive = bookService.getTotalActiveBooks();;
        long totalAvailable = bookService.getTotalAvailableBooks();

        BookStatsResponse stats = new BookStatsResponse(totalActive,totalAvailable);
        return ResponseEntity.status(HttpStatus.OK).body(stats);

    }

    public static class BookStatsResponse{
        public long totalActiveBooks;
        public long totalAvailableBooks;

        public BookStatsResponse(long totalActiveBooks,long totalAvailableBooks){
            this.totalActiveBooks = totalActiveBooks;
            this.totalAvailableBooks = totalAvailableBooks;
        }
    }

}
