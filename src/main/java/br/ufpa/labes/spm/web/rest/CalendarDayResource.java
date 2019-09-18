package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.CalendarDayService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.CalendarDayDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.CalendarDay}.
 */
@RestController
@RequestMapping("/api")
public class CalendarDayResource {

    private final Logger log = LoggerFactory.getLogger(CalendarDayResource.class);

    private static final String ENTITY_NAME = "calendarDay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendarDayService calendarDayService;

    public CalendarDayResource(CalendarDayService calendarDayService) {
        this.calendarDayService = calendarDayService;
    }

    /**
     * {@code POST  /calendar-days} : Create a new calendarDay.
     *
     * @param calendarDayDTO the calendarDayDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendarDayDTO, or with status {@code 400 (Bad Request)} if the calendarDay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendar-days")
    public ResponseEntity<CalendarDayDTO> createCalendarDay(@RequestBody CalendarDayDTO calendarDayDTO) throws URISyntaxException {
        log.debug("REST request to save CalendarDay : {}", calendarDayDTO);
        if (calendarDayDTO.getId() != null) {
            throw new BadRequestAlertException("A new calendarDay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalendarDayDTO result = calendarDayService.save(calendarDayDTO);
        return ResponseEntity.created(new URI("/api/calendar-days/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendar-days} : Updates an existing calendarDay.
     *
     * @param calendarDayDTO the calendarDayDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendarDayDTO,
     * or with status {@code 400 (Bad Request)} if the calendarDayDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendarDayDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendar-days")
    public ResponseEntity<CalendarDayDTO> updateCalendarDay(@RequestBody CalendarDayDTO calendarDayDTO) throws URISyntaxException {
        log.debug("REST request to update CalendarDay : {}", calendarDayDTO);
        if (calendarDayDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CalendarDayDTO result = calendarDayService.save(calendarDayDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendarDayDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /calendar-days} : get all the calendarDays.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendarDays in body.
     */
    @GetMapping("/calendar-days")
    public List<CalendarDayDTO> getAllCalendarDays() {
        log.debug("REST request to get all CalendarDays");
        return calendarDayService.findAll();
    }

    /**
     * {@code GET  /calendar-days/:id} : get the "id" calendarDay.
     *
     * @param id the id of the calendarDayDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendarDayDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendar-days/{id}")
    public ResponseEntity<CalendarDayDTO> getCalendarDay(@PathVariable Long id) {
        log.debug("REST request to get CalendarDay : {}", id);
        Optional<CalendarDayDTO> calendarDayDTO = calendarDayService.findOne(id);
        return ResponseUtil.wrapOrNotFound(calendarDayDTO);
    }

    /**
     * {@code DELETE  /calendar-days/:id} : delete the "id" calendarDay.
     *
     * @param id the id of the calendarDayDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendar-days/{id}")
    public ResponseEntity<Void> deleteCalendarDay(@PathVariable Long id) {
        log.debug("REST request to delete CalendarDay : {}", id);
        calendarDayService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
