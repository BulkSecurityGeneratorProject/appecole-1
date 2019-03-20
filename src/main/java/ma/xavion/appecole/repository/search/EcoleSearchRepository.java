package ma.xavion.appecole.repository.search;

import ma.xavion.appecole.domain.Ecole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Ecole entity.
 */
public interface EcoleSearchRepository extends ElasticsearchRepository<Ecole, Long> {
}
