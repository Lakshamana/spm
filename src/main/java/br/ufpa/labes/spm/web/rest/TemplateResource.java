package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.Template;
import br.ufpa.labes.spm.repository.TemplateRepository;
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
 * REST controller for managing {@link br.ufpa.labes.spm.domain.Template}.
 */
@RestController
@RequestMapping("/api")
public class TemplateResource {

    private final Logger log = LoggerFactory.getLogger(TemplateResource.class);

    private static final String ENTITY_NAME = "template";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateRepository templateRepository;

    public TemplateResource(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /**
     * {@code POST  /templates} : Create a new template.
     *
     * @param template the template to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new template, or with status {@code 400 (Bad Request)} if the template has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/templates")
    public ResponseEntity<Template> createTemplate(@RequestBody Template template) throws URISyntaxException {
        log.debug("REST request to save Template : {}", template);
        if (template.getId() != null) {
            throw new BadRequestAlertException("A new template cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Template result = templateRepository.save(template);
        return ResponseEntity.created(new URI("/api/templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /templates} : Updates an existing template.
     *
     * @param template the template to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated template,
     * or with status {@code 400 (Bad Request)} if the template is not valid,
     * or with status {@code 500 (Internal Server Error)} if the template couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/templates")
    public ResponseEntity<Template> updateTemplate(@RequestBody Template template) throws URISyntaxException {
        log.debug("REST request to update Template : {}", template);
        if (template.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Template result = templateRepository.save(template);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, template.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /templates} : get all the templates.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templates in body.
     */
    @GetMapping("/templates")
    public List<Template> getAllTemplates() {
        log.debug("REST request to get all Templates");
        return templateRepository.findAll();
    }

    /**
     * {@code GET  /templates/:id} : get the "id" template.
     *
     * @param id the id of the template to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the template, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/templates/{id}")
    public ResponseEntity<Template> getTemplate(@PathVariable Long id) {
        log.debug("REST request to get Template : {}", id);
        Optional<Template> template = templateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(template);
    }

    /**
     * {@code DELETE  /templates/:id} : delete the "id" template.
     *
     * @param id the id of the template to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/templates/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        log.debug("REST request to delete Template : {}", id);
        templateRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
