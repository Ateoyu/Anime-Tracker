package pl.edu.pjatk.backend.service.mapper;

public interface EntityMapper<T, S> {
    T toEntity(S dto);
    S toDto(T entity);
}
