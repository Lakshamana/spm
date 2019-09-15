package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.AutomaticActivityService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.AutomaticActivityDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.AutomaticActivity}.
 */
@RestController
@RequestMapping("/api")
public class AutomaticActivityResource {

    private final Logger log = LoggerFactory.getLogger(AutomaticActivityResource.class);

    private static final String ENTITY_NAME = "automaticActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutomaticActivityService automaticActivityService;

    public AutomaticActivityResource(AutomaticActivityService automaticActivityService) {
        this.automaticActivityService = automaticActivityService;
    }

    /**
     * {@code POST  /automatic-activities} : Create a new automaticActivity.
     *
     * @param automaticActivityDTO the automaticActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new automaticActivityDTO, or with status {@code 400 (Bad Request)} if the automaticActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/automatic-activities")
    public ResponseEntity<AutomaticActivityDTO> createAutomaticActivity(@RequestBody AutomaticActivityDTO automaticActivityDTO) throws URISyntaxException {
        log.debug("REST request to save AutomaticActivity : {}", automaticActivityDTO);
        if (automaticActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new automaticActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutomaticActivityDTO result = automaticActivityService.save(automaticActivityDTO);
        return ResponseEntity.created(new URI("/api/automatic-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /automatic-activities} : Updates an existing automaticActivity.
     *
     * @param automaticActivityDTO the automaticActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated automaticActivityDTO,
     * or with status {@code 400 (Bad Request)} if the automaticActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the automaticActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/automatic-activities")
    public ResponseEntity<AutomaticActivityDTO> updateAutomaticActivity(@RequestBody AutomaticActivityDTO automaticActivityDTO) throws URISyntaxException {
        log.debug("REST request to update AutomaticActivity : {}", automaticActivityDTO);
        if (automaticActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AutomaticActivityDTO result = automaticActivityService.save(automaticActivityDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, automaticActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /automatic-activities} : get all the automaticActivities.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of automaticActivities in body.
     */
    @GetMapping("/automatic-activities")
    public List<AutomaticActivityDTO> getAllAutomaticActivities(@RequestParam(required = false) String filter) {
        if ("theautomatic-is-null".equals(filter)) {
            log.debug("REST request to get all AutomaticActivitys where theAutomatic is null");
            return automaticActivityService.findAllWhereTheAutomaticIsNull();
        }
        log.debug("REST request to get all AutomaticActivities");
        return automaticActivityService.findAll();
    }

    /**
     * {@code GET  /automatic-activities/:id} : get the "id" automaticActivity.
     *
     * @param id the id of the automaticActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the automaticActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/automatic-activities/{id}")
    public ResponseEntity<AutomaticActivityDTO> getAutomaticActivity(@PathVariable Long id) {
        log.debug("REST request to get AutomaticActivity : {}", id);
        Optional<AutomaticActivityDTO> automaticActivityDTO = automaticActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(automaticActivityDTO);
    }

    /**
     * {@code DELETE  /automatic-activities/:id} : delete the "id" automaticActivity.
     *
     * @param id the id of the automaticActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/automatic-activities/{id}")
    public ResponseEntity<Void> deleteAutomaticActivity(@PathVariable Long id) {
        log.debug("REST request to delete AutomaticActivity : {}", id);
        automaticActivityService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
