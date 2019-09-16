package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AutomaticService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AutomaticDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Automatic}. */
@RestController
@RequestMapping("/api")
public class AutomaticResource {

  private final Logger log = LoggerFactory.getLogger(AutomaticResource.class);

  private static final String ENTITY_NAME = "automatic";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AutomaticService automaticService;

  public AutomaticResource(AutomaticService automaticService) {
    this.automaticService = automaticService;
  }

  /**
   * {@code POST /automatics} : Create a new automatic.
   *
   * @param automaticDTO the automaticDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     automaticDTO, or with status {@code 400 (Bad Request)} if the automatic has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/automatics")
  public ResponseEntity<AutomaticDTO> createAutomatic(@RequestBody AutomaticDTO automaticDTO)
      throws URISyntaxException {
    log.debug("REST request to save Automatic : {}", automaticDTO);
    if (automaticDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new automatic cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AutomaticDTO result = automaticService.save(automaticDTO);
    return ResponseEntity.created(new URI("/api/automatics/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /automatics} : Updates an existing automatic.
   *
   * @param automaticDTO the automaticDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     automaticDTO, or with status {@code 400 (Bad Request)} if the automaticDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the automaticDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/automatics")
  public ResponseEntity<AutomaticDTO> updateAutomatic(@RequestBody AutomaticDTO automaticDTO)
      throws URISyntaxException {
    log.debug("REST request to update Automatic : {}", automaticDTO);
    if (automaticDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AutomaticDTO result = automaticService.save(automaticDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, automaticDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /automatics} : get all the automatics.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of automatics in
   *     body.
   */
  @GetMapping("/automatics")
  public List<AutomaticDTO> getAllAutomatics(@RequestParam(required = false) String filter) {
    if ("theplainsuper-is-null".equals(filter)) {
      log.debug("REST request to get all Automatics where thePlainSuper is null");
      return automaticService.findAllWhereThePlainSuperIsNull();
    }
    log.debug("REST request to get all Automatics");
    return automaticService.findAll();
  }

  /**
   * {@code GET /automatics/:id} : get the "id" automatic.
   *
   * @param id the id of the automaticDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the automaticDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/automatics/{id}")
  public ResponseEntity<AutomaticDTO> getAutomatic(@PathVariable Long id) {
    log.debug("REST request to get Automatic : {}", id);
    Optional<AutomaticDTO> automaticDTO = automaticService.findOne(id);
    return ResponseUtil.wrapOrNotFound(automaticDTO);
  }

  /**
   * {@code DELETE /automatics/:id} : delete the "id" automatic.
   *
   * @param id the id of the automaticDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/automatics/{id}")
  public ResponseEntity<Void> deleteAutomatic(@PathVariable Long id) {
    log.debug("REST request to delete Automatic : {}", id);
    automaticService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
