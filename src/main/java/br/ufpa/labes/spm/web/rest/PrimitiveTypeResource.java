package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.PrimitiveType;
import br.ufpa.labes.spm.repository.PrimitiveTypeRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.PrimitiveType}.
 */
@RestController
@RequestMapping("/api")
public class PrimitiveTypeResource {

    private final Logger log = LoggerFactory.getLogger(PrimitiveTypeResource.class);

    private static final String ENTITY_NAME = "primitiveType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrimitiveTypeRepository primitiveTypeRepository;

    public PrimitiveTypeResource(PrimitiveTypeRepository primitiveTypeRepository) {
        this.primitiveTypeRepository = primitiveTypeRepository;
    }

    /**
     * {@code POST  /primitive-types} : Create a new primitiveType.
     *
     * @param primitiveType the primitiveType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new primitiveType, or with status {@code 400 (Bad Request)} if the primitiveType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/primitive-types")
    public ResponseEntity<PrimitiveType> createPrimitiveType(@RequestBody PrimitiveType primitiveType) throws URISyntaxException {
        log.debug("REST request to save PrimitiveType : {}", primitiveType);
        if (primitiveType.getId() != null) {
            throw new BadRequestAlertException("A new primitiveType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrimitiveType result = primitiveTypeRepository.save(primitiveType);
        return ResponseEntity.created(new URI("/api/primitive-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /primitive-types} : Updates an existing primitiveType.
     *
     * @param primitiveType the primitiveType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated primitiveType,
     * or with status {@code 400 (Bad Request)} if the primitiveType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the primitiveType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/primitive-types")
    public ResponseEntity<PrimitiveType> updatePrimitiveType(@RequestBody PrimitiveType primitiveType) throws URISyntaxException {
        log.debug("REST request to update PrimitiveType : {}", primitiveType);
        if (primitiveType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrimitiveType result = primitiveTypeRepository.save(primitiveType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, primitiveType.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /primitive-types} : get all the primitiveTypes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of primitiveTypes in body.
     */
    @GetMapping("/primitive-types")
    public List<PrimitiveType> getAllPrimitiveTypes() {
        log.debug("REST request to get all PrimitiveTypes");
        return primitiveTypeRepository.findAll();
    }

    /**
     * {@code GET  /primitive-types/:id} : get the "id" primitiveType.
     *
     * @param id the id of the primitiveType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the primitiveType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/primitive-types/{id}")
    public ResponseEntity<PrimitiveType> getPrimitiveType(@PathVariable Long id) {
        log.debug("REST request to get PrimitiveType : {}", id);
        Optional<PrimitiveType> primitiveType = primitiveTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(primitiveType);
    }

    /**
     * {@code DELETE  /primitive-types/:id} : delete the "id" primitiveType.
     *
     * @param id the id of the primitiveType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/primitive-types/{id}")
    public ResponseEntity<Void> deletePrimitiveType(@PathVariable Long id) {
        log.debug("REST request to delete PrimitiveType : {}", id);
        primitiveTypeRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
