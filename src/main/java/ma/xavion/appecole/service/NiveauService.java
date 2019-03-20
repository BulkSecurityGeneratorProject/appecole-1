package ma.xavion.appecole.service;

import ma.xavion.appecole.domain.Niveau;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Niveau.
 */
public interface NiveauService {

    /**
     * Save a niveau.
     *
     * @param niveau the entity to save
     * @return the persisted entity
     */
    Niveau save(Niveau niveau);

    /**
     * Get all the niveaus.
     *
     * @return the list of entities
     */
    List<Niveau> findAll();


    /**
     * Get the "id" niveau.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Niveau> findOne(Long id);

    /**
     * Delete the "id" niveau.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the niveau corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Niveau> search(String query);
}
