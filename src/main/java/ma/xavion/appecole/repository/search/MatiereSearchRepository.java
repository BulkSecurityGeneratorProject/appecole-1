package ma.xavion.appecole.repository.search;

import ma.xavion.appecole.domain.Matiere;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Matiere entity.
 */
public interface MatiereSearchRepository extends ElasticsearchRepository<Matiere, Long> {
}
