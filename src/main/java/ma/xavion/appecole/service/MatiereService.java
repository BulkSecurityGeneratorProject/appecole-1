package ma.xavion.appecole.service;

import ma.xavion.appecole.domain.Matiere;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Matiere.
 */
public interface MatiereService {

    /**
     * Save a matiere.
     *
     * @param matiere the entity to save
     * @return the persisted entity
     */
    Matiere save(Matiere matiere);

    /**
     * Get all the matieres.
     *
     * @return the list of entities
     */
    List<Matiere> findAll();

    /**
     * Get all the Matiere with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    Page<Matiere> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" matiere.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Matiere> findOne(Long id);

    /**
     * Delete the "id" matiere.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the matiere corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @return the list of entities
     */
    List<Matiere> search(String query);
}
