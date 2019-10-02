package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.JoinCon;
import br.ufpa.labes.spm.repository.JoinConRepository;
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

    private final JoinConRepository joinConRepository;

    public JoinConResource(JoinConRepository joinConRepository) {
        this.joinConRepository = joinConRepository;
    }

    /**
     * {@code POST  /join-cons} : Create a new joinCon.
     *
     * @param joinCon the joinCon to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new joinCon, or with status {@code 400 (Bad Request)} if the joinCon has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/join-cons")
    public ResponseEntity<JoinCon> createJoinCon(@RequestBody JoinCon joinCon) throws URISyntaxException {
        log.debug("REST request to save JoinCon : {}", joinCon);
        if (joinCon.getId() != null) {
            throw new BadRequestAlertException("A new joinCon cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JoinCon result = joinConRepository.save(joinCon);
        return ResponseEntity.created(new URI("/api/join-cons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /join-cons} : Updates an existing joinCon.
     *
     * @param joinCon the joinCon to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated joinCon,
     * or with status {@code 400 (Bad Request)} if the joinCon is not valid,
     * or with status {@code 500 (Internal Server Error)} if the joinCon couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/join-cons")
    public ResponseEntity<JoinCon> updateJoinCon(@RequestBody JoinCon joinCon) throws URISyntaxException {
        log.debug("REST request to update JoinCon : {}", joinCon);
        if (joinCon.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        JoinCon result = joinConRepository.save(joinCon);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, joinCon.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /join-cons} : get all the joinCons.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of joinCons in body.
     */
    @GetMapping("/join-cons")
    public List<JoinCon> getAllJoinCons(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all JoinCons");
        return joinConRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /join-cons/:id} : get the "id" joinCon.
     *
     * @param id the id of the joinCon to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the joinCon, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/join-cons/{id}")
    public ResponseEntity<JoinCon> getJoinCon(@PathVariable Long id) {
        log.debug("REST request to get JoinCon : {}", id);
        Optional<JoinCon> joinCon = joinConRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(joinCon);
    }

    /**
     * {@code DELETE  /join-cons/:id} : delete the "id" joinCon.
     *
     * @param id the id of the joinCon to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/join-cons/{id}")
    public ResponseEntity<Void> deleteJoinCon(@PathVariable Long id) {
        log.debug("REST request to delete JoinCon : {}", id);
        joinConRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
