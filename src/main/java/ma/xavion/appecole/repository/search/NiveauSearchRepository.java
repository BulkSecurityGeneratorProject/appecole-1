package ma.xavion.appecole.repository.search;

import ma.xavion.appecole.domain.Niveau;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Niveau entity.
 */
public interface NiveauSearchRepository extends ElasticsearchRepository<Niveau, Long> {
}
