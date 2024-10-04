package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductDistrictAllocation;
import com.mycompany.myapp.repository.ProductDistrictAllocationRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductDistrictAllocation} entity.
 */
public interface ProductDistrictAllocationSearchRepository
    extends ElasticsearchRepository<ProductDistrictAllocation, Long>, ProductDistrictAllocationSearchRepositoryInternal {}

interface ProductDistrictAllocationSearchRepositoryInternal {
    Stream<ProductDistrictAllocation> search(String query);

    Stream<ProductDistrictAllocation> search(Query query);

    @Async
    void index(ProductDistrictAllocation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductDistrictAllocationSearchRepositoryInternalImpl implements ProductDistrictAllocationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductDistrictAllocationRepository repository;

    ProductDistrictAllocationSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ProductDistrictAllocationRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductDistrictAllocation> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductDistrictAllocation> search(Query query) {
        return elasticsearchTemplate.search(query, ProductDistrictAllocation.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductDistrictAllocation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductDistrictAllocation.class);
    }
}
