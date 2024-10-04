package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductChangeHistory;
import com.mycompany.myapp.repository.ProductChangeHistoryRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductChangeHistory} entity.
 */
public interface ProductChangeHistorySearchRepository
    extends ElasticsearchRepository<ProductChangeHistory, Long>, ProductChangeHistorySearchRepositoryInternal {}

interface ProductChangeHistorySearchRepositoryInternal {
    Stream<ProductChangeHistory> search(String query);

    Stream<ProductChangeHistory> search(Query query);

    @Async
    void index(ProductChangeHistory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductChangeHistorySearchRepositoryInternalImpl implements ProductChangeHistorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductChangeHistoryRepository repository;

    ProductChangeHistorySearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ProductChangeHistoryRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductChangeHistory> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductChangeHistory> search(Query query) {
        return elasticsearchTemplate.search(query, ProductChangeHistory.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductChangeHistory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductChangeHistory.class);
    }
}
