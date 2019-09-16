package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.StructureService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.StructureDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Structure}. */
@RestController
@RequestMapping("/api")
public class StructureResource {

  private final Logger log = LoggerFactory.getLogger(StructureResource.class);

  private static final String ENTITY_NAME = "structure";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final StructureService structureService;

  public StructureResource(StructureService structureService) {
    this.structureService = structureService;
  }

  /**
   * {@code POST /structures} : Create a new structure.
   *
   * @param structureDTO the structureDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     structureDTO, or with status {@code 400 (Bad Request)} if the structure has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/structures")
  public ResponseEntity<StructureDTO> createStructure(@RequestBody StructureDTO structureDTO)
      throws URISyntaxException {
    log.debug("REST request to save Structure : {}", structureDTO);
    if (structureDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new structure cannot already have an ID", ENTITY_NAME, "idexists");
    }
    StructureDTO result = structureService.save(structureDTO);
    return ResponseEntity.created(new URI("/api/structures/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /structures} : Updates an existing structure.
   *
   * @param structureDTO the structureDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     structureDTO, or with status {@code 400 (Bad Request)} if the structureDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the structureDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/structures")
  public ResponseEntity<StructureDTO> updateStructure(@RequestBody StructureDTO structureDTO)
      throws URISyntaxException {
    log.debug("REST request to update Structure : {}", structureDTO);
    if (structureDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    StructureDTO result = structureService.save(structureDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, structureDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /structures} : get all the structures.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of structures in
   *     body.
   */
  @GetMapping("/structures")
  public List<StructureDTO> getAllStructures() {
    log.debug("REST request to get all Structures");
    return structureService.findAll();
  }

  /**
   * {@code GET /structures/:id} : get the "id" structure.
   *
   * @param id the id of the structureDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the structureDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/structures/{id}")
  public ResponseEntity<StructureDTO> getStructure(@PathVariable Long id) {
    log.debug("REST request to get Structure : {}", id);
    Optional<StructureDTO> structureDTO = structureService.findOne(id);
    return ResponseUtil.wrapOrNotFound(structureDTO);
  }

  /**
   * {@code DELETE /structures/:id} : delete the "id" structure.
   *
   * @param id the id of the structureDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/structures/{id}")
  public ResponseEntity<Void> deleteStructure(@PathVariable Long id) {
    log.debug("REST request to delete Structure : {}", id);
    structureService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
