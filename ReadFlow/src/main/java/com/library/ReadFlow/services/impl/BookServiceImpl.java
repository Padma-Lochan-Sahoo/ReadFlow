package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.entites.Book;
import com.library.ReadFlow.entites.Genre;
import com.library.ReadFlow.exceptions.BookException;
import com.library.ReadFlow.mapper.BookMapper;
import com.library.ReadFlow.payload.dtos.BookDTO;
import com.library.ReadFlow.payload.request.BookSearchRequest;
import com.library.ReadFlow.payload.response.PageResponse;
import com.library.ReadFlow.repositories.BookRepository;
import com.library.ReadFlow.repositories.GenreRepository;
import com.library.ReadFlow.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final GenreRepository genreRepository;

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        if(bookRepository.existsByIsbn(bookDTO.getIsbn())){
            throw new BookException("Book with isbn "+bookDTO.getIsbn()+" already exists");
        }
        Book book = bookMapper.toEntity(bookDTO);
        Genre genre = genreRepository
                .findById(bookDTO.getGenreId())
                .orElseThrow(
                        () -> new BookException(
                                "Genre not found with id "
                                        + bookDTO.getGenreId()
                        )
                );

        book.setGenre(genre);
        if(!book.isAvailableCopiesValid()){
            throw new BookException(
                    "Available copies cannot exceed total copies"
            );
        }
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDTO(savedBook);
    }

    @Override
    public List<BookDTO> createBooksBulk(List<BookDTO> bookDTOS) {
        List<BookDTO> createdBooks = new ArrayList<>();
        for(BookDTO bookDTO:bookDTOS){
            BookDTO book = createBook(bookDTO);
            createdBooks.add(book);
        }
        return createdBooks;
    }

    @Override
    public BookDTO getBookById(Long bookId) {
        Book returnedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book is not found with id "+bookId));

        return bookMapper.toDTO(returnedBook);
    }

    @Override
    public BookDTO getBookByISBN(String isbn) {
        Book returnedBook = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BookException("Book is not found with isbn "+isbn));
        return bookMapper.toDTO(returnedBook);
    }

    @Override
    public BookDTO updateBook(Long bookId,BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found!"));
        bookMapper.updateEntityFromDTO(bookDTO,existingBook);
        if(bookDTO.getGenreId() != null){

            Genre genre = genreRepository
                    .findById(bookDTO.getGenreId())
                    .orElseThrow(
                            () -> new BookException(
                                    "Genre not found with id "
                                            + bookDTO.getGenreId()
                            )
                    );

            existingBook.setGenre(genre);
        }
        if(!existingBook.isAvailableCopiesValid()){
            throw new BookException(
                    "Available copies cannot exceed total copies"
            );
        }
        Book savedBook = bookRepository.save(existingBook);
        return bookMapper.toDTO(savedBook);
    }

    @Override
    public void deleteBook(Long bookId) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found!"));
        existingBook.setActive(false);
        bookRepository.save(existingBook);
    }

    @Override
    public void hardDeleteBook(Long bookId) {
        Book existingBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookException("Book not found!"));
        bookRepository.delete(existingBook);
    }

    @Override
    public PageResponse<BookDTO> searchBookWithFilters(BookSearchRequest searchRequest) {
        Pageable pageable = createPageable(searchRequest.getPage(),
                searchRequest.getSize(),
                searchRequest.getSortBy(),
                searchRequest.getSortDirection()
        );
        Page<Book> bookPage = bookRepository.searchBooksWithFilters(
                searchRequest.getSearchTerm(),
                searchRequest.getGenreId(),
                searchRequest.getAvailableOnly(),
                pageable
        );
        return convertToPageResponse(bookPage);
    }

    @Override
    public long getTotalActiveBooks() {
        return bookRepository.countByActiveTrue();
    }

    @Override
    public long getTotalAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }
    private Pageable createPageable(int page,int size, String sortBy, String sortDirection){
        size=Math.min(size,10);
        size=Math.max(size,1);
        Sort sort= "ASC".equalsIgnoreCase(sortDirection)
                ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        return PageRequest.of(page,size,sort);

    }
    private PageResponse<BookDTO> convertToPageResponse(Page<Book> books){
        List<BookDTO> bookDTOS = books.getContent()
                .stream()
                .map(bookMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(bookDTOS,
                books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty()
        );
    }
}
