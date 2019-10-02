package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.domain.AuthorStat;
import br.ufpa.labes.spm.repository.AuthorStatRepository;
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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.AuthorStat}. */
@RestController
@RequestMapping("/api")
public class AuthorStatResource {

  private final Logger log = LoggerFactory.getLogger(AuthorStatResource.class);

  private static final String ENTITY_NAME = "authorStat";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final AuthorStatRepository authorStatRepository;

  public AuthorStatResource(AuthorStatRepository authorStatRepository) {
    this.authorStatRepository = authorStatRepository;
  }

  /**
   * {@code POST /author-stats} : Create a new authorStat.
   *
   * @param authorStat the authorStat to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     authorStat, or with status {@code 400 (Bad Request)} if the authorStat has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/author-stats")
  public ResponseEntity<AuthorStat> createAuthorStat(@RequestBody AuthorStat authorStat)
      throws URISyntaxException {
    log.debug("REST request to save AuthorStat : {}", authorStat);
    if (authorStat.getId() != null) {
      throw new BadRequestAlertException(
          "A new authorStat cannot already have an ID", ENTITY_NAME, "idexists");
    }
    AuthorStat result = authorStatRepository.save(authorStat);
    return ResponseEntity.created(new URI("/api/author-stats/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /author-stats} : Updates an existing authorStat.
   *
   * @param authorStat the authorStat to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     authorStat, or with status {@code 400 (Bad Request)} if the authorStat is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the authorStat couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/author-stats")
  public ResponseEntity<AuthorStat> updateAuthorStat(@RequestBody AuthorStat authorStat)
      throws URISyntaxException {
    log.debug("REST request to update AuthorStat : {}", authorStat);
    if (authorStat.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    AuthorStat result = authorStatRepository.save(authorStat);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, authorStat.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /author-stats} : get all the authorStats.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of authorStats in
   *     body.
   */
  @GetMapping("/author-stats")
  public List<AuthorStat> getAllAuthorStats() {
    log.debug("REST request to get all AuthorStats");
    return authorStatRepository.findAll();
  }

  /**
   * {@code GET /author-stats/:id} : get the "id" authorStat.
   *
   * @param id the id of the authorStat to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the authorStat,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/author-stats/{id}")
  public ResponseEntity<AuthorStat> getAuthorStat(@PathVariable Long id) {
    log.debug("REST request to get AuthorStat : {}", id);
    Optional<AuthorStat> authorStat = authorStatRepository.findById(id);
    return ResponseUtil.wrapOrNotFound(authorStat);
  }

  /**
   * {@code DELETE /author-stats/:id} : delete the "id" authorStat.
   *
   * @param id the id of the authorStat to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/author-stats/{id}")
  public ResponseEntity<Void> deleteAuthorStat(@PathVariable Long id) {
    log.debug("REST request to delete AuthorStat : {}", id);
    authorStatRepository.deleteById(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
