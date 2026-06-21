package com.library.ReadFlow.mapper;

import com.library.ReadFlow.entites.Genre;
import com.library.ReadFlow.payload.dtos.GenreDTO;
import com.library.ReadFlow.repositories.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GenreMapper {
    public GenreDTO toDTO(Genre savedGenre){
        if(savedGenre == null) {
            return null;
        }
            GenreDTO dto = GenreDTO.builder()
                    .id(savedGenre.getId())
                    .code(savedGenre.getCode())
                    .name(savedGenre.getName())
                    .description(savedGenre.getDescription())
                    .displayOrder(savedGenre.getDisplayOrder())
                    .active(savedGenre.getActive())
                    .createdAt(savedGenre.getCreatedAt())
                    .updatedAt(savedGenre.getUpdatedAt())
                    .build();

            if(savedGenre.getParentGenre() != null){
                dto.setParentGenreId(savedGenre.getParentGenre().getId());
                dto.setParentGenreName(savedGenre.getParentGenre().getName());
            }
            if(savedGenre.getSubGenres() != null && !savedGenre.getSubGenres().isEmpty()){
                dto.setSubGenres(savedGenre.getSubGenres()
                        .stream()
                        .filter( subGenre -> subGenre.getActive())
                        .map(subGenre -> toDTO(subGenre)).collect(Collectors.toList()));
            }


//        dto.setBookCount();
            return dto;


    }

    public Genre toEntity(GenreDTO genreDTO){
        if(genreDTO == null){
            return null;
        }

        return Genre.builder()
                .code(genreDTO.getCode())
                .name(genreDTO.getName())
                .description(genreDTO.getDescription())
                .displayOrder(genreDTO.getDisplayOrder())
                .active(true)
                .build();
    }
}
