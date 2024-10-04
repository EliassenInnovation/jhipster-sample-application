package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.MonthlyNumbers;
import com.mycompany.myapp.repository.MonthlyNumbersRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link MonthlyNumbers} entity.
 */
public interface MonthlyNumbersSearchRepository
    extends ElasticsearchRepository<MonthlyNumbers, Long>, MonthlyNumbersSearchRepositoryInternal {}

interface MonthlyNumbersSearchRepositoryInternal {
    Stream<MonthlyNumbers> search(String query);

    Stream<MonthlyNumbers> search(Query query);

    @Async
    void index(MonthlyNumbers entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MonthlyNumbersSearchRepositoryInternalImpl implements MonthlyNumbersSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MonthlyNumbersRepository repository;

    MonthlyNumbersSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MonthlyNumbersRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<MonthlyNumbers> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<MonthlyNumbers> search(Query query) {
        return elasticsearchTemplate.search(query, MonthlyNumbers.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(MonthlyNumbers entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MonthlyNumbers.class);
    }
}
