package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.ClassMethodCall;
import br.ufpa.labes.spm.repository.ClassMethodCallRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.ClassMethodCall}. */
@RestController
@RequestMapping("/api")
public class ClassMethodCallResource {

  private final Logger log = LoggerFactory.getLogger(ClassMethodCallResource.class);

  private static final String ENTITY_NAME = "classMethodCall";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final ClassMethodCallRepository classMethodCallRepository;

  public ClassMethodCallResource(ClassMethodCallRepository classMethodCallRepository) {
    this.classMethodCallRepository = classMethodCallRepository;
  }

  /**
   * {@code POST /class-method-calls} : Create a new classMethodCall.
   *
   * @param classMethodCall the classMethodCall to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     classMethodCall, or with status {@code 400 (Bad Request)} if the classMethodCall has
   *     already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/class-method-calls")
  public ResponseEntity<ClassMethodCall> createClassMethodCall(
      @RequestBody ClassMethodCall classMethodCall) throws URISyntaxException {
    log.debug("REST request to save ClassMethodCall : {}", classMethodCall);
    if (classMethodCall.getId() != null) {
      throw new BadRequestAlertException(
          "A new classMethodCall cannot already have an ID", ENTITY_NAME, "idexists");
    }
    ClassMethodCall result = classMethodCallRepository.save(classMethodCall);
    return ResponseEntity.created(new URI("/api/class-method-calls/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /class-method-calls} : Updates an existing classMethodCall.
   *
   * @param classMethodCall the classMethodCall to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     classMethodCall, or with status {@code 400 (Bad Request)} if the classMethodCall is not
   *     valid, or with status {@code 500 (Internal Server Error)} if the classMethodCall couldn't
   *     be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/class-method-calls")
  public ResponseEntity<ClassMethodCall> updateClassMethodCall(
      @RequestBody ClassMethodCall classMethodCall) throws URISyntaxException {
    log.debug("REST request to update ClassMethodCall : {}", classMethodCall);
    if (classMethodCall.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    ClassMethodCall result = classMethodCallRepository.save(classMethodCall);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, classMethodCall.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /class-method-calls} : get all the classMethodCalls.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of
   *     classMethodCalls in body.
   */
  @GetMapping("/class-method-calls")
  public List<ClassMethodCall> getAllClassMethodCalls() {
    log.debug("REST request to get all ClassMethodCalls");
    return classMethodCallRepository.findAll();
  }

  /**
   * {@code GET /class-method-calls/:id} : get the "id" classMethodCall.
   *
   * @param id the id of the classMethodCall to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the
   *     classMethodCall, or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/class-method-calls/{id}")
  public ResponseEntity<ClassMethodCall> getClassMethodCall(@PathVariable Long id) {
    log.debug("REST request to get ClassMethodCall : {}", id);
    Optional<ClassMethodCall> classMethodCall = classMethodCallRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(classMethodCall);
  }

  /**
   * {@code DELETE /class-method-calls/:id} : delete the "id" classMethodCall.
   *
   * @param id the id of the classMethodCall to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/class-method-calls/{id}")
  public ResponseEntity<Void> deleteClassMethodCall(@PathVariable Long id) {
    log.debug("REST request to delete ClassMethodCall : {}", id);
    classMethodCallRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
