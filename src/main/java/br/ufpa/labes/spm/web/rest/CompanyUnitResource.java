package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.CompanyUnit;
import br.ufpa.labes.spm.repository.CompanyUnitRepository;
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

/**
 * REST controller for managing {@link br.ufpa.labes.spm.domain.CompanyUnit}.
 */
@RestController
@RequestMapping("/api")
public class CompanyUnitResource {

    private final Logger log = LoggerFactory.getLogger(CompanyUnitResource.class);

    private static final String ENTITY_NAME = "companyUnit";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompanyUnitRepository companyUnitRepository;

    public CompanyUnitResource(CompanyUnitRepository companyUnitRepository) {
        this.companyUnitRepository = companyUnitRepository;
    }

    /**
     * {@code POST  /company-units} : Create a new companyUnit.
     *
     * @param companyUnit the companyUnit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new companyUnit, or with status {@code 400 (Bad Request)} if the companyUnit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/company-units")
    public ResponseEntity<CompanyUnit> createCompanyUnit(@RequestBody CompanyUnit companyUnit) throws URISyntaxException {
        log.debug("REST request to save CompanyUnit : {}", companyUnit);
        if (companyUnit.getId() != null) {
            throw new BadRequestAlertException("A new companyUnit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompanyUnit result = companyUnitRepository.save(companyUnit);
        return ResponseEntity.created(new URI("/api/company-units/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /company-units} : Updates an existing companyUnit.
     *
     * @param companyUnit the companyUnit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated companyUnit,
     * or with status {@code 400 (Bad Request)} if the companyUnit is not valid,
     * or with status {@code 500 (Internal Server Error)} if the companyUnit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/company-units")
    public ResponseEntity<CompanyUnit> updateCompanyUnit(@RequestBody CompanyUnit companyUnit) throws URISyntaxException {
        log.debug("REST request to update CompanyUnit : {}", companyUnit);
        if (companyUnit.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompanyUnit result = companyUnitRepository.save(companyUnit);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, companyUnit.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /company-units} : get all the companyUnits.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of companyUnits in body.
     */
    @GetMapping("/company-units")
    public List<CompanyUnit> getAllCompanyUnits() {
        log.debug("REST request to get all CompanyUnits");
        return companyUnitRepository.findAll();
    }

    /**
     * {@code GET  /company-units/:id} : get the "id" companyUnit.
     *
     * @param id the id of the companyUnit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the companyUnit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/company-units/{id}")
    public ResponseEntity<CompanyUnit> getCompanyUnit(@PathVariable Long id) {
        log.debug("REST request to get CompanyUnit : {}", id);
        Optional<CompanyUnit> companyUnit = companyUnitRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(companyUnit);
    }

    /**
     * {@code DELETE  /company-units/:id} : delete the "id" companyUnit.
     *
     * @param id the id of the companyUnit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/company-units/{id}")
    public ResponseEntity<Void> deleteCompanyUnit(@PathVariable Long id) {
        log.debug("REST request to delete CompanyUnit : {}", id);
        companyUnitRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
