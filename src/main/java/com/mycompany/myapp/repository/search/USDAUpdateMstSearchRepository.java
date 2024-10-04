package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.USDAUpdateMst;
import com.mycompany.myapp.repository.USDAUpdateMstRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link USDAUpdateMst} entity.
 */
public interface USDAUpdateMstSearchRepository
    extends ElasticsearchRepository<USDAUpdateMst, Long>, USDAUpdateMstSearchRepositoryInternal {}

interface USDAUpdateMstSearchRepositoryInternal {
    Stream<USDAUpdateMst> search(String query);

    Stream<USDAUpdateMst> search(Query query);

    @Async
    void index(USDAUpdateMst entity);

    @Async
    void deleteFromIndexById(Long id);
}

class USDAUpdateMstSearchRepositoryInternalImpl implements USDAUpdateMstSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final USDAUpdateMstRepository repository;

    USDAUpdateMstSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, USDAUpdateMstRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<USDAUpdateMst> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<USDAUpdateMst> search(Query query) {
        return elasticsearchTemplate.search(query, USDAUpdateMst.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(USDAUpdateMst entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), USDAUpdateMst.class);
    }
}
