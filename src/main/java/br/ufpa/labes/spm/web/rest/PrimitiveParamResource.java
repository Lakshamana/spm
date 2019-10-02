package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.PrimitiveParam;
import br.ufpa.labes.spm.repository.PrimitiveParamRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.PrimitiveParam}.
 */
@RestController
@RequestMapping("/api")
public class PrimitiveParamResource {

    private final Logger log = LoggerFactory.getLogger(PrimitiveParamResource.class);

    private static final String ENTITY_NAME = "primitiveParam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrimitiveParamRepository primitiveParamRepository;

    public PrimitiveParamResource(PrimitiveParamRepository primitiveParamRepository) {
        this.primitiveParamRepository = primitiveParamRepository;
    }

    /**
     * {@code POST  /primitive-params} : Create a new primitiveParam.
     *
     * @param primitiveParam the primitiveParam to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new primitiveParam, or with status {@code 400 (Bad Request)} if the primitiveParam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/primitive-params")
    public ResponseEntity<PrimitiveParam> createPrimitiveParam(@RequestBody PrimitiveParam primitiveParam) throws URISyntaxException {
        log.debug("REST request to save PrimitiveParam : {}", primitiveParam);
        if (primitiveParam.getId() != null) {
            throw new BadRequestAlertException("A new primitiveParam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrimitiveParam result = primitiveParamRepository.save(primitiveParam);
        return ResponseEntity.created(new URI("/api/primitive-params/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /primitive-params} : Updates an existing primitiveParam.
     *
     * @param primitiveParam the primitiveParam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated primitiveParam,
     * or with status {@code 400 (Bad Request)} if the primitiveParam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the primitiveParam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/primitive-params")
    public ResponseEntity<PrimitiveParam> updatePrimitiveParam(@RequestBody PrimitiveParam primitiveParam) throws URISyntaxException {
        log.debug("REST request to update PrimitiveParam : {}", primitiveParam);
        if (primitiveParam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PrimitiveParam result = primitiveParamRepository.save(primitiveParam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, primitiveParam.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /primitive-params} : get all the primitiveParams.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of primitiveParams in body.
     */
    @GetMapping("/primitive-params")
    public List<PrimitiveParam> getAllPrimitiveParams() {
        log.debug("REST request to get all PrimitiveParams");
        return primitiveParamRepository.findAll();
    }

    /**
     * {@code GET  /primitive-params/:id} : get the "id" primitiveParam.
     *
     * @param id the id of the primitiveParam to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the primitiveParam, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/primitive-params/{id}")
    public ResponseEntity<PrimitiveParam> getPrimitiveParam(@PathVariable Long id) {
        log.debug("REST request to get PrimitiveParam : {}", id);
        Optional<PrimitiveParam> primitiveParam = primitiveParamRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(primitiveParam);
    }

    /**
     * {@code DELETE  /primitive-params/:id} : delete the "id" primitiveParam.
     *
     * @param id the id of the primitiveParam to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/primitive-params/{id}")
    public ResponseEntity<Void> deletePrimitiveParam(@PathVariable Long id) {
        log.debug("REST request to delete PrimitiveParam : {}", id);
        primitiveParamRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
