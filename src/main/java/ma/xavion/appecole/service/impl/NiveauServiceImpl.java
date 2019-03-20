package ma.xavion.appecole.service.impl;

import ma.xavion.appecole.service.NiveauService;
import ma.xavion.appecole.domain.Niveau;
import ma.xavion.appecole.repository.NiveauRepository;
import ma.xavion.appecole.repository.search.NiveauSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Niveau.
 */
@Service
@Transactional
public class NiveauServiceImpl implements NiveauService {

    private final Logger log = LoggerFactory.getLogger(NiveauServiceImpl.class);

    private final NiveauRepository niveauRepository;

    private final NiveauSearchRepository niveauSearchRepository;

    public NiveauServiceImpl(NiveauRepository niveauRepository, NiveauSearchRepository niveauSearchRepository) {
        this.niveauRepository = niveauRepository;
        this.niveauSearchRepository = niveauSearchRepository;
    }

    /**
     * Save a niveau.
     *
     * @param niveau the entity to save
     * @return the persisted entity
     */
    @Override
    public Niveau save(Niveau niveau) {
        log.debug("Request to save Niveau : {}", niveau);
        Niveau result = niveauRepository.save(niveau);
        niveauSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the niveaus.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Niveau> findAll() {
        log.debug("Request to get all Niveaus");
        return niveauRepository.findAll();
    }


    /**
     * Get one niveau by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Niveau> findOne(Long id) {
        log.debug("Request to get Niveau : {}", id);
        return niveauRepository.findById(id);
    }

    /**
     * Delete the niveau by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Niveau : {}", id);
        niveauRepository.deleteById(id);
        niveauSearchRepository.deleteById(id);
    }

    /**
     * Search for the niveau corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Niveau> search(String query) {
        log.debug("Request to search Niveaus for query {}", query);
        return StreamSupport
            .stream(niveauSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
