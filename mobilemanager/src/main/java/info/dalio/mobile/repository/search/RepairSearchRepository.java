package info.dalio.mobile.repository.search;

import info.dalio.mobile.domain.Repair;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Repair entity.
 */
public interface RepairSearchRepository extends ElasticsearchRepository<Repair, Long> {
}
