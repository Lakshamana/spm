package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.GraphicCoordinate;
import br.ufpa.labes.spm.repository.GraphicCoordinateRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.GraphicCoordinate}.
 */
@RestController
@RequestMapping("/api")
public class GraphicCoordinateResource {

    private final Logger log = LoggerFactory.getLogger(GraphicCoordinateResource.class);

    private static final String ENTITY_NAME = "graphicCoordinate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GraphicCoordinateRepository graphicCoordinateRepository;

    public GraphicCoordinateResource(GraphicCoordinateRepository graphicCoordinateRepository) {
        this.graphicCoordinateRepository = graphicCoordinateRepository;
    }

    /**
     * {@code POST  /graphic-coordinates} : Create a new graphicCoordinate.
     *
     * @param graphicCoordinate the graphicCoordinate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new graphicCoordinate, or with status {@code 400 (Bad Request)} if the graphicCoordinate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/graphic-coordinates")
    public ResponseEntity<GraphicCoordinate> createGraphicCoordinate(@RequestBody GraphicCoordinate graphicCoordinate) throws URISyntaxException {
        log.debug("REST request to save GraphicCoordinate : {}", graphicCoordinate);
        if (graphicCoordinate.getId() != null) {
            throw new BadRequestAlertException("A new graphicCoordinate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GraphicCoordinate result = graphicCoordinateRepository.save(graphicCoordinate);
        return ResponseEntity.created(new URI("/api/graphic-coordinates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /graphic-coordinates} : Updates an existing graphicCoordinate.
     *
     * @param graphicCoordinate the graphicCoordinate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated graphicCoordinate,
     * or with status {@code 400 (Bad Request)} if the graphicCoordinate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the graphicCoordinate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/graphic-coordinates")
    public ResponseEntity<GraphicCoordinate> updateGraphicCoordinate(@RequestBody GraphicCoordinate graphicCoordinate) throws URISyntaxException {
        log.debug("REST request to update GraphicCoordinate : {}", graphicCoordinate);
        if (graphicCoordinate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GraphicCoordinate result = graphicCoordinateRepository.save(graphicCoordinate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, graphicCoordinate.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /graphic-coordinates} : get all the graphicCoordinates.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of graphicCoordinates in body.
     */
    @GetMapping("/graphic-coordinates")
    public List<GraphicCoordinate> getAllGraphicCoordinates(@RequestParam(required = false) String filter) {
        if ("theobjectreference-is-null".equals(filter)) {
            log.debug("REST request to get all GraphicCoordinates where theObjectReference is null");
            return StreamSupport
                .stream(graphicCoordinateRepository.findAll().spliterator(), false)
                .filter(graphicCoordinate -> graphicCoordinate.getTheObjectReference() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all GraphicCoordinates");
        return graphicCoordinateRepository.findAll();
    }

    /**
     * {@code GET  /graphic-coordinates/:id} : get the "id" graphicCoordinate.
     *
     * @param id the id of the graphicCoordinate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the graphicCoordinate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/graphic-coordinates/{id}")
    public ResponseEntity<GraphicCoordinate> getGraphicCoordinate(@PathVariable Long id) {
        log.debug("REST request to get GraphicCoordinate : {}", id);
        Optional<GraphicCoordinate> graphicCoordinate = graphicCoordinateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(graphicCoordinate);
    }

    /**
     * {@code DELETE  /graphic-coordinates/:id} : delete the "id" graphicCoordinate.
     *
     * @param id the id of the graphicCoordinate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/graphic-coordinates/{id}")
    public ResponseEntity<Void> deleteGraphicCoordinate(@PathVariable Long id) {
        log.debug("REST request to delete GraphicCoordinate : {}", id);
        graphicCoordinateRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
