package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.service.FeedbackService;
import br.ufpa.labes.spm.web.rest.errors.BadRequestAlertException;
import br.ufpa.labes.spm.service.dto.FeedbackDTO;

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

/** REST controller for managing {@link br.ufpa.labes.spm.domain.Feedback}. */
@RestController
@RequestMapping("/api")
public class FeedbackResource {

  private final Logger log = LoggerFactory.getLogger(FeedbackResource.class);

  private static final String ENTITY_NAME = "feedback";

  @Value("${jhipster.clientApp.name}")
  private String applicationName;

  private final FeedbackService feedbackService;

  public FeedbackResource(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  /**
   * {@code POST /feedbacks} : Create a new feedback.
   *
   * @param feedbackDTO the feedbackDTO to create.
   * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new
   *     feedbackDTO, or with status {@code 400 (Bad Request)} if the feedback has already an ID.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PostMapping("/feedbacks")
  public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO)
      throws URISyntaxException {
    log.debug("REST request to save Feedback : {}", feedbackDTO);
    if (feedbackDTO.getId() != null) {
      throw new BadRequestAlertException(
          "A new feedback cannot already have an ID", ENTITY_NAME, "idexists");
    }
    FeedbackDTO result = feedbackService.save(feedbackDTO);
    return ResponseEntity.created(new URI("/api/feedbacks/" + result.getId()))
        .headers(
            HeaderUtil.createEntityCreationAlert(
                applicationName, true, ENTITY_NAME, result.getId().toString()))
        .body(result);
  }

  /**
   * {@code PUT /feedbacks} : Updates an existing feedback.
   *
   * @param feedbackDTO the feedbackDTO to update.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated
   *     feedbackDTO, or with status {@code 400 (Bad Request)} if the feedbackDTO is not valid, or
   *     with status {@code 500 (Internal Server Error)} if the feedbackDTO couldn't be updated.
   * @throws URISyntaxException if the Location URI syntax is incorrect.
   */
  @PutMapping("/feedbacks")
  public ResponseEntity<FeedbackDTO> updateFeedback(@RequestBody FeedbackDTO feedbackDTO)
      throws URISyntaxException {
    log.debug("REST request to update Feedback : {}", feedbackDTO);
    if (feedbackDTO.getId() == null) {
      throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
    }
    FeedbackDTO result = feedbackService.save(feedbackDTO);
    return ResponseEntity.ok()
        .headers(
            HeaderUtil.createEntityUpdateAlert(
                applicationName, true, ENTITY_NAME, feedbackDTO.getId().toString()))
        .body(result);
  }

  /**
   * {@code GET /feedbacks} : get all the feedbacks.
   *
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feedbacks in
   *     body.
   */
  @GetMapping("/feedbacks")
  public List<FeedbackDTO> getAllFeedbacks() {
    log.debug("REST request to get all Feedbacks");
    return feedbackService.findAll();
  }

  /**
   * {@code GET /feedbacks/:id} : get the "id" feedback.
   *
   * @param id the id of the feedbackDTO to retrieve.
   * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feedbackDTO,
   *     or with status {@code 404 (Not Found)}.
   */
  @GetMapping("/feedbacks/{id}")
  public ResponseEntity<FeedbackDTO> getFeedback(@PathVariable Long id) {
    log.debug("REST request to get Feedback : {}", id);
    Optional<FeedbackDTO> feedbackDTO = feedbackService.findOne(id);
    return ResponseUtil.wrapOrNotFound(feedbackDTO);
  }

  /**
   * {@code DELETE /feedbacks/:id} : delete the "id" feedback.
   *
   * @param id the id of the feedbackDTO to delete.
   * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
   */
  @DeleteMapping("/feedbacks/{id}")
  public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
    log.debug("REST request to delete Feedback : {}", id);
    feedbackService.delete(id);
    return ResponseEntity.noContent()
        .headers(
            HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
        .build();
  }
}
