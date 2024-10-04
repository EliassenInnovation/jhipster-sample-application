package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.SuggestedProducts;
import com.mycompany.myapp.repository.SuggestedProductsRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SuggestedProducts} entity.
 */
public interface SuggestedProductsSearchRepository
    extends ElasticsearchRepository<SuggestedProducts, Long>, SuggestedProductsSearchRepositoryInternal {}

interface SuggestedProductsSearchRepositoryInternal {
    Stream<SuggestedProducts> search(String query);

    Stream<SuggestedProducts> search(Query query);

    @Async
    void index(SuggestedProducts entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SuggestedProductsSearchRepositoryInternalImpl implements SuggestedProductsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SuggestedProductsRepository repository;

    SuggestedProductsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SuggestedProductsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SuggestedProducts> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SuggestedProducts> search(Query query) {
        return elasticsearchTemplate.search(query, SuggestedProducts.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SuggestedProducts entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SuggestedProducts.class);
    }
}
