package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.SubroutineService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.SubroutineDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Subroutine}.
 */
@RestController
@RequestMapping("/api")
public class SubroutineResource {

    private final Logger log = LoggerFactory.getLogger(SubroutineResource.class);

    private static final String ENTITY_NAME = "subroutine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubroutineService subroutineService;

    public SubroutineResource(SubroutineService subroutineService) {
        this.subroutineService = subroutineService;
    }

    /**
     * {@code POST  /subroutines} : Create a new subroutine.
     *
     * @param subroutineDTO the subroutineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subroutineDTO, or with status {@code 400 (Bad Request)} if the subroutine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subroutines")
    public ResponseEntity<SubroutineDTO> createSubroutine(@RequestBody SubroutineDTO subroutineDTO) throws URISyntaxException {
        log.debug("REST request to save Subroutine : {}", subroutineDTO);
        if (subroutineDTO.getId() != null) {
            throw new BadRequestAlertException("A new subroutine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubroutineDTO result = subroutineService.save(subroutineDTO);
        return ResponseEntity.created(new URI("/api/subroutines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /subroutines} : Updates an existing subroutine.
     *
     * @param subroutineDTO the subroutineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subroutineDTO,
     * or with status {@code 400 (Bad Request)} if the subroutineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subroutineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subroutines")
    public ResponseEntity<SubroutineDTO> updateSubroutine(@RequestBody SubroutineDTO subroutineDTO) throws URISyntaxException {
        log.debug("REST request to update Subroutine : {}", subroutineDTO);
        if (subroutineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SubroutineDTO result = subroutineService.save(subroutineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, subroutineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /subroutines} : get all the subroutines.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subroutines in body.
     */
    @GetMapping("/subroutines")
    public List<SubroutineDTO> getAllSubroutines(@RequestParam(required = false) String filter) {
        if ("theautomatic-is-null".equals(filter)) {
            log.debug("REST request to get all Subroutines where theAutomatic is null");
            return subroutineService.findAllWhereTheAutomaticIsNull();
        }
        log.debug("REST request to get all Subroutines");
        return subroutineService.findAll();
    }

    /**
     * {@code GET  /subroutines/:id} : get the "id" subroutine.
     *
     * @param id the id of the subroutineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subroutineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subroutines/{id}")
    public ResponseEntity<SubroutineDTO> getSubroutine(@PathVariable Long id) {
        log.debug("REST request to get Subroutine : {}", id);
        Optional<SubroutineDTO> subroutineDTO = subroutineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subroutineDTO);
    }

    /**
     * {@code DELETE  /subroutines/:id} : delete the "id" subroutine.
     *
     * @param id the id of the subroutineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subroutines/{id}")
    public ResponseEntity<Void> deleteSubroutine(@PathVariable Long id) {
        log.debug("REST request to delete Subroutine : {}", id);
        subroutineService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
