package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Manufacturer;
import com.mycompany.myapp.repository.ManufacturerRepository;
import com.mycompany.myapp.repository.search.ManufacturerSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Manufacturer}.
 */
@RestController
@RequestMapping("/api/manufacturers")
@Transactional
public class ManufacturerResource {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerResource.class);

    private static final String ENTITY_NAME = "manufacturer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManufacturerRepository manufacturerRepository;

    private final ManufacturerSearchRepository manufacturerSearchRepository;

    public ManufacturerResource(ManufacturerRepository manufacturerRepository, ManufacturerSearchRepository manufacturerSearchRepository) {
        this.manufacturerRepository = manufacturerRepository;
        this.manufacturerSearchRepository = manufacturerSearchRepository;
    }

    /**
     * {@code POST  /manufacturers} : Create a new manufacturer.
     *
     * @param manufacturer the manufacturer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new manufacturer, or with status {@code 400 (Bad Request)} if the manufacturer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) throws URISyntaxException {
        LOG.debug("REST request to save Manufacturer : {}", manufacturer);
        if (manufacturer.getId() != null) {
            throw new BadRequestAlertException("A new manufacturer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        manufacturer = manufacturerRepository.save(manufacturer);
        manufacturerSearchRepository.index(manufacturer);
        return ResponseEntity.created(new URI("/api/manufacturers/" + manufacturer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, manufacturer.getId().toString()))
            .body(manufacturer);
    }

    /**
     * {@code PUT  /manufacturers/:id} : Updates an existing manufacturer.
     *
     * @param id the id of the manufacturer to save.
     * @param manufacturer the manufacturer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufacturer,
     * or with status {@code 400 (Bad Request)} if the manufacturer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the manufacturer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Manufacturer> updateManufacturer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manufacturer manufacturer
    ) throws URISyntaxException {
        LOG.debug("REST request to update Manufacturer : {}, {}", id, manufacturer);
        if (manufacturer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufacturer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufacturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        manufacturer = manufacturerRepository.save(manufacturer);
        manufacturerSearchRepository.index(manufacturer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manufacturer.getId().toString()))
            .body(manufacturer);
    }

    /**
     * {@code PATCH  /manufacturers/:id} : Partial updates given fields of an existing manufacturer, field will ignore if it is null
     *
     * @param id the id of the manufacturer to save.
     * @param manufacturer the manufacturer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated manufacturer,
     * or with status {@code 400 (Bad Request)} if the manufacturer is not valid,
     * or with status {@code 404 (Not Found)} if the manufacturer is not found,
     * or with status {@code 500 (Internal Server Error)} if the manufacturer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Manufacturer> partialUpdateManufacturer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Manufacturer manufacturer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Manufacturer partially : {}, {}", id, manufacturer);
        if (manufacturer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, manufacturer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!manufacturerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Manufacturer> result = manufacturerRepository
            .findById(manufacturer.getId())
            .map(existingManufacturer -> {
                if (manufacturer.getCreatedBy() != null) {
                    existingManufacturer.setCreatedBy(manufacturer.getCreatedBy());
                }
                if (manufacturer.getCreatedOn() != null) {
                    existingManufacturer.setCreatedOn(manufacturer.getCreatedOn());
                }
                if (manufacturer.getEmail() != null) {
                    existingManufacturer.setEmail(manufacturer.getEmail());
                }
                if (manufacturer.getFirstName() != null) {
                    existingManufacturer.setFirstName(manufacturer.getFirstName());
                }
                if (manufacturer.getGlnNumber() != null) {
                    existingManufacturer.setGlnNumber(manufacturer.getGlnNumber());
                }
                if (manufacturer.getIsActive() != null) {
                    existingManufacturer.setIsActive(manufacturer.getIsActive());
                }
                if (manufacturer.getLastName() != null) {
                    existingManufacturer.setLastName(manufacturer.getLastName());
                }
                if (manufacturer.getManufacturer() != null) {
                    existingManufacturer.setManufacturer(manufacturer.getManufacturer());
                }
                if (manufacturer.getManufacturerId() != null) {
                    existingManufacturer.setManufacturerId(manufacturer.getManufacturerId());
                }
                if (manufacturer.getPassword() != null) {
                    existingManufacturer.setPassword(manufacturer.getPassword());
                }
                if (manufacturer.getPhoneNumber() != null) {
                    existingManufacturer.setPhoneNumber(manufacturer.getPhoneNumber());
                }

                return existingManufacturer;
            })
            .map(manufacturerRepository::save)
            .map(savedManufacturer -> {
                manufacturerSearchRepository.index(savedManufacturer);
                return savedManufacturer;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, manufacturer.getId().toString())
        );
    }

    /**
     * {@code GET  /manufacturers} : get all the manufacturers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of manufacturers in body.
     */
    @GetMapping("")
    public List<Manufacturer> getAllManufacturers() {
        LOG.debug("REST request to get all Manufacturers");
        return manufacturerRepository.findAll();
    }

    /**
     * {@code GET  /manufacturers/:id} : get the "id" manufacturer.
     *
     * @param id the id of the manufacturer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the manufacturer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Manufacturer> getManufacturer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Manufacturer : {}", id);
        Optional<Manufacturer> manufacturer = manufacturerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(manufacturer);
    }

    /**
     * {@code DELETE  /manufacturers/:id} : delete the "id" manufacturer.
     *
     * @param id the id of the manufacturer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManufacturer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Manufacturer : {}", id);
        manufacturerRepository.deleteById(id);
        manufacturerSearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /manufacturers/_search?query=:query} : search for the manufacturer corresponding
     * to the query.
     *
     * @param query the query of the manufacturer search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<Manufacturer> searchManufacturers(@RequestParam("query") String query) {
        LOG.debug("REST request to search Manufacturers for query {}", query);
        try {
            return StreamSupport.stream(manufacturerSearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
