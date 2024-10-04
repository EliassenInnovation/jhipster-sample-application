package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.UserDistrictAllocation;
import com.mycompany.myapp.repository.UserDistrictAllocationRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link UserDistrictAllocation} entity.
 */
public interface UserDistrictAllocationSearchRepository
    extends ElasticsearchRepository<UserDistrictAllocation, Long>, UserDistrictAllocationSearchRepositoryInternal {}

interface UserDistrictAllocationSearchRepositoryInternal {
    Stream<UserDistrictAllocation> search(String query);

    Stream<UserDistrictAllocation> search(Query query);

    @Async
    void index(UserDistrictAllocation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class UserDistrictAllocationSearchRepositoryInternalImpl implements UserDistrictAllocationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final UserDistrictAllocationRepository repository;

    UserDistrictAllocationSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        UserDistrictAllocationRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<UserDistrictAllocation> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<UserDistrictAllocation> search(Query query) {
        return elasticsearchTemplate.search(query, UserDistrictAllocation.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(UserDistrictAllocation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), UserDistrictAllocation.class);
    }
}
