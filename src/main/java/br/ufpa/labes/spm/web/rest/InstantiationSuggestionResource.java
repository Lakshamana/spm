package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.InstantiationSuggestionService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.InstantiationSuggestionDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.InstantiationSuggestion}.
 */
@RestController
@RequestMapping("/api")
public class InstantiationSuggestionResource {

    private final Logger log = LoggerFactory.getLogger(InstantiationSuggestionResource.class);

    private static final String ENTITY_NAME = "instantiationSuggestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstantiationSuggestionService instantiationSuggestionService;

    public InstantiationSuggestionResource(InstantiationSuggestionService instantiationSuggestionService) {
        this.instantiationSuggestionService = instantiationSuggestionService;
    }

    /**
     * {@code POST  /instantiation-suggestions} : Create a new instantiationSuggestion.
     *
     * @param instantiationSuggestionDTO the instantiationSuggestionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new instantiationSuggestionDTO, or with status {@code 400 (Bad Request)} if the instantiationSuggestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/instantiation-suggestions")
    public ResponseEntity<InstantiationSuggestionDTO> createInstantiationSuggestion(@RequestBody InstantiationSuggestionDTO instantiationSuggestionDTO) throws URISyntaxException {
        log.debug("REST request to save InstantiationSuggestion : {}", instantiationSuggestionDTO);
        if (instantiationSuggestionDTO.getId() != null) {
            throw new BadRequestAlertException("A new instantiationSuggestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstantiationSuggestionDTO result = instantiationSuggestionService.save(instantiationSuggestionDTO);
        return ResponseEntity.created(new URI("/api/instantiation-suggestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /instantiation-suggestions} : Updates an existing instantiationSuggestion.
     *
     * @param instantiationSuggestionDTO the instantiationSuggestionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated instantiationSuggestionDTO,
     * or with status {@code 400 (Bad Request)} if the instantiationSuggestionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the instantiationSuggestionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/instantiation-suggestions")
    public ResponseEntity<InstantiationSuggestionDTO> updateInstantiationSuggestion(@RequestBody InstantiationSuggestionDTO instantiationSuggestionDTO) throws URISyntaxException {
        log.debug("REST request to update InstantiationSuggestion : {}", instantiationSuggestionDTO);
        if (instantiationSuggestionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InstantiationSuggestionDTO result = instantiationSuggestionService.save(instantiationSuggestionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, instantiationSuggestionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /instantiation-suggestions} : get all the instantiationSuggestions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of instantiationSuggestions in body.
     */
    @GetMapping("/instantiation-suggestions")
    public List<InstantiationSuggestionDTO> getAllInstantiationSuggestions() {
        log.debug("REST request to get all InstantiationSuggestions");
        return instantiationSuggestionService.findAll();
    }

    /**
     * {@code GET  /instantiation-suggestions/:id} : get the "id" instantiationSuggestion.
     *
     * @param id the id of the instantiationSuggestionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the instantiationSuggestionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/instantiation-suggestions/{id}")
    public ResponseEntity<InstantiationSuggestionDTO> getInstantiationSuggestion(@PathVariable Long id) {
        log.debug("REST request to get InstantiationSuggestion : {}", id);
        Optional<InstantiationSuggestionDTO> instantiationSuggestionDTO = instantiationSuggestionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(instantiationSuggestionDTO);
    }

    /**
     * {@code DELETE  /instantiation-suggestions/:id} : delete the "id" instantiationSuggestion.
     *
     * @param id the id of the instantiationSuggestionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/instantiation-suggestions/{id}")
    public ResponseEntity<Void> deleteInstantiationSuggestion(@PathVariable Long id) {
        log.debug("REST request to delete InstantiationSuggestion : {}", id);
        instantiationSuggestionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
