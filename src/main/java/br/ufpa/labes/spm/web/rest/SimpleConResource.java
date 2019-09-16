package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.SimpleConService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.SimpleConDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.SimpleCon}. */
@RestController
@RequestMapping("/api")
public class SimpleConResource {

  private final Logger log = LoggerFactory.getLogger(SimpleConResource.class);

  private static final String ENTITY_NAME = "simpleCon";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SimpleConService simpleConService;

  public SimpleConResource(SimpleConService simpleConService) {
    this.simpleConService = simpleConService;
  }

  /**
   * {@code POST /simple-cons} : Create a new simpleCon.
   *
   * @param simpleConDTO the simpleConDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     simpleConDTO, or with status {@code 400 (Bad Request)} if the simpleCon has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/simple-cons")
  public ResponseEntity<SimpleConDTO> createSimpleCon(@RequestBody SimpleConDTO simpleConDTO)
      throws URISyntaxException {
    log.debug("REST request to save SimpleCon : {}", simpleConDTO);
    if (simpleConDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new simpleCon cannot already have an ID", ENTITY_NAME, "idexists");
    }
    SimpleConDTO result = simpleConService.save(simpleConDTO);
    return ResponseEntity.created(new URI("/api/simple-cons/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /simple-cons} : Updates an existing simpleCon.
   *
   * @param simpleConDTO the simpleConDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     simpleConDTO, or with status {@code 400 (Bad Request)} if the simpleConDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the simpleConDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/simple-cons")
  public ResponseEntity<SimpleConDTO> updateSimpleCon(@RequestBody SimpleConDTO simpleConDTO)
      throws URISyntaxException {
    log.debug("REST request to update SimpleCon : {}", simpleConDTO);
    if (simpleConDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    SimpleConDTO result = simpleConService.save(simpleConDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, simpleConDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /simple-cons} : get all the simpleCons.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of simpleCons in
   *     body.
   */
  @GetMapping("/simple-cons")
  public List<SimpleConDTO> getAllSimpleCons(@RequestParam(required = false) String filter) {
    if ("theconnectionsuper-is-null".equals(filter)) {
      log.debug("REST request to get all SimpleCons where theConnectionSuper is null");
      return simpleConService.findAllWhereTheConnectionSuperIsNull();
    }
    log.debug("REST request to get all SimpleCons");
    return simpleConService.findAll();
  }

  /**
   * {@code GET /simple-cons/:id} : get the "id" simpleCon.
   *
   * @param id the id of the simpleConDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the simpleConDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/simple-cons/{id}")
  public ResponseEntity<SimpleConDTO> getSimpleCon(@PathVariable Long id) {
    log.debug("REST request to get SimpleCon : {}", id);
    Optional<SimpleConDTO> simpleConDTO = simpleConService.findOne(id);
    return ResponseUtil.wrapOrNotFound(simpleConDTO);
  }

  /**
   * {@code DELETE /simple-cons/:id} : delete the "id" simpleCon.
   *
   * @param id the id of the simpleConDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/simple-cons/{id}")
  public ResponseEntity<Void> deleteSimpleCon(@PathVariable Long id) {
    log.debug("REST request to delete SimpleCon : {}", id);
    simpleConService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
