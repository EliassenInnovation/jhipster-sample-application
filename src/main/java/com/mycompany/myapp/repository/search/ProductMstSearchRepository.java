package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductMst;
import com.mycompany.myapp.repository.ProductMstRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductMst} entity.
 */
public interface ProductMstSearchRepository extends ElasticsearchRepository<ProductMst, Long>, ProductMstSearchRepositoryInternal {}

interface ProductMstSearchRepositoryInternal {
    Stream<ProductMst> search(String query);

    Stream<ProductMst> search(Query query);

    @Async
    void index(ProductMst entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductMstSearchRepositoryInternalImpl implements ProductMstSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductMstRepository repository;

    ProductMstSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ProductMstRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductMst> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductMst> search(Query query) {
        return elasticsearchTemplate.search(query, ProductMst.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductMst entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductMst.class);
    }
}
