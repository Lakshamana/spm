package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.Consumable;
import br.ufpa.labes.spm.repository.ConsumableRepository;
import br.ufpa.labes.spm.service.ConsumableService;
import br.ufpa.labes.spm.service.dto.ConsumableDTO;
import br.ufpa.labes.spm.service.mapper.ConsumableMapper;
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
 * Integration tests for the {@link ConsumableResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class ConsumableResourceIT {

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final Float DEFAULT_TOTAL_QUANTITY = 1F;
    private static final Float UPDATED_TOTAL_QUANTITY = 2F;
    private static final Float SMALLER_TOTAL_QUANTITY = 1F - 1F;

    private static final Float DEFAULT_AMOUNT_USED = 1F;
    private static final Float UPDATED_AMOUNT_USED = 2F;
    private static final Float SMALLER_AMOUNT_USED = 1F - 1F;

    @Autowired
    private ConsumableRepository consumableRepository;

    @Autowired
    private ConsumableMapper consumableMapper;

    @Autowired
    private ConsumableService consumableService;

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

    private MockMvc restConsumableMockMvc;

    private Consumable consumable;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConsumableResource consumableResource = new ConsumableResource(consumableService);
        this.restConsumableMockMvc = MockMvcBuilders.standaloneSetup(consumableResource)
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
    public static Consumable createEntity(EntityManager em) {
        Consumable consumable = new Consumable()
            .state(DEFAULT_STATE)
            .unit(DEFAULT_UNIT)
            .totalQuantity(DEFAULT_TOTAL_QUANTITY)
            .amountUsed(DEFAULT_AMOUNT_USED);
        return consumable;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Consumable createUpdatedEntity(EntityManager em) {
        Consumable consumable = new Consumable()
            .state(UPDATED_STATE)
            .unit(UPDATED_UNIT)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .amountUsed(UPDATED_AMOUNT_USED);
        return consumable;
    }

    @BeforeEach
    public void initTest() {
        consumable = createEntity(em);
    }

    @Test
    @Transactional
    public void createConsumable() throws Exception {
        int databaseSizeBeforeCreate = consumableRepository.findAll().size();

        // Create the Consumable
        ConsumableDTO consumableDTO = consumableMapper.toDto(consumable);
        restConsumableMockMvc.perform(post("/api/consumables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumableDTO)))
            .andExpect(status().isCreated());

        // Validate the Consumable in the database
        List<Consumable> consumableList = consumableRepository.findAll();
        assertThat(consumableList).hasSize(databaseSizeBeforeCreate + 1);
        Consumable testConsumable = consumableList.get(consumableList.size() - 1);
        assertThat(testConsumable.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testConsumable.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testConsumable.getTotalQuantity()).isEqualTo(DEFAULT_TOTAL_QUANTITY);
        assertThat(testConsumable.getAmountUsed()).isEqualTo(DEFAULT_AMOUNT_USED);
    }

    @Test
    @Transactional
    public void createConsumableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = consumableRepository.findAll().size();

        // Create the Consumable with an existing ID
        consumable.setId(1L);
        ConsumableDTO consumableDTO = consumableMapper.toDto(consumable);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConsumableMockMvc.perform(post("/api/consumables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consumable in the database
        List<Consumable> consumableList = consumableRepository.findAll();
        assertThat(consumableList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllConsumables() throws Exception {
        // Initialize the database
        consumableRepository.saveAndFlush(consumable);

        // Get all the consumableList
        restConsumableMockMvc.perform(get("/api/consumables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(consumable.getId().intValue())))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].totalQuantity").value(hasItem(DEFAULT_TOTAL_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].amountUsed").value(hasItem(DEFAULT_AMOUNT_USED.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getConsumable() throws Exception {
        // Initialize the database
        consumableRepository.saveAndFlush(consumable);

        // Get the consumable
        restConsumableMockMvc.perform(get("/api/consumables/{id}", consumable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(consumable.getId().intValue()))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.totalQuantity").value(DEFAULT_TOTAL_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.amountUsed").value(DEFAULT_AMOUNT_USED.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingConsumable() throws Exception {
        // Get the consumable
        restConsumableMockMvc.perform(get("/api/consumables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConsumable() throws Exception {
        // Initialize the database
        consumableRepository.saveAndFlush(consumable);

        int databaseSizeBeforeUpdate = consumableRepository.findAll().size();

        // Update the consumable
        Consumable updatedConsumable = consumableRepository.findById(consumable.getId()).get();
        // Disconnect from session so that the updates on updatedConsumable are not directly saved in db
        em.detach(updatedConsumable);
        updatedConsumable
            .state(UPDATED_STATE)
            .unit(UPDATED_UNIT)
            .totalQuantity(UPDATED_TOTAL_QUANTITY)
            .amountUsed(UPDATED_AMOUNT_USED);
        ConsumableDTO consumableDTO = consumableMapper.toDto(updatedConsumable);

        restConsumableMockMvc.perform(put("/api/consumables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumableDTO)))
            .andExpect(status().isOk());

        // Validate the Consumable in the database
        List<Consumable> consumableList = consumableRepository.findAll();
        assertThat(consumableList).hasSize(databaseSizeBeforeUpdate);
        Consumable testConsumable = consumableList.get(consumableList.size() - 1);
        assertThat(testConsumable.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testConsumable.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testConsumable.getTotalQuantity()).isEqualTo(UPDATED_TOTAL_QUANTITY);
        assertThat(testConsumable.getAmountUsed()).isEqualTo(UPDATED_AMOUNT_USED);
    }

    @Test
    @Transactional
    public void updateNonExistingConsumable() throws Exception {
        int databaseSizeBeforeUpdate = consumableRepository.findAll().size();

        // Create the Consumable
        ConsumableDTO consumableDTO = consumableMapper.toDto(consumable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConsumableMockMvc.perform(put("/api/consumables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(consumableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Consumable in the database
        List<Consumable> consumableList = consumableRepository.findAll();
        assertThat(consumableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteConsumable() throws Exception {
        // Initialize the database
        consumableRepository.saveAndFlush(consumable);

        int databaseSizeBeforeDelete = consumableRepository.findAll().size();

        // Delete the consumable
        restConsumableMockMvc.perform(delete("/api/consumables/{id}", consumable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Consumable> consumableList = consumableRepository.findAll();
        assertThat(consumableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consumable.class);
        Consumable consumable1 = new Consumable();
        consumable1.setId(1L);
        Consumable consumable2 = new Consumable();
        consumable2.setId(consumable1.getId());
        assertThat(consumable1).isEqualTo(consumable2);
        consumable2.setId(2L);
        assertThat(consumable1).isNotEqualTo(consumable2);
        consumable1.setId(null);
        assertThat(consumable1).isNotEqualTo(consumable2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConsumableDTO.class);
        ConsumableDTO consumableDTO1 = new ConsumableDTO();
        consumableDTO1.setId(1L);
        ConsumableDTO consumableDTO2 = new ConsumableDTO();
        assertThat(consumableDTO1).isNotEqualTo(consumableDTO2);
        consumableDTO2.setId(consumableDTO1.getId());
        assertThat(consumableDTO1).isEqualTo(consumableDTO2);
        consumableDTO2.setId(2L);
        assertThat(consumableDTO1).isNotEqualTo(consumableDTO2);
        consumableDTO1.setId(null);
        assertThat(consumableDTO1).isNotEqualTo(consumableDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(consumableMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(consumableMapper.fromId(null)).isNull();
    }
}
