package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.PlainService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.PlainDTO;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Plain}.
 */
@RestController
@RequestMapping("/api")
public class PlainResource {

    private final Logger log = LoggerFactory.getLogger(PlainResource.class);

    private static final String ENTITY_NAME = "plain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlainService plainService;

    public PlainResource(PlainService plainService) {
        this.plainService = plainService;
    }

    /**
     * {@code POST  /plain-activities} : Create a new plain.
     *
     * @param plainDTO the plainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plainDTO, or with status {@code 400 (Bad Request)} if the plain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plain-activities")
    public ResponseEntity<PlainDTO> createPlain(@RequestBody PlainDTO plainDTO) throws URISyntaxException {
        log.debug("REST request to save Plain : {}", plainDTO);
        if (plainDTO.getId() != null) {
            throw new BadRequestAlertException("A new plain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlainDTO result = plainService.save(plainDTO);
        return ResponseEntity.created(new URI("/api/plain-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plain-activities} : Updates an existing plain.
     *
     * @param plainDTO the plainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plainDTO,
     * or with status {@code 400 (Bad Request)} if the plainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plain-activities")
    public ResponseEntity<PlainDTO> updatePlain(@RequestBody PlainDTO plainDTO) throws URISyntaxException {
        log.debug("REST request to update Plain : {}", plainDTO);
        if (plainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlainDTO result = plainService.save(plainDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plainDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plain-activities} : get all the plainActivities.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plainActivities in body.
     */
    @GetMapping("/plain-activities")
    public List<PlainDTO> getAllPlainActivities(@RequestParam(required = false) String filter) {
        if ("theactivitysuper-is-null".equals(filter)) {
            log.debug("REST request to get all Plains where theActivitySuper is null");
            return plainService.findAllWhereTheActivitySuperIsNull();
        }
        log.debug("REST request to get all PlainActivities");
        return plainService.findAll();
    }

    /**
     * {@code GET  /plain-activities/:id} : get the "id" plain.
     *
     * @param id the id of the plainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plain-activities/{id}")
    public ResponseEntity<PlainDTO> getPlain(@PathVariable Long id) {
        log.debug("REST request to get Plain : {}", id);
        Optional<PlainDTO> plainDTO = plainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plainDTO);
    }

    /**
     * {@code DELETE  /plain-activities/:id} : delete the "id" plain.
     *
     * @param id the id of the plainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plain-activities/{id}")
    public ResponseEntity<Void> deletePlain(@PathVariable Long id) {
        log.debug("REST request to delete Plain : {}", id);
        plainService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
