package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.EmailConfiguration;
import br.ufpa.labes.spm.repository.EmailConfigurationRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.EmailConfiguration}. */
@RestController
@RequestMapping("/api")
public class EmailConfigurationResource {

  private final Logger log = LoggerFactory.getLogger(EmailConfigurationResource.class);

  private static final String ENTITY_NAME = "emailConfiguration";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final EmailConfigurationRepository emailConfigurationRepository;

  public EmailConfigurationResource(EmailConfigurationRepository emailConfigurationRepository) {
    this.emailConfigurationRepository = emailConfigurationRepository;
  }

  /**
   * {@code POST /email-configurations} : Create a new emailConfiguration.
   *
   * @param emailConfiguration the emailConfiguration to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     emailConfiguration, or with status {@code 400 (Bad Request)} if the emailConfiguration has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/email-configurations")
  public ResponseEntity<EmailConfiguration> createEmailConfiguration(
      @RequestBody EmailConfiguration emailConfiguration) throws URISyntaxException {
    log.debug("REST request to save EmailConfiguration : {}", emailConfiguration);
    if (emailConfiguration.getId() != null) {
      throw new BadRequestAlertException(
          "A new emailConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
    }
    EmailConfiguration result = emailConfigurationRepository.save(emailConfiguration);
    return ResponseEntity.created(new URI("/api/email-configurations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /email-configurations} : Updates an existing emailConfiguration.
   *
   * @param emailConfiguration the emailConfiguration to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     emailConfiguration, or with status {@code 400 (Bad Request)} if the emailConfiguration is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the emailConfiguration
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/email-configurations")
  public ResponseEntity<EmailConfiguration> updateEmailConfiguration(
      @RequestBody EmailConfiguration emailConfiguration) throws URISyntaxException {
    log.debug("REST request to update EmailConfiguration : {}", emailConfiguration);
    if (emailConfiguration.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    EmailConfiguration result = emailConfigurationRepository.save(emailConfiguration);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, emailConfiguration.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /email-configurations} : get all the emailConfigurations.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     emailConfigurations in body.
   */
  @GetMapping("/email-configurations")
  public List<EmailConfiguration> getAllEmailConfigurations() {
    log.debug("REST request to get all EmailConfigurations");
    return emailConfigurationRepository.findAll();
  }

  /**
   * {@code GET /email-configurations/:id} : get the "id" emailConfiguration.
   *
   * @param id the id of the emailConfiguration to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     emailConfiguration, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/email-configurations/{id}")
  public ResponseEntity<EmailConfiguration> getEmailConfiguration(@PathVariable Long id) {
    log.debug("REST request to get EmailConfiguration : {}", id);
    Optional<EmailConfiguration> emailConfiguration = emailConfigurationRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(emailConfiguration);
  }

  /**
   * {@code DELETE /email-configurations/:id} : delete the "id" emailConfiguration.
   *
   * @param id the id of the emailConfiguration to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/email-configurations/{id}")
  public ResponseEntity<Void> deleteEmailConfiguration(@PathVariable Long id) {
    log.debug("REST request to delete EmailConfiguration : {}", id);
    emailConfigurationRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
