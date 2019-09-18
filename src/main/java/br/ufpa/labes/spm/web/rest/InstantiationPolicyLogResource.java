package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.InstantiationPolicyLogService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.InstantiationPolicyLogDTO;

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

    private final InstantiationPolicyLogService instantiationPolicyLogService;

    public InstantiationPolicyLogResource(InstantiationPolicyLogService instantiationPolicyLogService) {
        this.instantiationPolicyLogService = instantiationPolicyLogService;
    }

    /**
     * {@code POST  /instantiation-policy-logs} : Create a new instantiationPolicyLog.
     *
     * @param instantiationPolicyLogDTO the instantiationPolicyLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instantiationPolicyLogDTO, or with status {@code 400 (Bad Request)} if the instantiationPolicyLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instantiation-policy-logs")
    public ResponseEntity<InstantiationPolicyLogDTO> createInstantiationPolicyLog(@RequestBody InstantiationPolicyLogDTO instantiationPolicyLogDTO) throws URISyntaxException {
        log.debug("REST request to save InstantiationPolicyLog : {}", instantiationPolicyLogDTO);
        if (instantiationPolicyLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new instantiationPolicyLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstantiationPolicyLogDTO result = instantiationPolicyLogService.save(instantiationPolicyLogDTO);
        return ResponseEntity.created(new URI("/api/instantiation-policy-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instantiation-policy-logs} : Updates an existing instantiationPolicyLog.
     *
     * @param instantiationPolicyLogDTO the instantiationPolicyLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instantiationPolicyLogDTO,
     * or with status {@code 400 (Bad Request)} if the instantiationPolicyLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instantiationPolicyLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instantiation-policy-logs")
    public ResponseEntity<InstantiationPolicyLogDTO> updateInstantiationPolicyLog(@RequestBody InstantiationPolicyLogDTO instantiationPolicyLogDTO) throws URISyntaxException {
        log.debug("REST request to update InstantiationPolicyLog : {}", instantiationPolicyLogDTO);
        if (instantiationPolicyLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InstantiationPolicyLogDTO result = instantiationPolicyLogService.save(instantiationPolicyLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instantiationPolicyLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /instantiation-policy-logs} : get all the instantiationPolicyLogs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instantiationPolicyLogs in body.
     */
    @GetMapping("/instantiation-policy-logs")
    public List<InstantiationPolicyLogDTO> getAllInstantiationPolicyLogs() {
        log.debug("REST request to get all InstantiationPolicyLogs");
        return instantiationPolicyLogService.findAll();
    }

    /**
     * {@code GET  /instantiation-policy-logs/:id} : get the "id" instantiationPolicyLog.
     *
     * @param id the id of the instantiationPolicyLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instantiationPolicyLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instantiation-policy-logs/{id}")
    public ResponseEntity<InstantiationPolicyLogDTO> getInstantiationPolicyLog(@PathVariable Long id) {
        log.debug("REST request to get InstantiationPolicyLog : {}", id);
        Optional<InstantiationPolicyLogDTO> instantiationPolicyLogDTO = instantiationPolicyLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instantiationPolicyLogDTO);
    }

    /**
     * {@code DELETE  /instantiation-policy-logs/:id} : delete the "id" instantiationPolicyLog.
     *
     * @param id the id of the instantiationPolicyLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instantiation-policy-logs/{id}")
    public ResponseEntity<Void> deleteInstantiationPolicyLog(@PathVariable Long id) {
        log.debug("REST request to delete InstantiationPolicyLog : {}", id);
        instantiationPolicyLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
