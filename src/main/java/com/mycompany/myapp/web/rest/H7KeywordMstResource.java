package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.H7KeywordMst;
import com.mycompany.myapp.repository.H7KeywordMstRepository;
import com.mycompany.myapp.repository.search.H7KeywordMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.H7KeywordMst}.
 */
@RestController
@RequestMapping("/api/h-7-keyword-msts")
@Transactional
public class H7KeywordMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(H7KeywordMstResource.class);

    private static final String ENTITY_NAME = "h7KeywordMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final H7KeywordMstRepository h7KeywordMstRepository;

    private final H7KeywordMstSearchRepository h7KeywordMstSearchRepository;

    public H7KeywordMstResource(H7KeywordMstRepository h7KeywordMstRepository, H7KeywordMstSearchRepository h7KeywordMstSearchRepository) {
        this.h7KeywordMstRepository = h7KeywordMstRepository;
        this.h7KeywordMstSearchRepository = h7KeywordMstSearchRepository;
    }

    /**
     * {@code POST  /h-7-keyword-msts} : Create a new h7KeywordMst.
     *
     * @param h7KeywordMst the h7KeywordMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new h7KeywordMst, or with status {@code 400 (Bad Request)} if the h7KeywordMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<H7KeywordMst> createH7KeywordMst(@RequestBody H7KeywordMst h7KeywordMst) throws URISyntaxException {
        LOG.debug("REST request to save H7KeywordMst : {}", h7KeywordMst);
        if (h7KeywordMst.getId() != null) {
            throw new BadRequestAlertException("A new h7KeywordMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        h7KeywordMst = h7KeywordMstRepository.save(h7KeywordMst);
        h7KeywordMstSearchRepository.index(h7KeywordMst);
        return ResponseEntity.created(new URI("/api/h-7-keyword-msts/" + h7KeywordMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, h7KeywordMst.getId().toString()))
            .body(h7KeywordMst);
    }

    /**
     * {@code PUT  /h-7-keyword-msts/:id} : Updates an existing h7KeywordMst.
     *
     * @param id the id of the h7KeywordMst to save.
     * @param h7KeywordMst the h7KeywordMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated h7KeywordMst,
     * or with status {@code 400 (Bad Request)} if the h7KeywordMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the h7KeywordMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<H7KeywordMst> updateH7KeywordMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody H7KeywordMst h7KeywordMst
    ) throws URISyntaxException {
        LOG.debug("REST request to update H7KeywordMst : {}, {}", id, h7KeywordMst);
        if (h7KeywordMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, h7KeywordMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!h7KeywordMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        h7KeywordMst = h7KeywordMstRepository.save(h7KeywordMst);
        h7KeywordMstSearchRepository.index(h7KeywordMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, h7KeywordMst.getId().toString()))
            .body(h7KeywordMst);
    }

    /**
     * {@code PATCH  /h-7-keyword-msts/:id} : Partial updates given fields of an existing h7KeywordMst, field will ignore if it is null
     *
     * @param id the id of the h7KeywordMst to save.
     * @param h7KeywordMst the h7KeywordMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated h7KeywordMst,
     * or with status {@code 400 (Bad Request)} if the h7KeywordMst is not valid,
     * or with status {@code 404 (Not Found)} if the h7KeywordMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the h7KeywordMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<H7KeywordMst> partialUpdateH7KeywordMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody H7KeywordMst h7KeywordMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update H7KeywordMst partially : {}, {}", id, h7KeywordMst);
        if (h7KeywordMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, h7KeywordMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!h7KeywordMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<H7KeywordMst> result = h7KeywordMstRepository
            .findById(h7KeywordMst.getId())
            .map(existingH7KeywordMst -> {
                if (h7KeywordMst.geth7Group() != null) {
                    existingH7KeywordMst.seth7Group(h7KeywordMst.geth7Group());
                }
                if (h7KeywordMst.geth7Keyword() != null) {
                    existingH7KeywordMst.seth7Keyword(h7KeywordMst.geth7Keyword());
                }
                if (h7KeywordMst.geth7keywordId() != null) {
                    existingH7KeywordMst.seth7keywordId(h7KeywordMst.geth7keywordId());
                }
                if (h7KeywordMst.getIocGroup() != null) {
                    existingH7KeywordMst.setIocGroup(h7KeywordMst.getIocGroup());
                }

                return existingH7KeywordMst;
            })
            .map(h7KeywordMstRepository::save)
            .map(savedH7KeywordMst -> {
                h7KeywordMstSearchRepository.index(savedH7KeywordMst);
                return savedH7KeywordMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, h7KeywordMst.getId().toString())
        );
    }

    /**
     * {@code GET  /h-7-keyword-msts} : get all the h7KeywordMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of h7KeywordMsts in body.
     */
    @GetMapping("")
    public List<H7KeywordMst> getAllH7KeywordMsts() {
        LOG.debug("REST request to get all H7KeywordMsts");
        return h7KeywordMstRepository.findAll();
    }

    /**
     * {@code GET  /h-7-keyword-msts/:id} : get the "id" h7KeywordMst.
     *
     * @param id the id of the h7KeywordMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the h7KeywordMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<H7KeywordMst> getH7KeywordMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get H7KeywordMst : {}", id);
        Optional<H7KeywordMst> h7KeywordMst = h7KeywordMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(h7KeywordMst);
    }

    /**
     * {@code DELETE  /h-7-keyword-msts/:id} : delete the "id" h7KeywordMst.
     *
     * @param id the id of the h7KeywordMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteH7KeywordMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete H7KeywordMst : {}", id);
        h7KeywordMstRepository.deleteById(id);
        h7KeywordMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /h-7-keyword-msts/_search?query=:query} : search for the h7KeywordMst corresponding
     * to the query.
     *
     * @param query the query of the h7KeywordMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<H7KeywordMst> searchH7KeywordMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search H7KeywordMsts for query {}", query);
        try {
            return StreamSupport.stream(h7KeywordMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
