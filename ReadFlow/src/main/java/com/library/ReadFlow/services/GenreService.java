package com.library.ReadFlow.services;
import com.library.ReadFlow.exceptions.GenreException;
import com.library.ReadFlow.payload.dtos.GenreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GenreService {

    GenreDTO createGenre(GenreDTO genre);

    List<GenreDTO> getAllGenres();

    GenreDTO getGenreById(Long id);

    GenreDTO updateGenre(Long genreId,GenreDTO genreDTO);

    void deleteGenre(Long genreId);

    void hardDeleteGenre(Long genreId);

    List<GenreDTO> getAllActiveGenresWithSubGenres();

    List<GenreDTO> getTopLevelGenres();

//    Page<GenreDTO> searchGenres(String searchTerm, Pageable pageable);
    
    long getTotalActiveGenres();

    long getBookCountByGenreId(Long genreId);
}
