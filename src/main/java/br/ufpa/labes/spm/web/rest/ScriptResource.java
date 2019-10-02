package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Script;
import br.ufpa.labes.spm.repository.ScriptRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Script}. */
@RestController
@RequestMapping("/api")
public class ScriptResource {

  private final Logger log = LoggerFactory.getLogger(ScriptResource.class);

  private static final String ENTITY_NAME = "script";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ScriptRepository scriptRepository;

  public ScriptResource(ScriptRepository scriptRepository) {
    this.scriptRepository = scriptRepository;
  }

  /**
   * {@code POST /scripts} : Create a new script.
   *
   * @param script the script to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     script, or with status {@code 400 (Bad Request)} if the script has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/scripts")
  public ResponseEntity<Script> createScript(@RequestBody Script script) throws URISyntaxException {
    log.debug("REST request to save Script : {}", script);
    if (script.getId() != null) {
      throw new BadRequestAlertException(
          "A new script cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Script result = scriptRepository.save(script);
    return ResponseEntity.created(new URI("/api/scripts/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /scripts} : Updates an existing script.
   *
   * @param script the script to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     script, or with status {@code 400 (Bad Request)} if the script is not valid, or with status
   *     {@code 500 (Internal Server Error)} if the script couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/scripts")
  public ResponseEntity<Script> updateScript(@RequestBody Script script) throws URISyntaxException {
    log.debug("REST request to update Script : {}", script);
    if (script.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Script result = scriptRepository.save(script);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, script.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /scripts} : get all the scripts.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scripts in
   *     body.
   */
  @GetMapping("/scripts")
  public List<Script> getAllScripts() {
    log.debug("REST request to get all Scripts");
    return scriptRepository.findAll();
  }

  /**
   * {@code GET /scripts/:id} : get the "id" script.
   *
   * @param id the id of the script to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the script, or
   *     with status {@code 404 (Not Found)}.
   */
  @GetMapping("/scripts/{id}")
  public ResponseEntity<Script> getScript(@PathVariable Long id) {
    log.debug("REST request to get Script : {}", id);
    Optional<Script> script = scriptRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(script);
  }

  /**
   * {@code DELETE /scripts/:id} : delete the "id" script.
   *
   * @param id the id of the script to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/scripts/{id}")
  public ResponseEntity<Void> deleteScript(@PathVariable Long id) {
    log.debug("REST request to delete Script : {}", id);
    scriptRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
