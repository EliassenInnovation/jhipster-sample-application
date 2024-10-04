package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityPostTransactions;
import com.mycompany.myapp.repository.CommunityPostTransactionsRepository;
import com.mycompany.myapp.repository.search.CommunityPostTransactionsSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityPostTransactions}.
 */
@RestController
@RequestMapping("/api/community-post-transactions")
@Transactional
public class CommunityPostTransactionsResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommunityPostTransactionsResource.class);

    private static final String ENTITY_NAME = "communityPostTransactions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityPostTransactionsRepository communityPostTransactionsRepository;

    private final CommunityPostTransactionsSearchRepository communityPostTransactionsSearchRepository;

    public CommunityPostTransactionsResource(
        CommunityPostTransactionsRepository communityPostTransactionsRepository,
        CommunityPostTransactionsSearchRepository communityPostTransactionsSearchRepository
    ) {
        this.communityPostTransactionsRepository = communityPostTransactionsRepository;
        this.communityPostTransactionsSearchRepository = communityPostTransactionsSearchRepository;
    }

    /**
     * {@code POST  /community-post-transactions} : Create a new communityPostTransactions.
     *
     * @param communityPostTransactions the communityPostTransactions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityPostTransactions, or with status {@code 400 (Bad Request)} if the communityPostTransactions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommunityPostTransactions> createCommunityPostTransactions(
        @RequestBody CommunityPostTransactions communityPostTransactions
    ) throws URISyntaxException {
        LOG.debug("REST request to save CommunityPostTransactions : {}", communityPostTransactions);
        if (communityPostTransactions.getId() != null) {
            throw new BadRequestAlertException("A new communityPostTransactions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        communityPostTransactions = communityPostTransactionsRepository.save(communityPostTransactions);
        communityPostTransactionsSearchRepository.index(communityPostTransactions);
        return ResponseEntity.created(new URI("/api/community-post-transactions/" + communityPostTransactions.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, communityPostTransactions.getId().toString()))
            .body(communityPostTransactions);
    }

    /**
     * {@code PUT  /community-post-transactions/:id} : Updates an existing communityPostTransactions.
     *
     * @param id the id of the communityPostTransactions to save.
     * @param communityPostTransactions the communityPostTransactions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityPostTransactions,
     * or with status {@code 400 (Bad Request)} if the communityPostTransactions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityPostTransactions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommunityPostTransactions> updateCommunityPostTransactions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityPostTransactions communityPostTransactions
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommunityPostTransactions : {}, {}", id, communityPostTransactions);
        if (communityPostTransactions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityPostTransactions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityPostTransactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        communityPostTransactions = communityPostTransactionsRepository.save(communityPostTransactions);
        communityPostTransactionsSearchRepository.index(communityPostTransactions);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityPostTransactions.getId().toString()))
            .body(communityPostTransactions);
    }

    /**
     * {@code PATCH  /community-post-transactions/:id} : Partial updates given fields of an existing communityPostTransactions, field will ignore if it is null
     *
     * @param id the id of the communityPostTransactions to save.
     * @param communityPostTransactions the communityPostTransactions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityPostTransactions,
     * or with status {@code 400 (Bad Request)} if the communityPostTransactions is not valid,
     * or with status {@code 404 (Not Found)} if the communityPostTransactions is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityPostTransactions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunityPostTransactions> partialUpdateCommunityPostTransactions(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityPostTransactions communityPostTransactions
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommunityPostTransactions partially : {}, {}", id, communityPostTransactions);
        if (communityPostTransactions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityPostTransactions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityPostTransactionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityPostTransactions> result = communityPostTransactionsRepository
            .findById(communityPostTransactions.getId())
            .map(existingCommunityPostTransactions -> {
                if (communityPostTransactions.getAttachmentUrl() != null) {
                    existingCommunityPostTransactions.setAttachmentUrl(communityPostTransactions.getAttachmentUrl());
                }
                if (communityPostTransactions.getCommunityPostId() != null) {
                    existingCommunityPostTransactions.setCommunityPostId(communityPostTransactions.getCommunityPostId());
                }
                if (communityPostTransactions.getCommunityPostTransactionId() != null) {
                    existingCommunityPostTransactions.setCommunityPostTransactionId(
                        communityPostTransactions.getCommunityPostTransactionId()
                    );
                }
                if (communityPostTransactions.getCreatedBy() != null) {
                    existingCommunityPostTransactions.setCreatedBy(communityPostTransactions.getCreatedBy());
                }
                if (communityPostTransactions.getCreatedOn() != null) {
                    existingCommunityPostTransactions.setCreatedOn(communityPostTransactions.getCreatedOn());
                }
                if (communityPostTransactions.getIsActive() != null) {
                    existingCommunityPostTransactions.setIsActive(communityPostTransactions.getIsActive());
                }
                if (communityPostTransactions.getLastUpdatedBy() != null) {
                    existingCommunityPostTransactions.setLastUpdatedBy(communityPostTransactions.getLastUpdatedBy());
                }
                if (communityPostTransactions.getLastUpdatedOn() != null) {
                    existingCommunityPostTransactions.setLastUpdatedOn(communityPostTransactions.getLastUpdatedOn());
                }

                return existingCommunityPostTransactions;
            })
            .map(communityPostTransactionsRepository::save)
            .map(savedCommunityPostTransactions -> {
                communityPostTransactionsSearchRepository.index(savedCommunityPostTransactions);
                return savedCommunityPostTransactions;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityPostTransactions.getId().toString())
        );
    }

    /**
     * {@code GET  /community-post-transactions} : get all the communityPostTransactions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityPostTransactions in body.
     */
    @GetMapping("")
    public List<CommunityPostTransactions> getAllCommunityPostTransactions() {
        LOG.debug("REST request to get all CommunityPostTransactions");
        return communityPostTransactionsRepository.findAll();
    }

    /**
     * {@code GET  /community-post-transactions/:id} : get the "id" communityPostTransactions.
     *
     * @param id the id of the communityPostTransactions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityPostTransactions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommunityPostTransactions> getCommunityPostTransactions(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommunityPostTransactions : {}", id);
        Optional<CommunityPostTransactions> communityPostTransactions = communityPostTransactionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(communityPostTransactions);
    }

    /**
     * {@code DELETE  /community-post-transactions/:id} : delete the "id" communityPostTransactions.
     *
     * @param id the id of the communityPostTransactions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunityPostTransactions(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommunityPostTransactions : {}", id);
        communityPostTransactionsRepository.deleteById(id);
        communityPostTransactionsSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /community-post-transactions/_search?query=:query} : search for the communityPostTransactions corresponding
     * to the query.
     *
     * @param query the query of the communityPostTransactions search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<CommunityPostTransactions> searchCommunityPostTransactions(@RequestParam("query") String query) {
        LOG.debug("REST request to search CommunityPostTransactions for query {}", query);
        try {
            return StreamSupport.stream(communityPostTransactionsSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
