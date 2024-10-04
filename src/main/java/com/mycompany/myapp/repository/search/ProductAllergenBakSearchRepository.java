package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.ProductAllergenBak;
import com.mycompany.myapp.repository.ProductAllergenBakRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link ProductAllergenBak} entity.
 */
public interface ProductAllergenBakSearchRepository
    extends ElasticsearchRepository<ProductAllergenBak, Long>, ProductAllergenBakSearchRepositoryInternal {}

interface ProductAllergenBakSearchRepositoryInternal {
    Stream<ProductAllergenBak> search(String query);

    Stream<ProductAllergenBak> search(Query query);

    @Async
    void index(ProductAllergenBak entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ProductAllergenBakSearchRepositoryInternalImpl implements ProductAllergenBakSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ProductAllergenBakRepository repository;

    ProductAllergenBakSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ProductAllergenBakRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<ProductAllergenBak> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<ProductAllergenBak> search(Query query) {
        return elasticsearchTemplate.search(query, ProductAllergenBak.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(ProductAllergenBak entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ProductAllergenBak.class);
    }
}
