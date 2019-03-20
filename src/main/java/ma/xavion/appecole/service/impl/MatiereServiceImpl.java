package ma.xavion.appecole.service.impl;

import ma.xavion.appecole.service.MatiereService;
import ma.xavion.appecole.domain.Matiere;
import ma.xavion.appecole.repository.MatiereRepository;
import ma.xavion.appecole.repository.search.MatiereSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Matiere.
 */
@Service
@Transactional
public class MatiereServiceImpl implements MatiereService {

    private final Logger log = LoggerFactory.getLogger(MatiereServiceImpl.class);

    private final MatiereRepository matiereRepository;

    private final MatiereSearchRepository matiereSearchRepository;

    public MatiereServiceImpl(MatiereRepository matiereRepository, MatiereSearchRepository matiereSearchRepository) {
        this.matiereRepository = matiereRepository;
        this.matiereSearchRepository = matiereSearchRepository;
    }

    /**
     * Save a matiere.
     *
     * @param matiere the entity to save
     * @return the persisted entity
     */
    @Override
    public Matiere save(Matiere matiere) {
        log.debug("Request to save Matiere : {}", matiere);
        Matiere result = matiereRepository.save(matiere);
        matiereSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the matieres.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Matiere> findAll() {
        log.debug("Request to get all Matieres");
        return matiereRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the Matiere with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<Matiere> findAllWithEagerRelationships(Pageable pageable) {
        return matiereRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one matiere by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Matiere> findOne(Long id) {
        log.debug("Request to get Matiere : {}", id);
        return matiereRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the matiere by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Matiere : {}", id);
        matiereRepository.deleteById(id);
        matiereSearchRepository.deleteById(id);
    }

    /**
     * Search for the matiere corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Matiere> search(String query) {
        log.debug("Request to search Matieres for query {}", query);
        return StreamSupport
            .stream(matiereSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
