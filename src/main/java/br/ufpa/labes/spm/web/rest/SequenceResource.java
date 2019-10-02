package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Sequence;
import br.ufpa.labes.spm.repository.SequenceRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Sequence}.
 */
@RestController
@RequestMapping("/api")
public class SequenceResource {

    private final Logger log = LoggerFactory.getLogger(SequenceResource.class);

    private static final String ENTITY_NAME = "sequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SequenceRepository sequenceRepository;

    public SequenceResource(SequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    /**
     * {@code POST  /sequences} : Create a new sequence.
     *
     * @param sequence the sequence to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sequence, or with status {@code 400 (Bad Request)} if the sequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sequences")
    public ResponseEntity<Sequence> createSequence(@RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to save Sequence : {}", sequence);
        if (sequence.getId() != null) {
            throw new BadRequestAlertException("A new sequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Sequence result = sequenceRepository.save(sequence);
        return ResponseEntity.created(new URI("/api/sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sequences} : Updates an existing sequence.
     *
     * @param sequence the sequence to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sequence,
     * or with status {@code 400 (Bad Request)} if the sequence is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sequence couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sequences")
    public ResponseEntity<Sequence> updateSequence(@RequestBody Sequence sequence) throws URISyntaxException {
        log.debug("REST request to update Sequence : {}", sequence);
        if (sequence.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Sequence result = sequenceRepository.save(sequence);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sequence.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sequences} : get all the sequences.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sequences in body.
     */
    @GetMapping("/sequences")
    public List<Sequence> getAllSequences(@RequestParam(required = false) String filter) {
        if ("thedependency-is-null".equals(filter)) {
            log.debug("REST request to get all Sequences where theDependency is null");
            return StreamSupport
                .stream(sequenceRepository.findAll().spliterator(), false)
                .filter(sequence -> sequence.getTheDependency() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Sequences");
        return sequenceRepository.findAll();
    }

    /**
     * {@code GET  /sequences/:id} : get the "id" sequence.
     *
     * @param id the id of the sequence to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sequence, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sequences/{id}")
    public ResponseEntity<Sequence> getSequence(@PathVariable Long id) {
        log.debug("REST request to get Sequence : {}", id);
        Optional<Sequence> sequence = sequenceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sequence);
    }

    /**
     * {@code DELETE  /sequences/:id} : delete the "id" sequence.
     *
     * @param id the id of the sequence to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sequences/{id}")
    public ResponseEntity<Void> deleteSequence(@PathVariable Long id) {
        log.debug("REST request to delete Sequence : {}", id);
        sequenceRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
