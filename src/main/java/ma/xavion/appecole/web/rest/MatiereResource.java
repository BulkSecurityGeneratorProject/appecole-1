package ma.xavion.appecole.web.rest;
import ma.xavion.appecole.domain.Matiere;
import ma.xavion.appecole.service.MatiereService;
import ma.xavion.appecole.web.rest.errors.BadRequestAlertException;
import ma.xavion.appecole.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Matiere.
 */
@RestController
@RequestMapping("/api")
public class MatiereResource {

    private final Logger log = LoggerFactory.getLogger(MatiereResource.class);

    private static final String ENTITY_NAME = "matiere";

    private final MatiereService matiereService;

    public MatiereResource(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    /**
     * POST  /matieres : Create a new matiere.
     *
     * @param matiere the matiere to create
     * @return the ResponseEntity with status 201 (Created) and with body the new matiere, or with status 400 (Bad Request) if the matiere has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/matieres")
    public ResponseEntity<Matiere> createMatiere(@RequestBody Matiere matiere) throws URISyntaxException {
        log.debug("REST request to save Matiere : {}", matiere);
        if (matiere.getId() != null) {
            throw new BadRequestAlertException("A new matiere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Matiere result = matiereService.save(matiere);
        return ResponseEntity.created(new URI("/api/matieres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /matieres : Updates an existing matiere.
     *
     * @param matiere the matiere to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated matiere,
     * or with status 400 (Bad Request) if the matiere is not valid,
     * or with status 500 (Internal Server Error) if the matiere couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/matieres")
    public ResponseEntity<Matiere> updateMatiere(@RequestBody Matiere matiere) throws URISyntaxException {
        log.debug("REST request to update Matiere : {}", matiere);
        if (matiere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Matiere result = matiereService.save(matiere);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, matiere.getId().toString()))
            .body(result);
    }

    /**
     * GET  /matieres : get all the matieres.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of matieres in body
     */
    @GetMapping("/matieres")
    public List<Matiere> getAllMatieres(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Matieres");
        return matiereService.findAll();
    }

    /**
     * GET  /matieres/:id : get the "id" matiere.
     *
     * @param id the id of the matiere to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the matiere, or with status 404 (Not Found)
     */
    @GetMapping("/matieres/{id}")
    public ResponseEntity<Matiere> getMatiere(@PathVariable Long id) {
        log.debug("REST request to get Matiere : {}", id);
        Optional<Matiere> matiere = matiereService.findOne(id);
        return ResponseUtil.wrapOrNotFound(matiere);
    }

    /**
     * DELETE  /matieres/:id : delete the "id" matiere.
     *
     * @param id the id of the matiere to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/matieres/{id}")
    public ResponseEntity<Void> deleteMatiere(@PathVariable Long id) {
        log.debug("REST request to delete Matiere : {}", id);
        matiereService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/matieres?query=:query : search for the matiere corresponding
     * to the query.
     *
     * @param query the query of the matiere search
     * @return the result of the search
     */
    @GetMapping("/_search/matieres")
    public List<Matiere> searchMatieres(@RequestParam String query) {
        log.debug("REST request to search Matieres for query {}", query);
        return matiereService.search(query);
    }

}
