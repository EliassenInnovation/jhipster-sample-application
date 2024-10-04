package com.mycompany.myapp.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.mycompany.myapp.domain.SupportTicketTransaction;
import com.mycompany.myapp.repository.SupportTicketTransactionRepository;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link SupportTicketTransaction} entity.
 */
public interface SupportTicketTransactionSearchRepository
    extends ElasticsearchRepository<SupportTicketTransaction, Long>, SupportTicketTransactionSearchRepositoryInternal {}

interface SupportTicketTransactionSearchRepositoryInternal {
    Stream<SupportTicketTransaction> search(String query);

    Stream<SupportTicketTransaction> search(Query query);

    @Async
    void index(SupportTicketTransaction entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SupportTicketTransactionSearchRepositoryInternalImpl implements SupportTicketTransactionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SupportTicketTransactionRepository repository;

    SupportTicketTransactionSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        SupportTicketTransactionRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Stream<SupportTicketTransaction> search(String query) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery);
    }

    @Override
    public Stream<SupportTicketTransaction> search(Query query) {
        return elasticsearchTemplate.search(query, SupportTicketTransaction.class).map(SearchHit::getContent).stream();
    }

    @Override
    public void index(SupportTicketTransaction entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SupportTicketTransaction.class);
    }
}
