package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserMst;
import com.mycompany.myapp.repository.UserMstRepository;
import com.mycompany.myapp.repository.search.UserMstSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserMst}.
 */
@RestController
@RequestMapping("/api/user-msts")
@Transactional
public class UserMstResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserMstResource.class);

    private static final String ENTITY_NAME = "userMst";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserMstRepository userMstRepository;

    private final UserMstSearchRepository userMstSearchRepository;

    public UserMstResource(UserMstRepository userMstRepository, UserMstSearchRepository userMstSearchRepository) {
        this.userMstRepository = userMstRepository;
        this.userMstSearchRepository = userMstSearchRepository;
    }

    /**
     * {@code POST  /user-msts} : Create a new userMst.
     *
     * @param userMst the userMst to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userMst, or with status {@code 400 (Bad Request)} if the userMst has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserMst> createUserMst(@RequestBody UserMst userMst) throws URISyntaxException {
        LOG.debug("REST request to save UserMst : {}", userMst);
        if (userMst.getId() != null) {
            throw new BadRequestAlertException("A new userMst cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userMst = userMstRepository.save(userMst);
        userMstSearchRepository.index(userMst);
        return ResponseEntity.created(new URI("/api/user-msts/" + userMst.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userMst.getId().toString()))
            .body(userMst);
    }

    /**
     * {@code PUT  /user-msts/:id} : Updates an existing userMst.
     *
     * @param id the id of the userMst to save.
     * @param userMst the userMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMst,
     * or with status {@code 400 (Bad Request)} if the userMst is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserMst> updateUserMst(@PathVariable(value = "id", required = false) final Long id, @RequestBody UserMst userMst)
        throws URISyntaxException {
        LOG.debug("REST request to update UserMst : {}, {}", id, userMst);
        if (userMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userMst = userMstRepository.save(userMst);
        userMstSearchRepository.index(userMst);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMst.getId().toString()))
            .body(userMst);
    }

    /**
     * {@code PATCH  /user-msts/:id} : Partial updates given fields of an existing userMst, field will ignore if it is null
     *
     * @param id the id of the userMst to save.
     * @param userMst the userMst to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMst,
     * or with status {@code 400 (Bad Request)} if the userMst is not valid,
     * or with status {@code 404 (Not Found)} if the userMst is not found,
     * or with status {@code 500 (Internal Server Error)} if the userMst couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserMst> partialUpdateUserMst(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserMst userMst
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserMst partially : {}, {}", id, userMst);
        if (userMst.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMst.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMstRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserMst> result = userMstRepository
            .findById(userMst.getId())
            .map(existingUserMst -> {
                if (userMst.getAddressLine1() != null) {
                    existingUserMst.setAddressLine1(userMst.getAddressLine1());
                }
                if (userMst.getAddressLine2() != null) {
                    existingUserMst.setAddressLine2(userMst.getAddressLine2());
                }
                if (userMst.getCity() != null) {
                    existingUserMst.setCity(userMst.getCity());
                }
                if (userMst.getCountry() != null) {
                    existingUserMst.setCountry(userMst.getCountry());
                }
                if (userMst.getCoverImage() != null) {
                    existingUserMst.setCoverImage(userMst.getCoverImage());
                }
                if (userMst.getCreateBy() != null) {
                    existingUserMst.setCreateBy(userMst.getCreateBy());
                }
                if (userMst.getCreatedOn() != null) {
                    existingUserMst.setCreatedOn(userMst.getCreatedOn());
                }
                if (userMst.getEmail() != null) {
                    existingUserMst.setEmail(userMst.getEmail());
                }
                if (userMst.getFirstName() != null) {
                    existingUserMst.setFirstName(userMst.getFirstName());
                }
                if (userMst.getIsActive() != null) {
                    existingUserMst.setIsActive(userMst.getIsActive());
                }
                if (userMst.getLastName() != null) {
                    existingUserMst.setLastName(userMst.getLastName());
                }
                if (userMst.getManufacturerId() != null) {
                    existingUserMst.setManufacturerId(userMst.getManufacturerId());
                }
                if (userMst.getMobile() != null) {
                    existingUserMst.setMobile(userMst.getMobile());
                }
                if (userMst.getObjectId() != null) {
                    existingUserMst.setObjectId(userMst.getObjectId());
                }
                if (userMst.getPassword() != null) {
                    existingUserMst.setPassword(userMst.getPassword());
                }
                if (userMst.getProfileImage() != null) {
                    existingUserMst.setProfileImage(userMst.getProfileImage());
                }
                if (userMst.getRoleId() != null) {
                    existingUserMst.setRoleId(userMst.getRoleId());
                }
                if (userMst.getSchoolDistrictId() != null) {
                    existingUserMst.setSchoolDistrictId(userMst.getSchoolDistrictId());
                }
                if (userMst.getState() != null) {
                    existingUserMst.setState(userMst.getState());
                }
                if (userMst.getUpdatedBy() != null) {
                    existingUserMst.setUpdatedBy(userMst.getUpdatedBy());
                }
                if (userMst.getUpdatedOn() != null) {
                    existingUserMst.setUpdatedOn(userMst.getUpdatedOn());
                }
                if (userMst.getUserId() != null) {
                    existingUserMst.setUserId(userMst.getUserId());
                }
                if (userMst.getZipCode() != null) {
                    existingUserMst.setZipCode(userMst.getZipCode());
                }

                return existingUserMst;
            })
            .map(userMstRepository::save)
            .map(savedUserMst -> {
                userMstSearchRepository.index(savedUserMst);
                return savedUserMst;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMst.getId().toString())
        );
    }

    /**
     * {@code GET  /user-msts} : get all the userMsts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userMsts in body.
     */
    @GetMapping("")
    public List<UserMst> getAllUserMsts() {
        LOG.debug("REST request to get all UserMsts");
        return userMstRepository.findAll();
    }

    /**
     * {@code GET  /user-msts/:id} : get the "id" userMst.
     *
     * @param id the id of the userMst to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userMst, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserMst> getUserMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserMst : {}", id);
        Optional<UserMst> userMst = userMstRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userMst);
    }

    /**
     * {@code DELETE  /user-msts/:id} : delete the "id" userMst.
     *
     * @param id the id of the userMst to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserMst(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserMst : {}", id);
        userMstRepository.deleteById(id);
        userMstSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /user-msts/_search?query=:query} : search for the userMst corresponding
     * to the query.
     *
     * @param query the query of the userMst search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<UserMst> searchUserMsts(@RequestParam("query") String query) {
        LOG.debug("REST request to search UserMsts for query {}", query);
        try {
            return StreamSupport.stream(userMstSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
