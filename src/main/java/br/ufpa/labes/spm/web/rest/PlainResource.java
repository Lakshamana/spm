package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.PlainActivityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.PlainActivityDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.PlainActivity}.
 */
@RestController
@RequestMapping("/api")
public class PlainActivityResource {

    private final Logger log = LoggerFactory.getLogger(PlainActivityResource.class);

    private static final String ENTITY_NAME = "plainActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlainActivityService plainActivityService;

    public PlainActivityResource(PlainActivityService plainActivityService) {
        this.plainActivityService = plainActivityService;
    }

    /**
     * {@code POST  /plain-activities} : Create a new plainActivity.
     *
     * @param plainActivityDTO the plainActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plainActivityDTO, or with status {@code 400 (Bad Request)} if the plainActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plain-activities")
    public ResponseEntity<PlainActivityDTO> createPlainActivity(@RequestBody PlainActivityDTO plainActivityDTO) throws URISyntaxException {
        log.debug("REST request to save PlainActivity : {}", plainActivityDTO);
        if (plainActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new plainActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlainActivityDTO result = plainActivityService.save(plainActivityDTO);
        return ResponseEntity.created(new URI("/api/plain-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plain-activities} : Updates an existing plainActivity.
     *
     * @param plainActivityDTO the plainActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plainActivityDTO,
     * or with status {@code 400 (Bad Request)} if the plainActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plainActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plain-activities")
    public ResponseEntity<PlainActivityDTO> updatePlainActivity(@RequestBody PlainActivityDTO plainActivityDTO) throws URISyntaxException {
        log.debug("REST request to update PlainActivity : {}", plainActivityDTO);
        if (plainActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlainActivityDTO result = plainActivityService.save(plainActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plainActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plain-activities} : get all the plainActivities.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plainActivities in body.
     */
    @GetMapping("/plain-activities")
    public List<PlainActivityDTO> getAllPlainActivities(@RequestParam(required = false) String filter) {
        if ("theactivitysuper-is-null".equals(filter)) {
            log.debug("REST request to get all PlainActivitys where theActivitySuper is null");
            return plainActivityService.findAllWhereTheActivitySuperIsNull();
        }
        log.debug("REST request to get all PlainActivities");
        return plainActivityService.findAll();
    }

    /**
     * {@code GET  /plain-activities/:id} : get the "id" plainActivity.
     *
     * @param id the id of the plainActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plainActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plain-activities/{id}")
    public ResponseEntity<PlainActivityDTO> getPlainActivity(@PathVariable Long id) {
        log.debug("REST request to get PlainActivity : {}", id);
        Optional<PlainActivityDTO> plainActivityDTO = plainActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plainActivityDTO);
    }

    /**
     * {@code DELETE  /plain-activities/:id} : delete the "id" plainActivity.
     *
     * @param id the id of the plainActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plain-activities/{id}")
    public ResponseEntity<Void> deletePlainActivity(@PathVariable Long id) {
        log.debug("REST request to delete PlainActivity : {}", id);
        plainActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
