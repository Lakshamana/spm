package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ActivityEstimationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ActivityEstimationDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ActivityEstimation}.
 */
@RestController
@RequestMapping("/api")
public class ActivityEstimationResource {

    private final Logger log = LoggerFactory.getLogger(ActivityEstimationResource.class);

    private static final String ENTITY_NAME = "activityEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityEstimationService activityEstimationService;

    public ActivityEstimationResource(ActivityEstimationService activityEstimationService) {
        this.activityEstimationService = activityEstimationService;
    }

    /**
     * {@code POST  /activity-estimations} : Create a new activityEstimation.
     *
     * @param activityEstimationDTO the activityEstimationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityEstimationDTO, or with status {@code 400 (Bad Request)} if the activityEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activity-estimations")
    public ResponseEntity<ActivityEstimationDTO> createActivityEstimation(@RequestBody ActivityEstimationDTO activityEstimationDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityEstimation : {}", activityEstimationDTO);
        if (activityEstimationDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityEstimationDTO result = activityEstimationService.save(activityEstimationDTO);
        return ResponseEntity.created(new URI("/api/activity-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activity-estimations} : Updates an existing activityEstimation.
     *
     * @param activityEstimationDTO the activityEstimationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityEstimationDTO,
     * or with status {@code 400 (Bad Request)} if the activityEstimationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityEstimationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activity-estimations")
    public ResponseEntity<ActivityEstimationDTO> updateActivityEstimation(@RequestBody ActivityEstimationDTO activityEstimationDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityEstimation : {}", activityEstimationDTO);
        if (activityEstimationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityEstimationDTO result = activityEstimationService.save(activityEstimationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activityEstimationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /activity-estimations} : get all the activityEstimations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityEstimations in body.
     */
    @GetMapping("/activity-estimations")
    public List<ActivityEstimationDTO> getAllActivityEstimations() {
        log.debug("REST request to get all ActivityEstimations");
        return activityEstimationService.findAll();
    }

    /**
     * {@code GET  /activity-estimations/:id} : get the "id" activityEstimation.
     *
     * @param id the id of the activityEstimationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityEstimationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-estimations/{id}")
    public ResponseEntity<ActivityEstimationDTO> getActivityEstimation(@PathVariable Long id) {
        log.debug("REST request to get ActivityEstimation : {}", id);
        Optional<ActivityEstimationDTO> activityEstimationDTO = activityEstimationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityEstimationDTO);
    }

    /**
     * {@code DELETE  /activity-estimations/:id} : delete the "id" activityEstimation.
     *
     * @param id the id of the activityEstimationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activity-estimations/{id}")
    public ResponseEntity<Void> deleteActivityEstimation(@PathVariable Long id) {
        log.debug("REST request to delete ActivityEstimation : {}", id);
        activityEstimationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
