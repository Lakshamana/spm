package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.JoinConService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.JoinConDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.JoinCon}.
 */
@RestController
@RequestMapping("/api")
public class JoinConResource {

    private final Logger log = LoggerFactory.getLogger(JoinConResource.class);

    private static final String ENTITY_NAME = "joinCon";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JoinConService joinConService;

    public JoinConResource(JoinConService joinConService) {
        this.joinConService = joinConService;
    }

    /**
     * {@code POST  /join-cons} : Create a new joinCon.
     *
     * @param joinConDTO the joinConDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new joinConDTO, or with status {@code 400 (Bad Request)} if the joinCon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/join-cons")
    public ResponseEntity<JoinConDTO> createJoinCon(@RequestBody JoinConDTO joinConDTO) throws URISyntaxException {
        log.debug("REST request to save JoinCon : {}", joinConDTO);
        if (joinConDTO.getId() != null) {
            throw new BadRequestAlertException("A new joinCon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JoinConDTO result = joinConService.save(joinConDTO);
        return ResponseEntity.created(new URI("/api/join-cons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /join-cons} : Updates an existing joinCon.
     *
     * @param joinConDTO the joinConDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated joinConDTO,
     * or with status {@code 400 (Bad Request)} if the joinConDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the joinConDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/join-cons")
    public ResponseEntity<JoinConDTO> updateJoinCon(@RequestBody JoinConDTO joinConDTO) throws URISyntaxException {
        log.debug("REST request to update JoinCon : {}", joinConDTO);
        if (joinConDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JoinConDTO result = joinConService.save(joinConDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, joinConDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /join-cons} : get all the joinCons.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of joinCons in body.
     */
    @GetMapping("/join-cons")
    public List<JoinConDTO> getAllJoinCons(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all JoinCons");
        return joinConService.findAll();
    }

    /**
     * {@code GET  /join-cons/:id} : get the "id" joinCon.
     *
     * @param id the id of the joinConDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the joinConDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/join-cons/{id}")
    public ResponseEntity<JoinConDTO> getJoinCon(@PathVariable Long id) {
        log.debug("REST request to get JoinCon : {}", id);
        Optional<JoinConDTO> joinConDTO = joinConService.findOne(id);
        return ResponseUtil.wrapOrNotFound(joinConDTO);
    }

    /**
     * {@code DELETE  /join-cons/:id} : delete the "id" joinCon.
     *
     * @param id the id of the joinConDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/join-cons/{id}")
    public ResponseEntity<Void> deleteJoinCon(@PathVariable Long id) {
        log.debug("REST request to delete JoinCon : {}", id);
        joinConService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
