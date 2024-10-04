package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityComment;
import com.mycompany.myapp.repository.CommunityCommentRepository;
import com.mycompany.myapp.repository.search.CommunityCommentSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityComment}.
 */
@RestController
@RequestMapping("/api/community-comments")
@Transactional
public class CommunityCommentResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommunityCommentResource.class);

    private static final String ENTITY_NAME = "communityComment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityCommentRepository communityCommentRepository;

    private final CommunityCommentSearchRepository communityCommentSearchRepository;

    public CommunityCommentResource(
        CommunityCommentRepository communityCommentRepository,
        CommunityCommentSearchRepository communityCommentSearchRepository
    ) {
        this.communityCommentRepository = communityCommentRepository;
        this.communityCommentSearchRepository = communityCommentSearchRepository;
    }

    /**
     * {@code POST  /community-comments} : Create a new communityComment.
     *
     * @param communityComment the communityComment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityComment, or with status {@code 400 (Bad Request)} if the communityComment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommunityComment> createCommunityComment(@RequestBody CommunityComment communityComment)
        throws URISyntaxException {
        LOG.debug("REST request to save CommunityComment : {}", communityComment);
        if (communityComment.getId() != null) {
            throw new BadRequestAlertException("A new communityComment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        communityComment = communityCommentRepository.save(communityComment);
        communityCommentSearchRepository.index(communityComment);
        return ResponseEntity.created(new URI("/api/community-comments/" + communityComment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, communityComment.getId().toString()))
            .body(communityComment);
    }

    /**
     * {@code PUT  /community-comments/:id} : Updates an existing communityComment.
     *
     * @param id the id of the communityComment to save.
     * @param communityComment the communityComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityComment,
     * or with status {@code 400 (Bad Request)} if the communityComment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommunityComment> updateCommunityComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityComment communityComment
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommunityComment : {}, {}", id, communityComment);
        if (communityComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        communityComment = communityCommentRepository.save(communityComment);
        communityCommentSearchRepository.index(communityComment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityComment.getId().toString()))
            .body(communityComment);
    }

    /**
     * {@code PATCH  /community-comments/:id} : Partial updates given fields of an existing communityComment, field will ignore if it is null
     *
     * @param id the id of the communityComment to save.
     * @param communityComment the communityComment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityComment,
     * or with status {@code 400 (Bad Request)} if the communityComment is not valid,
     * or with status {@code 404 (Not Found)} if the communityComment is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityComment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunityComment> partialUpdateCommunityComment(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityComment communityComment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommunityComment partially : {}, {}", id, communityComment);
        if (communityComment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityComment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityCommentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityComment> result = communityCommentRepository
            .findById(communityComment.getId())
            .map(existingCommunityComment -> {
                if (communityComment.getComment() != null) {
                    existingCommunityComment.setComment(communityComment.getComment());
                }
                if (communityComment.getCommentByUserId() != null) {
                    existingCommunityComment.setCommentByUserId(communityComment.getCommentByUserId());
                }
                if (communityComment.getCommunityCommentId() != null) {
                    existingCommunityComment.setCommunityCommentId(communityComment.getCommunityCommentId());
                }
                if (communityComment.getCommunityPostId() != null) {
                    existingCommunityComment.setCommunityPostId(communityComment.getCommunityPostId());
                }
                if (communityComment.getCreatedOn() != null) {
                    existingCommunityComment.setCreatedOn(communityComment.getCreatedOn());
                }
                if (communityComment.getIsActive() != null) {
                    existingCommunityComment.setIsActive(communityComment.getIsActive());
                }
                if (communityComment.getLastUpdatedOn() != null) {
                    existingCommunityComment.setLastUpdatedOn(communityComment.getLastUpdatedOn());
                }

                return existingCommunityComment;
            })
            .map(communityCommentRepository::save)
            .map(savedCommunityComment -> {
                communityCommentSearchRepository.index(savedCommunityComment);
                return savedCommunityComment;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityComment.getId().toString())
        );
    }

    /**
     * {@code GET  /community-comments} : get all the communityComments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityComments in body.
     */
    @GetMapping("")
    public List<CommunityComment> getAllCommunityComments() {
        LOG.debug("REST request to get all CommunityComments");
        return communityCommentRepository.findAll();
    }

    /**
     * {@code GET  /community-comments/:id} : get the "id" communityComment.
     *
     * @param id the id of the communityComment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityComment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommunityComment> getCommunityComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommunityComment : {}", id);
        Optional<CommunityComment> communityComment = communityCommentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(communityComment);
    }

    /**
     * {@code DELETE  /community-comments/:id} : delete the "id" communityComment.
     *
     * @param id the id of the communityComment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunityComment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommunityComment : {}", id);
        communityCommentRepository.deleteById(id);
        communityCommentSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /community-comments/_search?query=:query} : search for the communityComment corresponding
     * to the query.
     *
     * @param query the query of the communityComment search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<CommunityComment> searchCommunityComments(@RequestParam("query") String query) {
        LOG.debug("REST request to search CommunityComments for query {}", query);
        try {
            return StreamSupport.stream(communityCommentSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
