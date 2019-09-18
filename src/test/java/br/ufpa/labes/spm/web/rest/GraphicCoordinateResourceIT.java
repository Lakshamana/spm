package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.GraphicCoordinate;
import br.ufpa.labes.spm.repository.GraphicCoordinateRepository;
import br.ufpa.labes.spm.service.GraphicCoordinateService;
import br.ufpa.labes.spm.service.dto.GraphicCoordinateDTO;
import br.ufpa.labes.spm.service.mapper.GraphicCoordinateMapper;
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

/**
 * Integration tests for the {@link GraphicCoordinateResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class GraphicCoordinateResourceIT {

    private static final Integer DEFAULT_X = 1;
    private static final Integer UPDATED_X = 2;
    private static final Integer SMALLER_X = 1 - 1;

    private static final Integer DEFAULT_Y = 1;
    private static final Integer UPDATED_Y = 2;
    private static final Integer SMALLER_Y = 1 - 1;

    private static final Boolean DEFAULT_VISIBLE = false;
    private static final Boolean UPDATED_VISIBLE = true;

    private static final String DEFAULT_THE_PROCESS = "AAAAAAAAAA";
    private static final String UPDATED_THE_PROCESS = "BBBBBBBBBB";

    @Autowired
    private GraphicCoordinateRepository graphicCoordinateRepository;

    @Autowired
    private GraphicCoordinateMapper graphicCoordinateMapper;

    @Autowired
    private GraphicCoordinateService graphicCoordinateService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restGraphicCoordinateMockMvc;

    private GraphicCoordinate graphicCoordinate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final GraphicCoordinateResource graphicCoordinateResource = new GraphicCoordinateResource(graphicCoordinateService);
        this.restGraphicCoordinateMockMvc = MockMvcBuilders.standaloneSetup(graphicCoordinateResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GraphicCoordinate createEntity(EntityManager em) {
        GraphicCoordinate graphicCoordinate = new GraphicCoordinate()
            .x(DEFAULT_X)
            .y(DEFAULT_Y)
            .visible(DEFAULT_VISIBLE)
            .theProcess(DEFAULT_THE_PROCESS);
        return graphicCoordinate;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GraphicCoordinate createUpdatedEntity(EntityManager em) {
        GraphicCoordinate graphicCoordinate = new GraphicCoordinate()
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .visible(UPDATED_VISIBLE)
            .theProcess(UPDATED_THE_PROCESS);
        return graphicCoordinate;
    }

    @BeforeEach
    public void initTest() {
        graphicCoordinate = createEntity(em);
    }

    @Test
    @Transactional
    public void createGraphicCoordinate() throws Exception {
        int databaseSizeBeforeCreate = graphicCoordinateRepository.findAll().size();

        // Create the GraphicCoordinate
        GraphicCoordinateDTO graphicCoordinateDTO = graphicCoordinateMapper.toDto(graphicCoordinate);
        restGraphicCoordinateMockMvc.perform(post("/api/graphic-coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(graphicCoordinateDTO)))
            .andExpect(status().isCreated());

        // Validate the GraphicCoordinate in the database
        List<GraphicCoordinate> graphicCoordinateList = graphicCoordinateRepository.findAll();
        assertThat(graphicCoordinateList).hasSize(databaseSizeBeforeCreate + 1);
        GraphicCoordinate testGraphicCoordinate = graphicCoordinateList.get(graphicCoordinateList.size() - 1);
        assertThat(testGraphicCoordinate.getX()).isEqualTo(DEFAULT_X);
        assertThat(testGraphicCoordinate.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testGraphicCoordinate.isVisible()).isEqualTo(DEFAULT_VISIBLE);
        assertThat(testGraphicCoordinate.getTheProcess()).isEqualTo(DEFAULT_THE_PROCESS);
    }

    @Test
    @Transactional
    public void createGraphicCoordinateWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = graphicCoordinateRepository.findAll().size();

        // Create the GraphicCoordinate with an existing ID
        graphicCoordinate.setId(1L);
        GraphicCoordinateDTO graphicCoordinateDTO = graphicCoordinateMapper.toDto(graphicCoordinate);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGraphicCoordinateMockMvc.perform(post("/api/graphic-coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(graphicCoordinateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GraphicCoordinate in the database
        List<GraphicCoordinate> graphicCoordinateList = graphicCoordinateRepository.findAll();
        assertThat(graphicCoordinateList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGraphicCoordinates() throws Exception {
        // Initialize the database
        graphicCoordinateRepository.saveAndFlush(graphicCoordinate);

        // Get all the graphicCoordinateList
        restGraphicCoordinateMockMvc.perform(get("/api/graphic-coordinates?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(graphicCoordinate.getId().intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
            .andExpect(jsonPath("$.[*].visible").value(hasItem(DEFAULT_VISIBLE.booleanValue())))
            .andExpect(jsonPath("$.[*].theProcess").value(hasItem(DEFAULT_THE_PROCESS.toString())));
    }
    
    @Test
    @Transactional
    public void getGraphicCoordinate() throws Exception {
        // Initialize the database
        graphicCoordinateRepository.saveAndFlush(graphicCoordinate);

        // Get the graphicCoordinate
        restGraphicCoordinateMockMvc.perform(get("/api/graphic-coordinates/{id}", graphicCoordinate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(graphicCoordinate.getId().intValue()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y))
            .andExpect(jsonPath("$.visible").value(DEFAULT_VISIBLE.booleanValue()))
            .andExpect(jsonPath("$.theProcess").value(DEFAULT_THE_PROCESS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGraphicCoordinate() throws Exception {
        // Get the graphicCoordinate
        restGraphicCoordinateMockMvc.perform(get("/api/graphic-coordinates/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGraphicCoordinate() throws Exception {
        // Initialize the database
        graphicCoordinateRepository.saveAndFlush(graphicCoordinate);

        int databaseSizeBeforeUpdate = graphicCoordinateRepository.findAll().size();

        // Update the graphicCoordinate
        GraphicCoordinate updatedGraphicCoordinate = graphicCoordinateRepository.findById(graphicCoordinate.getId()).get();
        // Disconnect from session so that the updates on updatedGraphicCoordinate are not directly saved in db
        em.detach(updatedGraphicCoordinate);
        updatedGraphicCoordinate
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .visible(UPDATED_VISIBLE)
            .theProcess(UPDATED_THE_PROCESS);
        GraphicCoordinateDTO graphicCoordinateDTO = graphicCoordinateMapper.toDto(updatedGraphicCoordinate);

        restGraphicCoordinateMockMvc.perform(put("/api/graphic-coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(graphicCoordinateDTO)))
            .andExpect(status().isOk());

        // Validate the GraphicCoordinate in the database
        List<GraphicCoordinate> graphicCoordinateList = graphicCoordinateRepository.findAll();
        assertThat(graphicCoordinateList).hasSize(databaseSizeBeforeUpdate);
        GraphicCoordinate testGraphicCoordinate = graphicCoordinateList.get(graphicCoordinateList.size() - 1);
        assertThat(testGraphicCoordinate.getX()).isEqualTo(UPDATED_X);
        assertThat(testGraphicCoordinate.getY()).isEqualTo(UPDATED_Y);
        assertThat(testGraphicCoordinate.isVisible()).isEqualTo(UPDATED_VISIBLE);
        assertThat(testGraphicCoordinate.getTheProcess()).isEqualTo(UPDATED_THE_PROCESS);
    }

    @Test
    @Transactional
    public void updateNonExistingGraphicCoordinate() throws Exception {
        int databaseSizeBeforeUpdate = graphicCoordinateRepository.findAll().size();

        // Create the GraphicCoordinate
        GraphicCoordinateDTO graphicCoordinateDTO = graphicCoordinateMapper.toDto(graphicCoordinate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGraphicCoordinateMockMvc.perform(put("/api/graphic-coordinates")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(graphicCoordinateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the GraphicCoordinate in the database
        List<GraphicCoordinate> graphicCoordinateList = graphicCoordinateRepository.findAll();
        assertThat(graphicCoordinateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGraphicCoordinate() throws Exception {
        // Initialize the database
        graphicCoordinateRepository.saveAndFlush(graphicCoordinate);

        int databaseSizeBeforeDelete = graphicCoordinateRepository.findAll().size();

        // Delete the graphicCoordinate
        restGraphicCoordinateMockMvc.perform(delete("/api/graphic-coordinates/{id}", graphicCoordinate.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GraphicCoordinate> graphicCoordinateList = graphicCoordinateRepository.findAll();
        assertThat(graphicCoordinateList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GraphicCoordinate.class);
        GraphicCoordinate graphicCoordinate1 = new GraphicCoordinate();
        graphicCoordinate1.setId(1L);
        GraphicCoordinate graphicCoordinate2 = new GraphicCoordinate();
        graphicCoordinate2.setId(graphicCoordinate1.getId());
        assertThat(graphicCoordinate1).isEqualTo(graphicCoordinate2);
        graphicCoordinate2.setId(2L);
        assertThat(graphicCoordinate1).isNotEqualTo(graphicCoordinate2);
        graphicCoordinate1.setId(null);
        assertThat(graphicCoordinate1).isNotEqualTo(graphicCoordinate2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GraphicCoordinateDTO.class);
        GraphicCoordinateDTO graphicCoordinateDTO1 = new GraphicCoordinateDTO();
        graphicCoordinateDTO1.setId(1L);
        GraphicCoordinateDTO graphicCoordinateDTO2 = new GraphicCoordinateDTO();
        assertThat(graphicCoordinateDTO1).isNotEqualTo(graphicCoordinateDTO2);
        graphicCoordinateDTO2.setId(graphicCoordinateDTO1.getId());
        assertThat(graphicCoordinateDTO1).isEqualTo(graphicCoordinateDTO2);
        graphicCoordinateDTO2.setId(2L);
        assertThat(graphicCoordinateDTO1).isNotEqualTo(graphicCoordinateDTO2);
        graphicCoordinateDTO1.setId(null);
        assertThat(graphicCoordinateDTO1).isNotEqualTo(graphicCoordinateDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(graphicCoordinateMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(graphicCoordinateMapper.fromId(null)).isNull();
    }
}
