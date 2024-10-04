package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductH7New;
import com.mycompany.myapp.repository.ProductH7NewRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductH7New} entity.
 */
public interface ProductH7NewSearchRepository extends ElasticsearchRepository<ProductH7New, Long>, ProductH7NewSearchRepositoryInternal {}

interface ProductH7NewSearchRepositoryInternal {
    Stream<ProductH7New> search(String query);

    Stream<ProductH7New> search(Query query);

    @Async
    void index(ProductH7New entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductH7NewSearchRepositoryInternalImpl implements ProductH7NewSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductH7NewRepository repository;

    ProductH7NewSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ProductH7NewRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductH7New> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductH7New> search(Query query) {
        return elasticsearchTemplate.search(query, ProductH7New.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductH7New entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductH7New.class);
    }
}
