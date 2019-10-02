package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ActivityType;
import br.ufpa.labes.spm.repository.ActivityTypeRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ActivityType}.
 */
@RestController
@RequestMapping("/api")
public class ActivityTypeResource {

    private final Logger log = LoggerFactory.getLogger(ActivityTypeResource.class);

    private static final String ENTITY_NAME = "activityType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityTypeRepository activityTypeRepository;

    public ActivityTypeResource(ActivityTypeRepository activityTypeRepository) {
        this.activityTypeRepository = activityTypeRepository;
    }

    /**
     * {@code POST  /activity-types} : Create a new activityType.
     *
     * @param activityType the activityType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityType, or with status {@code 400 (Bad Request)} if the activityType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activity-types")
    public ResponseEntity<ActivityType> createActivityType(@RequestBody ActivityType activityType) throws URISyntaxException {
        log.debug("REST request to save ActivityType : {}", activityType);
        if (activityType.getId() != null) {
            throw new BadRequestAlertException("A new activityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityType result = activityTypeRepository.save(activityType);
        return ResponseEntity.created(new URI("/api/activity-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activity-types} : Updates an existing activityType.
     *
     * @param activityType the activityType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityType,
     * or with status {@code 400 (Bad Request)} if the activityType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activity-types")
    public ResponseEntity<ActivityType> updateActivityType(@RequestBody ActivityType activityType) throws URISyntaxException {
        log.debug("REST request to update ActivityType : {}", activityType);
        if (activityType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityType result = activityTypeRepository.save(activityType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activityType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /activity-types} : get all the activityTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityTypes in body.
     */
    @GetMapping("/activity-types")
    public List<ActivityType> getAllActivityTypes() {
        log.debug("REST request to get all ActivityTypes");
        return activityTypeRepository.findAll();
    }

    /**
     * {@code GET  /activity-types/:id} : get the "id" activityType.
     *
     * @param id the id of the activityType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-types/{id}")
    public ResponseEntity<ActivityType> getActivityType(@PathVariable Long id) {
        log.debug("REST request to get ActivityType : {}", id);
        Optional<ActivityType> activityType = activityTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activityType);
    }

    /**
     * {@code DELETE  /activity-types/:id} : delete the "id" activityType.
     *
     * @param id the id of the activityType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activity-types/{id}")
    public ResponseEntity<Void> deleteActivityType(@PathVariable Long id) {
        log.debug("REST request to delete ActivityType : {}", id);
        activityTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
