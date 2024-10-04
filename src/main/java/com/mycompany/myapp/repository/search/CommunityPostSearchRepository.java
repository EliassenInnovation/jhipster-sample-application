package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.CommunityPost;
import com.mycompany.myapp.repository.CommunityPostRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link CommunityPost} entity.
 */
public interface CommunityPostSearchRepository
    extends ElasticsearchRepository<CommunityPost, Long>, CommunityPostSearchRepositoryInternal {}

interface CommunityPostSearchRepositoryInternal {
    Stream<CommunityPost> search(String query);

    Stream<CommunityPost> search(Query query);

    @Async
    void index(CommunityPost entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CommunityPostSearchRepositoryInternalImpl implements CommunityPostSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CommunityPostRepository repository;

    CommunityPostSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CommunityPostRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<CommunityPost> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<CommunityPost> search(Query query) {
        return elasticsearchTemplate.search(query, CommunityPost.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(CommunityPost entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), CommunityPost.class);
    }
}
