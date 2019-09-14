package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ReqWorkGroupService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ReqWorkGroupDTO;

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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.ReqWorkGroup}.
 */
@RestController
@RequestMapping("/api")
public class ReqWorkGroupResource {

    private final Logger log = LoggerFactory.getLogger(ReqWorkGroupResource.class);

    private static final String ENTITY_NAME = "reqWorkGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReqWorkGroupService reqWorkGroupService;

    public ReqWorkGroupResource(ReqWorkGroupService reqWorkGroupService) {
        this.reqWorkGroupService = reqWorkGroupService;
    }

    /**
     * {@code POST  /req-work-groups} : Create a new reqWorkGroup.
     *
     * @param reqWorkGroupDTO the reqWorkGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reqWorkGroupDTO, or with status {@code 400 (Bad Request)} if the reqWorkGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/req-work-groups")
    public ResponseEntity<ReqWorkGroupDTO> createReqWorkGroup(@RequestBody ReqWorkGroupDTO reqWorkGroupDTO) throws URISyntaxException {
        log.debug("REST request to save ReqWorkGroup : {}", reqWorkGroupDTO);
        if (reqWorkGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new reqWorkGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReqWorkGroupDTO result = reqWorkGroupService.save(reqWorkGroupDTO);
        return ResponseEntity.created(new URI("/api/req-work-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /req-work-groups} : Updates an existing reqWorkGroup.
     *
     * @param reqWorkGroupDTO the reqWorkGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reqWorkGroupDTO,
     * or with status {@code 400 (Bad Request)} if the reqWorkGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reqWorkGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/req-work-groups")
    public ResponseEntity<ReqWorkGroupDTO> updateReqWorkGroup(@RequestBody ReqWorkGroupDTO reqWorkGroupDTO) throws URISyntaxException {
        log.debug("REST request to update ReqWorkGroup : {}", reqWorkGroupDTO);
        if (reqWorkGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReqWorkGroupDTO result = reqWorkGroupService.save(reqWorkGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reqWorkGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /req-work-groups} : get all the reqWorkGroups.
     *

     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reqWorkGroups in body.
     */
    @GetMapping("/req-work-groups")
    public List<ReqWorkGroupDTO> getAllReqWorkGroups(@RequestParam(required = false) String filter) {
        if ("therequiredpeoplesuper-is-null".equals(filter)) {
            log.debug("REST request to get all ReqWorkGroups where theRequiredPeopleSuper is null");
            return reqWorkGroupService.findAllWhereTheRequiredPeopleSuperIsNull();
        }
        log.debug("REST request to get all ReqWorkGroups");
        return reqWorkGroupService.findAll();
    }

    /**
     * {@code GET  /req-work-groups/:id} : get the "id" reqWorkGroup.
     *
     * @param id the id of the reqWorkGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reqWorkGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/req-work-groups/{id}")
    public ResponseEntity<ReqWorkGroupDTO> getReqWorkGroup(@PathVariable Long id) {
        log.debug("REST request to get ReqWorkGroup : {}", id);
        Optional<ReqWorkGroupDTO> reqWorkGroupDTO = reqWorkGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reqWorkGroupDTO);
    }

    /**
     * {@code DELETE  /req-work-groups/:id} : delete the "id" reqWorkGroup.
     *
     * @param id the id of the reqWorkGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/req-work-groups/{id}")
    public ResponseEntity<Void> deleteReqWorkGroup(@PathVariable Long id) {
        log.debug("REST request to delete ReqWorkGroup : {}", id);
        reqWorkGroupService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
