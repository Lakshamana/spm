package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ArtifactTask;
import br.ufpa.labes.spm.repository.ArtifactTaskRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ArtifactTask}.
 */
@RestController
@RequestMapping("/api")
public class ArtifactTaskResource {

    private final Logger log = LoggerFactory.getLogger(ArtifactTaskResource.class);

    private static final String ENTITY_NAME = "artifactTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtifactTaskRepository artifactTaskRepository;

    public ArtifactTaskResource(ArtifactTaskRepository artifactTaskRepository) {
        this.artifactTaskRepository = artifactTaskRepository;
    }

    /**
     * {@code POST  /artifact-tasks} : Create a new artifactTask.
     *
     * @param artifactTask the artifactTask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artifactTask, or with status {@code 400 (Bad Request)} if the artifactTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artifact-tasks")
    public ResponseEntity<ArtifactTask> createArtifactTask(@RequestBody ArtifactTask artifactTask) throws URISyntaxException {
        log.debug("REST request to save ArtifactTask : {}", artifactTask);
        if (artifactTask.getId() != null) {
            throw new BadRequestAlertException("A new artifactTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtifactTask result = artifactTaskRepository.save(artifactTask);
        return ResponseEntity.created(new URI("/api/artifact-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artifact-tasks} : Updates an existing artifactTask.
     *
     * @param artifactTask the artifactTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artifactTask,
     * or with status {@code 400 (Bad Request)} if the artifactTask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artifactTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artifact-tasks")
    public ResponseEntity<ArtifactTask> updateArtifactTask(@RequestBody ArtifactTask artifactTask) throws URISyntaxException {
        log.debug("REST request to update ArtifactTask : {}", artifactTask);
        if (artifactTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArtifactTask result = artifactTaskRepository.save(artifactTask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artifactTask.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /artifact-tasks} : get all the artifactTasks.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifactTasks in body.
     */
    @GetMapping("/artifact-tasks")
    public List<ArtifactTask> getAllArtifactTasks() {
        log.debug("REST request to get all ArtifactTasks");
        return artifactTaskRepository.findAll();
    }

    /**
     * {@code GET  /artifact-tasks/:id} : get the "id" artifactTask.
     *
     * @param id the id of the artifactTask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artifactTask, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artifact-tasks/{id}")
    public ResponseEntity<ArtifactTask> getArtifactTask(@PathVariable Long id) {
        log.debug("REST request to get ArtifactTask : {}", id);
        Optional<ArtifactTask> artifactTask = artifactTaskRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(artifactTask);
    }

    /**
     * {@code DELETE  /artifact-tasks/:id} : delete the "id" artifactTask.
     *
     * @param id the id of the artifactTask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artifact-tasks/{id}")
    public ResponseEntity<Void> deleteArtifactTask(@PathVariable Long id) {
        log.debug("REST request to delete ArtifactTask : {}", id);
        artifactTaskRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
