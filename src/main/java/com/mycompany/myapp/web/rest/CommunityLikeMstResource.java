package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityLikeMst;
import com.mycompany.myapp.repository.CommunityLikeMstRepository;
import com.mycompany.myapp.repository.search.CommunityLikeMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityLikeMst}.
 */
@RestController
@RequestMapping("/api/community-like-msts")
@Transactional
public class CommunityLikeMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommunityLikeMstResource.class);

    private static final String ENTITY_NAME = "communityLikeMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityLikeMstRepository communityLikeMstRepository;

    private final CommunityLikeMstSearchRepository communityLikeMstSearchRepository;

    public CommunityLikeMstResource(
        CommunityLikeMstRepository communityLikeMstRepository,
        CommunityLikeMstSearchRepository communityLikeMstSearchRepository
    ) {
        this.communityLikeMstRepository = communityLikeMstRepository;
        this.communityLikeMstSearchRepository = communityLikeMstSearchRepository;
    }

    /**
     * {@code POST  /community-like-msts} : Create a new communityLikeMst.
     *
     * @param communityLikeMst the communityLikeMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityLikeMst, or with status {@code 400 (Bad Request)} if the communityLikeMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommunityLikeMst> createCommunityLikeMst(@RequestBody CommunityLikeMst communityLikeMst)
        throws URISyntaxException {
        LOG.debug("REST request to save CommunityLikeMst : {}", communityLikeMst);
        if (communityLikeMst.getId() != null) {
            throw new BadRequestAlertException("A new communityLikeMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        communityLikeMst = communityLikeMstRepository.save(communityLikeMst);
        communityLikeMstSearchRepository.index(communityLikeMst);
        return ResponseEntity.created(new URI("/api/community-like-msts/" + communityLikeMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, communityLikeMst.getId().toString()))
            .body(communityLikeMst);
    }

    /**
     * {@code PUT  /community-like-msts/:id} : Updates an existing communityLikeMst.
     *
     * @param id the id of the communityLikeMst to save.
     * @param communityLikeMst the communityLikeMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityLikeMst,
     * or with status {@code 400 (Bad Request)} if the communityLikeMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityLikeMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommunityLikeMst> updateCommunityLikeMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityLikeMst communityLikeMst
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommunityLikeMst : {}, {}", id, communityLikeMst);
        if (communityLikeMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityLikeMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityLikeMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        communityLikeMst = communityLikeMstRepository.save(communityLikeMst);
        communityLikeMstSearchRepository.index(communityLikeMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityLikeMst.getId().toString()))
            .body(communityLikeMst);
    }

    /**
     * {@code PATCH  /community-like-msts/:id} : Partial updates given fields of an existing communityLikeMst, field will ignore if it is null
     *
     * @param id the id of the communityLikeMst to save.
     * @param communityLikeMst the communityLikeMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityLikeMst,
     * or with status {@code 400 (Bad Request)} if the communityLikeMst is not valid,
     * or with status {@code 404 (Not Found)} if the communityLikeMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityLikeMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunityLikeMst> partialUpdateCommunityLikeMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityLikeMst communityLikeMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommunityLikeMst partially : {}, {}", id, communityLikeMst);
        if (communityLikeMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityLikeMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityLikeMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityLikeMst> result = communityLikeMstRepository
            .findById(communityLikeMst.getId())
            .map(existingCommunityLikeMst -> {
                if (communityLikeMst.getCommunityLikeId() != null) {
                    existingCommunityLikeMst.setCommunityLikeId(communityLikeMst.getCommunityLikeId());
                }
                if (communityLikeMst.getCommunityPostId() != null) {
                    existingCommunityLikeMst.setCommunityPostId(communityLikeMst.getCommunityPostId());
                }
                if (communityLikeMst.getCreatedOn() != null) {
                    existingCommunityLikeMst.setCreatedOn(communityLikeMst.getCreatedOn());
                }
                if (communityLikeMst.getIsActive() != null) {
                    existingCommunityLikeMst.setIsActive(communityLikeMst.getIsActive());
                }
                if (communityLikeMst.getIsLiked() != null) {
                    existingCommunityLikeMst.setIsLiked(communityLikeMst.getIsLiked());
                }
                if (communityLikeMst.getLikedByUserId() != null) {
                    existingCommunityLikeMst.setLikedByUserId(communityLikeMst.getLikedByUserId());
                }

                return existingCommunityLikeMst;
            })
            .map(communityLikeMstRepository::save)
            .map(savedCommunityLikeMst -> {
                communityLikeMstSearchRepository.index(savedCommunityLikeMst);
                return savedCommunityLikeMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityLikeMst.getId().toString())
        );
    }

    /**
     * {@code GET  /community-like-msts} : get all the communityLikeMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityLikeMsts in body.
     */
    @GetMapping("")
    public List<CommunityLikeMst> getAllCommunityLikeMsts() {
        LOG.debug("REST request to get all CommunityLikeMsts");
        return communityLikeMstRepository.findAll();
    }

    /**
     * {@code GET  /community-like-msts/:id} : get the "id" communityLikeMst.
     *
     * @param id the id of the communityLikeMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityLikeMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommunityLikeMst> getCommunityLikeMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommunityLikeMst : {}", id);
        Optional<CommunityLikeMst> communityLikeMst = communityLikeMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(communityLikeMst);
    }

    /**
     * {@code DELETE  /community-like-msts/:id} : delete the "id" communityLikeMst.
     *
     * @param id the id of the communityLikeMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunityLikeMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommunityLikeMst : {}", id);
        communityLikeMstRepository.deleteById(id);
        communityLikeMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /community-like-msts/_search?query=:query} : search for the communityLikeMst corresponding
     * to the query.
     *
     * @param query the query of the communityLikeMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<CommunityLikeMst> searchCommunityLikeMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search CommunityLikeMsts for query {}", query);
        try {
            return StreamSupport.stream(communityLikeMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
