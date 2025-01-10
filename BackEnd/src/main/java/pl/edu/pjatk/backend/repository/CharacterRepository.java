package pl.edu.pjatk.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pjatk.backend.model.Character;

import java.util.Optional;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Integer> {
    Optional<Character> findByAnilistId(Integer anilistId);
}
