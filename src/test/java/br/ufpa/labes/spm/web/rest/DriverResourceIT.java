package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Driver;
import br.ufpa.labes.spm.repository.DriverRepository;
import br.ufpa.labes.spm.service.DriverService;
import br.ufpa.labes.spm.service.dto.DriverDTO;
import br.ufpa.labes.spm.service.mapper.DriverMapper;
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
 * Integration tests for the {@link DriverResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class DriverResourceIT {

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final String DEFAULT_APP_KEY = "AAAAAAAAAA";
    private static final String UPDATED_APP_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_APP_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_APP_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_APP_KEY_GOOGLE = "AAAAAAAAAA";
    private static final String UPDATED_APP_KEY_GOOGLE = "BBBBBBBBBB";

    private static final String DEFAULT_APP_SECRET_GOOGLE = "AAAAAAAAAA";
    private static final String UPDATED_APP_SECRET_GOOGLE = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_URL = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_URL = "BBBBBBBBBB";

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverMapper driverMapper;

    @Autowired
    private DriverService driverService;

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

    private MockMvc restDriverMockMvc;

    private Driver driver;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DriverResource driverResource = new DriverResource(driverService);
        this.restDriverMockMvc = MockMvcBuilders.standaloneSetup(driverResource)
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
    public static Driver createEntity(EntityManager em) {
        Driver driver = new Driver()
            .tipo(DEFAULT_TIPO)
            .appKey(DEFAULT_APP_KEY)
            .appSecret(DEFAULT_APP_SECRET)
            .appKeyGoogle(DEFAULT_APP_KEY_GOOGLE)
            .appSecretGoogle(DEFAULT_APP_SECRET_GOOGLE)
            .requestUrl(DEFAULT_REQUEST_URL);
        return driver;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Driver createUpdatedEntity(EntityManager em) {
        Driver driver = new Driver()
            .tipo(UPDATED_TIPO)
            .appKey(UPDATED_APP_KEY)
            .appSecret(UPDATED_APP_SECRET)
            .appKeyGoogle(UPDATED_APP_KEY_GOOGLE)
            .appSecretGoogle(UPDATED_APP_SECRET_GOOGLE)
            .requestUrl(UPDATED_REQUEST_URL);
        return driver;
    }

    @BeforeEach
    public void initTest() {
        driver = createEntity(em);
    }

    @Test
    @Transactional
    public void createDriver() throws Exception {
        int databaseSizeBeforeCreate = driverRepository.findAll().size();

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);
        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isCreated());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate + 1);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testDriver.getAppKey()).isEqualTo(DEFAULT_APP_KEY);
        assertThat(testDriver.getAppSecret()).isEqualTo(DEFAULT_APP_SECRET);
        assertThat(testDriver.getAppKeyGoogle()).isEqualTo(DEFAULT_APP_KEY_GOOGLE);
        assertThat(testDriver.getAppSecretGoogle()).isEqualTo(DEFAULT_APP_SECRET_GOOGLE);
        assertThat(testDriver.getRequestUrl()).isEqualTo(DEFAULT_REQUEST_URL);
    }

    @Test
    @Transactional
    public void createDriverWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = driverRepository.findAll().size();

        // Create the Driver with an existing ID
        driver.setId(1L);
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDriverMockMvc.perform(post("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDrivers() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get all the driverList
        restDriverMockMvc.perform(get("/api/drivers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(driver.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].appKey").value(hasItem(DEFAULT_APP_KEY.toString())))
            .andExpect(jsonPath("$.[*].appSecret").value(hasItem(DEFAULT_APP_SECRET.toString())))
            .andExpect(jsonPath("$.[*].appKeyGoogle").value(hasItem(DEFAULT_APP_KEY_GOOGLE.toString())))
            .andExpect(jsonPath("$.[*].appSecretGoogle").value(hasItem(DEFAULT_APP_SECRET_GOOGLE.toString())))
            .andExpect(jsonPath("$.[*].requestUrl").value(hasItem(DEFAULT_REQUEST_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        // Get the driver
        restDriverMockMvc.perform(get("/api/drivers/{id}", driver.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(driver.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.appKey").value(DEFAULT_APP_KEY.toString()))
            .andExpect(jsonPath("$.appSecret").value(DEFAULT_APP_SECRET.toString()))
            .andExpect(jsonPath("$.appKeyGoogle").value(DEFAULT_APP_KEY_GOOGLE.toString()))
            .andExpect(jsonPath("$.appSecretGoogle").value(DEFAULT_APP_SECRET_GOOGLE.toString()))
            .andExpect(jsonPath("$.requestUrl").value(DEFAULT_REQUEST_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDriver() throws Exception {
        // Get the driver
        restDriverMockMvc.perform(get("/api/drivers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Update the driver
        Driver updatedDriver = driverRepository.findById(driver.getId()).get();
        // Disconnect from session so that the updates on updatedDriver are not directly saved in db
        em.detach(updatedDriver);
        updatedDriver
            .tipo(UPDATED_TIPO)
            .appKey(UPDATED_APP_KEY)
            .appSecret(UPDATED_APP_SECRET)
            .appKeyGoogle(UPDATED_APP_KEY_GOOGLE)
            .appSecretGoogle(UPDATED_APP_SECRET_GOOGLE)
            .requestUrl(UPDATED_REQUEST_URL);
        DriverDTO driverDTO = driverMapper.toDto(updatedDriver);

        restDriverMockMvc.perform(put("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isOk());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
        Driver testDriver = driverList.get(driverList.size() - 1);
        assertThat(testDriver.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testDriver.getAppKey()).isEqualTo(UPDATED_APP_KEY);
        assertThat(testDriver.getAppSecret()).isEqualTo(UPDATED_APP_SECRET);
        assertThat(testDriver.getAppKeyGoogle()).isEqualTo(UPDATED_APP_KEY_GOOGLE);
        assertThat(testDriver.getAppSecretGoogle()).isEqualTo(UPDATED_APP_SECRET_GOOGLE);
        assertThat(testDriver.getRequestUrl()).isEqualTo(UPDATED_REQUEST_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingDriver() throws Exception {
        int databaseSizeBeforeUpdate = driverRepository.findAll().size();

        // Create the Driver
        DriverDTO driverDTO = driverMapper.toDto(driver);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDriverMockMvc.perform(put("/api/drivers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(driverDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Driver in the database
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDriver() throws Exception {
        // Initialize the database
        driverRepository.saveAndFlush(driver);

        int databaseSizeBeforeDelete = driverRepository.findAll().size();

        // Delete the driver
        restDriverMockMvc.perform(delete("/api/drivers/{id}", driver.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Driver> driverList = driverRepository.findAll();
        assertThat(driverList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Driver.class);
        Driver driver1 = new Driver();
        driver1.setId(1L);
        Driver driver2 = new Driver();
        driver2.setId(driver1.getId());
        assertThat(driver1).isEqualTo(driver2);
        driver2.setId(2L);
        assertThat(driver1).isNotEqualTo(driver2);
        driver1.setId(null);
        assertThat(driver1).isNotEqualTo(driver2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriverDTO.class);
        DriverDTO driverDTO1 = new DriverDTO();
        driverDTO1.setId(1L);
        DriverDTO driverDTO2 = new DriverDTO();
        assertThat(driverDTO1).isNotEqualTo(driverDTO2);
        driverDTO2.setId(driverDTO1.getId());
        assertThat(driverDTO1).isEqualTo(driverDTO2);
        driverDTO2.setId(2L);
        assertThat(driverDTO1).isNotEqualTo(driverDTO2);
        driverDTO1.setId(null);
        assertThat(driverDTO1).isNotEqualTo(driverDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(driverMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(driverMapper.fromId(null)).isNull();
    }
}
