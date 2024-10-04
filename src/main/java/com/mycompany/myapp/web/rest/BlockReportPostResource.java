package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BlockReportPost;
import com.mycompany.myapp.repository.BlockReportPostRepository;
import com.mycompany.myapp.repository.search.BlockReportPostSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BlockReportPost}.
 */
@RestController
@RequestMapping("/api/block-report-posts")
@Transactional
public class BlockReportPostResource {

    private static final Logger LOG = LoggerFactory.getLogger(BlockReportPostResource.class);

    private static final String ENTITY_NAME = "blockReportPost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BlockReportPostRepository blockReportPostRepository;

    private final BlockReportPostSearchRepository blockReportPostSearchRepository;

    public BlockReportPostResource(
        BlockReportPostRepository blockReportPostRepository,
        BlockReportPostSearchRepository blockReportPostSearchRepository
    ) {
        this.blockReportPostRepository = blockReportPostRepository;
        this.blockReportPostSearchRepository = blockReportPostSearchRepository;
    }

    /**
     * {@code POST  /block-report-posts} : Create a new blockReportPost.
     *
     * @param blockReportPost the blockReportPost to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new blockReportPost, or with status {@code 400 (Bad Request)} if the blockReportPost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BlockReportPost> createBlockReportPost(@RequestBody BlockReportPost blockReportPost) throws URISyntaxException {
        LOG.debug("REST request to save BlockReportPost : {}", blockReportPost);
        if (blockReportPost.getId() != null) {
            throw new BadRequestAlertException("A new blockReportPost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        blockReportPost = blockReportPostRepository.save(blockReportPost);
        blockReportPostSearchRepository.index(blockReportPost);
        return ResponseEntity.created(new URI("/api/block-report-posts/" + blockReportPost.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, blockReportPost.getId().toString()))
            .body(blockReportPost);
    }

    /**
     * {@code PUT  /block-report-posts/:id} : Updates an existing blockReportPost.
     *
     * @param id the id of the blockReportPost to save.
     * @param blockReportPost the blockReportPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockReportPost,
     * or with status {@code 400 (Bad Request)} if the blockReportPost is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blockReportPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BlockReportPost> updateBlockReportPost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlockReportPost blockReportPost
    ) throws URISyntaxException {
        LOG.debug("REST request to update BlockReportPost : {}, {}", id, blockReportPost);
        if (blockReportPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockReportPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockReportPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        blockReportPost = blockReportPostRepository.save(blockReportPost);
        blockReportPostSearchRepository.index(blockReportPost);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockReportPost.getId().toString()))
            .body(blockReportPost);
    }

    /**
     * {@code PATCH  /block-report-posts/:id} : Partial updates given fields of an existing blockReportPost, field will ignore if it is null
     *
     * @param id the id of the blockReportPost to save.
     * @param blockReportPost the blockReportPost to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated blockReportPost,
     * or with status {@code 400 (Bad Request)} if the blockReportPost is not valid,
     * or with status {@code 404 (Not Found)} if the blockReportPost is not found,
     * or with status {@code 500 (Internal Server Error)} if the blockReportPost couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BlockReportPost> partialUpdateBlockReportPost(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody BlockReportPost blockReportPost
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BlockReportPost partially : {}, {}", id, blockReportPost);
        if (blockReportPost.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, blockReportPost.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!blockReportPostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BlockReportPost> result = blockReportPostRepository
            .findById(blockReportPost.getId())
            .map(existingBlockReportPost -> {
                if (blockReportPost.getBlockCategories() != null) {
                    existingBlockReportPost.setBlockCategories(blockReportPost.getBlockCategories());
                }
                if (blockReportPost.getBlockingReason() != null) {
                    existingBlockReportPost.setBlockingReason(blockReportPost.getBlockingReason());
                }
                if (blockReportPost.getPostBlockReportId() != null) {
                    existingBlockReportPost.setPostBlockReportId(blockReportPost.getPostBlockReportId());
                }
                if (blockReportPost.getPostId() != null) {
                    existingBlockReportPost.setPostId(blockReportPost.getPostId());
                }
                if (blockReportPost.getPostType() != null) {
                    existingBlockReportPost.setPostType(blockReportPost.getPostType());
                }
                if (blockReportPost.getRequestedBy() != null) {
                    existingBlockReportPost.setRequestedBy(blockReportPost.getRequestedBy());
                }

                return existingBlockReportPost;
            })
            .map(blockReportPostRepository::save)
            .map(savedBlockReportPost -> {
                blockReportPostSearchRepository.index(savedBlockReportPost);
                return savedBlockReportPost;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blockReportPost.getId().toString())
        );
    }

    /**
     * {@code GET  /block-report-posts} : get all the blockReportPosts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of blockReportPosts in body.
     */
    @GetMapping("")
    public List<BlockReportPost> getAllBlockReportPosts() {
        LOG.debug("REST request to get all BlockReportPosts");
        return blockReportPostRepository.findAll();
    }

    /**
     * {@code GET  /block-report-posts/:id} : get the "id" blockReportPost.
     *
     * @param id the id of the blockReportPost to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the blockReportPost, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BlockReportPost> getBlockReportPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BlockReportPost : {}", id);
        Optional<BlockReportPost> blockReportPost = blockReportPostRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(blockReportPost);
    }

    /**
     * {@code DELETE  /block-report-posts/:id} : delete the "id" blockReportPost.
     *
     * @param id the id of the blockReportPost to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlockReportPost(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BlockReportPost : {}", id);
        blockReportPostRepository.deleteById(id);
        blockReportPostSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /block-report-posts/_search?query=:query} : search for the blockReportPost corresponding
     * to the query.
     *
     * @param query the query of the blockReportPost search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<BlockReportPost> searchBlockReportPosts(@RequestParam("query") String query) {
        LOG.debug("REST request to search BlockReportPosts for query {}", query);
        try {
            return StreamSupport.stream(blockReportPostSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
