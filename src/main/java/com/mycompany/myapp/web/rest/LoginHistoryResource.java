package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.LoginHistory;
import com.mycompany.myapp.repository.LoginHistoryRepository;
import com.mycompany.myapp.repository.search.LoginHistorySearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.LoginHistory}.
 */
@RestController
@RequestMapping("/api/login-histories")
@Transactional
public class LoginHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(LoginHistoryResource.class);

    private static final String ENTITY_NAME = "loginHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoginHistoryRepository loginHistoryRepository;

    private final LoginHistorySearchRepository loginHistorySearchRepository;

    public LoginHistoryResource(LoginHistoryRepository loginHistoryRepository, LoginHistorySearchRepository loginHistorySearchRepository) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.loginHistorySearchRepository = loginHistorySearchRepository;
    }

    /**
     * {@code POST  /login-histories} : Create a new loginHistory.
     *
     * @param loginHistory the loginHistory to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loginHistory, or with status {@code 400 (Bad Request)} if the loginHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LoginHistory> createLoginHistory(@RequestBody LoginHistory loginHistory) throws URISyntaxException {
        LOG.debug("REST request to save LoginHistory : {}", loginHistory);
        if (loginHistory.getId() != null) {
            throw new BadRequestAlertException("A new loginHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        loginHistory = loginHistoryRepository.save(loginHistory);
        loginHistorySearchRepository.index(loginHistory);
        return ResponseEntity.created(new URI("/api/login-histories/" + loginHistory.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, loginHistory.getId().toString()))
            .body(loginHistory);
    }

    /**
     * {@code PUT  /login-histories/:id} : Updates an existing loginHistory.
     *
     * @param id the id of the loginHistory to save.
     * @param loginHistory the loginHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginHistory,
     * or with status {@code 400 (Bad Request)} if the loginHistory is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loginHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LoginHistory> updateLoginHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoginHistory loginHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to update LoginHistory : {}, {}", id, loginHistory);
        if (loginHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loginHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        loginHistory = loginHistoryRepository.save(loginHistory);
        loginHistorySearchRepository.index(loginHistory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loginHistory.getId().toString()))
            .body(loginHistory);
    }

    /**
     * {@code PATCH  /login-histories/:id} : Partial updates given fields of an existing loginHistory, field will ignore if it is null
     *
     * @param id the id of the loginHistory to save.
     * @param loginHistory the loginHistory to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginHistory,
     * or with status {@code 400 (Bad Request)} if the loginHistory is not valid,
     * or with status {@code 404 (Not Found)} if the loginHistory is not found,
     * or with status {@code 500 (Internal Server Error)} if the loginHistory couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoginHistory> partialUpdateLoginHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LoginHistory loginHistory
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LoginHistory partially : {}, {}", id, loginHistory);
        if (loginHistory.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loginHistory.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loginHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoginHistory> result = loginHistoryRepository
            .findById(loginHistory.getId())
            .map(existingLoginHistory -> {
                if (loginHistory.getForgotPin() != null) {
                    existingLoginHistory.setForgotPin(loginHistory.getForgotPin());
                }
                if (loginHistory.getIsActive() != null) {
                    existingLoginHistory.setIsActive(loginHistory.getIsActive());
                }
                if (loginHistory.getLoginDate() != null) {
                    existingLoginHistory.setLoginDate(loginHistory.getLoginDate());
                }
                if (loginHistory.getLoginLogId() != null) {
                    existingLoginHistory.setLoginLogId(loginHistory.getLoginLogId());
                }
                if (loginHistory.getLoginType() != null) {
                    existingLoginHistory.setLoginType(loginHistory.getLoginType());
                }
                if (loginHistory.getLogOutDate() != null) {
                    existingLoginHistory.setLogOutDate(loginHistory.getLogOutDate());
                }
                if (loginHistory.getUserId() != null) {
                    existingLoginHistory.setUserId(loginHistory.getUserId());
                }

                return existingLoginHistory;
            })
            .map(loginHistoryRepository::save)
            .map(savedLoginHistory -> {
                loginHistorySearchRepository.index(savedLoginHistory);
                return savedLoginHistory;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loginHistory.getId().toString())
        );
    }

    /**
     * {@code GET  /login-histories} : get all the loginHistories.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loginHistories in body.
     */
    @GetMapping("")
    public List<LoginHistory> getAllLoginHistories() {
        LOG.debug("REST request to get all LoginHistories");
        return loginHistoryRepository.findAll();
    }

    /**
     * {@code GET  /login-histories/:id} : get the "id" loginHistory.
     *
     * @param id the id of the loginHistory to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loginHistory, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LoginHistory> getLoginHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LoginHistory : {}", id);
        Optional<LoginHistory> loginHistory = loginHistoryRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(loginHistory);
    }

    /**
     * {@code DELETE  /login-histories/:id} : delete the "id" loginHistory.
     *
     * @param id the id of the loginHistory to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoginHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LoginHistory : {}", id);
        loginHistoryRepository.deleteById(id);
        loginHistorySearchRepository.deleteFromIndexById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /login-histories/_search?query=:query} : search for the loginHistory corresponding
     * to the query.
     *
     * @param query the query of the loginHistory search.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public List<LoginHistory> searchLoginHistories(@RequestParam("query") String query) {
        LOG.debug("REST request to search LoginHistories for query {}", query);
        try {
            return StreamSupport.stream(loginHistorySearchRepository.search(query).spliterator(), false).toList();
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
