package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.SpmLogService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.SpmLogDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.SpmLog}.
 */
@RestController
@RequestMapping("/api")
public class SpmLogResource {

    private final Logger log = LoggerFactory.getLogger(SpmLogResource.class);

    private static final String ENTITY_NAME = "spmLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpmLogService spmLogService;

    public SpmLogResource(SpmLogService spmLogService) {
        this.spmLogService = spmLogService;
    }

    /**
     * {@code POST  /spm-logs} : Create a new spmLog.
     *
     * @param spmLogDTO the spmLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spmLogDTO, or with status {@code 400 (Bad Request)} if the spmLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spm-logs")
    public ResponseEntity<SpmLogDTO> createSpmLog(@RequestBody SpmLogDTO spmLogDTO) throws URISyntaxException {
        log.debug("REST request to save SpmLog : {}", spmLogDTO);
        if (spmLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new spmLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpmLogDTO result = spmLogService.save(spmLogDTO);
        return ResponseEntity.created(new URI("/api/spm-logs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spm-logs} : Updates an existing spmLog.
     *
     * @param spmLogDTO the spmLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spmLogDTO,
     * or with status {@code 400 (Bad Request)} if the spmLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spmLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spm-logs")
    public ResponseEntity<SpmLogDTO> updateSpmLog(@RequestBody SpmLogDTO spmLogDTO) throws URISyntaxException {
        log.debug("REST request to update SpmLog : {}", spmLogDTO);
        if (spmLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpmLogDTO result = spmLogService.save(spmLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spmLogDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /spm-logs} : get all the spmLogs.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spmLogs in body.
     */
    @GetMapping("/spm-logs")
    public List<SpmLogDTO> getAllSpmLogs() {
        log.debug("REST request to get all SpmLogs");
        return spmLogService.findAll();
    }

    /**
     * {@code GET  /spm-logs/:id} : get the "id" spmLog.
     *
     * @param id the id of the spmLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spmLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spm-logs/{id}")
    public ResponseEntity<SpmLogDTO> getSpmLog(@PathVariable Long id) {
        log.debug("REST request to get SpmLog : {}", id);
        Optional<SpmLogDTO> spmLogDTO = spmLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spmLogDTO);
    }

    /**
     * {@code DELETE  /spm-logs/:id} : delete the "id" spmLog.
     *
     * @param id the id of the spmLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spm-logs/{id}")
    public ResponseEntity<Void> deleteSpmLog(@PathVariable Long id) {
        log.debug("REST request to delete SpmLog : {}", id);
        spmLogService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
