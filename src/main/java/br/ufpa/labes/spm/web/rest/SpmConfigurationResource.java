package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.SpmConfigurationService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.SpmConfigurationDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.SpmConfiguration}.
 */
@RestController
@RequestMapping("/api")
public class SpmConfigurationResource {

    private final Logger log = LoggerFactory.getLogger(SpmConfigurationResource.class);

    private static final String ENTITY_NAME = "spmConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpmConfigurationService spmConfigurationService;

    public SpmConfigurationResource(SpmConfigurationService spmConfigurationService) {
        this.spmConfigurationService = spmConfigurationService;
    }

    /**
     * {@code POST  /spm-configurations} : Create a new spmConfiguration.
     *
     * @param spmConfigurationDTO the spmConfigurationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new spmConfigurationDTO, or with status {@code 400 (Bad Request)} if the spmConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/spm-configurations")
    public ResponseEntity<SpmConfigurationDTO> createSpmConfiguration(@RequestBody SpmConfigurationDTO spmConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to save SpmConfiguration : {}", spmConfigurationDTO);
        if (spmConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new spmConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpmConfigurationDTO result = spmConfigurationService.save(spmConfigurationDTO);
        return ResponseEntity.created(new URI("/api/spm-configurations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /spm-configurations} : Updates an existing spmConfiguration.
     *
     * @param spmConfigurationDTO the spmConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated spmConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the spmConfigurationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the spmConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/spm-configurations")
    public ResponseEntity<SpmConfigurationDTO> updateSpmConfiguration(@RequestBody SpmConfigurationDTO spmConfigurationDTO) throws URISyntaxException {
        log.debug("REST request to update SpmConfiguration : {}", spmConfigurationDTO);
        if (spmConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpmConfigurationDTO result = spmConfigurationService.save(spmConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, spmConfigurationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /spm-configurations} : get all the spmConfigurations.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of spmConfigurations in body.
     */
    @GetMapping("/spm-configurations")
    public List<SpmConfigurationDTO> getAllSpmConfigurations(@RequestParam(required = false) String filter) {
        if ("agent-is-null".equals(filter)) {
            log.debug("REST request to get all SpmConfigurations where agent is null");
            return spmConfigurationService.findAllWhereAgentIsNull();
        }
        log.debug("REST request to get all SpmConfigurations");
        return spmConfigurationService.findAll();
    }

    /**
     * {@code GET  /spm-configurations/:id} : get the "id" spmConfiguration.
     *
     * @param id the id of the spmConfigurationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the spmConfigurationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/spm-configurations/{id}")
    public ResponseEntity<SpmConfigurationDTO> getSpmConfiguration(@PathVariable Long id) {
        log.debug("REST request to get SpmConfiguration : {}", id);
        Optional<SpmConfigurationDTO> spmConfigurationDTO = spmConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(spmConfigurationDTO);
    }

    /**
     * {@code DELETE  /spm-configurations/:id} : delete the "id" spmConfiguration.
     *
     * @param id the id of the spmConfigurationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/spm-configurations/{id}")
    public ResponseEntity<Void> deleteSpmConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete SpmConfiguration : {}", id);
        spmConfigurationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
