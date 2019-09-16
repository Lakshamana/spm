package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.PrimitiveParamService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.PrimitiveParamDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.PrimitiveParam}. */
@RestController
@RequestMapping("/api")
public class PrimitiveParamResource {

  private final Logger log = LoggerFactory.getLogger(PrimitiveParamResource.class);

  private static final String ENTITY_NAME = "primitiveParam";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final PrimitiveParamService primitiveParamService;

  public PrimitiveParamResource(PrimitiveParamService primitiveParamService) {
    this.primitiveParamService = primitiveParamService;
  }

  /**
   * {@code POST /primitive-params} : Create a new primitiveParam.
   *
   * @param primitiveParamDTO the primitiveParamDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     primitiveParamDTO, or with status {@code 400 (Bad Request)} if the primitiveParam has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/primitive-params")
  public ResponseEntity<PrimitiveParamDTO> createPrimitiveParam(
      @RequestBody PrimitiveParamDTO primitiveParamDTO) throws URISyntaxException {
    log.debug("REST request to save PrimitiveParam : {}", primitiveParamDTO);
    if (primitiveParamDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new primitiveParam cannot already have an ID", ENTITY_NAME, "idexists");
    }
    PrimitiveParamDTO result = primitiveParamService.save(primitiveParamDTO);
    return ResponseEntity.created(new URI("/api/primitive-params/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /primitive-params} : Updates an existing primitiveParam.
   *
   * @param primitiveParamDTO the primitiveParamDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     primitiveParamDTO, or with status {@code 400 (Bad Request)} if the primitiveParamDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the primitiveParamDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/primitive-params")
  public ResponseEntity<PrimitiveParamDTO> updatePrimitiveParam(
      @RequestBody PrimitiveParamDTO primitiveParamDTO) throws URISyntaxException {
    log.debug("REST request to update PrimitiveParam : {}", primitiveParamDTO);
    if (primitiveParamDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    PrimitiveParamDTO result = primitiveParamService.save(primitiveParamDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, primitiveParamDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /primitive-params} : get all the primitiveParams.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of primitiveParams
   *     in body.
   */
  @GetMapping("/primitive-params")
  public List<PrimitiveParamDTO> getAllPrimitiveParams(
      @RequestParam(required = false) String filter) {
    if ("theparametersuper-is-null".equals(filter)) {
      log.debug("REST request to get all PrimitiveParams where theParameterSuper is null");
      return primitiveParamService.findAllWhereTheParameterSuperIsNull();
    }
    log.debug("REST request to get all PrimitiveParams");
    return primitiveParamService.findAll();
  }

  /**
   * {@code GET /primitive-params/:id} : get the "id" primitiveParam.
   *
   * @param id the id of the primitiveParamDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     primitiveParamDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/primitive-params/{id}")
  public ResponseEntity<PrimitiveParamDTO> getPrimitiveParam(@PathVariable Long id) {
    log.debug("REST request to get PrimitiveParam : {}", id);
    Optional<PrimitiveParamDTO> primitiveParamDTO = primitiveParamService.findOne(id);
    return ResponseUtil.wrapOrNotFound(primitiveParamDTO);
  }

  /**
   * {@code DELETE /primitive-params/:id} : delete the "id" primitiveParam.
   *
   * @param id the id of the primitiveParamDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/primitive-params/{id}")
  public ResponseEntity<Void> deletePrimitiveParam(@PathVariable Long id) {
    log.debug("REST request to delete PrimitiveParam : {}", id);
    primitiveParamService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
