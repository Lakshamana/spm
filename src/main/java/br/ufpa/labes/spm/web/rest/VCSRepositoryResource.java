package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.VCSRepositoryService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.VCSRepositoryDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.VCSRepository}.
 */
@RestController
@RequestMapping("/api")
public class VCSRepositoryResource {

    private final Logger log = LoggerFactory.getLogger(VCSRepositoryResource.class);

    private static final String ENTITY_NAME = "vCSRepository";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VCSRepositoryService vCSRepositoryService;

    public VCSRepositoryResource(VCSRepositoryService vCSRepositoryService) {
        this.vCSRepositoryService = vCSRepositoryService;
    }

    /**
     * {@code POST  /vcs-repositories} : Create a new vCSRepository.
     *
     * @param vCSRepositoryDTO the vCSRepositoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vCSRepositoryDTO, or with status {@code 400 (Bad Request)} if the vCSRepository has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vcs-repositories")
    public ResponseEntity<VCSRepositoryDTO> createVCSRepository(@RequestBody VCSRepositoryDTO vCSRepositoryDTO) throws URISyntaxException {
        log.debug("REST request to save VCSRepository : {}", vCSRepositoryDTO);
        if (vCSRepositoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new vCSRepository cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VCSRepositoryDTO result = vCSRepositoryService.save(vCSRepositoryDTO);
        return ResponseEntity.created(new URI("/api/vcs-repositories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vcs-repositories} : Updates an existing vCSRepository.
     *
     * @param vCSRepositoryDTO the vCSRepositoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vCSRepositoryDTO,
     * or with status {@code 400 (Bad Request)} if the vCSRepositoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vCSRepositoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vcs-repositories")
    public ResponseEntity<VCSRepositoryDTO> updateVCSRepository(@RequestBody VCSRepositoryDTO vCSRepositoryDTO) throws URISyntaxException {
        log.debug("REST request to update VCSRepository : {}", vCSRepositoryDTO);
        if (vCSRepositoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VCSRepositoryDTO result = vCSRepositoryService.save(vCSRepositoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vCSRepositoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /vcs-repositories} : get all the vCSRepositories.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vCSRepositories in body.
     */
    @GetMapping("/vcs-repositories")
    public List<VCSRepositoryDTO> getAllVCSRepositories() {
        log.debug("REST request to get all VCSRepositories");
        return vCSRepositoryService.findAll();
    }

    /**
     * {@code GET  /vcs-repositories/:id} : get the "id" vCSRepository.
     *
     * @param id the id of the vCSRepositoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vCSRepositoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vcs-repositories/{id}")
    public ResponseEntity<VCSRepositoryDTO> getVCSRepository(@PathVariable Long id) {
        log.debug("REST request to get VCSRepository : {}", id);
        Optional<VCSRepositoryDTO> vCSRepositoryDTO = vCSRepositoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(vCSRepositoryDTO);
    }

    /**
     * {@code DELETE  /vcs-repositories/:id} : delete the "id" vCSRepository.
     *
     * @param id the id of the vCSRepositoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vcs-repositories/{id}")
    public ResponseEntity<Void> deleteVCSRepository(@PathVariable Long id) {
        log.debug("REST request to delete VCSRepository : {}", id);
        vCSRepositoryService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
