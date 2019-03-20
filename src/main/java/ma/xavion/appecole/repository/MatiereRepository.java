package ma.xavion.appecole.repository;

import ma.xavion.appecole.domain.Matiere;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Matiere entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MatiereRepository extends JpaRepository<Matiere, Long> {

    @Query(value = "select distinct matiere from Matiere matiere left join fetch matiere.niveaus",
        countQuery = "select count(distinct matiere) from Matiere matiere")
    Page<Matiere> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct matiere from Matiere matiere left join fetch matiere.niveaus")
    List<Matiere> findAllWithEagerRelationships();

    @Query("select matiere from Matiere matiere left join fetch matiere.niveaus where matiere.id =:id")
    Optional<Matiere> findOneWithEagerRelationships(@Param("id") Long id);

}
