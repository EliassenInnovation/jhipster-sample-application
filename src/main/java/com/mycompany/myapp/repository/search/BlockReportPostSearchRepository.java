package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.BlockReportPost;
import com.mycompany.myapp.repository.BlockReportPostRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link BlockReportPost} entity.
 */
public interface BlockReportPostSearchRepository
    extends ElasticsearchRepository<BlockReportPost, Long>, BlockReportPostSearchRepositoryInternal {}

interface BlockReportPostSearchRepositoryInternal {
    Stream<BlockReportPost> search(String query);

    Stream<BlockReportPost> search(Query query);

    @Async
    void index(BlockReportPost entity);

    @Async
    void deleteFromIndexById(Long id);
}

class BlockReportPostSearchRepositoryInternalImpl implements BlockReportPostSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final BlockReportPostRepository repository;

    BlockReportPostSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, BlockReportPostRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<BlockReportPost> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<BlockReportPost> search(Query query) {
        return elasticsearchTemplate.search(query, BlockReportPost.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(BlockReportPost entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), BlockReportPost.class);
    }
}
