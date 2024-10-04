package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.SchoolDistrict;
import com.mycompany.myapp.repository.SchoolDistrictRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SchoolDistrict} entity.
 */
public interface SchoolDistrictSearchRepository
    extends ElasticsearchRepository<SchoolDistrict, Long>, SchoolDistrictSearchRepositoryInternal {}

interface SchoolDistrictSearchRepositoryInternal {
    Stream<SchoolDistrict> search(String query);

    Stream<SchoolDistrict> search(Query query);

    @Async
    void index(SchoolDistrict entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SchoolDistrictSearchRepositoryInternalImpl implements SchoolDistrictSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SchoolDistrictRepository repository;

    SchoolDistrictSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SchoolDistrictRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SchoolDistrict> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SchoolDistrict> search(Query query) {
        return elasticsearchTemplate.search(query, SchoolDistrict.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SchoolDistrict entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SchoolDistrict.class);
    }
}
