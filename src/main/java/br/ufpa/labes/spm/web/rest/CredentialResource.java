package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.CredentialService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.CredentialDTO;

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

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Credential}.
 */
@RestController
@RequestMapping("/api")
public class CredentialResource {

    private final Logger log = LoggerFactory.getLogger(CredentialResource.class);

    private static final String ENTITY_NAME = "credential";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CredentialService credentialService;

    public CredentialResource(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    /**
     * {@code POST  /credentials} : Create a new credential.
     *
     * @param credentialDTO the credentialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new credentialDTO, or with status {@code 400 (Bad Request)} if the credential has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/credentials")
    public ResponseEntity<CredentialDTO> createCredential(@RequestBody CredentialDTO credentialDTO) throws URISyntaxException {
        log.debug("REST request to save Credential : {}", credentialDTO);
        if (credentialDTO.getId() != null) {
            throw new BadRequestAlertException("A new credential cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CredentialDTO result = credentialService.save(credentialDTO);
        return ResponseEntity.created(new URI("/api/credentials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /credentials} : Updates an existing credential.
     *
     * @param credentialDTO the credentialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated credentialDTO,
     * or with status {@code 400 (Bad Request)} if the credentialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the credentialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/credentials")
    public ResponseEntity<CredentialDTO> updateCredential(@RequestBody CredentialDTO credentialDTO) throws URISyntaxException {
        log.debug("REST request to update Credential : {}", credentialDTO);
        if (credentialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CredentialDTO result = credentialService.save(credentialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, credentialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /credentials} : get all the credentials.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of credentials in body.
     */
    @GetMapping("/credentials")
    public List<CredentialDTO> getAllCredentials() {
        log.debug("REST request to get all Credentials");
        return credentialService.findAll();
    }

    /**
     * {@code GET  /credentials/:id} : get the "id" credential.
     *
     * @param id the id of the credentialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the credentialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/credentials/{id}")
    public ResponseEntity<CredentialDTO> getCredential(@PathVariable Long id) {
        log.debug("REST request to get Credential : {}", id);
        Optional<CredentialDTO> credentialDTO = credentialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(credentialDTO);
    }

    /**
     * {@code DELETE  /credentials/:id} : delete the "id" credential.
     *
     * @param id the id of the credentialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/credentials/{id}")
    public ResponseEntity<Void> deleteCredential(@PathVariable Long id) {
        log.debug("REST request to delete Credential : {}", id);
        credentialService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
