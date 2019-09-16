package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ToolTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ToolTypeDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ToolType}. */
@RestController
@RequestMapping("/api")
public class ToolTypeResource {

  private final Logger log = LoggerFactory.getLogger(ToolTypeResource.class);

  private static final String ENTITY_NAME = "toolType";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ToolTypeService toolTypeService;

  public ToolTypeResource(ToolTypeService toolTypeService) {
    this.toolTypeService = toolTypeService;
  }

  /**
   * {@code POST /tool-types} : Create a new toolType.
   *
   * @param toolTypeDTO the toolTypeDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     toolTypeDTO, or with status {@code 400 (Bad Request)} if the toolType has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/tool-types")
  public ResponseEntity<ToolTypeDTO> createToolType(@RequestBody ToolTypeDTO toolTypeDTO)
      throws URISyntaxException {
    log.debug("REST request to save ToolType : {}", toolTypeDTO);
    if (toolTypeDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new toolType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ToolTypeDTO result = toolTypeService.save(toolTypeDTO);
    return ResponseEntity.created(new URI("/api/tool-types/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /tool-types} : Updates an existing toolType.
   *
   * @param toolTypeDTO the toolTypeDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     toolTypeDTO, or with status {@code 400 (Bad Request)} if the toolTypeDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the toolTypeDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/tool-types")
  public ResponseEntity<ToolTypeDTO> updateToolType(@RequestBody ToolTypeDTO toolTypeDTO)
      throws URISyntaxException {
    log.debug("REST request to update ToolType : {}", toolTypeDTO);
    if (toolTypeDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ToolTypeDTO result = toolTypeService.save(toolTypeDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, toolTypeDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /tool-types} : get all the toolTypes.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolTypes in
   *     body.
   */
  @GetMapping("/tool-types")
  public List<ToolTypeDTO> getAllToolTypes(@RequestParam(required = false) String filter) {
    if ("thetypesuper-is-null".equals(filter)) {
      log.debug("REST request to get all ToolTypes where theTypeSuper is null");
      return toolTypeService.findAllWhereTheTypeSuperIsNull();
    }
    log.debug("REST request to get all ToolTypes");
    return toolTypeService.findAll();
  }

  /**
   * {@code GET /tool-types/:id} : get the "id" toolType.
   *
   * @param id the id of the toolTypeDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toolTypeDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/tool-types/{id}")
  public ResponseEntity<ToolTypeDTO> getToolType(@PathVariable Long id) {
    log.debug("REST request to get ToolType : {}", id);
    Optional<ToolTypeDTO> toolTypeDTO = toolTypeService.findOne(id);
    return ResponseUtil.wrapOrNotFound(toolTypeDTO);
  }

  /**
   * {@code DELETE /tool-types/:id} : delete the "id" toolType.
   *
   * @param id the id of the toolTypeDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/tool-types/{id}")
  public ResponseEntity<Void> deleteToolType(@PathVariable Long id) {
    log.debug("REST request to delete ToolType : {}", id);
    toolTypeService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
