package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.PrivacyType;
import com.mycompany.myapp.repository.PrivacyTypeRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link PrivacyType} entity.
 */
public interface PrivacyTypeSearchRepository extends ElasticsearchRepository<PrivacyType, Long>, PrivacyTypeSearchRepositoryInternal {}

interface PrivacyTypeSearchRepositoryInternal {
    Stream<PrivacyType> search(String query);

    Stream<PrivacyType> search(Query query);

    @Async
    void index(PrivacyType entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PrivacyTypeSearchRepositoryInternalImpl implements PrivacyTypeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PrivacyTypeRepository repository;

    PrivacyTypeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PrivacyTypeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<PrivacyType> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<PrivacyType> search(Query query) {
        return elasticsearchTemplate.search(query, PrivacyType.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(PrivacyType entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PrivacyType.class);
    }
}
