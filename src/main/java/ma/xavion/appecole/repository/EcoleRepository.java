package ma.xavion.appecole.repository;

import ma.xavion.appecole.domain.Ecole;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Ecole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EcoleRepository extends JpaRepository<Ecole, Long> {

    @Query("select ecole from Ecole ecole where ecole.user.login = ?#{principal.username}")
    List<Ecole> findByUserIsCurrentUser();

}
