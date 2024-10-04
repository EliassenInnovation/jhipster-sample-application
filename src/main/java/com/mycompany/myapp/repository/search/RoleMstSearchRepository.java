package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.RoleMst;
import com.mycompany.myapp.repository.RoleMstRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link RoleMst} entity.
 */
public interface RoleMstSearchRepository extends ElasticsearchRepository<RoleMst, Long>, RoleMstSearchRepositoryInternal {}

interface RoleMstSearchRepositoryInternal {
    Stream<RoleMst> search(String query);

    Stream<RoleMst> search(Query query);

    @Async
    void index(RoleMst entity);

    @Async
    void deleteFromIndexById(Long id);
}

class RoleMstSearchRepositoryInternalImpl implements RoleMstSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final RoleMstRepository repository;

    RoleMstSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, RoleMstRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<RoleMst> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<RoleMst> search(Query query) {
        return elasticsearchTemplate.search(query, RoleMst.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(RoleMst entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), RoleMst.class);
    }
}
