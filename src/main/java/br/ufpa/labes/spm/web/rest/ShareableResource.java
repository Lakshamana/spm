package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ShareableService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ShareableDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Shareable}. */
@RestController
@RequestMapping("/api")
public class ShareableResource {

  private final Logger log = LoggerFactory.getLogger(ShareableResource.class);

  private static final String ENTITY_NAME = "shareable";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ShareableService shareableService;

  public ShareableResource(ShareableService shareableService) {
    this.shareableService = shareableService;
  }

  /**
   * {@code POST /shareables} : Create a new shareable.
   *
   * @param shareableDTO the shareableDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     shareableDTO, or with status {@code 400 (Bad Request)} if the shareable has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/shareables")
  public ResponseEntity<ShareableDTO> createShareable(@RequestBody ShareableDTO shareableDTO)
      throws URISyntaxException {
    log.debug("REST request to save Shareable : {}", shareableDTO);
    if (shareableDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new shareable cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ShareableDTO result = shareableService.save(shareableDTO);
    return ResponseEntity.created(new URI("/api/shareables/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /shareables} : Updates an existing shareable.
   *
   * @param shareableDTO the shareableDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     shareableDTO, or with status {@code 400 (Bad Request)} if the shareableDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the shareableDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/shareables")
  public ResponseEntity<ShareableDTO> updateShareable(@RequestBody ShareableDTO shareableDTO)
      throws URISyntaxException {
    log.debug("REST request to update Shareable : {}", shareableDTO);
    if (shareableDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ShareableDTO result = shareableService.save(shareableDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, shareableDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /shareables} : get all the shareables.
   *
   * @param filter the filter of the request.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shareables in
   *     body.
   */
  @GetMapping("/shareables")
  public List<ShareableDTO> getAllShareables(@RequestParam(required = false) String filter) {
    if ("theresourcesuper-is-null".equals(filter)) {
      log.debug("REST request to get all Shareables where theResourceSuper is null");
      return shareableService.findAllWhereTheResourceSuperIsNull();
    }
    log.debug("REST request to get all Shareables");
    return shareableService.findAll();
  }

  /**
   * {@code GET /shareables/:id} : get the "id" shareable.
   *
   * @param id the id of the shareableDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shareableDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/shareables/{id}")
  public ResponseEntity<ShareableDTO> getShareable(@PathVariable Long id) {
    log.debug("REST request to get Shareable : {}", id);
    Optional<ShareableDTO> shareableDTO = shareableService.findOne(id);
    return ResponseUtil.wrapOrNotFound(shareableDTO);
  }

  /**
   * {@code DELETE /shareables/:id} : delete the "id" shareable.
   *
   * @param id the id of the shareableDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/shareables/{id}")
  public ResponseEntity<Void> deleteShareable(@PathVariable Long id) {
    log.debug("REST request to delete Shareable : {}", id);
    shareableService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
