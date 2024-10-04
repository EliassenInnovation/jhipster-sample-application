package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Distributor;
import com.mycompany.myapp.repository.DistributorRepository;
import com.mycompany.myapp.repository.search.DistributorSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Distributor}.
 */
@RestController
@RequestMapping("/api/distributors")
@Transactional
public class DistributorResource {

    private static final Logger LOG = LoggerFactory.getLogger(DistributorResource.class);

    private static final String ENTITY_NAME = "distributor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DistributorRepository distributorRepository;

    private final DistributorSearchRepository distributorSearchRepository;

    public DistributorResource(DistributorRepository distributorRepository, DistributorSearchRepository distributorSearchRepository) {
        this.distributorRepository = distributorRepository;
        this.distributorSearchRepository = distributorSearchRepository;
    }

    /**
     * {@code POST  /distributors} : Create a new distributor.
     *
     * @param distributor the distributor to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new distributor, or with status {@code 400 (Bad Request)} if the distributor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Distributor> createDistributor(@RequestBody Distributor distributor) throws URISyntaxException {
        LOG.debug("REST request to save Distributor : {}", distributor);
        if (distributor.getId() != null) {
            throw new BadRequestAlertException("A new distributor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        distributor = distributorRepository.save(distributor);
        distributorSearchRepository.index(distributor);
        return ResponseEntity.created(new URI("/api/distributors/" + distributor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, distributor.getId().toString()))
            .body(distributor);
    }

    /**
     * {@code PUT  /distributors/:id} : Updates an existing distributor.
     *
     * @param id the id of the distributor to save.
     * @param distributor the distributor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distributor,
     * or with status {@code 400 (Bad Request)} if the distributor is not valid,
     * or with status {@code 500 (Internal Server Error)} if the distributor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Distributor> updateDistributor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Distributor distributor
    ) throws URISyntaxException {
        LOG.debug("REST request to update Distributor : {}, {}", id, distributor);
        if (distributor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distributor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distributorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        distributor = distributorRepository.save(distributor);
        distributorSearchRepository.index(distributor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, distributor.getId().toString()))
            .body(distributor);
    }

    /**
     * {@code PATCH  /distributors/:id} : Partial updates given fields of an existing distributor, field will ignore if it is null
     *
     * @param id the id of the distributor to save.
     * @param distributor the distributor to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated distributor,
     * or with status {@code 400 (Bad Request)} if the distributor is not valid,
     * or with status {@code 404 (Not Found)} if the distributor is not found,
     * or with status {@code 500 (Internal Server Error)} if the distributor couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Distributor> partialUpdateDistributor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Distributor distributor
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Distributor partially : {}, {}", id, distributor);
        if (distributor.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, distributor.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!distributorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Distributor> result = distributorRepository
            .findById(distributor.getId())
            .map(existingDistributor -> {
                if (distributor.getCreatedBy() != null) {
                    existingDistributor.setCreatedBy(distributor.getCreatedBy());
                }
                if (distributor.getCreatedOn() != null) {
                    existingDistributor.setCreatedOn(distributor.getCreatedOn());
                }
                if (distributor.getDistributorCode() != null) {
                    existingDistributor.setDistributorCode(distributor.getDistributorCode());
                }
                if (distributor.getDistributorID() != null) {
                    existingDistributor.setDistributorID(distributor.getDistributorID());
                }
                if (distributor.getDistributorName() != null) {
                    existingDistributor.setDistributorName(distributor.getDistributorName());
                }
                if (distributor.getIsActive() != null) {
                    existingDistributor.setIsActive(distributor.getIsActive());
                }
                if (distributor.getUpdatedBy() != null) {
                    existingDistributor.setUpdatedBy(distributor.getUpdatedBy());
                }
                if (distributor.getUpdatedOn() != null) {
                    existingDistributor.setUpdatedOn(distributor.getUpdatedOn());
                }

                return existingDistributor;
            })
            .map(distributorRepository::save)
            .map(savedDistributor -> {
                distributorSearchRepository.index(savedDistributor);
                return savedDistributor;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, distributor.getId().toString())
        );
    }

    /**
     * {@code GET  /distributors} : get all the distributors.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of distributors in body.
     */
    @GetMapping("")
    public List<Distributor> getAllDistributors() {
        LOG.debug("REST request to get all Distributors");
        return distributorRepository.findAll();
    }

    /**
     * {@code GET  /distributors/:id} : get the "id" distributor.
     *
     * @param id the id of the distributor to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the distributor, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Distributor> getDistributor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Distributor : {}", id);
        Optional<Distributor> distributor = distributorRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(distributor);
    }

    /**
     * {@code DELETE  /distributors/:id} : delete the "id" distributor.
     *
     * @param id the id of the distributor to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDistributor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Distributor : {}", id);
        distributorRepository.deleteById(id);
        distributorSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /distributors/_search?query=:query} : search for the distributor corresponding
     * to the query.
     *
     * @param query the query of the distributor search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Distributor> searchDistributors(@RequestParam("query") String query) {
        LOG.debug("REST request to search Distributors for query {}", query);
        try {
            return StreamSupport.stream(distributorSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
