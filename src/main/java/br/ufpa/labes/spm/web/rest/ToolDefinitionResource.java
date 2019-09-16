package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ToolDefinitionService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ToolDefinitionDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ToolDefinition}. */
@RestController
@RequestMapping("/api")
public class ToolDefinitionResource {

  private final Logger log = LoggerFactory.getLogger(ToolDefinitionResource.class);

  private static final String ENTITY_NAME = "toolDefinition";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ToolDefinitionService toolDefinitionService;

  public ToolDefinitionResource(ToolDefinitionService toolDefinitionService) {
    this.toolDefinitionService = toolDefinitionService;
  }

  /**
   * {@code POST /tool-definitions} : Create a new toolDefinition.
   *
   * @param toolDefinitionDTO the toolDefinitionDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     toolDefinitionDTO, or with status {@code 400 (Bad Request)} if the toolDefinition has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/tool-definitions")
  public ResponseEntity<ToolDefinitionDTO> createToolDefinition(
      @RequestBody ToolDefinitionDTO toolDefinitionDTO) throws URISyntaxException {
    log.debug("REST request to save ToolDefinition : {}", toolDefinitionDTO);
    if (toolDefinitionDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new toolDefinition cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ToolDefinitionDTO result = toolDefinitionService.save(toolDefinitionDTO);
    return ResponseEntity.created(new URI("/api/tool-definitions/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /tool-definitions} : Updates an existing toolDefinition.
   *
   * @param toolDefinitionDTO the toolDefinitionDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     toolDefinitionDTO, or with status {@code 400 (Bad Request)} if the toolDefinitionDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the toolDefinitionDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/tool-definitions")
  public ResponseEntity<ToolDefinitionDTO> updateToolDefinition(
      @RequestBody ToolDefinitionDTO toolDefinitionDTO) throws URISyntaxException {
    log.debug("REST request to update ToolDefinition : {}", toolDefinitionDTO);
    if (toolDefinitionDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ToolDefinitionDTO result = toolDefinitionService.save(toolDefinitionDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, toolDefinitionDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /tool-definitions} : get all the toolDefinitions.
   *
   * @param eagerload flag to eager load entities from relationships (This is applicable for
   *     many-to-many).
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toolDefinitions
   *     in body.
   */
  @GetMapping("/tool-definitions")
  public List<ToolDefinitionDTO> getAllToolDefinitions(
      @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
    log.debug("REST request to get all ToolDefinitions");
    return toolDefinitionService.findAll();
  }

  /**
   * {@code GET /tool-definitions/:id} : get the "id" toolDefinition.
   *
   * @param id the id of the toolDefinitionDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     toolDefinitionDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/tool-definitions/{id}")
  public ResponseEntity<ToolDefinitionDTO> getToolDefinition(@PathVariable Long id) {
    log.debug("REST request to get ToolDefinition : {}", id);
    Optional<ToolDefinitionDTO> toolDefinitionDTO = toolDefinitionService.findOne(id);
    return ResponseUtil.wrapOrNotFound(toolDefinitionDTO);
  }

  /**
   * {@code DELETE /tool-definitions/:id} : delete the "id" toolDefinition.
   *
   * @param id the id of the toolDefinitionDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/tool-definitions/{id}")
  public ResponseEntity<Void> deleteToolDefinition(@PathVariable Long id) {
    log.debug("REST request to delete ToolDefinition : {}", id);
    toolDefinitionService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
