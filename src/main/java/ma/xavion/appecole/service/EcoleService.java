package ma.xavion.appecole.service;

import ma.xavion.appecole.domain.Ecole;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Ecole.
 */
public interface EcoleService {

    /**
     * Save a ecole.
     *
     * @param ecole the entity to save
     * @return the persisted entity
     */
    Ecole save(Ecole ecole);

    /**
     * Get all the ecoles.
     *
     * @return the list of entities
     */
    List<Ecole> findAll();


    /**
     * Get the "id" ecole.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Ecole> findOne(Long id);

    /**
     * Delete the "id" ecole.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the ecole corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Ecole> search(String query);
}
