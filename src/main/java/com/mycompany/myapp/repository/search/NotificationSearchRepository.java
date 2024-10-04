package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.Notification;
import com.mycompany.myapp.repository.NotificationRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link Notification} entity.
 */
public interface NotificationSearchRepository extends ElasticsearchRepository<Notification, Long>, NotificationSearchRepositoryInternal {}

interface NotificationSearchRepositoryInternal {
    Stream<Notification> search(String query);

    Stream<Notification> search(Query query);

    @Async
    void index(Notification entity);

    @Async
    void deleteFromIndexById(Long id);
}

class NotificationSearchRepositoryInternalImpl implements NotificationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final NotificationRepository repository;

    NotificationSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, NotificationRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<Notification> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<Notification> search(Query query) {
        return elasticsearchTemplate.search(query, Notification.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(Notification entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Notification.class);
    }
}
