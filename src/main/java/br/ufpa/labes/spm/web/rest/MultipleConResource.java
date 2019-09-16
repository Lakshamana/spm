package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.MultipleConService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.MultipleConDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.MultipleCon}. */
@RestController
@RequestMapping("/api")
public class MultipleConResource {

  private final Logger log = LoggerFactory.getLogger(MultipleConResource.class);

  private static final String ENTITY_NAME = "multipleCon";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final MultipleConService multipleConService;

  public MultipleConResource(MultipleConService multipleConService) {
    this.multipleConService = multipleConService;
  }

  /**
   * {@code POST /multiple-cons} : Create a new multipleCon.
   *
   * @param multipleConDTO the multipleConDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     multipleConDTO, or with status {@code 400 (Bad Request)} if the multipleCon has already an
   *     ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/multiple-cons")
  public ResponseEntity<MultipleConDTO> createMultipleCon(
      @RequestBody MultipleConDTO multipleConDTO) throws URISyntaxException {
    log.debug("REST request to save MultipleCon : {}", multipleConDTO);
    if (multipleConDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new multipleCon cannot already have an ID", ENTITY_NAME, "idexists");
    }
    MultipleConDTO result = multipleConService.save(multipleConDTO);
    return ResponseEntity.created(new URI("/api/multiple-cons/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /multiple-cons} : Updates an existing multipleCon.
   *
   * @param multipleConDTO the multipleConDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     multipleConDTO, or with status {@code 400 (Bad Request)} if the multipleConDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the multipleConDTO couldn't be
   *     updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/multiple-cons")
  public ResponseEntity<MultipleConDTO> updateMultipleCon(
      @RequestBody MultipleConDTO multipleConDTO) throws URISyntaxException {
    log.debug("REST request to update MultipleCon : {}", multipleConDTO);
    if (multipleConDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    MultipleConDTO result = multipleConService.save(multipleConDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, multipleConDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /multiple-cons} : get all the multipleCons.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of multipleCons in
   *     body.
   */
  @GetMapping("/multiple-cons")
  public List<MultipleConDTO> getAllMultipleCons(@RequestParam(required = false) String filter) {
    if ("theconnectionsuper-is-null".equals(filter)) {
      log.debug("REST request to get all MultipleCons where theConnectionSuper is null");
      return multipleConService.findAllWhereTheConnectionSuperIsNull();
    }
    log.debug("REST request to get all MultipleCons");
    return multipleConService.findAll();
  }

  /**
   * {@code GET /multiple-cons/:id} : get the "id" multipleCon.
   *
   * @param id the id of the multipleConDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     multipleConDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/multiple-cons/{id}")
  public ResponseEntity<MultipleConDTO> getMultipleCon(@PathVariable Long id) {
    log.debug("REST request to get MultipleCon : {}", id);
    Optional<MultipleConDTO> multipleConDTO = multipleConService.findOne(id);
    return ResponseUtil.wrapOrNotFound(multipleConDTO);
  }

  /**
   * {@code DELETE /multiple-cons/:id} : delete the "id" multipleCon.
   *
   * @param id the id of the multipleConDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/multiple-cons/{id}")
  public ResponseEntity<Void> deleteMultipleCon(@PathVariable Long id) {
    log.debug("REST request to delete MultipleCon : {}", id);
    multipleConService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
