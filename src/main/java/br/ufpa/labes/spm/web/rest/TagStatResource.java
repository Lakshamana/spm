package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.TagStatsService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.TagStatsDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.TagStats}.
 */
@RestController
@RequestMapping("/api")
public class TagStatResource {

    private final Logger log = LoggerFactory.getLogger(TagStatResource.class);

    private static final String ENTITY_NAME = "tagStat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagStatsService tagStatsService;

    public TagStatResource(TagStatsService tagStatsService) {
        this.tagStatsService = tagStatsService;
    }

    /**
     * {@code POST  /tag-stats} : Create a new tagStat.
     *
     * @param tagStatsDTO the tagStatsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagStatsDTO, or with status {@code 400 (Bad Request)} if the tagStat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tag-stats")
    public ResponseEntity<TagStatsDTO> createTagStat(@RequestBody TagStatsDTO tagStatsDTO) throws URISyntaxException {
        log.debug("REST request to save TagStats : {}", tagStatsDTO);
        if (tagStatsDTO.getId() != null) {
            throw new BadRequestAlertException("A new tagStat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TagStatsDTO result = tagStatsService.save(tagStatsDTO);
        return ResponseEntity.created(new URI("/api/tag-stats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tag-stats} : Updates an existing tagStat.
     *
     * @param tagStatsDTO the tagStatsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagStatsDTO,
     * or with status {@code 400 (Bad Request)} if the tagStatsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagStatsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tag-stats")
    public ResponseEntity<TagStatsDTO> updateTagStat(@RequestBody TagStatsDTO tagStatsDTO) throws URISyntaxException {
        log.debug("REST request to update TagStats : {}", tagStatsDTO);
        if (tagStatsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TagStatsDTO result = tagStatsService.save(tagStatsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagStatsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tag-stats} : get all the tagStats.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tagStats in body.
     */
    @GetMapping("/tag-stats")
    public List<TagStatsDTO> getAllTagStats() {
        log.debug("REST request to get all TagStats");
        return tagStatsService.findAll();
    }

    /**
     * {@code GET  /tag-stats/:id} : get the "id" tagStat.
     *
     * @param id the id of the tagStatsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagStatsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tag-stats/{id}")
    public ResponseEntity<TagStatsDTO> getTagStat(@PathVariable Long id) {
        log.debug("REST request to get TagStats : {}", id);
        Optional<TagStatsDTO> tagStatsDTO = tagStatsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagStatsDTO);
    }

    /**
     * {@code DELETE  /tag-stats/:id} : delete the "id" tagStat.
     *
     * @param id the id of the tagStatsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tag-stats/{id}")
    public ResponseEntity<Void> deleteTagStat(@PathVariable Long id) {
        log.debug("REST request to delete TagStats : {}", id);
        tagStatsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
