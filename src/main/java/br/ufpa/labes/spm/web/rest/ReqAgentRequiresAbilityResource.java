package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ReqAgentRequiresAbility;
import br.ufpa.labes.spm.repository.ReqAgentRequiresAbilityRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ReqAgentRequiresAbility}.
 */
@RestController
@RequestMapping("/api")
public class ReqAgentRequiresAbilityResource {

    private final Logger log = LoggerFactory.getLogger(ReqAgentRequiresAbilityResource.class);

    private static final String ENTITY_NAME = "reqAgentRequiresAbility";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReqAgentRequiresAbilityRepository reqAgentRequiresAbilityRepository;

    public ReqAgentRequiresAbilityResource(ReqAgentRequiresAbilityRepository reqAgentRequiresAbilityRepository) {
        this.reqAgentRequiresAbilityRepository = reqAgentRequiresAbilityRepository;
    }

    /**
     * {@code POST  /req-agent-requires-abilities} : Create a new reqAgentRequiresAbility.
     *
     * @param reqAgentRequiresAbility the reqAgentRequiresAbility to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reqAgentRequiresAbility, or with status {@code 400 (Bad Request)} if the reqAgentRequiresAbility has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/req-agent-requires-abilities")
    public ResponseEntity<ReqAgentRequiresAbility> createReqAgentRequiresAbility(@RequestBody ReqAgentRequiresAbility reqAgentRequiresAbility) throws URISyntaxException {
        log.debug("REST request to save ReqAgentRequiresAbility : {}", reqAgentRequiresAbility);
        if (reqAgentRequiresAbility.getId() != null) {
            throw new BadRequestAlertException("A new reqAgentRequiresAbility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReqAgentRequiresAbility result = reqAgentRequiresAbilityRepository.save(reqAgentRequiresAbility);
        return ResponseEntity.created(new URI("/api/req-agent-requires-abilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /req-agent-requires-abilities} : Updates an existing reqAgentRequiresAbility.
     *
     * @param reqAgentRequiresAbility the reqAgentRequiresAbility to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reqAgentRequiresAbility,
     * or with status {@code 400 (Bad Request)} if the reqAgentRequiresAbility is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reqAgentRequiresAbility couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/req-agent-requires-abilities")
    public ResponseEntity<ReqAgentRequiresAbility> updateReqAgentRequiresAbility(@RequestBody ReqAgentRequiresAbility reqAgentRequiresAbility) throws URISyntaxException {
        log.debug("REST request to update ReqAgentRequiresAbility : {}", reqAgentRequiresAbility);
        if (reqAgentRequiresAbility.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReqAgentRequiresAbility result = reqAgentRequiresAbilityRepository.save(reqAgentRequiresAbility);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reqAgentRequiresAbility.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /req-agent-requires-abilities} : get all the reqAgentRequiresAbilities.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reqAgentRequiresAbilities in body.
     */
    @GetMapping("/req-agent-requires-abilities")
    public List<ReqAgentRequiresAbility> getAllReqAgentRequiresAbilities() {
        log.debug("REST request to get all ReqAgentRequiresAbilities");
        return reqAgentRequiresAbilityRepository.findAll();
    }

    /**
     * {@code GET  /req-agent-requires-abilities/:id} : get the "id" reqAgentRequiresAbility.
     *
     * @param id the id of the reqAgentRequiresAbility to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reqAgentRequiresAbility, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/req-agent-requires-abilities/{id}")
    public ResponseEntity<ReqAgentRequiresAbility> getReqAgentRequiresAbility(@PathVariable Long id) {
        log.debug("REST request to get ReqAgentRequiresAbility : {}", id);
        Optional<ReqAgentRequiresAbility> reqAgentRequiresAbility = reqAgentRequiresAbilityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reqAgentRequiresAbility);
    }

    /**
     * {@code DELETE  /req-agent-requires-abilities/:id} : delete the "id" reqAgentRequiresAbility.
     *
     * @param id the id of the reqAgentRequiresAbility to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/req-agent-requires-abilities/{id}")
    public ResponseEntity<Void> deleteReqAgentRequiresAbility(@PathVariable Long id) {
        log.debug("REST request to delete ReqAgentRequiresAbility : {}", id);
        reqAgentRequiresAbilityRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
