package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ActivityEstimation;
import br.ufpa.labes.spm.repository.ActivityEstimationRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ActivityEstimation}.
 */
@RestController
@RequestMapping("/api")
public class ActivityEstimationResource {

    private final Logger log = LoggerFactory.getLogger(ActivityEstimationResource.class);

    private static final String ENTITY_NAME = "activityEstimation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityEstimationRepository activityEstimationRepository;

    public ActivityEstimationResource(ActivityEstimationRepository activityEstimationRepository) {
        this.activityEstimationRepository = activityEstimationRepository;
    }

    /**
     * {@code POST  /activity-estimations} : Create a new activityEstimation.
     *
     * @param activityEstimation the activityEstimation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityEstimation, or with status {@code 400 (Bad Request)} if the activityEstimation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activity-estimations")
    public ResponseEntity<ActivityEstimation> createActivityEstimation(@RequestBody ActivityEstimation activityEstimation) throws URISyntaxException {
        log.debug("REST request to save ActivityEstimation : {}", activityEstimation);
        if (activityEstimation.getId() != null) {
            throw new BadRequestAlertException("A new activityEstimation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityEstimation result = activityEstimationRepository.save(activityEstimation);
        return ResponseEntity.created(new URI("/api/activity-estimations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activity-estimations} : Updates an existing activityEstimation.
     *
     * @param activityEstimation the activityEstimation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityEstimation,
     * or with status {@code 400 (Bad Request)} if the activityEstimation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityEstimation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activity-estimations")
    public ResponseEntity<ActivityEstimation> updateActivityEstimation(@RequestBody ActivityEstimation activityEstimation) throws URISyntaxException {
        log.debug("REST request to update ActivityEstimation : {}", activityEstimation);
        if (activityEstimation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityEstimation result = activityEstimationRepository.save(activityEstimation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activityEstimation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /activity-estimations} : get all the activityEstimations.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityEstimations in body.
     */
    @GetMapping("/activity-estimations")
    public List<ActivityEstimation> getAllActivityEstimations() {
        log.debug("REST request to get all ActivityEstimations");
        return activityEstimationRepository.findAll();
    }

    /**
     * {@code GET  /activity-estimations/:id} : get the "id" activityEstimation.
     *
     * @param id the id of the activityEstimation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityEstimation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-estimations/{id}")
    public ResponseEntity<ActivityEstimation> getActivityEstimation(@PathVariable Long id) {
        log.debug("REST request to get ActivityEstimation : {}", id);
        Optional<ActivityEstimation> activityEstimation = activityEstimationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activityEstimation);
    }

    /**
     * {@code DELETE  /activity-estimations/:id} : delete the "id" activityEstimation.
     *
     * @param id the id of the activityEstimation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activity-estimations/{id}")
    public ResponseEntity<Void> deleteActivityEstimation(@PathVariable Long id) {
        log.debug("REST request to delete ActivityEstimation : {}", id);
        activityEstimationRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
