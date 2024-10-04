package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.SetMappings;
import com.mycompany.myapp.repository.SetMappingsRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SetMappings} entity.
 */
public interface SetMappingsSearchRepository extends ElasticsearchRepository<SetMappings, Long>, SetMappingsSearchRepositoryInternal {}

interface SetMappingsSearchRepositoryInternal {
    Stream<SetMappings> search(String query);

    Stream<SetMappings> search(Query query);

    @Async
    void index(SetMappings entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SetMappingsSearchRepositoryInternalImpl implements SetMappingsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SetMappingsRepository repository;

    SetMappingsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SetMappingsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SetMappings> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SetMappings> search(Query query) {
        return elasticsearchTemplate.search(query, SetMappings.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SetMappings entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SetMappings.class);
    }
}
