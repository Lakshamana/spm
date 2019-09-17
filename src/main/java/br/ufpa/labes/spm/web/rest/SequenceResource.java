package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.SequenceService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.SequenceDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Sequence}.
 */
@RestController
@RequestMapping("/api")
public class SequenceResource {

    private final Logger log = LoggerFactory.getLogger(SequenceResource.class);

    private static final String ENTITY_NAME = "sequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SequenceService sequenceService;

    public SequenceResource(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    /**
     * {@code POST  /sequences} : Create a new sequence.
     *
     * @param sequenceDTO the sequenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sequenceDTO, or with status {@code 400 (Bad Request)} if the sequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sequences")
    public ResponseEntity<SequenceDTO> createSequence(@RequestBody SequenceDTO sequenceDTO) throws URISyntaxException {
        log.debug("REST request to save Sequence : {}", sequenceDTO);
        if (sequenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new sequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SequenceDTO result = sequenceService.save(sequenceDTO);
        return ResponseEntity.created(new URI("/api/sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sequences} : Updates an existing sequence.
     *
     * @param sequenceDTO the sequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sequenceDTO,
     * or with status {@code 400 (Bad Request)} if the sequenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sequences")
    public ResponseEntity<SequenceDTO> updateSequence(@RequestBody SequenceDTO sequenceDTO) throws URISyntaxException {
        log.debug("REST request to update Sequence : {}", sequenceDTO);
        if (sequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SequenceDTO result = sequenceService.save(sequenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sequenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sequences} : get all the sequences.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sequences in body.
     */
    @GetMapping("/sequences")
    public List<SequenceDTO> getAllSequences() {
        log.debug("REST request to get all Sequences");
        return sequenceService.findAll();
    }

    /**
     * {@code GET  /sequences/:id} : get the "id" sequence.
     *
     * @param id the id of the sequenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sequenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sequences/{id}")
    public ResponseEntity<SequenceDTO> getSequence(@PathVariable Long id) {
        log.debug("REST request to get Sequence : {}", id);
        Optional<SequenceDTO> sequenceDTO = sequenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sequenceDTO);
    }

    /**
     * {@code DELETE  /sequences/:id} : delete the "id" sequence.
     *
     * @param id the id of the sequenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sequences/{id}")
    public ResponseEntity<Void> deleteSequence(@PathVariable Long id) {
        log.debug("REST request to delete Sequence : {}", id);
        sequenceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
