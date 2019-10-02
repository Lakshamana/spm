package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.CalendarDay;
import br.ufpa.labes.spm.repository.CalendarDayRepository;
import br.ufpa.labes.spm.service.CalendarDayService;
import br.ufpa.labes.spm.service.dto.CalendarDayDTO;
import br.ufpa.labes.spm.service.mapper.CalendarDayMapper;
import br.ufpa.labes.spm.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/** Integration tests for the {@link CalendarDayResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class CalendarDayResourceIT {

  private static final String DEFAULT_DAY = "AAAAAAAAAA";
  private static final String UPDATED_DAY = "BBBBBBBBBB";

  @Autowired private CalendarDayRepository calendarDayRepository;

  @Autowired private CalendarDayMapper calendarDayMapper;

  @Autowired private CalendarDayService calendarDayService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restCalendarDayMockMvc;

  private CalendarDay calendarDay;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final CalendarDayResource calendarDayResource = new CalendarDayResource(calendarDayService);
    this.restCalendarDayMockMvc =
        MockMvcBuilders.standaloneSetup(calendarDayResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator)
            .build();
  }

  /**
   * Create an entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static CalendarDay createEntity(EntityManager em) {
    CalendarDay calendarDay = new CalendarDay().day(DEFAULT_DAY);
    return calendarDay;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static CalendarDay createUpdatedEntity(EntityManager em) {
    CalendarDay calendarDay = new CalendarDay().day(UPDATED_DAY);
    return calendarDay;
  }

  @BeforeEach
  public void initTest() {
    calendarDay = createEntity(em);
  }

  @Test
  @Transactional
  public void createCalendarDay() throws Exception {
    int databaseSizeBeforeCreate = calendarDayRepository.findAll().size();

    // Create the CalendarDay
    CalendarDayDTO calendarDayDTO = calendarDayMapper.toDto(calendarDay);
    restCalendarDayMockMvc
        .perform(
            post("/api/calendar-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDayDTO)))
        .andExpect(status().isCreated());

    // Validate the CalendarDay in the database
    List<CalendarDay> calendarDayList = calendarDayRepository.findAll();
    assertThat(calendarDayList).hasSize(databaseSizeBeforeCreate + 1);
    CalendarDay testCalendarDay = calendarDayList.get(calendarDayList.size() - 1);
    assertThat(testCalendarDay.getDay()).isEqualTo(DEFAULT_DAY);
  }

  @Test
  @Transactional
  public void createCalendarDayWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = calendarDayRepository.findAll().size();

    // Create the CalendarDay with an existing ID
    calendarDay.setId(1L);
    CalendarDayDTO calendarDayDTO = calendarDayMapper.toDto(calendarDay);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCalendarDayMockMvc
        .perform(
            post("/api/calendar-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDayDTO)))
        .andExpect(status().isBadRequest());

    // Validate the CalendarDay in the database
    List<CalendarDay> calendarDayList = calendarDayRepository.findAll();
    assertThat(calendarDayList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllCalendarDays() throws Exception {
    // Initialize the database
    calendarDayRepository.saveAndFlush(calendarDay);

    // Get all the calendarDayList
    restCalendarDayMockMvc
        .perform(get("/api/calendar-days?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(calendarDay.getId().intValue())))
        .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())));
  }

  @Test
  @Transactional
  public void getCalendarDay() throws Exception {
    // Initialize the database
    calendarDayRepository.saveAndFlush(calendarDay);

    // Get the calendarDay
    restCalendarDayMockMvc
        .perform(get("/api/calendar-days/{id}", calendarDay.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(calendarDay.getId().intValue()))
        .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCalendarDay() throws Exception {
    // Get the calendarDay
    restCalendarDayMockMvc
        .perform(get("/api/calendar-days/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCalendarDay() throws Exception {
    // Initialize the database
    calendarDayRepository.saveAndFlush(calendarDay);

    int databaseSizeBeforeUpdate = calendarDayRepository.findAll().size();

    // Update the calendarDay
    CalendarDay updatedCalendarDay = calendarDayRepository.findById(calendarDay.getId()).get();
    // Disconnect from session so that the updates on updatedCalendarDay are not directly saved in
    // db
    em.detach(updatedCalendarDay);
    updatedCalendarDay.day(UPDATED_DAY);
    CalendarDayDTO calendarDayDTO = calendarDayMapper.toDto(updatedCalendarDay);

    restCalendarDayMockMvc
        .perform(
            put("/api/calendar-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDayDTO)))
        .andExpect(status().isOk());

    // Validate the CalendarDay in the database
    List<CalendarDay> calendarDayList = calendarDayRepository.findAll();
    assertThat(calendarDayList).hasSize(databaseSizeBeforeUpdate);
    CalendarDay testCalendarDay = calendarDayList.get(calendarDayList.size() - 1);
    assertThat(testCalendarDay.getDay()).isEqualTo(UPDATED_DAY);
  }

  @Test
  @Transactional
  public void updateNonExistingCalendarDay() throws Exception {
    int databaseSizeBeforeUpdate = calendarDayRepository.findAll().size();

    // Create the CalendarDay
    CalendarDayDTO calendarDayDTO = calendarDayMapper.toDto(calendarDay);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCalendarDayMockMvc
        .perform(
            put("/api/calendar-days")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDayDTO)))
        .andExpect(status().isBadRequest());

    // Validate the CalendarDay in the database
    List<CalendarDay> calendarDayList = calendarDayRepository.findAll();
    assertThat(calendarDayList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteCalendarDay() throws Exception {
    // Initialize the database
    calendarDayRepository.saveAndFlush(calendarDay);

    int databaseSizeBeforeDelete = calendarDayRepository.findAll().size();

    // Delete the calendarDay
    restCalendarDayMockMvc
        .perform(
            delete("/api/calendar-days/{id}", calendarDay.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<CalendarDay> calendarDayList = calendarDayRepository.findAll();
    assertThat(calendarDayList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(CalendarDay.class);
    CalendarDay calendarDay1 = new CalendarDay();
    calendarDay1.setId(1L);
    CalendarDay calendarDay2 = new CalendarDay();
    calendarDay2.setId(calendarDay1.getId());
    assertThat(calendarDay1).isEqualTo(calendarDay2);
    calendarDay2.setId(2L);
    assertThat(calendarDay1).isNotEqualTo(calendarDay2);
    calendarDay1.setId(null);
    assertThat(calendarDay1).isNotEqualTo(calendarDay2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(CalendarDayDTO.class);
    CalendarDayDTO calendarDayDTO1 = new CalendarDayDTO();
    calendarDayDTO1.setId(1L);
    CalendarDayDTO calendarDayDTO2 = new CalendarDayDTO();
    assertThat(calendarDayDTO1).isNotEqualTo(calendarDayDTO2);
    calendarDayDTO2.setId(calendarDayDTO1.getId());
    assertThat(calendarDayDTO1).isEqualTo(calendarDayDTO2);
    calendarDayDTO2.setId(2L);
    assertThat(calendarDayDTO1).isNotEqualTo(calendarDayDTO2);
    calendarDayDTO1.setId(null);
    assertThat(calendarDayDTO1).isNotEqualTo(calendarDayDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(calendarDayMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(calendarDayMapper.fromId(null)).isNull();
  }
}
