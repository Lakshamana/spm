package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.OcurrenceService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.OcurrenceDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Ocurrence}.
 */
@RestController
@RequestMapping("/api")
public class OcurrenceResource {

    private final Logger log = LoggerFactory.getLogger(OcurrenceResource.class);

    private static final String ENTITY_NAME = "ocurrence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OcurrenceService ocurrenceService;

    public OcurrenceResource(OcurrenceService ocurrenceService) {
        this.ocurrenceService = ocurrenceService;
    }

    /**
     * {@code POST  /ocurrences} : Create a new ocurrence.
     *
     * @param ocurrenceDTO the ocurrenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ocurrenceDTO, or with status {@code 400 (Bad Request)} if the ocurrence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ocurrences")
    public ResponseEntity<OcurrenceDTO> createOcurrence(@RequestBody OcurrenceDTO ocurrenceDTO) throws URISyntaxException {
        log.debug("REST request to save Ocurrence : {}", ocurrenceDTO);
        if (ocurrenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new ocurrence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OcurrenceDTO result = ocurrenceService.save(ocurrenceDTO);
        return ResponseEntity.created(new URI("/api/ocurrences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ocurrences} : Updates an existing ocurrence.
     *
     * @param ocurrenceDTO the ocurrenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocurrenceDTO,
     * or with status {@code 400 (Bad Request)} if the ocurrenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ocurrenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ocurrences")
    public ResponseEntity<OcurrenceDTO> updateOcurrence(@RequestBody OcurrenceDTO ocurrenceDTO) throws URISyntaxException {
        log.debug("REST request to update Ocurrence : {}", ocurrenceDTO);
        if (ocurrenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OcurrenceDTO result = ocurrenceService.save(ocurrenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocurrenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ocurrences} : get all the ocurrences.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ocurrences in body.
     */
    @GetMapping("/ocurrences")
    public List<OcurrenceDTO> getAllOcurrences() {
        log.debug("REST request to get all Ocurrences");
        return ocurrenceService.findAll();
    }

    /**
     * {@code GET  /ocurrences/:id} : get the "id" ocurrence.
     *
     * @param id the id of the ocurrenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ocurrenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ocurrences/{id}")
    public ResponseEntity<OcurrenceDTO> getOcurrence(@PathVariable Long id) {
        log.debug("REST request to get Ocurrence : {}", id);
        Optional<OcurrenceDTO> ocurrenceDTO = ocurrenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ocurrenceDTO);
    }

    /**
     * {@code DELETE  /ocurrences/:id} : delete the "id" ocurrence.
     *
     * @param id the id of the ocurrenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ocurrences/{id}")
    public ResponseEntity<Void> deleteOcurrence(@PathVariable Long id) {
        log.debug("REST request to delete Ocurrence : {}", id);
        ocurrenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
