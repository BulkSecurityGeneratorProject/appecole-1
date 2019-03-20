package ma.xavion.appecole.web.rest;
import ma.xavion.appecole.domain.Ecole;
import ma.xavion.appecole.service.EcoleService;
import ma.xavion.appecole.web.rest.errors.BadRequestAlertException;
import ma.xavion.appecole.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Ecole.
 */
@RestController
@RequestMapping("/api")
public class EcoleResource {

    private final Logger log = LoggerFactory.getLogger(EcoleResource.class);

    private static final String ENTITY_NAME = "ecole";

    private final EcoleService ecoleService;

    public EcoleResource(EcoleService ecoleService) {
        this.ecoleService = ecoleService;
    }

    /**
     * POST  /ecoles : Create a new ecole.
     *
     * @param ecole the ecole to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ecole, or with status 400 (Bad Request) if the ecole has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ecoles")
    public ResponseEntity<Ecole> createEcole(@Valid @RequestBody Ecole ecole) throws URISyntaxException {
        log.debug("REST request to save Ecole : {}", ecole);
        if (ecole.getId() != null) {
            throw new BadRequestAlertException("A new ecole cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ecole result = ecoleService.save(ecole);
        return ResponseEntity.created(new URI("/api/ecoles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ecoles : Updates an existing ecole.
     *
     * @param ecole the ecole to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ecole,
     * or with status 400 (Bad Request) if the ecole is not valid,
     * or with status 500 (Internal Server Error) if the ecole couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ecoles")
    public ResponseEntity<Ecole> updateEcole(@Valid @RequestBody Ecole ecole) throws URISyntaxException {
        log.debug("REST request to update Ecole : {}", ecole);
        if (ecole.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ecole result = ecoleService.save(ecole);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ecole.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ecoles : get all the ecoles.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ecoles in body
     */
    @GetMapping("/ecoles")
    public List<Ecole> getAllEcoles() {
        log.debug("REST request to get all Ecoles");
        return ecoleService.findAll();
    }

    /**
     * GET  /ecoles/:id : get the "id" ecole.
     *
     * @param id the id of the ecole to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ecole, or with status 404 (Not Found)
     */
    @GetMapping("/ecoles/{id}")
    public ResponseEntity<Ecole> getEcole(@PathVariable Long id) {
        log.debug("REST request to get Ecole : {}", id);
        Optional<Ecole> ecole = ecoleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ecole);
    }

    /**
     * DELETE  /ecoles/:id : delete the "id" ecole.
     *
     * @param id the id of the ecole to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ecoles/{id}")
    public ResponseEntity<Void> deleteEcole(@PathVariable Long id) {
        log.debug("REST request to delete Ecole : {}", id);
        ecoleService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/ecoles?query=:query : search for the ecole corresponding
     * to the query.
     *
     * @param query the query of the ecole search
     * @return the result of the search
     */
    @GetMapping("/_search/ecoles")
    public List<Ecole> searchEcoles(@RequestParam String query) {
        log.debug("REST request to search Ecoles for query {}", query);
        return ecoleService.search(query);
    }

}
