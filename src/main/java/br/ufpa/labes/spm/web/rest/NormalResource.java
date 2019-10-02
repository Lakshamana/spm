package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Normal;
import br.ufpa.labes.spm.repository.NormalRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Normal}.
 */
@RestController
@RequestMapping("/api")
public class NormalResource {

    private final Logger log = LoggerFactory.getLogger(NormalResource.class);

    private static final String ENTITY_NAME = "normal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NormalRepository normalRepository;

    public NormalResource(NormalRepository normalRepository) {
        this.normalRepository = normalRepository;
    }

    /**
     * {@code POST  /normals} : Create a new normal.
     *
     * @param normal the normal to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new normal, or with status {@code 400 (Bad Request)} if the normal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/normals")
    public ResponseEntity<Normal> createNormal(@RequestBody Normal normal) throws URISyntaxException {
        log.debug("REST request to save Normal : {}", normal);
        if (normal.getId() != null) {
            throw new BadRequestAlertException("A new normal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Normal result = normalRepository.save(normal);
        return ResponseEntity.created(new URI("/api/normals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /normals} : Updates an existing normal.
     *
     * @param normal the normal to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated normal,
     * or with status {@code 400 (Bad Request)} if the normal is not valid,
     * or with status {@code 500 (Internal Server Error)} if the normal couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/normals")
    public ResponseEntity<Normal> updateNormal(@RequestBody Normal normal) throws URISyntaxException {
        log.debug("REST request to update Normal : {}", normal);
        if (normal.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Normal result = normalRepository.save(normal);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, normal.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /normals} : get all the normals.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of normals in body.
     */
    @GetMapping("/normals")
    public List<Normal> getAllNormals(@RequestParam(required = false) String filter) {
        if ("theresourceevent-is-null".equals(filter)) {
            log.debug("REST request to get all Normals where theResourceEvent is null");
            return StreamSupport
                .stream(normalRepository.findAll().spliterator(), false)
                .filter(normal -> normal.getTheResourceEvent() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Normals");
        return normalRepository.findAll();
    }

    /**
     * {@code GET  /normals/:id} : get the "id" normal.
     *
     * @param id the id of the normal to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the normal, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/normals/{id}")
    public ResponseEntity<Normal> getNormal(@PathVariable Long id) {
        log.debug("REST request to get Normal : {}", id);
        Optional<Normal> normal = normalRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(normal);
    }

    /**
     * {@code DELETE  /normals/:id} : delete the "id" normal.
     *
     * @param id the id of the normal to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/normals/{id}")
    public ResponseEntity<Void> deleteNormal(@PathVariable Long id) {
        log.debug("REST request to delete Normal : {}", id);
        normalRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
