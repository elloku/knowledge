package mediinfo.dev.knowledge.repository;

import mediinfo.dev.knowledge.entity.DocModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IRepository extends ElasticsearchRepository<DocModel ,String> {
}
