package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.entites.Genre;
import com.library.ReadFlow.exceptions.GenreException;
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

        if (genreDTO.getParentGenreId() != null) {

            Genre parentGenre = genreRepository
                    .findById(genreDTO.getParentGenreId())
                    .orElseThrow(
                            () -> new GenreException("Parent genre not found")
                    );

            genre.setParentGenre(parentGenre);
        }

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
    public GenreDTO getGenreById(Long genreId) throws GenreException {
        Genre genre = genreRepository.findById(genreId).orElseThrow(
                () -> new GenreException("Genre not found")
        );
        return genreMapper.toDTO(genre);
    }

    @Override
    public GenreDTO updateGenre(Long genreId, GenreDTO genreDTO) {
        Genre existingGenre = genreRepository.findById(genreId).orElseThrow(
                () -> new GenreException("Genre not found with id : "+genreId)
        );
        genreMapper.updateEntityFromDTO(genreDTO,existingGenre);
        if(genreDTO.getParentGenreId() != null){
            if(genreId.equals(
                    genreDTO.getParentGenreId()
            )){
                throw new GenreException(
                        "Genre cannot be its own parent"
                );
            }
            Genre parentGenre = genreRepository
                    .findById(
                            genreDTO.getParentGenreId()
                    )
                    .orElseThrow(
                            () -> new GenreException(
                                    "Parent Genre not found"
                            )
                    );
            existingGenre.setParentGenre(
                    parentGenre
            );
        } else {
            existingGenre.setParentGenre(null);
        }
        Genre updatedGenre = genreRepository.save(existingGenre);
        return genreMapper.toDTO(updatedGenre);
    }

    @Override
    public void deleteGenre(Long genreId) {
        Genre existingGenre = genreRepository.findById(genreId).orElseThrow(
                () -> new GenreException("Genre not found with id : "+genreId)
        );
        existingGenre.setActive(false);
        genreRepository.save(existingGenre);

    }

    @Override
    public void hardDeleteGenre(Long genreId) {
        Genre existingGenre = genreRepository.findById(genreId).orElseThrow(
                () -> new GenreException("Genre not found with id : "+genreId)
        );
        genreRepository.delete(existingGenre);
    }

    @Override
    public List<GenreDTO> getAllActiveGenresWithSubGenres() {
        List<Genre> topLevelGenres = genreRepository.findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.toDTOList(topLevelGenres);
    }

    @Override
    public List<GenreDTO> getTopLevelGenres() {
        List<Genre> topLevelGenres = genreRepository.findByParentGenreIsNullAndActiveTrueOrderByDisplayOrderAsc();
        return genreMapper.toDTOList(topLevelGenres);
    }

    @Override
    public long getTotalActiveGenres() {
        return genreRepository.countByActiveTrue();
    }

    @Override
    public long getBookCountByGenreId(Long genreId) {
        return 0;
    }

}
