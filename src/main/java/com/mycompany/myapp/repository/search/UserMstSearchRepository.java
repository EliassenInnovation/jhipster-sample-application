package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.UserMst;
import com.mycompany.myapp.repository.UserMstRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link UserMst} entity.
 */
public interface UserMstSearchRepository extends ElasticsearchRepository<UserMst, Long>, UserMstSearchRepositoryInternal {}

interface UserMstSearchRepositoryInternal {
    Stream<UserMst> search(String query);

    Stream<UserMst> search(Query query);

    @Async
    void index(UserMst entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UserMstSearchRepositoryInternalImpl implements UserMstSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UserMstRepository repository;

    UserMstSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, UserMstRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<UserMst> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<UserMst> search(Query query) {
        return elasticsearchTemplate.search(query, UserMst.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(UserMst entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), UserMst.class);
    }
}
