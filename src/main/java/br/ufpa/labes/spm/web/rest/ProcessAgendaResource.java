package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ProcessAgendaService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ProcessAgendaDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ProcessAgenda}.
 */
@RestController
@RequestMapping("/api")
public class ProcessAgendaResource {

    private final Logger log = LoggerFactory.getLogger(ProcessAgendaResource.class);

    private static final String ENTITY_NAME = "processAgenda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessAgendaService processAgendaService;

    public ProcessAgendaResource(ProcessAgendaService processAgendaService) {
        this.processAgendaService = processAgendaService;
    }

    /**
     * {@code POST  /process-agenda} : Create a new processAgenda.
     *
     * @param processAgendaDTO the processAgendaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processAgendaDTO, or with status {@code 400 (Bad Request)} if the processAgenda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-agenda")
    public ResponseEntity<ProcessAgendaDTO> createProcessAgenda(@RequestBody ProcessAgendaDTO processAgendaDTO) throws URISyntaxException {
        log.debug("REST request to save ProcessAgenda : {}", processAgendaDTO);
        if (processAgendaDTO.getId() != null) {
            throw new BadRequestAlertException("A new processAgenda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessAgendaDTO result = processAgendaService.save(processAgendaDTO);
        return ResponseEntity.created(new URI("/api/process-agenda/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-agenda} : Updates an existing processAgenda.
     *
     * @param processAgendaDTO the processAgendaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processAgendaDTO,
     * or with status {@code 400 (Bad Request)} if the processAgendaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processAgendaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-agenda")
    public ResponseEntity<ProcessAgendaDTO> updateProcessAgenda(@RequestBody ProcessAgendaDTO processAgendaDTO) throws URISyntaxException {
        log.debug("REST request to update ProcessAgenda : {}", processAgendaDTO);
        if (processAgendaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProcessAgendaDTO result = processAgendaService.save(processAgendaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processAgendaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /process-agenda} : get all the processAgenda.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processAgenda in body.
     */
    @GetMapping("/process-agenda")
    public List<ProcessAgendaDTO> getAllProcessAgenda() {
        log.debug("REST request to get all ProcessAgenda");
        return processAgendaService.findAll();
    }

    /**
     * {@code GET  /process-agenda/:id} : get the "id" processAgenda.
     *
     * @param id the id of the processAgendaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processAgendaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-agenda/{id}")
    public ResponseEntity<ProcessAgendaDTO> getProcessAgenda(@PathVariable Long id) {
        log.debug("REST request to get ProcessAgenda : {}", id);
        Optional<ProcessAgendaDTO> processAgendaDTO = processAgendaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processAgendaDTO);
    }

    /**
     * {@code DELETE  /process-agenda/:id} : delete the "id" processAgenda.
     *
     * @param id the id of the processAgendaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-agenda/{id}")
    public ResponseEntity<Void> deleteProcessAgenda(@PathVariable Long id) {
        log.debug("REST request to delete ProcessAgenda : {}", id);
        processAgendaService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
