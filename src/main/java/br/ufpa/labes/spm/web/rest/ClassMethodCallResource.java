package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.ClassMethodCallService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.ClassMethodCallDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ClassMethodCall}. */
@RestController
@RequestMapping("/api")
public class ClassMethodCallResource {

  private final Logger log = LoggerFactory.getLogger(ClassMethodCallResource.class);

  private static final String ENTITY_NAME = "classMethodCall";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ClassMethodCallService classMethodCallService;

  public ClassMethodCallResource(ClassMethodCallService classMethodCallService) {
    this.classMethodCallService = classMethodCallService;
  }

  /**
   * {@code POST /class-method-calls} : Create a new classMethodCall.
   *
   * @param classMethodCallDTO the classMethodCallDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     classMethodCallDTO, or with status {@code 400 (Bad Request)} if the classMethodCall has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/class-method-calls")
  public ResponseEntity<ClassMethodCallDTO> createClassMethodCall(
      @RequestBody ClassMethodCallDTO classMethodCallDTO) throws URISyntaxException {
    log.debug("REST request to save ClassMethodCall : {}", classMethodCallDTO);
    if (classMethodCallDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new classMethodCall cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ClassMethodCallDTO result = classMethodCallService.save(classMethodCallDTO);
    return ResponseEntity.created(new URI("/api/class-method-calls/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /class-method-calls} : Updates an existing classMethodCall.
   *
   * @param classMethodCallDTO the classMethodCallDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     classMethodCallDTO, or with status {@code 400 (Bad Request)} if the classMethodCallDTO is
   *     not valid, or with status {@code 500 (Internal Server Error)} if the classMethodCallDTO
   *     couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/class-method-calls")
  public ResponseEntity<ClassMethodCallDTO> updateClassMethodCall(
      @RequestBody ClassMethodCallDTO classMethodCallDTO) throws URISyntaxException {
    log.debug("REST request to update ClassMethodCall : {}", classMethodCallDTO);
    if (classMethodCallDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ClassMethodCallDTO result = classMethodCallService.save(classMethodCallDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, classMethodCallDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /class-method-calls} : get all the classMethodCalls.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     classMethodCalls in body.
   */
  @GetMapping("/class-method-calls")
  public List<ClassMethodCallDTO> getAllClassMethodCalls() {
    log.debug("REST request to get all ClassMethodCalls");
    return classMethodCallService.findAll();
  }

  /**
   * {@code GET /class-method-calls/:id} : get the "id" classMethodCall.
   *
   * @param id the id of the classMethodCallDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     classMethodCallDTO, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/class-method-calls/{id}")
  public ResponseEntity<ClassMethodCallDTO> getClassMethodCall(@PathVariable Long id) {
    log.debug("REST request to get ClassMethodCall : {}", id);
    Optional<ClassMethodCallDTO> classMethodCallDTO = classMethodCallService.findOne(id);
    return ResponseUtil.wrapOrNotFound(classMethodCallDTO);
  }

  /**
   * {@code DELETE /class-method-calls/:id} : delete the "id" classMethodCall.
   *
   * @param id the id of the classMethodCallDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/class-method-calls/{id}")
  public ResponseEntity<Void> deleteClassMethodCall(@PathVariable Long id) {
    log.debug("REST request to delete ClassMethodCall : {}", id);
    classMethodCallService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
