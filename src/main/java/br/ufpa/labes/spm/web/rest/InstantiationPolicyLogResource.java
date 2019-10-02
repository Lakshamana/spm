package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.InstantiationPolicyLog;
import br.ufpa.labes.spm.repository.InstantiationPolicyLogRepository;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.InstantiationPolicyLog}.
 */
@RestController
@RequestMapping("/api")
public class InstantiationPolicyLogResource {

    private final Logger log = LoggerFactory.getLogger(InstantiationPolicyLogResource.class);

    private static final String ENTITY_NAME = "instantiationPolicyLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstantiationPolicyLogRepository instantiationPolicyLogRepository;

    public InstantiationPolicyLogResource(InstantiationPolicyLogRepository instantiationPolicyLogRepository) {
        this.instantiationPolicyLogRepository = instantiationPolicyLogRepository;
    }

    /**
     * {@code POST  /instantiation-policy-logs} : Create a new instantiationPolicyLog.
     *
     * @param instantiationPolicyLog the instantiationPolicyLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instantiationPolicyLog, or with status {@code 400 (Bad Request)} if the instantiationPolicyLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instantiation-policy-logs")
    public ResponseEntity<InstantiationPolicyLog> createInstantiationPolicyLog(@RequestBody InstantiationPolicyLog instantiationPolicyLog) throws URISyntaxException {
        log.debug("REST request to save InstantiationPolicyLog : {}", instantiationPolicyLog);
        if (instantiationPolicyLog.getId() != null) {
            throw new BadRequestAlertException("A new instantiationPolicyLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstantiationPolicyLog result = instantiationPolicyLogRepository.save(instantiationPolicyLog);
        return ResponseEntity.created(new URI("/api/instantiation-policy-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instantiation-policy-logs} : Updates an existing instantiationPolicyLog.
     *
     * @param instantiationPolicyLog the instantiationPolicyLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instantiationPolicyLog,
     * or with status {@code 400 (Bad Request)} if the instantiationPolicyLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instantiationPolicyLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instantiation-policy-logs")
    public ResponseEntity<InstantiationPolicyLog> updateInstantiationPolicyLog(@RequestBody InstantiationPolicyLog instantiationPolicyLog) throws URISyntaxException {
        log.debug("REST request to update InstantiationPolicyLog : {}", instantiationPolicyLog);
        if (instantiationPolicyLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InstantiationPolicyLog result = instantiationPolicyLogRepository.save(instantiationPolicyLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instantiationPolicyLog.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /instantiation-policy-logs} : get all the instantiationPolicyLogs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instantiationPolicyLogs in body.
     */
    @GetMapping("/instantiation-policy-logs")
    public List<InstantiationPolicyLog> getAllInstantiationPolicyLogs() {
        log.debug("REST request to get all InstantiationPolicyLogs");
        return instantiationPolicyLogRepository.findAll();
    }

    /**
     * {@code GET  /instantiation-policy-logs/:id} : get the "id" instantiationPolicyLog.
     *
     * @param id the id of the instantiationPolicyLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instantiationPolicyLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instantiation-policy-logs/{id}")
    public ResponseEntity<InstantiationPolicyLog> getInstantiationPolicyLog(@PathVariable Long id) {
        log.debug("REST request to get InstantiationPolicyLog : {}", id);
        Optional<InstantiationPolicyLog> instantiationPolicyLog = instantiationPolicyLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(instantiationPolicyLog);
    }

    /**
     * {@code DELETE  /instantiation-policy-logs/:id} : delete the "id" instantiationPolicyLog.
     *
     * @param id the id of the instantiationPolicyLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instantiation-policy-logs/{id}")
    public ResponseEntity<Void> deleteInstantiationPolicyLog(@PathVariable Long id) {
        log.debug("REST request to delete InstantiationPolicyLog : {}", id);
        instantiationPolicyLogRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
