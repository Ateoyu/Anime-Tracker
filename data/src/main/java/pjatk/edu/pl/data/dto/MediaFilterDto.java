package pjatk.edu.pl.data.dto;

import lombok.Data;

@Data
public class MediaFilterDto {
    private String genreFilter;
    private String yearFilter;
    private String episodeFilter;
    private int page;
    
    public boolean hasActiveFilters() {
        return !"All".equals(genreFilter) ||
               !"All".equals(yearFilter) ||
               !"All".equals(episodeFilter);
    }
} 