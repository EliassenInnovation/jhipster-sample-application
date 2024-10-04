package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.PostTypes;
import com.mycompany.myapp.repository.PostTypesRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link PostTypes} entity.
 */
public interface PostTypesSearchRepository extends ElasticsearchRepository<PostTypes, Long>, PostTypesSearchRepositoryInternal {}

interface PostTypesSearchRepositoryInternal {
    Stream<PostTypes> search(String query);

    Stream<PostTypes> search(Query query);

    @Async
    void index(PostTypes entity);

    @Async
    void deleteFromIndexById(Long id);
}

class PostTypesSearchRepositoryInternalImpl implements PostTypesSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final PostTypesRepository repository;

    PostTypesSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, PostTypesRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<PostTypes> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<PostTypes> search(Query query) {
        return elasticsearchTemplate.search(query, PostTypes.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(PostTypes entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), PostTypes.class);
    }
}
