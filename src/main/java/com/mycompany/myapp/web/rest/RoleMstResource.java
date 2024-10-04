package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.RoleMst;
import com.mycompany.myapp.repository.RoleMstRepository;
import com.mycompany.myapp.repository.search.RoleMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.RoleMst}.
 */
@RestController
@RequestMapping("/api/role-msts")
@Transactional
public class RoleMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(RoleMstResource.class);

    private static final String ENTITY_NAME = "roleMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoleMstRepository roleMstRepository;

    private final RoleMstSearchRepository roleMstSearchRepository;

    public RoleMstResource(RoleMstRepository roleMstRepository, RoleMstSearchRepository roleMstSearchRepository) {
        this.roleMstRepository = roleMstRepository;
        this.roleMstSearchRepository = roleMstSearchRepository;
    }

    /**
     * {@code POST  /role-msts} : Create a new roleMst.
     *
     * @param roleMst the roleMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roleMst, or with status {@code 400 (Bad Request)} if the roleMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoleMst> createRoleMst(@RequestBody RoleMst roleMst) throws URISyntaxException {
        LOG.debug("REST request to save RoleMst : {}", roleMst);
        if (roleMst.getId() != null) {
            throw new BadRequestAlertException("A new roleMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        roleMst = roleMstRepository.save(roleMst);
        roleMstSearchRepository.index(roleMst);
        return ResponseEntity.created(new URI("/api/role-msts/" + roleMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, roleMst.getId().toString()))
            .body(roleMst);
    }

    /**
     * {@code PUT  /role-msts/:id} : Updates an existing roleMst.
     *
     * @param id the id of the roleMst to save.
     * @param roleMst the roleMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleMst,
     * or with status {@code 400 (Bad Request)} if the roleMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roleMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoleMst> updateRoleMst(@PathVariable(value = "id", required = false) final Long id, @RequestBody RoleMst roleMst)
        throws URISyntaxException {
        LOG.debug("REST request to update RoleMst : {}, {}", id, roleMst);
        if (roleMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        roleMst = roleMstRepository.save(roleMst);
        roleMstSearchRepository.index(roleMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleMst.getId().toString()))
            .body(roleMst);
    }

    /**
     * {@code PATCH  /role-msts/:id} : Partial updates given fields of an existing roleMst, field will ignore if it is null
     *
     * @param id the id of the roleMst to save.
     * @param roleMst the roleMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roleMst,
     * or with status {@code 400 (Bad Request)} if the roleMst is not valid,
     * or with status {@code 404 (Not Found)} if the roleMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the roleMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoleMst> partialUpdateRoleMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoleMst roleMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RoleMst partially : {}, {}", id, roleMst);
        if (roleMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roleMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoleMst> result = roleMstRepository
            .findById(roleMst.getId())
            .map(existingRoleMst -> {
                if (roleMst.getCreatedBy() != null) {
                    existingRoleMst.setCreatedBy(roleMst.getCreatedBy());
                }
                if (roleMst.getCreatedOn() != null) {
                    existingRoleMst.setCreatedOn(roleMst.getCreatedOn());
                }
                if (roleMst.getIsActive() != null) {
                    existingRoleMst.setIsActive(roleMst.getIsActive());
                }
                if (roleMst.getParentRoleId() != null) {
                    existingRoleMst.setParentRoleId(roleMst.getParentRoleId());
                }
                if (roleMst.getRoleId() != null) {
                    existingRoleMst.setRoleId(roleMst.getRoleId());
                }
                if (roleMst.getRoleName() != null) {
                    existingRoleMst.setRoleName(roleMst.getRoleName());
                }
                if (roleMst.getUpdatedBy() != null) {
                    existingRoleMst.setUpdatedBy(roleMst.getUpdatedBy());
                }
                if (roleMst.getUpdatedOn() != null) {
                    existingRoleMst.setUpdatedOn(roleMst.getUpdatedOn());
                }

                return existingRoleMst;
            })
            .map(roleMstRepository::save)
            .map(savedRoleMst -> {
                roleMstSearchRepository.index(savedRoleMst);
                return savedRoleMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roleMst.getId().toString())
        );
    }

    /**
     * {@code GET  /role-msts} : get all the roleMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roleMsts in body.
     */
    @GetMapping("")
    public List<RoleMst> getAllRoleMsts() {
        LOG.debug("REST request to get all RoleMsts");
        return roleMstRepository.findAll();
    }

    /**
     * {@code GET  /role-msts/:id} : get the "id" roleMst.
     *
     * @param id the id of the roleMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roleMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoleMst> getRoleMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RoleMst : {}", id);
        Optional<RoleMst> roleMst = roleMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(roleMst);
    }

    /**
     * {@code DELETE  /role-msts/:id} : delete the "id" roleMst.
     *
     * @param id the id of the roleMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoleMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RoleMst : {}", id);
        roleMstRepository.deleteById(id);
        roleMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /role-msts/_search?query=:query} : search for the roleMst corresponding
     * to the query.
     *
     * @param query the query of the roleMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<RoleMst> searchRoleMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search RoleMsts for query {}", query);
        try {
            return StreamSupport.stream(roleMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
