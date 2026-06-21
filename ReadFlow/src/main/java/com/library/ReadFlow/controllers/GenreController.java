package com.library.ReadFlow.controllers;

import com.library.ReadFlow.entites.Genre;
import com.library.ReadFlow.payload.dtos.GenreDTO;
import com.library.ReadFlow.services.GenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    public ResponseEntity<GenreDTO> createGenre(@Valid @RequestBody GenreDTO genreDto){
        GenreDTO createdGenre = genreService.createGenre(genreDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
    }
    @GetMapping
    public ResponseEntity<List<GenreDTO>> getAllGenres(){
        List<GenreDTO> genres = genreService.getAllGenres();
        return ResponseEntity.status(HttpStatus.OK).body(genres);
    }

    @GetMapping("{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable Long id){
        GenreDTO genre = genreService.getGenreById(id);
        return ResponseEntity.status(HttpStatus.OK).body(genre);
    }
}
