package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.entites.Genre;
import com.library.ReadFlow.mapper.GenreMapper;
import com.library.ReadFlow.payload.dtos.GenreDTO;
import com.library.ReadFlow.repositories.GenreRepository;
import com.library.ReadFlow.services.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    @Override
    public GenreDTO createGenre(GenreDTO genreDTO) {
       Genre genre = genreMapper.toEntity(genreDTO);

       Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toDTO(savedGenre);
    }
    @Override
    public List<GenreDTO> getAllGenres() {
        return genreRepository.findAll()
                .stream()
                .map(genreMapper::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public GenreDTO getGenreById(Long id) {
        Optional<Genre> genre = genreRepository.findById(id);
        return genre;
    }

    @Override
    public GenreDTO updateGenre(Long genreId, GenreDTO genreDTO) {
        return null;
    }

    @Override
    public void deleteGenre(Long genreId) {

    }

    @Override
    public void hardDeleteGenre(Long genreId) {

    }

    @Override
    public List<GenreDTO> getAllActiveGenresWithSubGenres() {
        return List.of();
    }

    @Override
    public List<GenreDTO> getTopLevelGenres() {
        return List.of();
    }

    @Override
    public long getTotalActiveGenres() {
        return 0;
    }

    @Override
    public long getBookCountByGenreId(Long genreId) {
        return 0;
    }

}
