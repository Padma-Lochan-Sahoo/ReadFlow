package com.library.ReadFlow.services;

import com.library.ReadFlow.payload.dtos.BookDTO;
import com.library.ReadFlow.payload.request.BookSearchRequest;
import com.library.ReadFlow.payload.response.PageResponse;

import java.util.List;

public interface BookService {

    BookDTO createBook(BookDTO bookDTO);
    List<BookDTO> createBooksBulk(List<BookDTO> bookDTOS);
    BookDTO getBookById(Long bookId);
    BookDTO getBookByISBN(String isbn);
    BookDTO updateBook(Long bookId,BookDTO bookDTO);
    void deleteBook(Long bookId);
    void hardDeleteBook(Long bookId);

    PageResponse<BookDTO> searchBookWithFilters(
            BookSearchRequest searchRequest
    );

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
