package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Calendar;
import br.ufpa.labes.spm.repository.CalendarRepository;
import br.ufpa.labes.spm.service.CalendarService;
import br.ufpa.labes.spm.service.dto.CalendarDTO;
import br.ufpa.labes.spm.service.mapper.CalendarMapper;
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

/** Integration tests for the {@link CalendarResource} REST controller. */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class CalendarResourceIT {

  private static final String DEFAULT_NAME = "AAAAAAAAAA";
  private static final String UPDATED_NAME = "BBBBBBBBBB";

  @Autowired private CalendarRepository calendarRepository;

  @Autowired private CalendarMapper calendarMapper;

  @Autowired private CalendarService calendarService;

  @Autowired private MappingJackson2HttpMessageConverter jacksonMessageConverter;

  @Autowired private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

  @Autowired private ExceptionTranslator exceptionTranslator;

  @Autowired private EntityManager em;

  @Autowired private Validator validator;

  private MockMvc restCalendarMockMvc;

  private Calendar calendar;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    final CalendarResource calendarResource = new CalendarResource(calendarService);
    this.restCalendarMockMvc =
        MockMvcBuilders.standaloneSetup(calendarResource)
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
  public static Calendar createEntity(EntityManager em) {
    Calendar calendar = new Calendar().name(DEFAULT_NAME);
    return calendar;
  }
  /**
   * Create an updated entity for this test.
   *
   * <p>This is a static method, as tests for other entities might also need it, if they test an
   * entity which requires the current entity.
   */
  public static Calendar createUpdatedEntity(EntityManager em) {
    Calendar calendar = new Calendar().name(UPDATED_NAME);
    return calendar;
  }

  @BeforeEach
  public void initTest() {
    calendar = createEntity(em);
  }

  @Test
  @Transactional
  public void createCalendar() throws Exception {
    int databaseSizeBeforeCreate = calendarRepository.findAll().size();

    // Create the Calendar
    CalendarDTO calendarDTO = calendarMapper.toDto(calendar);
    restCalendarMockMvc
        .perform(
            post("/api/calendars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
        .andExpect(status().isCreated());

    // Validate the Calendar in the database
    List<Calendar> calendarList = calendarRepository.findAll();
    assertThat(calendarList).hasSize(databaseSizeBeforeCreate + 1);
    Calendar testCalendar = calendarList.get(calendarList.size() - 1);
    assertThat(testCalendar.getName()).isEqualTo(DEFAULT_NAME);
  }

  @Test
  @Transactional
  public void createCalendarWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = calendarRepository.findAll().size();

    // Create the Calendar with an existing ID
    calendar.setId(1L);
    CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

    // An entity with an existing ID cannot be created, so this API call must fail
    restCalendarMockMvc
        .perform(
            post("/api/calendars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Calendar in the database
    List<Calendar> calendarList = calendarRepository.findAll();
    assertThat(calendarList).hasSize(databaseSizeBeforeCreate);
  }

  @Test
  @Transactional
  public void getAllCalendars() throws Exception {
    // Initialize the database
    calendarRepository.saveAndFlush(calendar);

    // Get all the calendarList
    restCalendarMockMvc
        .perform(get("/api/calendars?sort=id,desc"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.[*].id").value(hasItem(calendar.getId().intValue())))
        .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
  }

  @Test
  @Transactional
  public void getCalendar() throws Exception {
    // Initialize the database
    calendarRepository.saveAndFlush(calendar);

    // Get the calendar
    restCalendarMockMvc
        .perform(get("/api/calendars/{id}", calendar.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id").value(calendar.getId().intValue()))
        .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCalendar() throws Exception {
    // Get the calendar
    restCalendarMockMvc
        .perform(get("/api/calendars/{id}", Long.MAX_VALUE))
        .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCalendar() throws Exception {
    // Initialize the database
    calendarRepository.saveAndFlush(calendar);

    int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

    // Update the calendar
    Calendar updatedCalendar = calendarRepository.findById(calendar.getId()).get();
    // Disconnect from session so that the updates on updatedCalendar are not directly saved in db
    em.detach(updatedCalendar);
    updatedCalendar.name(UPDATED_NAME);
    CalendarDTO calendarDTO = calendarMapper.toDto(updatedCalendar);

    restCalendarMockMvc
        .perform(
            put("/api/calendars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
        .andExpect(status().isOk());

    // Validate the Calendar in the database
    List<Calendar> calendarList = calendarRepository.findAll();
    assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
    Calendar testCalendar = calendarList.get(calendarList.size() - 1);
    assertThat(testCalendar.getName()).isEqualTo(UPDATED_NAME);
  }

  @Test
  @Transactional
  public void updateNonExistingCalendar() throws Exception {
    int databaseSizeBeforeUpdate = calendarRepository.findAll().size();

    // Create the Calendar
    CalendarDTO calendarDTO = calendarMapper.toDto(calendar);

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCalendarMockMvc
        .perform(
            put("/api/calendars")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(calendarDTO)))
        .andExpect(status().isBadRequest());

    // Validate the Calendar in the database
    List<Calendar> calendarList = calendarRepository.findAll();
    assertThat(calendarList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteCalendar() throws Exception {
    // Initialize the database
    calendarRepository.saveAndFlush(calendar);

    int databaseSizeBeforeDelete = calendarRepository.findAll().size();

    // Delete the calendar
    restCalendarMockMvc
        .perform(
            delete("/api/calendars/{id}", calendar.getId()).accept(TestUtil.APPLICATION_JSON_UTF8))
        .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Calendar> calendarList = calendarRepository.findAll();
    assertThat(calendarList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  public void equalsVerifier() throws Exception {
    TestUtil.equalsVerifier(Calendar.class);
    Calendar calendar1 = new Calendar();
    calendar1.setId(1L);
    Calendar calendar2 = new Calendar();
    calendar2.setId(calendar1.getId());
    assertThat(calendar1).isEqualTo(calendar2);
    calendar2.setId(2L);
    assertThat(calendar1).isNotEqualTo(calendar2);
    calendar1.setId(null);
    assertThat(calendar1).isNotEqualTo(calendar2);
  }

  @Test
  @Transactional
  public void dtoEqualsVerifier() throws Exception {
    TestUtil.equalsVerifier(CalendarDTO.class);
    CalendarDTO calendarDTO1 = new CalendarDTO();
    calendarDTO1.setId(1L);
    CalendarDTO calendarDTO2 = new CalendarDTO();
    assertThat(calendarDTO1).isNotEqualTo(calendarDTO2);
    calendarDTO2.setId(calendarDTO1.getId());
    assertThat(calendarDTO1).isEqualTo(calendarDTO2);
    calendarDTO2.setId(2L);
    assertThat(calendarDTO1).isNotEqualTo(calendarDTO2);
    calendarDTO1.setId(null);
    assertThat(calendarDTO1).isNotEqualTo(calendarDTO2);
  }

  @Test
  @Transactional
  public void testEntityFromId() {
    assertThat(calendarMapper.fromId(42L).getId()).isEqualTo(42);
    assertThat(calendarMapper.fromId(null)).isNull();
  }
}
