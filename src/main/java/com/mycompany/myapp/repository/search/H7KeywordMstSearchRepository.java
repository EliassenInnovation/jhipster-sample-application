package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.H7KeywordMst;
import com.mycompany.myapp.repository.H7KeywordMstRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link H7KeywordMst} entity.
 */
public interface H7KeywordMstSearchRepository extends ElasticsearchRepository<H7KeywordMst, Long>, H7KeywordMstSearchRepositoryInternal {}

interface H7KeywordMstSearchRepositoryInternal {
    Stream<H7KeywordMst> search(String query);

    Stream<H7KeywordMst> search(Query query);

    @Async
    void index(H7KeywordMst entity);

    @Async
    void deleteFromIndexById(Long id);
}

class H7KeywordMstSearchRepositoryInternalImpl implements H7KeywordMstSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final H7KeywordMstRepository repository;

    H7KeywordMstSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, H7KeywordMstRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<H7KeywordMst> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<H7KeywordMst> search(Query query) {
        return elasticsearchTemplate.search(query, H7KeywordMst.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(H7KeywordMst entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), H7KeywordMst.class);
    }
}
