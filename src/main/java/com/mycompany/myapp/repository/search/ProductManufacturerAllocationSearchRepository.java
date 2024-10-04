package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductManufacturerAllocation;
import com.mycompany.myapp.repository.ProductManufacturerAllocationRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductManufacturerAllocation} entity.
 */
public interface ProductManufacturerAllocationSearchRepository
    extends ElasticsearchRepository<ProductManufacturerAllocation, Long>, ProductManufacturerAllocationSearchRepositoryInternal {}

interface ProductManufacturerAllocationSearchRepositoryInternal {
    Stream<ProductManufacturerAllocation> search(String query);

    Stream<ProductManufacturerAllocation> search(Query query);

    @Async
    void index(ProductManufacturerAllocation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductManufacturerAllocationSearchRepositoryInternalImpl implements ProductManufacturerAllocationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductManufacturerAllocationRepository repository;

    ProductManufacturerAllocationSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ProductManufacturerAllocationRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductManufacturerAllocation> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductManufacturerAllocation> search(Query query) {
        return elasticsearchTemplate.search(query, ProductManufacturerAllocation.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductManufacturerAllocation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductManufacturerAllocation.class);
    }
}
