package com.example.demo.repository;

import com.example.demo.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {


    Optional<Media> findByAnilistId(Integer anilistId);

    @Query("SELECT m FROM Media m WHERE " +
            "(m.startDate.year * 10000 + m.startDate.month * 100 + m.startDate.day) " +
            "BETWEEN :fromDate AND :toDate")
    List<Media> findByDateRange(@Param("fromDate") Integer fromDate, @Param("toDate") Integer toDate);

    @NativeQuery("SELECT DISTINCT start_year FROM media")
    List<Integer> getAllMediaYears();

    @NativeQuery("SELECT DISTINCT episodes FROM media ORDER BY episodes")
    List<Integer> getAllMediaEpisodes();
}