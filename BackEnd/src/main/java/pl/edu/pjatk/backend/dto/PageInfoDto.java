package pl.edu.pjatk.backend.dto;

public record PageInfoDto(
        int currentPage,
        boolean hasNextPage
) {
}
