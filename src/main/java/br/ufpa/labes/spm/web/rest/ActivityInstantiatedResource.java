package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ActivityInstantiatedService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ActivityInstantiatedDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ActivityInstantiated}.
 */
@RestController
@RequestMapping("/api")
public class ActivityInstantiatedResource {

    private final Logger log = LoggerFactory.getLogger(ActivityInstantiatedResource.class);

    private static final String ENTITY_NAME = "activityInstantiated";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivityInstantiatedService activityInstantiatedService;

    public ActivityInstantiatedResource(ActivityInstantiatedService activityInstantiatedService) {
        this.activityInstantiatedService = activityInstantiatedService;
    }

    /**
     * {@code POST  /activity-instantiateds} : Create a new activityInstantiated.
     *
     * @param activityInstantiatedDTO the activityInstantiatedDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activityInstantiatedDTO, or with status {@code 400 (Bad Request)} if the activityInstantiated has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activity-instantiateds")
    public ResponseEntity<ActivityInstantiatedDTO> createActivityInstantiated(@RequestBody ActivityInstantiatedDTO activityInstantiatedDTO) throws URISyntaxException {
        log.debug("REST request to save ActivityInstantiated : {}", activityInstantiatedDTO);
        if (activityInstantiatedDTO.getId() != null) {
            throw new BadRequestAlertException("A new activityInstantiated cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivityInstantiatedDTO result = activityInstantiatedService.save(activityInstantiatedDTO);
        return ResponseEntity.created(new URI("/api/activity-instantiateds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activity-instantiateds} : Updates an existing activityInstantiated.
     *
     * @param activityInstantiatedDTO the activityInstantiatedDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activityInstantiatedDTO,
     * or with status {@code 400 (Bad Request)} if the activityInstantiatedDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activityInstantiatedDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activity-instantiateds")
    public ResponseEntity<ActivityInstantiatedDTO> updateActivityInstantiated(@RequestBody ActivityInstantiatedDTO activityInstantiatedDTO) throws URISyntaxException {
        log.debug("REST request to update ActivityInstantiated : {}", activityInstantiatedDTO);
        if (activityInstantiatedDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ActivityInstantiatedDTO result = activityInstantiatedService.save(activityInstantiatedDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activityInstantiatedDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /activity-instantiateds} : get all the activityInstantiateds.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activityInstantiateds in body.
     */
    @GetMapping("/activity-instantiateds")
    public List<ActivityInstantiatedDTO> getAllActivityInstantiateds() {
        log.debug("REST request to get all ActivityInstantiateds");
        return activityInstantiatedService.findAll();
    }

    /**
     * {@code GET  /activity-instantiateds/:id} : get the "id" activityInstantiated.
     *
     * @param id the id of the activityInstantiatedDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activityInstantiatedDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activity-instantiateds/{id}")
    public ResponseEntity<ActivityInstantiatedDTO> getActivityInstantiated(@PathVariable Long id) {
        log.debug("REST request to get ActivityInstantiated : {}", id);
        Optional<ActivityInstantiatedDTO> activityInstantiatedDTO = activityInstantiatedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(activityInstantiatedDTO);
    }

    /**
     * {@code DELETE  /activity-instantiateds/:id} : delete the "id" activityInstantiated.
     *
     * @param id the id of the activityInstantiatedDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activity-instantiateds/{id}")
    public ResponseEntity<Void> deleteActivityInstantiated(@PathVariable Long id) {
        log.debug("REST request to delete ActivityInstantiated : {}", id);
        activityInstantiatedService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
