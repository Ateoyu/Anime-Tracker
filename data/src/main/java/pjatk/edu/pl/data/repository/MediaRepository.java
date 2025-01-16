package pjatk.edu.pl.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import pjatk.edu.pl.data.model.Media;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer>, 
                                       QueryByExampleExecutor<Media> {


    Optional<Media> findByAnilistId(Integer anilistId);

    @Query("SELECT m FROM Media m WHERE " +
            "(m.startDate.year * 10000 + m.startDate.month * 100 + m.startDate.day) " +
            "BETWEEN :fromDate AND :toDate")
    List<Media> findByDateRange(@Param("fromDate") Integer fromDate, @Param("toDate") Integer toDate);

    @NativeQuery("SELECT DISTINCT start_year FROM media")
    List<Integer> getAllMediaYears();

    @NativeQuery("SELECT DISTINCT episodes FROM media ORDER BY episodes")
    List<Integer> getAllMediaEpisodes();

    @Query("SELECT DISTINCT m FROM Media m JOIN m.genres g WHERE g.name = :genreName")
    Page<Media> findByGenreName(@Param("genreName") String genreName, Pageable pageable);
}