package com.library.ReadFlow.payload.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchRequest {

    private String searchTerm;
    private Long genreId;
    private Boolean availableOnly;
    private Boolean activeOnly;
    @Min(0)
    private Integer page=0;
    @Min(1)
    @Max(100)
    private Integer size=20;
    private String sortBy="createdAt";
    private String sortDirection="DESC";
    
}
