package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ApplicationValue;
import com.mycompany.myapp.repository.ApplicationValueRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ApplicationValue} entity.
 */
public interface ApplicationValueSearchRepository
    extends ElasticsearchRepository<ApplicationValue, Long>, ApplicationValueSearchRepositoryInternal {}

interface ApplicationValueSearchRepositoryInternal {
    Stream<ApplicationValue> search(String query);

    Stream<ApplicationValue> search(Query query);

    @Async
    void index(ApplicationValue entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ApplicationValueSearchRepositoryInternalImpl implements ApplicationValueSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ApplicationValueRepository repository;

    ApplicationValueSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ApplicationValueRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ApplicationValue> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ApplicationValue> search(Query query) {
        return elasticsearchTemplate.search(query, ApplicationValue.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ApplicationValue entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ApplicationValue.class);
    }
}
