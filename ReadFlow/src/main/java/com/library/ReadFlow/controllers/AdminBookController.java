package com.library.ReadFlow.controllers;

import com.library.ReadFlow.payload.dtos.BookDTO;
import com.library.ReadFlow.services.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/books")
public class AdminBookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO){
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<BookDTO>> createBooksBulk(@RequestBody List<@Valid BookDTO> bookDTOS){
        List<BookDTO> createdBooks = bookService.createBooksBulk(bookDTOS);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooks);
    }
}
