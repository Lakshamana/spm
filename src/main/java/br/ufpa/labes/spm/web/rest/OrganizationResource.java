package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.OrganizationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.OrganizationDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Organization}. */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

  private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

  private static final String ENTITY_NAME = "organization";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final OrganizationService organizationService;

  public OrganizationResource(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  /**
   * {@code POST /organizations} : Create a new organization.
   *
   * @param organizationDTO the organizationDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     organizationDTO, or with status {@code 400 (Bad Request)} if the organization has already
   *     an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/organizations")
  public ResponseEntity<OrganizationDTO> createOrganization(
      @RequestBody OrganizationDTO organizationDTO) throws URISyntaxException {
    log.debug("REST request to save Organization : {}", organizationDTO);
    if (organizationDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new organization cannot already have an ID", ENTITY_NAME, "idexists");
    }
    OrganizationDTO result = organizationService.save(organizationDTO);
    return ResponseEntity.created(new URI("/api/organizations/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /organizations} : Updates an existing organization.
   *
   * @param organizationDTO the organizationDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     organizationDTO, or with status {@code 400 (Bad Request)} if the organizationDTO is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the organizationDTO couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/organizations")
  public ResponseEntity<OrganizationDTO> updateOrganization(
      @RequestBody OrganizationDTO organizationDTO) throws URISyntaxException {
    log.debug("REST request to update Organization : {}", organizationDTO);
    if (organizationDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    OrganizationDTO result = organizationService.save(organizationDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, organizationDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /organizations} : get all the organizations.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations
   *     in body.
   */
  @GetMapping("/organizations")
  public List<OrganizationDTO> getAllOrganizations() {
    log.debug("REST request to get all Organizations");
    return organizationService.findAll();
  }

  /**
   * {@code GET /organizations/:id} : get the "id" organization.
   *
   * @param id the id of the organizationDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     organizationDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/organizations/{id}")
  public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable Long id) {
    log.debug("REST request to get Organization : {}", id);
    Optional<OrganizationDTO> organizationDTO = organizationService.findOne(id);
    return ResponseUtil.wrapOrNotFound(organizationDTO);
  }

  /**
   * {@code DELETE /organizations/:id} : delete the "id" organization.
   *
   * @param id the id of the organizationDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/organizations/{id}")
  public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
    log.debug("REST request to delete Organization : {}", id);
    organizationService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
