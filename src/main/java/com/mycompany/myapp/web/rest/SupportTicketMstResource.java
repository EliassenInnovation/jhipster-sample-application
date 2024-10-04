package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SupportTicketMst;
import com.mycompany.myapp.repository.SupportTicketMstRepository;
import com.mycompany.myapp.repository.search.SupportTicketMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SupportTicketMst}.
 */
@RestController
@RequestMapping("/api/support-ticket-msts")
@Transactional
public class SupportTicketMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(SupportTicketMstResource.class);

    private static final String ENTITY_NAME = "supportTicketMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SupportTicketMstRepository supportTicketMstRepository;

    private final SupportTicketMstSearchRepository supportTicketMstSearchRepository;

    public SupportTicketMstResource(
        SupportTicketMstRepository supportTicketMstRepository,
        SupportTicketMstSearchRepository supportTicketMstSearchRepository
    ) {
        this.supportTicketMstRepository = supportTicketMstRepository;
        this.supportTicketMstSearchRepository = supportTicketMstSearchRepository;
    }

    /**
     * {@code POST  /support-ticket-msts} : Create a new supportTicketMst.
     *
     * @param supportTicketMst the supportTicketMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new supportTicketMst, or with status {@code 400 (Bad Request)} if the supportTicketMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SupportTicketMst> createSupportTicketMst(@RequestBody SupportTicketMst supportTicketMst)
        throws URISyntaxException {
        LOG.debug("REST request to save SupportTicketMst : {}", supportTicketMst);
        if (supportTicketMst.getId() != null) {
            throw new BadRequestAlertException("A new supportTicketMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        supportTicketMst = supportTicketMstRepository.save(supportTicketMst);
        supportTicketMstSearchRepository.index(supportTicketMst);
        return ResponseEntity.created(new URI("/api/support-ticket-msts/" + supportTicketMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, supportTicketMst.getId().toString()))
            .body(supportTicketMst);
    }

    /**
     * {@code PUT  /support-ticket-msts/:id} : Updates an existing supportTicketMst.
     *
     * @param id the id of the supportTicketMst to save.
     * @param supportTicketMst the supportTicketMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supportTicketMst,
     * or with status {@code 400 (Bad Request)} if the supportTicketMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the supportTicketMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SupportTicketMst> updateSupportTicketMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupportTicketMst supportTicketMst
    ) throws URISyntaxException {
        LOG.debug("REST request to update SupportTicketMst : {}, {}", id, supportTicketMst);
        if (supportTicketMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supportTicketMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supportTicketMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        supportTicketMst = supportTicketMstRepository.save(supportTicketMst);
        supportTicketMstSearchRepository.index(supportTicketMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supportTicketMst.getId().toString()))
            .body(supportTicketMst);
    }

    /**
     * {@code PATCH  /support-ticket-msts/:id} : Partial updates given fields of an existing supportTicketMst, field will ignore if it is null
     *
     * @param id the id of the supportTicketMst to save.
     * @param supportTicketMst the supportTicketMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated supportTicketMst,
     * or with status {@code 400 (Bad Request)} if the supportTicketMst is not valid,
     * or with status {@code 404 (Not Found)} if the supportTicketMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the supportTicketMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SupportTicketMst> partialUpdateSupportTicketMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SupportTicketMst supportTicketMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SupportTicketMst partially : {}, {}", id, supportTicketMst);
        if (supportTicketMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, supportTicketMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!supportTicketMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SupportTicketMst> result = supportTicketMstRepository
            .findById(supportTicketMst.getId())
            .map(existingSupportTicketMst -> {
                if (supportTicketMst.getCreatedBy() != null) {
                    existingSupportTicketMst.setCreatedBy(supportTicketMst.getCreatedBy());
                }
                if (supportTicketMst.getCreatedOn() != null) {
                    existingSupportTicketMst.setCreatedOn(supportTicketMst.getCreatedOn());
                }
                if (supportTicketMst.getEmail() != null) {
                    existingSupportTicketMst.setEmail(supportTicketMst.getEmail());
                }
                if (supportTicketMst.getIsActive() != null) {
                    existingSupportTicketMst.setIsActive(supportTicketMst.getIsActive());
                }
                if (supportTicketMst.getIsWithOutLogin() != null) {
                    existingSupportTicketMst.setIsWithOutLogin(supportTicketMst.getIsWithOutLogin());
                }
                if (supportTicketMst.getLastUpdatedBy() != null) {
                    existingSupportTicketMst.setLastUpdatedBy(supportTicketMst.getLastUpdatedBy());
                }
                if (supportTicketMst.getLastUpdatedOn() != null) {
                    existingSupportTicketMst.setLastUpdatedOn(supportTicketMst.getLastUpdatedOn());
                }
                if (supportTicketMst.getPriority() != null) {
                    existingSupportTicketMst.setPriority(supportTicketMst.getPriority());
                }
                if (supportTicketMst.getSchoolDistrictId() != null) {
                    existingSupportTicketMst.setSchoolDistrictId(supportTicketMst.getSchoolDistrictId());
                }
                if (supportTicketMst.getStatus() != null) {
                    existingSupportTicketMst.setStatus(supportTicketMst.getStatus());
                }
                if (supportTicketMst.getSubject() != null) {
                    existingSupportTicketMst.setSubject(supportTicketMst.getSubject());
                }
                if (supportTicketMst.getSupportCategoryId() != null) {
                    existingSupportTicketMst.setSupportCategoryId(supportTicketMst.getSupportCategoryId());
                }
                if (supportTicketMst.getTicketId() != null) {
                    existingSupportTicketMst.setTicketId(supportTicketMst.getTicketId());
                }
                if (supportTicketMst.getTicketReferenceNumber() != null) {
                    existingSupportTicketMst.setTicketReferenceNumber(supportTicketMst.getTicketReferenceNumber());
                }
                if (supportTicketMst.getUserId() != null) {
                    existingSupportTicketMst.setUserId(supportTicketMst.getUserId());
                }
                if (supportTicketMst.getUserName() != null) {
                    existingSupportTicketMst.setUserName(supportTicketMst.getUserName());
                }

                return existingSupportTicketMst;
            })
            .map(supportTicketMstRepository::save)
            .map(savedSupportTicketMst -> {
                supportTicketMstSearchRepository.index(savedSupportTicketMst);
                return savedSupportTicketMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, supportTicketMst.getId().toString())
        );
    }

    /**
     * {@code GET  /support-ticket-msts} : get all the supportTicketMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of supportTicketMsts in body.
     */
    @GetMapping("")
    public List<SupportTicketMst> getAllSupportTicketMsts() {
        LOG.debug("REST request to get all SupportTicketMsts");
        return supportTicketMstRepository.findAll();
    }

    /**
     * {@code GET  /support-ticket-msts/:id} : get the "id" supportTicketMst.
     *
     * @param id the id of the supportTicketMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the supportTicketMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SupportTicketMst> getSupportTicketMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SupportTicketMst : {}", id);
        Optional<SupportTicketMst> supportTicketMst = supportTicketMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(supportTicketMst);
    }

    /**
     * {@code DELETE  /support-ticket-msts/:id} : delete the "id" supportTicketMst.
     *
     * @param id the id of the supportTicketMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupportTicketMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SupportTicketMst : {}", id);
        supportTicketMstRepository.deleteById(id);
        supportTicketMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /support-ticket-msts/_search?query=:query} : search for the supportTicketMst corresponding
     * to the query.
     *
     * @param query the query of the supportTicketMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SupportTicketMst> searchSupportTicketMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search SupportTicketMsts for query {}", query);
        try {
            return StreamSupport.stream(supportTicketMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
