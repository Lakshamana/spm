package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.DecomposedActivityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.DecomposedActivityDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.DecomposedActivity}.
 */
@RestController
@RequestMapping("/api")
public class DecomposedActivityResource {

    private final Logger log = LoggerFactory.getLogger(DecomposedActivityResource.class);

    private static final String ENTITY_NAME = "decomposedActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DecomposedActivityService decomposedActivityService;

    public DecomposedActivityResource(DecomposedActivityService decomposedActivityService) {
        this.decomposedActivityService = decomposedActivityService;
    }

    /**
     * {@code POST  /decomposed-activities} : Create a new decomposedActivity.
     *
     * @param decomposedActivityDTO the decomposedActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new decomposedActivityDTO, or with status {@code 400 (Bad Request)} if the decomposedActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/decomposed-activities")
    public ResponseEntity<DecomposedActivityDTO> createDecomposedActivity(@RequestBody DecomposedActivityDTO decomposedActivityDTO) throws URISyntaxException {
        log.debug("REST request to save DecomposedActivity : {}", decomposedActivityDTO);
        if (decomposedActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new decomposedActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DecomposedActivityDTO result = decomposedActivityService.save(decomposedActivityDTO);
        return ResponseEntity.created(new URI("/api/decomposed-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /decomposed-activities} : Updates an existing decomposedActivity.
     *
     * @param decomposedActivityDTO the decomposedActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated decomposedActivityDTO,
     * or with status {@code 400 (Bad Request)} if the decomposedActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the decomposedActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/decomposed-activities")
    public ResponseEntity<DecomposedActivityDTO> updateDecomposedActivity(@RequestBody DecomposedActivityDTO decomposedActivityDTO) throws URISyntaxException {
        log.debug("REST request to update DecomposedActivity : {}", decomposedActivityDTO);
        if (decomposedActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DecomposedActivityDTO result = decomposedActivityService.save(decomposedActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, decomposedActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /decomposed-activities} : get all the decomposedActivities.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of decomposedActivities in body.
     */
    @GetMapping("/decomposed-activities")
    public List<DecomposedActivityDTO> getAllDecomposedActivities(@RequestParam(required = false) String filter) {
        if ("theactivitysuper-is-null".equals(filter)) {
            log.debug("REST request to get all DecomposedActivitys where theActivitySuper is null");
            return decomposedActivityService.findAllWhereTheActivitySuperIsNull();
        }
        log.debug("REST request to get all DecomposedActivities");
        return decomposedActivityService.findAll();
    }

    /**
     * {@code GET  /decomposed-activities/:id} : get the "id" decomposedActivity.
     *
     * @param id the id of the decomposedActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the decomposedActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/decomposed-activities/{id}")
    public ResponseEntity<DecomposedActivityDTO> getDecomposedActivity(@PathVariable Long id) {
        log.debug("REST request to get DecomposedActivity : {}", id);
        Optional<DecomposedActivityDTO> decomposedActivityDTO = decomposedActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(decomposedActivityDTO);
    }

    /**
     * {@code DELETE  /decomposed-activities/:id} : delete the "id" decomposedActivity.
     *
     * @param id the id of the decomposedActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/decomposed-activities/{id}")
    public ResponseEntity<Void> deleteDecomposedActivity(@PathVariable Long id) {
        log.debug("REST request to delete DecomposedActivity : {}", id);
        decomposedActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
