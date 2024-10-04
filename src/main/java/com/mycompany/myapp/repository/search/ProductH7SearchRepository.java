package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductH7;
import com.mycompany.myapp.repository.ProductH7Repository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductH7} entity.
 */
public interface ProductH7SearchRepository extends ElasticsearchRepository<ProductH7, Long>, ProductH7SearchRepositoryInternal {}

interface ProductH7SearchRepositoryInternal {
    Stream<ProductH7> search(String query);

    Stream<ProductH7> search(Query query);

    @Async
    void index(ProductH7 entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductH7SearchRepositoryInternalImpl implements ProductH7SearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductH7Repository repository;

    ProductH7SearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ProductH7Repository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductH7> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductH7> search(Query query) {
        return elasticsearchTemplate.search(query, ProductH7.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductH7 entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductH7.class);
    }
}
