package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.SpmConfiguration;
import br.ufpa.labes.spm.repository.SpmConfigurationRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/** REST controller for managing {@link br.ufpa.labes.spm.domain.SpmConfiguration}. */
@RestController
@RequestMapping("/api")
public class SpmConfigurationResource {

  private final Logger log = LoggerFactory.getLogger(SpmConfigurationResource.class);

  private static final String ENTITY_NAME = "spmConfiguration";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final SpmConfigurationRepository spmConfigurationRepository;

  public SpmConfigurationResource(SpmConfigurationRepository spmConfigurationRepository) {
    this.spmConfigurationRepository = spmConfigurationRepository;
  }

  /**
   * {@code POST /spm-configurations} : Create a new spmConfiguration.
   *
   * @param spmConfiguration the spmConfiguration to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     spmConfiguration, or with status {@code 400 (Bad Request)} if the spmConfiguration has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/spm-configurations")
  public ResponseEntity<SpmConfiguration> createSpmConfiguration(
      @RequestBody SpmConfiguration spmConfiguration) throws URISyntaxException {
    log.debug("REST request to save SpmConfiguration : {}", spmConfiguration);
    if (spmConfiguration.getId() != null) {
      throw new BadRequestAlertException(
          "A new spmConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
    }
    SpmConfiguration result = spmConfigurationRepository.save(spmConfiguration);
    return ResponseEntity.created(new URI("/api/spm-configurations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /spm-configurations} : Updates an existing spmConfiguration.
   *
   * @param spmConfiguration the spmConfiguration to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     spmConfiguration, or with status {@code 400 (Bad Request)} if the spmConfiguration is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the spmConfiguration couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/spm-configurations")
  public ResponseEntity<SpmConfiguration> updateSpmConfiguration(
      @RequestBody SpmConfiguration spmConfiguration) throws URISyntaxException {
    log.debug("REST request to update SpmConfiguration : {}", spmConfiguration);
    if (spmConfiguration.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    SpmConfiguration result = spmConfigurationRepository.save(spmConfiguration);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, spmConfiguration.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /spm-configurations} : get all the spmConfigurations.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     spmConfigurations in body.
   */
  @GetMapping("/spm-configurations")
  public List<SpmConfiguration> getAllSpmConfigurations(
      @RequestParam(required = false) String filter) {
    if ("agent-is-null".equals(filter)) {
      log.debug("REST request to get all SpmConfigurations where agent is null");
      return StreamSupport.stream(spmConfigurationRepository.findAll().spliterator(), false)
          .filter(spmConfiguration -> spmConfiguration.getAgent() == null)
          .collect(Collectors.toList());
    }
    log.debug("REST request to get all SpmConfigurations");
    return spmConfigurationRepository.findAll();
  }

  /**
   * {@code GET /spm-configurations/:id} : get the "id" spmConfiguration.
   *
   * @param id the id of the spmConfiguration to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     spmConfiguration, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/spm-configurations/{id}")
  public ResponseEntity<SpmConfiguration> getSpmConfiguration(@PathVariable Long id) {
    log.debug("REST request to get SpmConfiguration : {}", id);
    Optional<SpmConfiguration> spmConfiguration = spmConfigurationRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(spmConfiguration);
  }

  /**
   * {@code DELETE /spm-configurations/:id} : delete the "id" spmConfiguration.
   *
   * @param id the id of the spmConfiguration to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/spm-configurations/{id}")
  public ResponseEntity<Void> deleteSpmConfiguration(@PathVariable Long id) {
    log.debug("REST request to delete SpmConfiguration : {}", id);
    spmConfigurationRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
