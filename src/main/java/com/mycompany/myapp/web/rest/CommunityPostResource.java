package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.CommunityPost;
import com.mycompany.myapp.repository.CommunityPostRepository;
import com.mycompany.myapp.repository.search.CommunityPostSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.CommunityPost}.
 */
@RestController
@RequestMapping("/api/community-posts")
@Transactional
public class CommunityPostResource {

    private static final Logger LOG = LoggerFactory.getLogger(CommunityPostResource.class);

    private static final String ENTITY_NAME = "communityPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommunityPostRepository communityPostRepository;

    private final CommunityPostSearchRepository communityPostSearchRepository;

    public CommunityPostResource(
        CommunityPostRepository communityPostRepository,
        CommunityPostSearchRepository communityPostSearchRepository
    ) {
        this.communityPostRepository = communityPostRepository;
        this.communityPostSearchRepository = communityPostSearchRepository;
    }

    /**
     * {@code POST  /community-posts} : Create a new communityPost.
     *
     * @param communityPost the communityPost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new communityPost, or with status {@code 400 (Bad Request)} if the communityPost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CommunityPost> createCommunityPost(@RequestBody CommunityPost communityPost) throws URISyntaxException {
        LOG.debug("REST request to save CommunityPost : {}", communityPost);
        if (communityPost.getId() != null) {
            throw new BadRequestAlertException("A new communityPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        communityPost = communityPostRepository.save(communityPost);
        communityPostSearchRepository.index(communityPost);
        return ResponseEntity.created(new URI("/api/community-posts/" + communityPost.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, communityPost.getId().toString()))
            .body(communityPost);
    }

    /**
     * {@code PUT  /community-posts/:id} : Updates an existing communityPost.
     *
     * @param id the id of the communityPost to save.
     * @param communityPost the communityPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityPost,
     * or with status {@code 400 (Bad Request)} if the communityPost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the communityPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommunityPost> updateCommunityPost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityPost communityPost
    ) throws URISyntaxException {
        LOG.debug("REST request to update CommunityPost : {}, {}", id, communityPost);
        if (communityPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        communityPost = communityPostRepository.save(communityPost);
        communityPostSearchRepository.index(communityPost);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityPost.getId().toString()))
            .body(communityPost);
    }

    /**
     * {@code PATCH  /community-posts/:id} : Partial updates given fields of an existing communityPost, field will ignore if it is null
     *
     * @param id the id of the communityPost to save.
     * @param communityPost the communityPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated communityPost,
     * or with status {@code 400 (Bad Request)} if the communityPost is not valid,
     * or with status {@code 404 (Not Found)} if the communityPost is not found,
     * or with status {@code 500 (Internal Server Error)} if the communityPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommunityPost> partialUpdateCommunityPost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CommunityPost communityPost
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CommunityPost partially : {}, {}", id, communityPost);
        if (communityPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, communityPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!communityPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommunityPost> result = communityPostRepository
            .findById(communityPost.getId())
            .map(existingCommunityPost -> {
                if (communityPost.getCommunityPostId() != null) {
                    existingCommunityPost.setCommunityPostId(communityPost.getCommunityPostId());
                }
                if (communityPost.getCreatedBy() != null) {
                    existingCommunityPost.setCreatedBy(communityPost.getCreatedBy());
                }
                if (communityPost.getCreatedOn() != null) {
                    existingCommunityPost.setCreatedOn(communityPost.getCreatedOn());
                }
                if (communityPost.getDate() != null) {
                    existingCommunityPost.setDate(communityPost.getDate());
                }
                if (communityPost.getDescription() != null) {
                    existingCommunityPost.setDescription(communityPost.getDescription());
                }
                if (communityPost.getIsActive() != null) {
                    existingCommunityPost.setIsActive(communityPost.getIsActive());
                }
                if (communityPost.getLastUpdatedBy() != null) {
                    existingCommunityPost.setLastUpdatedBy(communityPost.getLastUpdatedBy());
                }
                if (communityPost.getLastUpdatedOn() != null) {
                    existingCommunityPost.setLastUpdatedOn(communityPost.getLastUpdatedOn());
                }
                if (communityPost.getPostTypeId() != null) {
                    existingCommunityPost.setPostTypeId(communityPost.getPostTypeId());
                }
                if (communityPost.getPrivacyTypeId() != null) {
                    existingCommunityPost.setPrivacyTypeId(communityPost.getPrivacyTypeId());
                }
                if (communityPost.getSchoolDistrictId() != null) {
                    existingCommunityPost.setSchoolDistrictId(communityPost.getSchoolDistrictId());
                }
                if (communityPost.getUserId() != null) {
                    existingCommunityPost.setUserId(communityPost.getUserId());
                }

                return existingCommunityPost;
            })
            .map(communityPostRepository::save)
            .map(savedCommunityPost -> {
                communityPostSearchRepository.index(savedCommunityPost);
                return savedCommunityPost;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, communityPost.getId().toString())
        );
    }

    /**
     * {@code GET  /community-posts} : get all the communityPosts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of communityPosts in body.
     */
    @GetMapping("")
    public List<CommunityPost> getAllCommunityPosts() {
        LOG.debug("REST request to get all CommunityPosts");
        return communityPostRepository.findAll();
    }

    /**
     * {@code GET  /community-posts/:id} : get the "id" communityPost.
     *
     * @param id the id of the communityPost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the communityPost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommunityPost> getCommunityPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CommunityPost : {}", id);
        Optional<CommunityPost> communityPost = communityPostRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(communityPost);
    }

    /**
     * {@code DELETE  /community-posts/:id} : delete the "id" communityPost.
     *
     * @param id the id of the communityPost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunityPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CommunityPost : {}", id);
        communityPostRepository.deleteById(id);
        communityPostSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /community-posts/_search?query=:query} : search for the communityPost corresponding
     * to the query.
     *
     * @param query the query of the communityPost search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<CommunityPost> searchCommunityPosts(@RequestParam("query") String query) {
        LOG.debug("REST request to search CommunityPosts for query {}", query);
        try {
            return StreamSupport.stream(communityPostSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
