package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductsToUpdate;
import com.mycompany.myapp.repository.ProductsToUpdateRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductsToUpdate} entity.
 */
public interface ProductsToUpdateSearchRepository
    extends ElasticsearchRepository<ProductsToUpdate, Long>, ProductsToUpdateSearchRepositoryInternal {}

interface ProductsToUpdateSearchRepositoryInternal {
    Stream<ProductsToUpdate> search(String query);

    Stream<ProductsToUpdate> search(Query query);

    @Async
    void index(ProductsToUpdate entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductsToUpdateSearchRepositoryInternalImpl implements ProductsToUpdateSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductsToUpdateRepository repository;

    ProductsToUpdateSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ProductsToUpdateRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductsToUpdate> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductsToUpdate> search(Query query) {
        return elasticsearchTemplate.search(query, ProductsToUpdate.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductsToUpdate entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductsToUpdate.class);
    }
}
