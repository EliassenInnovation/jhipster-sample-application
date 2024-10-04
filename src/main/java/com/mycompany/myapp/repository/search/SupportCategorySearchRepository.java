package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.SupportCategory;
import com.mycompany.myapp.repository.SupportCategoryRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SupportCategory} entity.
 */
public interface SupportCategorySearchRepository
    extends ElasticsearchRepository<SupportCategory, Long>, SupportCategorySearchRepositoryInternal {}

interface SupportCategorySearchRepositoryInternal {
    Stream<SupportCategory> search(String query);

    Stream<SupportCategory> search(Query query);

    @Async
    void index(SupportCategory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SupportCategorySearchRepositoryInternalImpl implements SupportCategorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SupportCategoryRepository repository;

    SupportCategorySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SupportCategoryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SupportCategory> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SupportCategory> search(Query query) {
        return elasticsearchTemplate.search(query, SupportCategory.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SupportCategory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SupportCategory.class);
    }
}
