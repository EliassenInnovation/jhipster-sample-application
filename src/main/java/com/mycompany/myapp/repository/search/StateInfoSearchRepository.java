package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.StateInfo;
import com.mycompany.myapp.repository.StateInfoRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link StateInfo} entity.
 */
public interface StateInfoSearchRepository extends ElasticsearchRepository<StateInfo, Long>, StateInfoSearchRepositoryInternal {}

interface StateInfoSearchRepositoryInternal {
    Stream<StateInfo> search(String query);

    Stream<StateInfo> search(Query query);

    @Async
    void index(StateInfo entity);

    @Async
    void deleteFromIndexById(Long id);
}

class StateInfoSearchRepositoryInternalImpl implements StateInfoSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final StateInfoRepository repository;

    StateInfoSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, StateInfoRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<StateInfo> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<StateInfo> search(Query query) {
        return elasticsearchTemplate.search(query, StateInfo.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(StateInfo entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), StateInfo.class);
    }
}
