package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AbilityTypeService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AbilityTypeDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AbilityType}.
 */
@RestController
@RequestMapping("/api")
public class AbilityTypeResource {

    private final Logger log = LoggerFactory.getLogger(AbilityTypeResource.class);

    private static final String ENTITY_NAME = "abilityType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AbilityTypeService abilityTypeService;

    public AbilityTypeResource(AbilityTypeService abilityTypeService) {
        this.abilityTypeService = abilityTypeService;
    }

    /**
     * {@code POST  /ability-types} : Create a new abilityType.
     *
     * @param abilityTypeDTO the abilityTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new abilityTypeDTO, or with status {@code 400 (Bad Request)} if the abilityType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ability-types")
    public ResponseEntity<AbilityTypeDTO> createAbilityType(@RequestBody AbilityTypeDTO abilityTypeDTO) throws URISyntaxException {
        log.debug("REST request to save AbilityType : {}", abilityTypeDTO);
        if (abilityTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new abilityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AbilityTypeDTO result = abilityTypeService.save(abilityTypeDTO);
        return ResponseEntity.created(new URI("/api/ability-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ability-types} : Updates an existing abilityType.
     *
     * @param abilityTypeDTO the abilityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated abilityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the abilityTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the abilityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ability-types")
    public ResponseEntity<AbilityTypeDTO> updateAbilityType(@RequestBody AbilityTypeDTO abilityTypeDTO) throws URISyntaxException {
        log.debug("REST request to update AbilityType : {}", abilityTypeDTO);
        if (abilityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AbilityTypeDTO result = abilityTypeService.save(abilityTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, abilityTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ability-types} : get all the abilityTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of abilityTypes in body.
     */
    @GetMapping("/ability-types")
    public List<AbilityTypeDTO> getAllAbilityTypes() {
        log.debug("REST request to get all AbilityTypes");
        return abilityTypeService.findAll();
    }

    /**
     * {@code GET  /ability-types/:id} : get the "id" abilityType.
     *
     * @param id the id of the abilityTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the abilityTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ability-types/{id}")
    public ResponseEntity<AbilityTypeDTO> getAbilityType(@PathVariable Long id) {
        log.debug("REST request to get AbilityType : {}", id);
        Optional<AbilityTypeDTO> abilityTypeDTO = abilityTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(abilityTypeDTO);
    }

    /**
     * {@code DELETE  /ability-types/:id} : delete the "id" abilityType.
     *
     * @param id the id of the abilityTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ability-types/{id}")
    public ResponseEntity<Void> deleteAbilityType(@PathVariable Long id) {
        log.debug("REST request to delete AbilityType : {}", id);
        abilityTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
