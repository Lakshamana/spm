package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Consumable;
import br.ufpa.labes.spm.repository.ConsumableRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Consumable}. */
@RestController
@RequestMapping("/api")
public class ConsumableResource {

  private final Logger log = LoggerFactory.getLogger(ConsumableResource.class);

  private static final String ENTITY_NAME = "consumable";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ConsumableRepository consumableRepository;

  public ConsumableResource(ConsumableRepository consumableRepository) {
    this.consumableRepository = consumableRepository;
  }

  /**
   * {@code POST /consumables} : Create a new consumable.
   *
   * @param consumable the consumable to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     consumable, or with status {@code 400 (Bad Request)} if the consumable has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/consumables")
  public ResponseEntity<Consumable> createConsumable(@RequestBody Consumable consumable)
      throws URISyntaxException {
    log.debug("REST request to save Consumable : {}", consumable);
    if (consumable.getId() != null) {
      throw new BadRequestAlertException(
          "A new consumable cannot already have an ID", ENTITY_NAME, "idexists");
    }
    Consumable result = consumableRepository.save(consumable);
    return ResponseEntity.created(new URI("/api/consumables/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /consumables} : Updates an existing consumable.
   *
   * @param consumable the consumable to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     consumable, or with status {@code 400 (Bad Request)} if the consumable is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the consumable couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/consumables")
  public ResponseEntity<Consumable> updateConsumable(@RequestBody Consumable consumable)
      throws URISyntaxException {
    log.debug("REST request to update Consumable : {}", consumable);
    if (consumable.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    Consumable result = consumableRepository.save(consumable);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, consumable.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /consumables} : get all the consumables.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of consumables in
   *     body.
   */
  @GetMapping("/consumables")
  public List<Consumable> getAllConsumables() {
    log.debug("REST request to get all Consumables");
    return consumableRepository.findAll();
  }

  /**
   * {@code GET /consumables/:id} : get the "id" consumable.
   *
   * @param id the id of the consumable to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the consumable,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/consumables/{id}")
  public ResponseEntity<Consumable> getConsumable(@PathVariable Long id) {
    log.debug("REST request to get Consumable : {}", id);
    Optional<Consumable> consumable = consumableRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(consumable);
  }

  /**
   * {@code DELETE /consumables/:id} : delete the "id" consumable.
   *
   * @param id the id of the consumable to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/consumables/{id}")
  public ResponseEntity<Void> deleteConsumable(@PathVariable Long id) {
    log.debug("REST request to delete Consumable : {}", id);
    consumableRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
