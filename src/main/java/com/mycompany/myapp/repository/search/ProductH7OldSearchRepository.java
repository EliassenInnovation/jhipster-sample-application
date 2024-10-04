package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductH7Old;
import com.mycompany.myapp.repository.ProductH7OldRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductH7Old} entity.
 */
public interface ProductH7OldSearchRepository extends ElasticsearchRepository<ProductH7Old, Long>, ProductH7OldSearchRepositoryInternal {}

interface ProductH7OldSearchRepositoryInternal {
    Stream<ProductH7Old> search(String query);

    Stream<ProductH7Old> search(Query query);

    @Async
    void index(ProductH7Old entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductH7OldSearchRepositoryInternalImpl implements ProductH7OldSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductH7OldRepository repository;

    ProductH7OldSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ProductH7OldRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductH7Old> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductH7Old> search(Query query) {
        return elasticsearchTemplate.search(query, ProductH7Old.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductH7Old entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductH7Old.class);
    }
}
