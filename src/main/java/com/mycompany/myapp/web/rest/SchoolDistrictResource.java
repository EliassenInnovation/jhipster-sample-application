package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SchoolDistrict;
import com.mycompany.myapp.repository.SchoolDistrictRepository;
import com.mycompany.myapp.repository.search.SchoolDistrictSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SchoolDistrict}.
 */
@RestController
@RequestMapping("/api/school-districts")
@Transactional
public class SchoolDistrictResource {

    private static final Logger LOG = LoggerFactory.getLogger(SchoolDistrictResource.class);

    private static final String ENTITY_NAME = "schoolDistrict";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SchoolDistrictRepository schoolDistrictRepository;

    private final SchoolDistrictSearchRepository schoolDistrictSearchRepository;

    public SchoolDistrictResource(
        SchoolDistrictRepository schoolDistrictRepository,
        SchoolDistrictSearchRepository schoolDistrictSearchRepository
    ) {
        this.schoolDistrictRepository = schoolDistrictRepository;
        this.schoolDistrictSearchRepository = schoolDistrictSearchRepository;
    }

    /**
     * {@code POST  /school-districts} : Create a new schoolDistrict.
     *
     * @param schoolDistrict the schoolDistrict to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new schoolDistrict, or with status {@code 400 (Bad Request)} if the schoolDistrict has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SchoolDistrict> createSchoolDistrict(@RequestBody SchoolDistrict schoolDistrict) throws URISyntaxException {
        LOG.debug("REST request to save SchoolDistrict : {}", schoolDistrict);
        if (schoolDistrict.getId() != null) {
            throw new BadRequestAlertException("A new schoolDistrict cannot already have an ID", ENTITY_NAME, "idexists");
        }
        schoolDistrict = schoolDistrictRepository.save(schoolDistrict);
        schoolDistrictSearchRepository.index(schoolDistrict);
        return ResponseEntity.created(new URI("/api/school-districts/" + schoolDistrict.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, schoolDistrict.getId().toString()))
            .body(schoolDistrict);
    }

    /**
     * {@code PUT  /school-districts/:id} : Updates an existing schoolDistrict.
     *
     * @param id the id of the schoolDistrict to save.
     * @param schoolDistrict the schoolDistrict to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolDistrict,
     * or with status {@code 400 (Bad Request)} if the schoolDistrict is not valid,
     * or with status {@code 500 (Internal Server Error)} if the schoolDistrict couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SchoolDistrict> updateSchoolDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchoolDistrict schoolDistrict
    ) throws URISyntaxException {
        LOG.debug("REST request to update SchoolDistrict : {}, {}", id, schoolDistrict);
        if (schoolDistrict.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolDistrict.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolDistrictRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        schoolDistrict = schoolDistrictRepository.save(schoolDistrict);
        schoolDistrictSearchRepository.index(schoolDistrict);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolDistrict.getId().toString()))
            .body(schoolDistrict);
    }

    /**
     * {@code PATCH  /school-districts/:id} : Partial updates given fields of an existing schoolDistrict, field will ignore if it is null
     *
     * @param id the id of the schoolDistrict to save.
     * @param schoolDistrict the schoolDistrict to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated schoolDistrict,
     * or with status {@code 400 (Bad Request)} if the schoolDistrict is not valid,
     * or with status {@code 404 (Not Found)} if the schoolDistrict is not found,
     * or with status {@code 500 (Internal Server Error)} if the schoolDistrict couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SchoolDistrict> partialUpdateSchoolDistrict(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SchoolDistrict schoolDistrict
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SchoolDistrict partially : {}, {}", id, schoolDistrict);
        if (schoolDistrict.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, schoolDistrict.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!schoolDistrictRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SchoolDistrict> result = schoolDistrictRepository
            .findById(schoolDistrict.getId())
            .map(existingSchoolDistrict -> {
                if (schoolDistrict.getCity() != null) {
                    existingSchoolDistrict.setCity(schoolDistrict.getCity());
                }
                if (schoolDistrict.getContractCompany() != null) {
                    existingSchoolDistrict.setContractCompany(schoolDistrict.getContractCompany());
                }
                if (schoolDistrict.getCountry() != null) {
                    existingSchoolDistrict.setCountry(schoolDistrict.getCountry());
                }
                if (schoolDistrict.getCreatedBy() != null) {
                    existingSchoolDistrict.setCreatedBy(schoolDistrict.getCreatedBy());
                }
                if (schoolDistrict.getCreatedOn() != null) {
                    existingSchoolDistrict.setCreatedOn(schoolDistrict.getCreatedOn());
                }
                if (schoolDistrict.getDistrictLogo() != null) {
                    existingSchoolDistrict.setDistrictLogo(schoolDistrict.getDistrictLogo());
                }
                if (schoolDistrict.getDistrictName() != null) {
                    existingSchoolDistrict.setDistrictName(schoolDistrict.getDistrictName());
                }
                if (schoolDistrict.getEmail() != null) {
                    existingSchoolDistrict.setEmail(schoolDistrict.getEmail());
                }
                if (schoolDistrict.getFoodServiceOptions() != null) {
                    existingSchoolDistrict.setFoodServiceOptions(schoolDistrict.getFoodServiceOptions());
                }
                if (schoolDistrict.getIsActive() != null) {
                    existingSchoolDistrict.setIsActive(schoolDistrict.getIsActive());
                }
                if (schoolDistrict.getLastUpdatedBy() != null) {
                    existingSchoolDistrict.setLastUpdatedBy(schoolDistrict.getLastUpdatedBy());
                }
                if (schoolDistrict.getLastUpdatedOn() != null) {
                    existingSchoolDistrict.setLastUpdatedOn(schoolDistrict.getLastUpdatedOn());
                }
                if (schoolDistrict.getPhoneNumber() != null) {
                    existingSchoolDistrict.setPhoneNumber(schoolDistrict.getPhoneNumber());
                }
                if (schoolDistrict.getSchoolDistrictId() != null) {
                    existingSchoolDistrict.setSchoolDistrictId(schoolDistrict.getSchoolDistrictId());
                }
                if (schoolDistrict.getSiteCode() != null) {
                    existingSchoolDistrict.setSiteCode(schoolDistrict.getSiteCode());
                }
                if (schoolDistrict.getState() != null) {
                    existingSchoolDistrict.setState(schoolDistrict.getState());
                }

                return existingSchoolDistrict;
            })
            .map(schoolDistrictRepository::save)
            .map(savedSchoolDistrict -> {
                schoolDistrictSearchRepository.index(savedSchoolDistrict);
                return savedSchoolDistrict;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, schoolDistrict.getId().toString())
        );
    }

    /**
     * {@code GET  /school-districts} : get all the schoolDistricts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of schoolDistricts in body.
     */
    @GetMapping("")
    public List<SchoolDistrict> getAllSchoolDistricts() {
        LOG.debug("REST request to get all SchoolDistricts");
        return schoolDistrictRepository.findAll();
    }

    /**
     * {@code GET  /school-districts/:id} : get the "id" schoolDistrict.
     *
     * @param id the id of the schoolDistrict to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the schoolDistrict, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SchoolDistrict> getSchoolDistrict(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SchoolDistrict : {}", id);
        Optional<SchoolDistrict> schoolDistrict = schoolDistrictRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(schoolDistrict);
    }

    /**
     * {@code DELETE  /school-districts/:id} : delete the "id" schoolDistrict.
     *
     * @param id the id of the schoolDistrict to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchoolDistrict(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SchoolDistrict : {}", id);
        schoolDistrictRepository.deleteById(id);
        schoolDistrictSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /school-districts/_search?query=:query} : search for the schoolDistrict corresponding
     * to the query.
     *
     * @param query the query of the schoolDistrict search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<SchoolDistrict> searchSchoolDistricts(@RequestParam("query") String query) {
        LOG.debug("REST request to search SchoolDistricts for query {}", query);
        try {
            return StreamSupport.stream(schoolDistrictSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
