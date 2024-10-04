package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.AllergenMst;
import com.mycompany.myapp.repository.AllergenMstRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link AllergenMst} entity.
 */
public interface AllergenMstSearchRepository extends ElasticsearchRepository<AllergenMst, Long>, AllergenMstSearchRepositoryInternal {}

interface AllergenMstSearchRepositoryInternal {
    Stream<AllergenMst> search(String query);

    Stream<AllergenMst> search(Query query);

    @Async
    void index(AllergenMst entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AllergenMstSearchRepositoryInternalImpl implements AllergenMstSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AllergenMstRepository repository;

    AllergenMstSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AllergenMstRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<AllergenMst> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<AllergenMst> search(Query query) {
        return elasticsearchTemplate.search(query, AllergenMst.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(AllergenMst entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AllergenMst.class);
    }
}
