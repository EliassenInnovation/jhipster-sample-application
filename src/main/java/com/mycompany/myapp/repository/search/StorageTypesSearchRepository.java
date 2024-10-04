package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.StorageTypes;
import com.mycompany.myapp.repository.StorageTypesRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link StorageTypes} entity.
 */
public interface StorageTypesSearchRepository extends ElasticsearchRepository<StorageTypes, Long>, StorageTypesSearchRepositoryInternal {}

interface StorageTypesSearchRepositoryInternal {
    Stream<StorageTypes> search(String query);

    Stream<StorageTypes> search(Query query);

    @Async
    void index(StorageTypes entity);

    @Async
    void deleteFromIndexById(Long id);
}

class StorageTypesSearchRepositoryInternalImpl implements StorageTypesSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final StorageTypesRepository repository;

    StorageTypesSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, StorageTypesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<StorageTypes> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<StorageTypes> search(Query query) {
        return elasticsearchTemplate.search(query, StorageTypes.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(StorageTypes entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), StorageTypes.class);
    }
}
