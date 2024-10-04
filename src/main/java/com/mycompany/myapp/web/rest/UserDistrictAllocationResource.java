package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserDistrictAllocation;
import com.mycompany.myapp.repository.UserDistrictAllocationRepository;
import com.mycompany.myapp.repository.search.UserDistrictAllocationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserDistrictAllocation}.
 */
@RestController
@RequestMapping("/api/user-district-allocations")
@Transactional
public class UserDistrictAllocationResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserDistrictAllocationResource.class);

    private static final String ENTITY_NAME = "userDistrictAllocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserDistrictAllocationRepository userDistrictAllocationRepository;

    private final UserDistrictAllocationSearchRepository userDistrictAllocationSearchRepository;

    public UserDistrictAllocationResource(
        UserDistrictAllocationRepository userDistrictAllocationRepository,
        UserDistrictAllocationSearchRepository userDistrictAllocationSearchRepository
    ) {
        this.userDistrictAllocationRepository = userDistrictAllocationRepository;
        this.userDistrictAllocationSearchRepository = userDistrictAllocationSearchRepository;
    }

    /**
     * {@code POST  /user-district-allocations} : Create a new userDistrictAllocation.
     *
     * @param userDistrictAllocation the userDistrictAllocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userDistrictAllocation, or with status {@code 400 (Bad Request)} if the userDistrictAllocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserDistrictAllocation> createUserDistrictAllocation(@RequestBody UserDistrictAllocation userDistrictAllocation)
        throws URISyntaxException {
        LOG.debug("REST request to save UserDistrictAllocation : {}", userDistrictAllocation);
        if (userDistrictAllocation.getId() != null) {
            throw new BadRequestAlertException("A new userDistrictAllocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userDistrictAllocation = userDistrictAllocationRepository.save(userDistrictAllocation);
        userDistrictAllocationSearchRepository.index(userDistrictAllocation);
        return ResponseEntity.created(new URI("/api/user-district-allocations/" + userDistrictAllocation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userDistrictAllocation.getId().toString()))
            .body(userDistrictAllocation);
    }

    /**
     * {@code PUT  /user-district-allocations/:id} : Updates an existing userDistrictAllocation.
     *
     * @param id the id of the userDistrictAllocation to save.
     * @param userDistrictAllocation the userDistrictAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDistrictAllocation,
     * or with status {@code 400 (Bad Request)} if the userDistrictAllocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userDistrictAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDistrictAllocation> updateUserDistrictAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDistrictAllocation userDistrictAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserDistrictAllocation : {}, {}", id, userDistrictAllocation);
        if (userDistrictAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDistrictAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDistrictAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userDistrictAllocation = userDistrictAllocationRepository.save(userDistrictAllocation);
        userDistrictAllocationSearchRepository.index(userDistrictAllocation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDistrictAllocation.getId().toString()))
            .body(userDistrictAllocation);
    }

    /**
     * {@code PATCH  /user-district-allocations/:id} : Partial updates given fields of an existing userDistrictAllocation, field will ignore if it is null
     *
     * @param id the id of the userDistrictAllocation to save.
     * @param userDistrictAllocation the userDistrictAllocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userDistrictAllocation,
     * or with status {@code 400 (Bad Request)} if the userDistrictAllocation is not valid,
     * or with status {@code 404 (Not Found)} if the userDistrictAllocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the userDistrictAllocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserDistrictAllocation> partialUpdateUserDistrictAllocation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserDistrictAllocation userDistrictAllocation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserDistrictAllocation partially : {}, {}", id, userDistrictAllocation);
        if (userDistrictAllocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userDistrictAllocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userDistrictAllocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserDistrictAllocation> result = userDistrictAllocationRepository
            .findById(userDistrictAllocation.getId())
            .map(existingUserDistrictAllocation -> {
                if (userDistrictAllocation.getCreatedBy() != null) {
                    existingUserDistrictAllocation.setCreatedBy(userDistrictAllocation.getCreatedBy());
                }
                if (userDistrictAllocation.getCreatedOn() != null) {
                    existingUserDistrictAllocation.setCreatedOn(userDistrictAllocation.getCreatedOn());
                }
                if (userDistrictAllocation.getIsAllocated() != null) {
                    existingUserDistrictAllocation.setIsAllocated(userDistrictAllocation.getIsAllocated());
                }
                if (userDistrictAllocation.getSchoolDistrictId() != null) {
                    existingUserDistrictAllocation.setSchoolDistrictId(userDistrictAllocation.getSchoolDistrictId());
                }
                if (userDistrictAllocation.getUpdatedBy() != null) {
                    existingUserDistrictAllocation.setUpdatedBy(userDistrictAllocation.getUpdatedBy());
                }
                if (userDistrictAllocation.getUpdatedOn() != null) {
                    existingUserDistrictAllocation.setUpdatedOn(userDistrictAllocation.getUpdatedOn());
                }
                if (userDistrictAllocation.getUserDistrictAllocationId() != null) {
                    existingUserDistrictAllocation.setUserDistrictAllocationId(userDistrictAllocation.getUserDistrictAllocationId());
                }
                if (userDistrictAllocation.getUserId() != null) {
                    existingUserDistrictAllocation.setUserId(userDistrictAllocation.getUserId());
                }

                return existingUserDistrictAllocation;
            })
            .map(userDistrictAllocationRepository::save)
            .map(savedUserDistrictAllocation -> {
                userDistrictAllocationSearchRepository.index(savedUserDistrictAllocation);
                return savedUserDistrictAllocation;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userDistrictAllocation.getId().toString())
        );
    }

    /**
     * {@code GET  /user-district-allocations} : get all the userDistrictAllocations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userDistrictAllocations in body.
     */
    @GetMapping("")
    public List<UserDistrictAllocation> getAllUserDistrictAllocations() {
        LOG.debug("REST request to get all UserDistrictAllocations");
        return userDistrictAllocationRepository.findAll();
    }

    /**
     * {@code GET  /user-district-allocations/:id} : get the "id" userDistrictAllocation.
     *
     * @param id the id of the userDistrictAllocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userDistrictAllocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDistrictAllocation> getUserDistrictAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserDistrictAllocation : {}", id);
        Optional<UserDistrictAllocation> userDistrictAllocation = userDistrictAllocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userDistrictAllocation);
    }

    /**
     * {@code DELETE  /user-district-allocations/:id} : delete the "id" userDistrictAllocation.
     *
     * @param id the id of the userDistrictAllocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserDistrictAllocation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserDistrictAllocation : {}", id);
        userDistrictAllocationRepository.deleteById(id);
        userDistrictAllocationSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /user-district-allocations/_search?query=:query} : search for the userDistrictAllocation corresponding
     * to the query.
     *
     * @param query the query of the userDistrictAllocation search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<UserDistrictAllocation> searchUserDistrictAllocations(@RequestParam("query") String query) {
        LOG.debug("REST request to search UserDistrictAllocations for query {}", query);
        try {
            return StreamSupport.stream(userDistrictAllocationSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
