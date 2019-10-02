package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.EnactionDescription;
import br.ufpa.labes.spm.repository.EnactionDescriptionRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.EnactionDescription}.
 */
@RestController
@RequestMapping("/api")
public class EnactionDescriptionResource {

    private final Logger log = LoggerFactory.getLogger(EnactionDescriptionResource.class);

    private static final String ENTITY_NAME = "enactionDescription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnactionDescriptionRepository enactionDescriptionRepository;

    public EnactionDescriptionResource(EnactionDescriptionRepository enactionDescriptionRepository) {
        this.enactionDescriptionRepository = enactionDescriptionRepository;
    }

    /**
     * {@code POST  /enaction-descriptions} : Create a new enactionDescription.
     *
     * @param enactionDescription the enactionDescription to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enactionDescription, or with status {@code 400 (Bad Request)} if the enactionDescription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/enaction-descriptions")
    public ResponseEntity<EnactionDescription> createEnactionDescription(@RequestBody EnactionDescription enactionDescription) throws URISyntaxException {
        log.debug("REST request to save EnactionDescription : {}", enactionDescription);
        if (enactionDescription.getId() != null) {
            throw new BadRequestAlertException("A new enactionDescription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnactionDescription result = enactionDescriptionRepository.save(enactionDescription);
        return ResponseEntity.created(new URI("/api/enaction-descriptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /enaction-descriptions} : Updates an existing enactionDescription.
     *
     * @param enactionDescription the enactionDescription to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enactionDescription,
     * or with status {@code 400 (Bad Request)} if the enactionDescription is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enactionDescription couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/enaction-descriptions")
    public ResponseEntity<EnactionDescription> updateEnactionDescription(@RequestBody EnactionDescription enactionDescription) throws URISyntaxException {
        log.debug("REST request to update EnactionDescription : {}", enactionDescription);
        if (enactionDescription.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EnactionDescription result = enactionDescriptionRepository.save(enactionDescription);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enactionDescription.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /enaction-descriptions} : get all the enactionDescriptions.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enactionDescriptions in body.
     */
    @GetMapping("/enaction-descriptions")
    public List<EnactionDescription> getAllEnactionDescriptions(@RequestParam(required = false) String filter) {
        if ("theplain-is-null".equals(filter)) {
            log.debug("REST request to get all EnactionDescriptions where thePlain is null");
            return StreamSupport
                .stream(enactionDescriptionRepository.findAll().spliterator(), false)
                .filter(enactionDescription -> enactionDescription.getThePlain() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all EnactionDescriptions");
        return enactionDescriptionRepository.findAll();
    }

    /**
     * {@code GET  /enaction-descriptions/:id} : get the "id" enactionDescription.
     *
     * @param id the id of the enactionDescription to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enactionDescription, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/enaction-descriptions/{id}")
    public ResponseEntity<EnactionDescription> getEnactionDescription(@PathVariable Long id) {
        log.debug("REST request to get EnactionDescription : {}", id);
        Optional<EnactionDescription> enactionDescription = enactionDescriptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(enactionDescription);
    }

    /**
     * {@code DELETE  /enaction-descriptions/:id} : delete the "id" enactionDescription.
     *
     * @param id the id of the enactionDescription to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/enaction-descriptions/{id}")
    public ResponseEntity<Void> deleteEnactionDescription(@PathVariable Long id) {
        log.debug("REST request to delete EnactionDescription : {}", id);
        enactionDescriptionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
