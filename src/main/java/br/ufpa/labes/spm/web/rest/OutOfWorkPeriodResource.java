package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.OutOfWorkPeriodService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.OutOfWorkPeriodDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.OutOfWorkPeriod}.
 */
@RestController
@RequestMapping("/api")
public class OutOfWorkPeriodResource {

    private final Logger log = LoggerFactory.getLogger(OutOfWorkPeriodResource.class);

    private static final String ENTITY_NAME = "outOfWorkPeriod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutOfWorkPeriodService outOfWorkPeriodService;

    public OutOfWorkPeriodResource(OutOfWorkPeriodService outOfWorkPeriodService) {
        this.outOfWorkPeriodService = outOfWorkPeriodService;
    }

    /**
     * {@code POST  /out-of-work-periods} : Create a new outOfWorkPeriod.
     *
     * @param outOfWorkPeriodDTO the outOfWorkPeriodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outOfWorkPeriodDTO, or with status {@code 400 (Bad Request)} if the outOfWorkPeriod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/out-of-work-periods")
    public ResponseEntity<OutOfWorkPeriodDTO> createOutOfWorkPeriod(@RequestBody OutOfWorkPeriodDTO outOfWorkPeriodDTO) throws URISyntaxException {
        log.debug("REST request to save OutOfWorkPeriod : {}", outOfWorkPeriodDTO);
        if (outOfWorkPeriodDTO.getId() != null) {
            throw new BadRequestAlertException("A new outOfWorkPeriod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutOfWorkPeriodDTO result = outOfWorkPeriodService.save(outOfWorkPeriodDTO);
        return ResponseEntity.created(new URI("/api/out-of-work-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /out-of-work-periods} : Updates an existing outOfWorkPeriod.
     *
     * @param outOfWorkPeriodDTO the outOfWorkPeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outOfWorkPeriodDTO,
     * or with status {@code 400 (Bad Request)} if the outOfWorkPeriodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outOfWorkPeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/out-of-work-periods")
    public ResponseEntity<OutOfWorkPeriodDTO> updateOutOfWorkPeriod(@RequestBody OutOfWorkPeriodDTO outOfWorkPeriodDTO) throws URISyntaxException {
        log.debug("REST request to update OutOfWorkPeriod : {}", outOfWorkPeriodDTO);
        if (outOfWorkPeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OutOfWorkPeriodDTO result = outOfWorkPeriodService.save(outOfWorkPeriodDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, outOfWorkPeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /out-of-work-periods} : get all the outOfWorkPeriods.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outOfWorkPeriods in body.
     */
    @GetMapping("/out-of-work-periods")
    public List<OutOfWorkPeriodDTO> getAllOutOfWorkPeriods() {
        log.debug("REST request to get all OutOfWorkPeriods");
        return outOfWorkPeriodService.findAll();
    }

    /**
     * {@code GET  /out-of-work-periods/:id} : get the "id" outOfWorkPeriod.
     *
     * @param id the id of the outOfWorkPeriodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outOfWorkPeriodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/out-of-work-periods/{id}")
    public ResponseEntity<OutOfWorkPeriodDTO> getOutOfWorkPeriod(@PathVariable Long id) {
        log.debug("REST request to get OutOfWorkPeriod : {}", id);
        Optional<OutOfWorkPeriodDTO> outOfWorkPeriodDTO = outOfWorkPeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outOfWorkPeriodDTO);
    }

    /**
     * {@code DELETE  /out-of-work-periods/:id} : delete the "id" outOfWorkPeriod.
     *
     * @param id the id of the outOfWorkPeriodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/out-of-work-periods/{id}")
    public ResponseEntity<Void> deleteOutOfWorkPeriod(@PathVariable Long id) {
        log.debug("REST request to delete OutOfWorkPeriod : {}", id);
        outOfWorkPeriodService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
