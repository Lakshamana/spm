package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ArtifactTaskService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ArtifactTaskDTO;

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

    private final ArtifactTaskService artifactTaskService;

    public ArtifactTaskResource(ArtifactTaskService artifactTaskService) {
        this.artifactTaskService = artifactTaskService;
    }

    /**
     * {@code POST  /artifact-tasks} : Create a new artifactTask.
     *
     * @param artifactTaskDTO the artifactTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artifactTaskDTO, or with status {@code 400 (Bad Request)} if the artifactTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artifact-tasks")
    public ResponseEntity<ArtifactTaskDTO> createArtifactTask(@RequestBody ArtifactTaskDTO artifactTaskDTO) throws URISyntaxException {
        log.debug("REST request to save ArtifactTask : {}", artifactTaskDTO);
        if (artifactTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new artifactTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArtifactTaskDTO result = artifactTaskService.save(artifactTaskDTO);
        return ResponseEntity.created(new URI("/api/artifact-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artifact-tasks} : Updates an existing artifactTask.
     *
     * @param artifactTaskDTO the artifactTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artifactTaskDTO,
     * or with status {@code 400 (Bad Request)} if the artifactTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artifactTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artifact-tasks")
    public ResponseEntity<ArtifactTaskDTO> updateArtifactTask(@RequestBody ArtifactTaskDTO artifactTaskDTO) throws URISyntaxException {
        log.debug("REST request to update ArtifactTask : {}", artifactTaskDTO);
        if (artifactTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ArtifactTaskDTO result = artifactTaskService.save(artifactTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artifactTaskDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /artifact-tasks} : get all the artifactTasks.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artifactTasks in body.
     */
    @GetMapping("/artifact-tasks")
    public List<ArtifactTaskDTO> getAllArtifactTasks() {
        log.debug("REST request to get all ArtifactTasks");
        return artifactTaskService.findAll();
    }

    /**
     * {@code GET  /artifact-tasks/:id} : get the "id" artifactTask.
     *
     * @param id the id of the artifactTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artifactTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artifact-tasks/{id}")
    public ResponseEntity<ArtifactTaskDTO> getArtifactTask(@PathVariable Long id) {
        log.debug("REST request to get ArtifactTask : {}", id);
        Optional<ArtifactTaskDTO> artifactTaskDTO = artifactTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(artifactTaskDTO);
    }

    /**
     * {@code DELETE  /artifact-tasks/:id} : delete the "id" artifactTask.
     *
     * @param id the id of the artifactTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artifact-tasks/{id}")
    public ResponseEntity<Void> deleteArtifactTask(@PathVariable Long id) {
        log.debug("REST request to delete ArtifactTask : {}", id);
        artifactTaskService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
