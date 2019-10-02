package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.NotWorkingDay;
import br.ufpa.labes.spm.repository.NotWorkingDayRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.NotWorkingDay}.
 */
@RestController
@RequestMapping("/api")
public class NotWorkingDayResource {

    private final Logger log = LoggerFactory.getLogger(NotWorkingDayResource.class);

    private static final String ENTITY_NAME = "notWorkingDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotWorkingDayRepository notWorkingDayRepository;

    public NotWorkingDayResource(NotWorkingDayRepository notWorkingDayRepository) {
        this.notWorkingDayRepository = notWorkingDayRepository;
    }

    /**
     * {@code POST  /not-working-days} : Create a new notWorkingDay.
     *
     * @param notWorkingDay the notWorkingDay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notWorkingDay, or with status {@code 400 (Bad Request)} if the notWorkingDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/not-working-days")
    public ResponseEntity<NotWorkingDay> createNotWorkingDay(@Valid @RequestBody NotWorkingDay notWorkingDay) throws URISyntaxException {
        log.debug("REST request to save NotWorkingDay : {}", notWorkingDay);
        if (notWorkingDay.getId() != null) {
            throw new BadRequestAlertException("A new notWorkingDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotWorkingDay result = notWorkingDayRepository.save(notWorkingDay);
        return ResponseEntity.created(new URI("/api/not-working-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /not-working-days} : Updates an existing notWorkingDay.
     *
     * @param notWorkingDay the notWorkingDay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notWorkingDay,
     * or with status {@code 400 (Bad Request)} if the notWorkingDay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notWorkingDay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/not-working-days")
    public ResponseEntity<NotWorkingDay> updateNotWorkingDay(@Valid @RequestBody NotWorkingDay notWorkingDay) throws URISyntaxException {
        log.debug("REST request to update NotWorkingDay : {}", notWorkingDay);
        if (notWorkingDay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NotWorkingDay result = notWorkingDayRepository.save(notWorkingDay);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notWorkingDay.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /not-working-days} : get all the notWorkingDays.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notWorkingDays in body.
     */
    @GetMapping("/not-working-days")
    public List<NotWorkingDay> getAllNotWorkingDays() {
        log.debug("REST request to get all NotWorkingDays");
        return notWorkingDayRepository.findAll();
    }

    /**
     * {@code GET  /not-working-days/:id} : get the "id" notWorkingDay.
     *
     * @param id the id of the notWorkingDay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notWorkingDay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/not-working-days/{id}")
    public ResponseEntity<NotWorkingDay> getNotWorkingDay(@PathVariable Long id) {
        log.debug("REST request to get NotWorkingDay : {}", id);
        Optional<NotWorkingDay> notWorkingDay = notWorkingDayRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(notWorkingDay);
    }

    /**
     * {@code DELETE  /not-working-days/:id} : delete the "id" notWorkingDay.
     *
     * @param id the id of the notWorkingDay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/not-working-days/{id}")
    public ResponseEntity<Void> deleteNotWorkingDay(@PathVariable Long id) {
        log.debug("REST request to delete NotWorkingDay : {}", id);
        notWorkingDayRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
