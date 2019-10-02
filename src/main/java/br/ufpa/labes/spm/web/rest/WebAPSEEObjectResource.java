package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.WebAPSEEObject;
import br.ufpa.labes.spm.repository.WebAPSEEObjectRepository;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.WebAPSEEObject}.
 */
@RestController
@RequestMapping("/api")
public class WebAPSEEObjectResource {

    private final Logger log = LoggerFactory.getLogger(WebAPSEEObjectResource.class);

    private static final String ENTITY_NAME = "webAPSEEObject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebAPSEEObjectRepository webAPSEEObjectRepository;

    public WebAPSEEObjectResource(WebAPSEEObjectRepository webAPSEEObjectRepository) {
        this.webAPSEEObjectRepository = webAPSEEObjectRepository;
    }

    /**
     * {@code POST  /web-apsee-objects} : Create a new webAPSEEObject.
     *
     * @param webAPSEEObject the webAPSEEObject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webAPSEEObject, or with status {@code 400 (Bad Request)} if the webAPSEEObject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/web-apsee-objects")
    public ResponseEntity<WebAPSEEObject> createWebAPSEEObject(@Valid @RequestBody WebAPSEEObject webAPSEEObject) throws URISyntaxException {
        log.debug("REST request to save WebAPSEEObject : {}", webAPSEEObject);
        if (webAPSEEObject.getId() != null) {
            throw new BadRequestAlertException("A new webAPSEEObject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebAPSEEObject result = webAPSEEObjectRepository.save(webAPSEEObject);
        return ResponseEntity.created(new URI("/api/web-apsee-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /web-apsee-objects} : Updates an existing webAPSEEObject.
     *
     * @param webAPSEEObject the webAPSEEObject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webAPSEEObject,
     * or with status {@code 400 (Bad Request)} if the webAPSEEObject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webAPSEEObject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/web-apsee-objects")
    public ResponseEntity<WebAPSEEObject> updateWebAPSEEObject(@Valid @RequestBody WebAPSEEObject webAPSEEObject) throws URISyntaxException {
        log.debug("REST request to update WebAPSEEObject : {}", webAPSEEObject);
        if (webAPSEEObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WebAPSEEObject result = webAPSEEObjectRepository.save(webAPSEEObject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webAPSEEObject.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /web-apsee-objects} : get all the webAPSEEObjects.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webAPSEEObjects in body.
     */
    @GetMapping("/web-apsee-objects")
    public List<WebAPSEEObject> getAllWebAPSEEObjects() {
        log.debug("REST request to get all WebAPSEEObjects");
        return webAPSEEObjectRepository.findAll();
    }

    /**
     * {@code GET  /web-apsee-objects/:id} : get the "id" webAPSEEObject.
     *
     * @param id the id of the webAPSEEObject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webAPSEEObject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/web-apsee-objects/{id}")
    public ResponseEntity<WebAPSEEObject> getWebAPSEEObject(@PathVariable Long id) {
        log.debug("REST request to get WebAPSEEObject : {}", id);
        Optional<WebAPSEEObject> webAPSEEObject = webAPSEEObjectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(webAPSEEObject);
    }

    /**
     * {@code DELETE  /web-apsee-objects/:id} : delete the "id" webAPSEEObject.
     *
     * @param id the id of the webAPSEEObject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/web-apsee-objects/{id}")
    public ResponseEntity<Void> deleteWebAPSEEObject(@PathVariable Long id) {
        log.debug("REST request to delete WebAPSEEObject : {}", id);
        webAPSEEObjectRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
