package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.SubCategory;
import com.mycompany.myapp.repository.SubCategoryRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SubCategory} entity.
 */
public interface SubCategorySearchRepository extends ElasticsearchRepository<SubCategory, Long>, SubCategorySearchRepositoryInternal {}

interface SubCategorySearchRepositoryInternal {
    Stream<SubCategory> search(String query);

    Stream<SubCategory> search(Query query);

    @Async
    void index(SubCategory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SubCategorySearchRepositoryInternalImpl implements SubCategorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SubCategoryRepository repository;

    SubCategorySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SubCategoryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SubCategory> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SubCategory> search(Query query) {
        return elasticsearchTemplate.search(query, SubCategory.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SubCategory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SubCategory.class);
    }
}
