package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PostTypes;
import com.mycompany.myapp.repository.PostTypesRepository;
import com.mycompany.myapp.repository.search.PostTypesSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PostTypes}.
 */
@RestController
@RequestMapping("/api/post-types")
@Transactional
public class PostTypesResource {

    private static final Logger LOG = LoggerFactory.getLogger(PostTypesResource.class);

    private static final String ENTITY_NAME = "postTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PostTypesRepository postTypesRepository;

    private final PostTypesSearchRepository postTypesSearchRepository;

    public PostTypesResource(PostTypesRepository postTypesRepository, PostTypesSearchRepository postTypesSearchRepository) {
        this.postTypesRepository = postTypesRepository;
        this.postTypesSearchRepository = postTypesSearchRepository;
    }

    /**
     * {@code POST  /post-types} : Create a new postTypes.
     *
     * @param postTypes the postTypes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new postTypes, or with status {@code 400 (Bad Request)} if the postTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PostTypes> createPostTypes(@RequestBody PostTypes postTypes) throws URISyntaxException {
        LOG.debug("REST request to save PostTypes : {}", postTypes);
        if (postTypes.getId() != null) {
            throw new BadRequestAlertException("A new postTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        postTypes = postTypesRepository.save(postTypes);
        postTypesSearchRepository.index(postTypes);
        return ResponseEntity.created(new URI("/api/post-types/" + postTypes.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, postTypes.getId().toString()))
            .body(postTypes);
    }

    /**
     * {@code PUT  /post-types/:id} : Updates an existing postTypes.
     *
     * @param id the id of the postTypes to save.
     * @param postTypes the postTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postTypes,
     * or with status {@code 400 (Bad Request)} if the postTypes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the postTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostTypes> updatePostTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PostTypes postTypes
    ) throws URISyntaxException {
        LOG.debug("REST request to update PostTypes : {}, {}", id, postTypes);
        if (postTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        postTypes = postTypesRepository.save(postTypes);
        postTypesSearchRepository.index(postTypes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postTypes.getId().toString()))
            .body(postTypes);
    }

    /**
     * {@code PATCH  /post-types/:id} : Partial updates given fields of an existing postTypes, field will ignore if it is null
     *
     * @param id the id of the postTypes to save.
     * @param postTypes the postTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated postTypes,
     * or with status {@code 400 (Bad Request)} if the postTypes is not valid,
     * or with status {@code 404 (Not Found)} if the postTypes is not found,
     * or with status {@code 500 (Internal Server Error)} if the postTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PostTypes> partialUpdatePostTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PostTypes postTypes
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PostTypes partially : {}, {}", id, postTypes);
        if (postTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, postTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!postTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PostTypes> result = postTypesRepository
            .findById(postTypes.getId())
            .map(existingPostTypes -> {
                if (postTypes.getCreatedBy() != null) {
                    existingPostTypes.setCreatedBy(postTypes.getCreatedBy());
                }
                if (postTypes.getCreatedOn() != null) {
                    existingPostTypes.setCreatedOn(postTypes.getCreatedOn());
                }
                if (postTypes.getIsActive() != null) {
                    existingPostTypes.setIsActive(postTypes.getIsActive());
                }
                if (postTypes.getLastUpdatedBy() != null) {
                    existingPostTypes.setLastUpdatedBy(postTypes.getLastUpdatedBy());
                }
                if (postTypes.getLastUpdatedOn() != null) {
                    existingPostTypes.setLastUpdatedOn(postTypes.getLastUpdatedOn());
                }
                if (postTypes.getPostType() != null) {
                    existingPostTypes.setPostType(postTypes.getPostType());
                }
                if (postTypes.getPostTypeId() != null) {
                    existingPostTypes.setPostTypeId(postTypes.getPostTypeId());
                }

                return existingPostTypes;
            })
            .map(postTypesRepository::save)
            .map(savedPostTypes -> {
                postTypesSearchRepository.index(savedPostTypes);
                return savedPostTypes;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, postTypes.getId().toString())
        );
    }

    /**
     * {@code GET  /post-types} : get all the postTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of postTypes in body.
     */
    @GetMapping("")
    public List<PostTypes> getAllPostTypes() {
        LOG.debug("REST request to get all PostTypes");
        return postTypesRepository.findAll();
    }

    /**
     * {@code GET  /post-types/:id} : get the "id" postTypes.
     *
     * @param id the id of the postTypes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the postTypes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostTypes> getPostTypes(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PostTypes : {}", id);
        Optional<PostTypes> postTypes = postTypesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(postTypes);
    }

    /**
     * {@code DELETE  /post-types/:id} : delete the "id" postTypes.
     *
     * @param id the id of the postTypes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePostTypes(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PostTypes : {}", id);
        postTypesRepository.deleteById(id);
        postTypesSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /post-types/_search?query=:query} : search for the postTypes corresponding
     * to the query.
     *
     * @param query the query of the postTypes search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<PostTypes> searchPostTypes(@RequestParam("query") String query) {
        LOG.debug("REST request to search PostTypes for query {}", query);
        try {
            return StreamSupport.stream(postTypesSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
