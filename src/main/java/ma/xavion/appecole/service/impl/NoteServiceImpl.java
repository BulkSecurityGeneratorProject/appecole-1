package ma.xavion.appecole.service.impl;

import ma.xavion.appecole.service.NoteService;
import ma.xavion.appecole.domain.Note;
import ma.xavion.appecole.repository.NoteRepository;
import ma.xavion.appecole.repository.search.NoteSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Note.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    private final NoteSearchRepository noteSearchRepository;

    public NoteServiceImpl(NoteRepository noteRepository, NoteSearchRepository noteSearchRepository) {
        this.noteRepository = noteRepository;
        this.noteSearchRepository = noteSearchRepository;
    }

    /**
     * Save a note.
     *
     * @param note the entity to save
     * @return the persisted entity
     */
    @Override
    public Note save(Note note) {
        log.debug("Request to save Note : {}", note);
        Note result = noteRepository.save(note);
        noteSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the notes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Note> findAll(Pageable pageable) {
        log.debug("Request to get all Notes");
        return noteRepository.findAll(pageable);
    }


    /**
     * Get one note by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Note> findOne(Long id) {
        log.debug("Request to get Note : {}", id);
        return noteRepository.findById(id);
    }

    /**
     * Delete the note by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Note : {}", id);
        noteRepository.deleteById(id);
        noteSearchRepository.deleteById(id);
    }

    /**
     * Search for the note corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Note> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Notes for query {}", query);
        return noteSearchRepository.search(queryStringQuery(query), pageable);    }
}
