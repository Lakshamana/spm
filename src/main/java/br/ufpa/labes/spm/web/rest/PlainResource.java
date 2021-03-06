package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.PlainService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.PlainDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Plain}.
 */
@RestController
@RequestMapping("/api")
public class PlainResource {

    private final Logger log = LoggerFactory.getLogger(PlainResource.class);

    private static final String ENTITY_NAME = "plain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlainService plainService;

    public PlainResource(PlainService plainService) {
        this.plainService = plainService;
    }

    /**
     * {@code POST  /plains} : Create a new plain.
     *
     * @param plainDTO the plainDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plainDTO, or with status {@code 400 (Bad Request)} if the plain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plains")
    public ResponseEntity<PlainDTO> createPlain(@RequestBody PlainDTO plainDTO) throws URISyntaxException {
        log.debug("REST request to save Plain : {}", plainDTO);
        if (plainDTO.getId() != null) {
            throw new BadRequestAlertException("A new plain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlainDTO result = plainService.save(plainDTO);
        return ResponseEntity.created(new URI("/api/plains/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plains} : Updates an existing plain.
     *
     * @param plainDTO the plainDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plainDTO,
     * or with status {@code 400 (Bad Request)} if the plainDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plainDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plains")
    public ResponseEntity<PlainDTO> updatePlain(@RequestBody PlainDTO plainDTO) throws URISyntaxException {
        log.debug("REST request to update Plain : {}", plainDTO);
        if (plainDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PlainDTO result = plainService.save(plainDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, plainDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /plains} : get all the plains.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plains in body.
     */
    @GetMapping("/plains")
    public List<PlainDTO> getAllPlains() {
        log.debug("REST request to get all Plains");
        return plainService.findAll();
    }

    /**
     * {@code GET  /plains/:id} : get the "id" plain.
     *
     * @param id the id of the plainDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plainDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/plains/{id}")
    public ResponseEntity<PlainDTO> getPlain(@PathVariable Long id) {
        log.debug("REST request to get Plain : {}", id);
        Optional<PlainDTO> plainDTO = plainService.findOne(id);
        return ResponseUtil.wrapOrNotFound(plainDTO);
    }

    /**
     * {@code DELETE  /plains/:id} : delete the "id" plain.
     *
     * @param id the id of the plainDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plains/{id}")
    public ResponseEntity<Void> deletePlain(@PathVariable Long id) {
        log.debug("REST request to delete Plain : {}", id);
        plainService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
