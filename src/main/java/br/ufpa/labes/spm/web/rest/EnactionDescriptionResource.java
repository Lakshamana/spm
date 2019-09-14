package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.EnactionDescriptionService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.EnactionDescriptionDTO;

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
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.EnactionDescription}.
 */
@RestController
@RequestMapping("/api")
public class EnactionDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(EnactionDescriptionResource.class);

    private static final String ENTITY_NAME = "enactionDescription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnactionDescriptionService enactionDescriptionService;

    public EnactionDescriptionResource(EnactionDescriptionService enactionDescriptionService) {
        this.enactionDescriptionService = enactionDescriptionService;
    }

    /**
     * {@code POST  /enaction-descriptions} : Create a new enactionDescription.
     *
     * @param enactionDescriptionDTO the enactionDescriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enactionDescriptionDTO, or with status {@code 400 (Bad Request)} if the enactionDescription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enaction-descriptions")
    public ResponseEntity<EnactionDescriptionDTO> createEnactionDescription(@RequestBody EnactionDescriptionDTO enactionDescriptionDTO) throws URISyntaxException {
        log.debug("REST request to save EnactionDescription : {}", enactionDescriptionDTO);
        if (enactionDescriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new enactionDescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnactionDescriptionDTO result = enactionDescriptionService.save(enactionDescriptionDTO);
        return ResponseEntity.created(new URI("/api/enaction-descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enaction-descriptions} : Updates an existing enactionDescription.
     *
     * @param enactionDescriptionDTO the enactionDescriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enactionDescriptionDTO,
     * or with status {@code 400 (Bad Request)} if the enactionDescriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enactionDescriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enaction-descriptions")
    public ResponseEntity<EnactionDescriptionDTO> updateEnactionDescription(@RequestBody EnactionDescriptionDTO enactionDescriptionDTO) throws URISyntaxException {
        log.debug("REST request to update EnactionDescription : {}", enactionDescriptionDTO);
        if (enactionDescriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EnactionDescriptionDTO result = enactionDescriptionService.save(enactionDescriptionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enactionDescriptionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /enaction-descriptions} : get all the enactionDescriptions.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enactionDescriptions in body.
     */
    @GetMapping("/enaction-descriptions")
    public List<EnactionDescriptionDTO> getAllEnactionDescriptions(@RequestParam(required = false) String filter) {
        if ("theplainactivity-is-null".equals(filter)) {
            log.debug("REST request to get all EnactionDescriptions where thePlainActivity is null");
            return enactionDescriptionService.findAllWhereThePlainActivityIsNull();
        }
        log.debug("REST request to get all EnactionDescriptions");
        return enactionDescriptionService.findAll();
    }

    /**
     * {@code GET  /enaction-descriptions/:id} : get the "id" enactionDescription.
     *
     * @param id the id of the enactionDescriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enactionDescriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enaction-descriptions/{id}")
    public ResponseEntity<EnactionDescriptionDTO> getEnactionDescription(@PathVariable Long id) {
        log.debug("REST request to get EnactionDescription : {}", id);
        Optional<EnactionDescriptionDTO> enactionDescriptionDTO = enactionDescriptionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enactionDescriptionDTO);
    }

    /**
     * {@code DELETE  /enaction-descriptions/:id} : delete the "id" enactionDescription.
     *
     * @param id the id of the enactionDescriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enaction-descriptions/{id}")
    public ResponseEntity<Void> deleteEnactionDescription(@PathVariable Long id) {
        log.debug("REST request to delete EnactionDescription : {}", id);
        enactionDescriptionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
