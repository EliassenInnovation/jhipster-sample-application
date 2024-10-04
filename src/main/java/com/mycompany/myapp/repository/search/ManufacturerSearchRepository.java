package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Manufacturer;
import com.mycompany.myapp.repository.ManufacturerRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Manufacturer} entity.
 */
public interface ManufacturerSearchRepository extends ElasticsearchRepository<Manufacturer, Long>, ManufacturerSearchRepositoryInternal {}

interface ManufacturerSearchRepositoryInternal {
    Stream<Manufacturer> search(String query);

    Stream<Manufacturer> search(Query query);

    @Async
    void index(Manufacturer entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ManufacturerSearchRepositoryInternalImpl implements ManufacturerSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ManufacturerRepository repository;

    ManufacturerSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ManufacturerRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Manufacturer> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Manufacturer> search(Query query) {
        return elasticsearchTemplate.search(query, Manufacturer.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Manufacturer entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Manufacturer.class);
    }
}
