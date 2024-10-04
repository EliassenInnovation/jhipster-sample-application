package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.IocCategory;
import com.mycompany.myapp.repository.IocCategoryRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link IocCategory} entity.
 */
public interface IocCategorySearchRepository extends ElasticsearchRepository<IocCategory, Long>, IocCategorySearchRepositoryInternal {}

interface IocCategorySearchRepositoryInternal {
    Stream<IocCategory> search(String query);

    Stream<IocCategory> search(Query query);

    @Async
    void index(IocCategory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class IocCategorySearchRepositoryInternalImpl implements IocCategorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final IocCategoryRepository repository;

    IocCategorySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, IocCategoryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<IocCategory> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<IocCategory> search(Query query) {
        return elasticsearchTemplate.search(query, IocCategory.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(IocCategory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), IocCategory.class);
    }
}
