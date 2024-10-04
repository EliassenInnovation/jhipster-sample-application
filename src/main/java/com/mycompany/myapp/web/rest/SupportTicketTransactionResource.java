package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SupportTicketTransaction;
import com.mycompany.myapp.repository.SupportTicketTransactionRepository;
import com.mycompany.myapp.repository.search.SupportTicketTransactionSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.ElasticsearchExceptionMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.SupportTicketTransaction}.
 */
@RestController
@RequestMapping("/api/support-ticket-transactions")
@Transactional
public class SupportTicketTransactionResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupportTicketTransactionResource.class);

    private static final String ENTITY_NAME = "supportTicketTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupportTicketTransactionRepository supportTicketTransactionRepository;

    private final SupportTicketTransactionSearchRepository supportTicketTransactionSearchRepository;

    public SupportTicketTransactionResource(
        SupportTicketTransactionRepository supportTicketTransactionRepository,
        SupportTicketTransactionSearchRepository supportTicketTransactionSearchRepository
    ) {
        this.supportTicketTransactionRepository = supportTicketTransactionRepository;
        this.supportTicketTransactionSearchRepository = supportTicketTransactionSearchRepository;
    }

    /**
     * {@code POST  /support-ticket-transactions} : Create a new supportTicketTransaction.
     *
     * @param supportTicketTransaction the supportTicketTransaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supportTicketTransaction, or with status {@code 400 (Bad Request)} if the supportTicketTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupportTicketTransaction> createSupportTicketTransaction(
        @RequestBody SupportTicketTransaction supportTicketTransaction
    ) throws URISyntaxException {
        LOG.debug("REST request to save SupportTicketTransaction : {}", supportTicketTransaction);
        if (supportTicketTransaction.getId() != null) {
            throw new BadRequestAlertException("A new supportTicketTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supportTicketTransaction = supportTicketTransactionRepository.save(supportTicketTransaction);
        supportTicketTransactionSearchRepository.index(supportTicketTransaction);
        return ResponseEntity.created(new URI("/api/support-ticket-transactions/" + supportTicketTransaction.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supportTicketTransaction.getId().toString()))
            .body(supportTicketTransaction);
    }

    /**
     * {@code PUT  /support-ticket-transactions/:id} : Updates an existing supportTicketTransaction.
     *
     * @param id the id of the supportTicketTransaction to save.
     * @param supportTicketTransaction the supportTicketTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supportTicketTransaction,
     * or with status {@code 400 (Bad Request)} if the supportTicketTransaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supportTicketTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupportTicketTransaction> updateSupportTicketTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupportTicketTransaction supportTicketTransaction
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupportTicketTransaction : {}, {}", id, supportTicketTransaction);
        if (supportTicketTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supportTicketTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supportTicketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supportTicketTransaction = supportTicketTransactionRepository.save(supportTicketTransaction);
        supportTicketTransactionSearchRepository.index(supportTicketTransaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supportTicketTransaction.getId().toString()))
            .body(supportTicketTransaction);
    }

    /**
     * {@code PATCH  /support-ticket-transactions/:id} : Partial updates given fields of an existing supportTicketTransaction, field will ignore if it is null
     *
     * @param id the id of the supportTicketTransaction to save.
     * @param supportTicketTransaction the supportTicketTransaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supportTicketTransaction,
     * or with status {@code 400 (Bad Request)} if the supportTicketTransaction is not valid,
     * or with status {@code 404 (Not Found)} if the supportTicketTransaction is not found,
     * or with status {@code 500 (Internal Server Error)} if the supportTicketTransaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupportTicketTransaction> partialUpdateSupportTicketTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupportTicketTransaction supportTicketTransaction
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupportTicketTransaction partially : {}, {}", id, supportTicketTransaction);
        if (supportTicketTransaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supportTicketTransaction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supportTicketTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupportTicketTransaction> result = supportTicketTransactionRepository
            .findById(supportTicketTransaction.getId())
            .map(existingSupportTicketTransaction -> {
                if (supportTicketTransaction.getCreatedBy() != null) {
                    existingSupportTicketTransaction.setCreatedBy(supportTicketTransaction.getCreatedBy());
                }
                if (supportTicketTransaction.getCreatedOn() != null) {
                    existingSupportTicketTransaction.setCreatedOn(supportTicketTransaction.getCreatedOn());
                }
                if (supportTicketTransaction.getDescription() != null) {
                    existingSupportTicketTransaction.setDescription(supportTicketTransaction.getDescription());
                }
                if (supportTicketTransaction.getFileExtension() != null) {
                    existingSupportTicketTransaction.setFileExtension(supportTicketTransaction.getFileExtension());
                }
                if (supportTicketTransaction.getFileName() != null) {
                    existingSupportTicketTransaction.setFileName(supportTicketTransaction.getFileName());
                }
                if (supportTicketTransaction.getFilePath() != null) {
                    existingSupportTicketTransaction.setFilePath(supportTicketTransaction.getFilePath());
                }
                if (supportTicketTransaction.getFileSize() != null) {
                    existingSupportTicketTransaction.setFileSize(supportTicketTransaction.getFileSize());
                }
                if (supportTicketTransaction.getIsActive() != null) {
                    existingSupportTicketTransaction.setIsActive(supportTicketTransaction.getIsActive());
                }
                if (supportTicketTransaction.getIsSentByFigSupport() != null) {
                    existingSupportTicketTransaction.setIsSentByFigSupport(supportTicketTransaction.getIsSentByFigSupport());
                }
                if (supportTicketTransaction.getLastUpdatedBy() != null) {
                    existingSupportTicketTransaction.setLastUpdatedBy(supportTicketTransaction.getLastUpdatedBy());
                }
                if (supportTicketTransaction.getLastUpdatedOn() != null) {
                    existingSupportTicketTransaction.setLastUpdatedOn(supportTicketTransaction.getLastUpdatedOn());
                }
                if (supportTicketTransaction.getTicketId() != null) {
                    existingSupportTicketTransaction.setTicketId(supportTicketTransaction.getTicketId());
                }
                if (supportTicketTransaction.getTicketTransactionId() != null) {
                    existingSupportTicketTransaction.setTicketTransactionId(supportTicketTransaction.getTicketTransactionId());
                }
                if (supportTicketTransaction.getUserId() != null) {
                    existingSupportTicketTransaction.setUserId(supportTicketTransaction.getUserId());
                }

                return existingSupportTicketTransaction;
            })
            .map(supportTicketTransactionRepository::save)
            .map(savedSupportTicketTransaction -> {
                supportTicketTransactionSearchRepository.index(savedSupportTicketTransaction);
                return savedSupportTicketTransaction;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supportTicketTransaction.getId().toString())
        );
    }

    /**
     * {@code GET  /support-ticket-transactions} : get all the supportTicketTransactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supportTicketTransactions in body.
     */
    @GetMapping("")
    public List<SupportTicketTransaction> getAllSupportTicketTransactions() {
        LOG.debug("REST request to get all SupportTicketTransactions");
        return supportTicketTransactionRepository.findAll();
    }

    /**
     * {@code GET  /support-ticket-transactions/:id} : get the "id" supportTicketTransaction.
     *
     * @param id the id of the supportTicketTransaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supportTicketTransaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketTransaction> getSupportTicketTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupportTicketTransaction : {}", id);
        Optional<SupportTicketTransaction> supportTicketTransaction = supportTicketTransactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(supportTicketTransaction);
    }

    /**
     * {@code DELETE  /support-ticket-transactions/:id} : delete the "id" supportTicketTransaction.
     *
     * @param id the id of the supportTicketTransaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupportTicketTransaction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupportTicketTransaction : {}", id);
        supportTicketTransactionRepository.deleteById(id);
        supportTicketTransactionSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /support-ticket-transactions/_search?query=:query} : search for the supportTicketTransaction corresponding
     * to the query.
     *
     * @param query the query of the supportTicketTransaction search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SupportTicketTransaction> searchSupportTicketTransactions(@RequestParam("query") String query) {
        LOG.debug("REST request to search SupportTicketTransactions for query {}", query);
        try {
            return StreamSupport.stream(supportTicketTransactionSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
