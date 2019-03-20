package ma.xavion.appecole.service.impl;

import ma.xavion.appecole.service.EcoleService;
import ma.xavion.appecole.domain.Ecole;
import ma.xavion.appecole.repository.EcoleRepository;
import ma.xavion.appecole.repository.search.EcoleSearchRepository;
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
 * Service Implementation for managing Ecole.
 */
@Service
@Transactional
public class EcoleServiceImpl implements EcoleService {

    private final Logger log = LoggerFactory.getLogger(EcoleServiceImpl.class);

    private final EcoleRepository ecoleRepository;

    private final EcoleSearchRepository ecoleSearchRepository;

    public EcoleServiceImpl(EcoleRepository ecoleRepository, EcoleSearchRepository ecoleSearchRepository) {
        this.ecoleRepository = ecoleRepository;
        this.ecoleSearchRepository = ecoleSearchRepository;
    }

    /**
     * Save a ecole.
     *
     * @param ecole the entity to save
     * @return the persisted entity
     */
    @Override
    public Ecole save(Ecole ecole) {
        log.debug("Request to save Ecole : {}", ecole);
        Ecole result = ecoleRepository.save(ecole);
        ecoleSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the ecoles.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ecole> findAll() {
        log.debug("Request to get all Ecoles");
        return ecoleRepository.findAll();
    }


    /**
     * Get one ecole by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ecole> findOne(Long id) {
        log.debug("Request to get Ecole : {}", id);
        return ecoleRepository.findById(id);
    }

    /**
     * Delete the ecole by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ecole : {}", id);
        ecoleRepository.deleteById(id);
        ecoleSearchRepository.deleteById(id);
    }

    /**
     * Search for the ecole corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Ecole> search(String query) {
        log.debug("Request to search Ecoles for query {}", query);
        return StreamSupport
            .stream(ecoleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
