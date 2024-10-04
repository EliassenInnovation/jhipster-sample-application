package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductImageBeforeApprove;
import com.mycompany.myapp.repository.ProductImageBeforeApproveRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductImageBeforeApprove} entity.
 */
public interface ProductImageBeforeApproveSearchRepository
    extends ElasticsearchRepository<ProductImageBeforeApprove, Long>, ProductImageBeforeApproveSearchRepositoryInternal {}

interface ProductImageBeforeApproveSearchRepositoryInternal {
    Stream<ProductImageBeforeApprove> search(String query);

    Stream<ProductImageBeforeApprove> search(Query query);

    @Async
    void index(ProductImageBeforeApprove entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductImageBeforeApproveSearchRepositoryInternalImpl implements ProductImageBeforeApproveSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductImageBeforeApproveRepository repository;

    ProductImageBeforeApproveSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ProductImageBeforeApproveRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductImageBeforeApprove> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductImageBeforeApprove> search(Query query) {
        return elasticsearchTemplate.search(query, ProductImageBeforeApprove.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductImageBeforeApprove entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductImageBeforeApprove.class);
    }
}
