package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductDistributorAllocation;
import com.mycompany.myapp.repository.ProductDistributorAllocationRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductDistributorAllocation} entity.
 */
public interface ProductDistributorAllocationSearchRepository
    extends ElasticsearchRepository<ProductDistributorAllocation, Long>, ProductDistributorAllocationSearchRepositoryInternal {}

interface ProductDistributorAllocationSearchRepositoryInternal {
    Stream<ProductDistributorAllocation> search(String query);

    Stream<ProductDistributorAllocation> search(Query query);

    @Async
    void index(ProductDistributorAllocation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductDistributorAllocationSearchRepositoryInternalImpl implements ProductDistributorAllocationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductDistributorAllocationRepository repository;

    ProductDistributorAllocationSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ProductDistributorAllocationRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductDistributorAllocation> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductDistributorAllocation> search(Query query) {
        return elasticsearchTemplate.search(query, ProductDistributorAllocation.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductDistributorAllocation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductDistributorAllocation.class);
    }
}
