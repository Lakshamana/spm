package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.PeopleInstSugService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.PeopleInstSugDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.PeopleInstSug}.
 */
@RestController
@RequestMapping("/api")
public class PeopleInstSugResource {

    private final Logger log = LoggerFactory.getLogger(PeopleInstSugResource.class);

    private static final String ENTITY_NAME = "peopleInstSug";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeopleInstSugService peopleInstSugService;

    public PeopleInstSugResource(PeopleInstSugService peopleInstSugService) {
        this.peopleInstSugService = peopleInstSugService;
    }

    /**
     * {@code POST  /people-inst-sugs} : Create a new peopleInstSug.
     *
     * @param peopleInstSugDTO the peopleInstSugDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peopleInstSugDTO, or with status {@code 400 (Bad Request)} if the peopleInstSug has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people-inst-sugs")
    public ResponseEntity<PeopleInstSugDTO> createPeopleInstSug(@RequestBody PeopleInstSugDTO peopleInstSugDTO) throws URISyntaxException {
        log.debug("REST request to save PeopleInstSug : {}", peopleInstSugDTO);
        if (peopleInstSugDTO.getId() != null) {
            throw new BadRequestAlertException("A new peopleInstSug cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeopleInstSugDTO result = peopleInstSugService.save(peopleInstSugDTO);
        return ResponseEntity.created(new URI("/api/people-inst-sugs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people-inst-sugs} : Updates an existing peopleInstSug.
     *
     * @param peopleInstSugDTO the peopleInstSugDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peopleInstSugDTO,
     * or with status {@code 400 (Bad Request)} if the peopleInstSugDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peopleInstSugDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people-inst-sugs")
    public ResponseEntity<PeopleInstSugDTO> updatePeopleInstSug(@RequestBody PeopleInstSugDTO peopleInstSugDTO) throws URISyntaxException {
        log.debug("REST request to update PeopleInstSug : {}", peopleInstSugDTO);
        if (peopleInstSugDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PeopleInstSugDTO result = peopleInstSugService.save(peopleInstSugDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peopleInstSugDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /people-inst-sugs} : get all the peopleInstSugs.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peopleInstSugs in body.
     */
    @GetMapping("/people-inst-sugs")
    public List<PeopleInstSugDTO> getAllPeopleInstSugs(@RequestParam(required = false) String filter) {
        if ("theinstsugsuper-is-null".equals(filter)) {
            log.debug("REST request to get all PeopleInstSugs where theInstSugSuper is null");
            return peopleInstSugService.findAllWhereTheInstSugSuperIsNull();
        }
        log.debug("REST request to get all PeopleInstSugs");
        return peopleInstSugService.findAll();
    }

    /**
     * {@code GET  /people-inst-sugs/:id} : get the "id" peopleInstSug.
     *
     * @param id the id of the peopleInstSugDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peopleInstSugDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people-inst-sugs/{id}")
    public ResponseEntity<PeopleInstSugDTO> getPeopleInstSug(@PathVariable Long id) {
        log.debug("REST request to get PeopleInstSug : {}", id);
        Optional<PeopleInstSugDTO> peopleInstSugDTO = peopleInstSugService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peopleInstSugDTO);
    }

    /**
     * {@code DELETE  /people-inst-sugs/:id} : delete the "id" peopleInstSug.
     *
     * @param id the id of the peopleInstSugDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people-inst-sugs/{id}")
    public ResponseEntity<Void> deletePeopleInstSug(@PathVariable Long id) {
        log.debug("REST request to delete PeopleInstSug : {}", id);
        peopleInstSugService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
