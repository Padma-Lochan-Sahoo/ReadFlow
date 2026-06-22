package com.library.ReadFlow.controllers;

import com.library.ReadFlow.entites.Genre;
import com.library.ReadFlow.exceptions.GenreException;
import com.library.ReadFlow.payload.dtos.GenreDTO;
import com.library.ReadFlow.payload.response.ApiResponse;
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

    @GetMapping("/{genreId}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable("genreId") Long genreId) {
        GenreDTO genreDto = genreService.getGenreById(genreId);
        return ResponseEntity.status(HttpStatus.OK).body(genreDto);
    }

    @PutMapping("/{genreId}")
    public ResponseEntity<GenreDTO> updateGenre(
            @PathVariable("genreId") Long genreId,
            @RequestBody GenreDTO genreDTO
            ) {
        GenreDTO updatedGenreDto = genreService.updateGenre(genreId,genreDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedGenreDto);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<?> deleteGenre(@PathVariable("genreId") Long genreId) {
        genreService.deleteGenre(genreId);
        ApiResponse response = new ApiResponse("Genre deleted - soft delete",true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{genreId}/hard")
    public ResponseEntity<?> hardDeleteGenre(@PathVariable("genreId") Long genreId) {
        genreService.hardDeleteGenre(genreId);
        ApiResponse response = new ApiResponse("Genre deleted - hard delete",true);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/top-level")
    public ResponseEntity<List<GenreDTO>> getTopLevelGenres() {
        List<GenreDTO> genreDtos = genreService.getTopLevelGenres();
        return ResponseEntity.status(HttpStatus.OK).body(genreDtos);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalActiveGenres() {
        Long genres = genreService.getTotalActiveGenres();
        return ResponseEntity.status(HttpStatus.OK).body(genres);
    }

    @GetMapping("/{genreId}/book-count")
    public ResponseEntity<Long> getBookCountByGenre(@PathVariable Long genreId) {
        Long bookCount = genreService.getBookCountByGenreId(genreId);
        return ResponseEntity.status(HttpStatus.OK).body(bookCount);
    }


}
