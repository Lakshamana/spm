package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ReqAgentRequiresAbilityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ReqAgentRequiresAbilityDTO;

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

    private final ReqAgentRequiresAbilityService reqAgentRequiresAbilityService;

    public ReqAgentRequiresAbilityResource(ReqAgentRequiresAbilityService reqAgentRequiresAbilityService) {
        this.reqAgentRequiresAbilityService = reqAgentRequiresAbilityService;
    }

    /**
     * {@code POST  /req-agent-requires-abilities} : Create a new reqAgentRequiresAbility.
     *
     * @param reqAgentRequiresAbilityDTO the reqAgentRequiresAbilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reqAgentRequiresAbilityDTO, or with status {@code 400 (Bad Request)} if the reqAgentRequiresAbility has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/req-agent-requires-abilities")
    public ResponseEntity<ReqAgentRequiresAbilityDTO> createReqAgentRequiresAbility(@RequestBody ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO) throws URISyntaxException {
        log.debug("REST request to save ReqAgentRequiresAbility : {}", reqAgentRequiresAbilityDTO);
        if (reqAgentRequiresAbilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new reqAgentRequiresAbility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReqAgentRequiresAbilityDTO result = reqAgentRequiresAbilityService.save(reqAgentRequiresAbilityDTO);
        return ResponseEntity.created(new URI("/api/req-agent-requires-abilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /req-agent-requires-abilities} : Updates an existing reqAgentRequiresAbility.
     *
     * @param reqAgentRequiresAbilityDTO the reqAgentRequiresAbilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reqAgentRequiresAbilityDTO,
     * or with status {@code 400 (Bad Request)} if the reqAgentRequiresAbilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reqAgentRequiresAbilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/req-agent-requires-abilities")
    public ResponseEntity<ReqAgentRequiresAbilityDTO> updateReqAgentRequiresAbility(@RequestBody ReqAgentRequiresAbilityDTO reqAgentRequiresAbilityDTO) throws URISyntaxException {
        log.debug("REST request to update ReqAgentRequiresAbility : {}", reqAgentRequiresAbilityDTO);
        if (reqAgentRequiresAbilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReqAgentRequiresAbilityDTO result = reqAgentRequiresAbilityService.save(reqAgentRequiresAbilityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reqAgentRequiresAbilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /req-agent-requires-abilities} : get all the reqAgentRequiresAbilities.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reqAgentRequiresAbilities in body.
     */
    @GetMapping("/req-agent-requires-abilities")
    public List<ReqAgentRequiresAbilityDTO> getAllReqAgentRequiresAbilities() {
        log.debug("REST request to get all ReqAgentRequiresAbilities");
        return reqAgentRequiresAbilityService.findAll();
    }

    /**
     * {@code GET  /req-agent-requires-abilities/:id} : get the "id" reqAgentRequiresAbility.
     *
     * @param id the id of the reqAgentRequiresAbilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reqAgentRequiresAbilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/req-agent-requires-abilities/{id}")
    public ResponseEntity<ReqAgentRequiresAbilityDTO> getReqAgentRequiresAbility(@PathVariable Long id) {
        log.debug("REST request to get ReqAgentRequiresAbility : {}", id);
        Optional<ReqAgentRequiresAbilityDTO> reqAgentRequiresAbilityDTO = reqAgentRequiresAbilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reqAgentRequiresAbilityDTO);
    }

    /**
     * {@code DELETE  /req-agent-requires-abilities/:id} : delete the "id" reqAgentRequiresAbility.
     *
     * @param id the id of the reqAgentRequiresAbilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/req-agent-requires-abilities/{id}")
    public ResponseEntity<Void> deleteReqAgentRequiresAbility(@PathVariable Long id) {
        log.debug("REST request to delete ReqAgentRequiresAbility : {}", id);
        reqAgentRequiresAbilityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
