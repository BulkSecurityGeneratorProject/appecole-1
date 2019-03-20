package ma.xavion.appecole.repository.search;

import ma.xavion.appecole.domain.Note;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Note entity.
 */
public interface NoteSearchRepository extends ElasticsearchRepository<Note, Long> {
}
