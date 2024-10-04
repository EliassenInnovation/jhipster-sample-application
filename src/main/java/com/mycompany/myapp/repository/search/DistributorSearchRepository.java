package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Distributor;
import com.mycompany.myapp.repository.DistributorRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Distributor} entity.
 */
public interface DistributorSearchRepository extends ElasticsearchRepository<Distributor, Long>, DistributorSearchRepositoryInternal {}

interface DistributorSearchRepositoryInternal {
    Stream<Distributor> search(String query);

    Stream<Distributor> search(Query query);

    @Async
    void index(Distributor entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DistributorSearchRepositoryInternalImpl implements DistributorSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DistributorRepository repository;

    DistributorSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DistributorRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Distributor> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Distributor> search(Query query) {
        return elasticsearchTemplate.search(query, Distributor.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Distributor entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Distributor.class);
    }
}
