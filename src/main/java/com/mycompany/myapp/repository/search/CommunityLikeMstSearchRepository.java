package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.CommunityLikeMst;
import com.mycompany.myapp.repository.CommunityLikeMstRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link CommunityLikeMst} entity.
 */
public interface CommunityLikeMstSearchRepository
    extends ElasticsearchRepository<CommunityLikeMst, Long>, CommunityLikeMstSearchRepositoryInternal {}

interface CommunityLikeMstSearchRepositoryInternal {
    Stream<CommunityLikeMst> search(String query);

    Stream<CommunityLikeMst> search(Query query);

    @Async
    void index(CommunityLikeMst entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CommunityLikeMstSearchRepositoryInternalImpl implements CommunityLikeMstSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CommunityLikeMstRepository repository;

    CommunityLikeMstSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CommunityLikeMstRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<CommunityLikeMst> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<CommunityLikeMst> search(Query query) {
        return elasticsearchTemplate.search(query, CommunityLikeMst.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(CommunityLikeMst entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), CommunityLikeMst.class);
    }
}
