package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ScriptService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ScriptDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Script}. */
@RestController
@RequestMapping("/api")
public class ScriptResource {

  private final Logger log = LoggerFactory.getLogger(ScriptResource.class);

  private static final String ENTITY_NAME = "script";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ScriptService scriptService;

  public ScriptResource(ScriptService scriptService) {
    this.scriptService = scriptService;
  }

  /**
   * {@code POST /scripts} : Create a new script.
   *
   * @param scriptDTO the scriptDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     scriptDTO, or with status {@code 400 (Bad Request)} if the script has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/scripts")
  public ResponseEntity<ScriptDTO> createScript(@RequestBody ScriptDTO scriptDTO)
      throws URISyntaxException {
    log.debug("REST request to save Script : {}", scriptDTO);
    if (scriptDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new script cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ScriptDTO result = scriptService.save(scriptDTO);
    return ResponseEntity.created(new URI("/api/scripts/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /scripts} : Updates an existing script.
   *
   * @param scriptDTO the scriptDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     scriptDTO, or with status {@code 400 (Bad Request)} if the scriptDTO is not valid, or with
   *     status {@code 500 (Internal Server Error)} if the scriptDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/scripts")
  public ResponseEntity<ScriptDTO> updateScript(@RequestBody ScriptDTO scriptDTO)
      throws URISyntaxException {
    log.debug("REST request to update Script : {}", scriptDTO);
    if (scriptDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ScriptDTO result = scriptService.save(scriptDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, scriptDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /scripts} : get all the scripts.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scripts in
   *     body.
   */
  @GetMapping("/scripts")
  public List<ScriptDTO> getAllScripts() {
    log.debug("REST request to get all Scripts");
    return scriptService.findAll();
  }

  /**
   * {@code GET /scripts/:id} : get the "id" script.
   *
   * @param id the id of the scriptDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scriptDTO, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/scripts/{id}")
  public ResponseEntity<ScriptDTO> getScript(@PathVariable Long id) {
    log.debug("REST request to get Script : {}", id);
    Optional<ScriptDTO> scriptDTO = scriptService.findOne(id);
    return ResponseUtil.wrapOrNotFound(scriptDTO);
  }

  /**
   * {@code DELETE /scripts/:id} : delete the "id" script.
   *
   * @param id the id of the scriptDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/scripts/{id}")
  public ResponseEntity<Void> deleteScript(@PathVariable Long id) {
    log.debug("REST request to delete Script : {}", id);
    scriptService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
