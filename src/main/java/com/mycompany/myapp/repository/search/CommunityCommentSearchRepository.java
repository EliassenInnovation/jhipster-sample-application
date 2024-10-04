package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.CommunityComment;
import com.mycompany.myapp.repository.CommunityCommentRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link CommunityComment} entity.
 */
public interface CommunityCommentSearchRepository
    extends ElasticsearchRepository<CommunityComment, Long>, CommunityCommentSearchRepositoryInternal {}

interface CommunityCommentSearchRepositoryInternal {
    Stream<CommunityComment> search(String query);

    Stream<CommunityComment> search(Query query);

    @Async
    void index(CommunityComment entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CommunityCommentSearchRepositoryInternalImpl implements CommunityCommentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CommunityCommentRepository repository;

    CommunityCommentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CommunityCommentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<CommunityComment> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<CommunityComment> search(Query query) {
        return elasticsearchTemplate.search(query, CommunityComment.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(CommunityComment entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), CommunityComment.class);
    }
}
