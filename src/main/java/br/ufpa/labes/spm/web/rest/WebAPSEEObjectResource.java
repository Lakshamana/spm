package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.WebAPSEEObjectService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.WebAPSEEObjectDTO;

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

    private final WebAPSEEObjectService webAPSEEObjectService;

    public WebAPSEEObjectResource(WebAPSEEObjectService webAPSEEObjectService) {
        this.webAPSEEObjectService = webAPSEEObjectService;
    }

    /**
     * {@code POST  /web-apsee-objects} : Create a new webAPSEEObject.
     *
     * @param webAPSEEObjectDTO the webAPSEEObjectDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webAPSEEObjectDTO, or with status {@code 400 (Bad Request)} if the webAPSEEObject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/web-apsee-objects")
    public ResponseEntity<WebAPSEEObjectDTO> createWebAPSEEObject(@Valid @RequestBody WebAPSEEObjectDTO webAPSEEObjectDTO) throws URISyntaxException {
        log.debug("REST request to save WebAPSEEObject : {}", webAPSEEObjectDTO);
        if (webAPSEEObjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new webAPSEEObject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebAPSEEObjectDTO result = webAPSEEObjectService.save(webAPSEEObjectDTO);
        return ResponseEntity.created(new URI("/api/web-apsee-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /web-apsee-objects} : Updates an existing webAPSEEObject.
     *
     * @param webAPSEEObjectDTO the webAPSEEObjectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webAPSEEObjectDTO,
     * or with status {@code 400 (Bad Request)} if the webAPSEEObjectDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webAPSEEObjectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/web-apsee-objects")
    public ResponseEntity<WebAPSEEObjectDTO> updateWebAPSEEObject(@Valid @RequestBody WebAPSEEObjectDTO webAPSEEObjectDTO) throws URISyntaxException {
        log.debug("REST request to update WebAPSEEObject : {}", webAPSEEObjectDTO);
        if (webAPSEEObjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WebAPSEEObjectDTO result = webAPSEEObjectService.save(webAPSEEObjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webAPSEEObjectDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /web-apsee-objects} : get all the webAPSEEObjects.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webAPSEEObjects in body.
     */
    @GetMapping("/web-apsee-objects")
    public List<WebAPSEEObjectDTO> getAllWebAPSEEObjects() {
        log.debug("REST request to get all WebAPSEEObjects");
        return webAPSEEObjectService.findAll();
    }

    /**
     * {@code GET  /web-apsee-objects/:id} : get the "id" webAPSEEObject.
     *
     * @param id the id of the webAPSEEObjectDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webAPSEEObjectDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/web-apsee-objects/{id}")
    public ResponseEntity<WebAPSEEObjectDTO> getWebAPSEEObject(@PathVariable Long id) {
        log.debug("REST request to get WebAPSEEObject : {}", id);
        Optional<WebAPSEEObjectDTO> webAPSEEObjectDTO = webAPSEEObjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(webAPSEEObjectDTO);
    }

    /**
     * {@code DELETE  /web-apsee-objects/:id} : delete the "id" webAPSEEObject.
     *
     * @param id the id of the webAPSEEObjectDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/web-apsee-objects/{id}")
    public ResponseEntity<Void> deleteWebAPSEEObject(@PathVariable Long id) {
        log.debug("REST request to delete WebAPSEEObject : {}", id);
        webAPSEEObjectService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
