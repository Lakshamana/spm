package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AbilityType;
import br.ufpa.labes.spm.repository.AbilityTypeRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AbilityType}. */
@RestController
@RequestMapping("/api")
public class AbilityTypeResource {

  private final Logger log = LoggerFactory.getLogger(AbilityTypeResource.class);

  private static final String ENTITY_NAME = "abilityType";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AbilityTypeRepository abilityTypeRepository;

  public AbilityTypeResource(AbilityTypeRepository abilityTypeRepository) {
    this.abilityTypeRepository = abilityTypeRepository;
  }

  /**
   * {@code POST /ability-types} : Create a new abilityType.
   *
   * @param abilityType the abilityType to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     abilityType, or with status {@code 400 (Bad Request)} if the abilityType has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/ability-types")
  public ResponseEntity<AbilityType> createAbilityType(@RequestBody AbilityType abilityType)
      throws URISyntaxException {
    log.debug("REST request to save AbilityType : {}", abilityType);
    if (abilityType.getId() != null) {
      throw new BadRequestAlertException(
          "A new abilityType cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AbilityType result = abilityTypeRepository.save(abilityType);
    return ResponseEntity.created(new URI("/api/ability-types/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /ability-types} : Updates an existing abilityType.
   *
   * @param abilityType the abilityType to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     abilityType, or with status {@code 400 (Bad Request)} if the abilityType is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the abilityType couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/ability-types")
  public ResponseEntity<AbilityType> updateAbilityType(@RequestBody AbilityType abilityType)
      throws URISyntaxException {
    log.debug("REST request to update AbilityType : {}", abilityType);
    if (abilityType.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AbilityType result = abilityTypeRepository.save(abilityType);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, abilityType.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /ability-types} : get all the abilityTypes.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abilityTypes in
   *     body.
   */
  @GetMapping("/ability-types")
  public List<AbilityType> getAllAbilityTypes() {
    log.debug("REST request to get all AbilityTypes");
    return abilityTypeRepository.findAll();
  }

  /**
   * {@code GET /ability-types/:id} : get the "id" abilityType.
   *
   * @param id the id of the abilityType to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abilityType,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/ability-types/{id}")
  public ResponseEntity<AbilityType> getAbilityType(@PathVariable Long id) {
    log.debug("REST request to get AbilityType : {}", id);
    Optional<AbilityType> abilityType = abilityTypeRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(abilityType);
  }

  /**
   * {@code DELETE /ability-types/:id} : delete the "id" abilityType.
   *
   * @param id the id of the abilityType to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/ability-types/{id}")
  public ResponseEntity<Void> deleteAbilityType(@PathVariable Long id) {
    log.debug("REST request to delete AbilityType : {}", id);
    abilityTypeRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
