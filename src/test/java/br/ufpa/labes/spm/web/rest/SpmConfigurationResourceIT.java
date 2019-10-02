package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.SpmConfiguration;
import br.ufpa.labes.spm.repository.SpmConfigurationRepository;
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
 * Integration tests for the {@link SpmConfigurationResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class SpmConfigurationResourceIT {

    private static final String DEFAULT_FILTRO = "AAAAAAAAAA";
    private static final String UPDATED_FILTRO = "BBBBBBBBBB";

    private static final String DEFAULT_IDIOMA = "AAAAAAAAAA";
    private static final String UPDATED_IDIOMA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_GRAFICO_DE_ESFORCO = false;
    private static final Boolean UPDATED_GRAFICO_DE_ESFORCO = true;

    private static final Boolean DEFAULT_GRAFICO_DE_CUSTOS = false;
    private static final Boolean UPDATED_GRAFICO_DE_CUSTOS = true;

    private static final Boolean DEFAULT_GRAFICO_DE_DESEMPENHO = false;
    private static final Boolean UPDATED_GRAFICO_DE_DESEMPENHO = true;

    private static final Boolean DEFAULT_GRAFICO_DE_TAREFAS = false;
    private static final Boolean UPDATED_GRAFICO_DE_TAREFAS = true;

    private static final Boolean DEFAULT_SENHA_EM_RECUPERACAO = false;
    private static final Boolean UPDATED_SENHA_EM_RECUPERACAO = true;

    @Autowired
    private SpmConfigurationRepository spmConfigurationRepository;

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

    private MockMvc restSpmConfigurationMockMvc;

    private SpmConfiguration spmConfiguration;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SpmConfigurationResource spmConfigurationResource = new SpmConfigurationResource(spmConfigurationRepository);
        this.restSpmConfigurationMockMvc = MockMvcBuilders.standaloneSetup(spmConfigurationResource)
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
    public static SpmConfiguration createEntity(EntityManager em) {
        SpmConfiguration spmConfiguration = new SpmConfiguration()
            .filtro(DEFAULT_FILTRO)
            .idioma(DEFAULT_IDIOMA)
            .graficoDeEsforco(DEFAULT_GRAFICO_DE_ESFORCO)
            .graficoDeCustos(DEFAULT_GRAFICO_DE_CUSTOS)
            .graficoDeDesempenho(DEFAULT_GRAFICO_DE_DESEMPENHO)
            .graficoDeTarefas(DEFAULT_GRAFICO_DE_TAREFAS)
            .senhaEmRecuperacao(DEFAULT_SENHA_EM_RECUPERACAO);
        return spmConfiguration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SpmConfiguration createUpdatedEntity(EntityManager em) {
        SpmConfiguration spmConfiguration = new SpmConfiguration()
            .filtro(UPDATED_FILTRO)
            .idioma(UPDATED_IDIOMA)
            .graficoDeEsforco(UPDATED_GRAFICO_DE_ESFORCO)
            .graficoDeCustos(UPDATED_GRAFICO_DE_CUSTOS)
            .graficoDeDesempenho(UPDATED_GRAFICO_DE_DESEMPENHO)
            .graficoDeTarefas(UPDATED_GRAFICO_DE_TAREFAS)
            .senhaEmRecuperacao(UPDATED_SENHA_EM_RECUPERACAO);
        return spmConfiguration;
    }

    @BeforeEach
    public void initTest() {
        spmConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpmConfiguration() throws Exception {
        int databaseSizeBeforeCreate = spmConfigurationRepository.findAll().size();

        // Create the SpmConfiguration
        restSpmConfigurationMockMvc.perform(post("/api/spm-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spmConfiguration)))
            .andExpect(status().isCreated());

        // Validate the SpmConfiguration in the database
        List<SpmConfiguration> spmConfigurationList = spmConfigurationRepository.findAll();
        assertThat(spmConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        SpmConfiguration testSpmConfiguration = spmConfigurationList.get(spmConfigurationList.size() - 1);
        assertThat(testSpmConfiguration.getFiltro()).isEqualTo(DEFAULT_FILTRO);
        assertThat(testSpmConfiguration.getIdioma()).isEqualTo(DEFAULT_IDIOMA);
        assertThat(testSpmConfiguration.isGraficoDeEsforco()).isEqualTo(DEFAULT_GRAFICO_DE_ESFORCO);
        assertThat(testSpmConfiguration.isGraficoDeCustos()).isEqualTo(DEFAULT_GRAFICO_DE_CUSTOS);
        assertThat(testSpmConfiguration.isGraficoDeDesempenho()).isEqualTo(DEFAULT_GRAFICO_DE_DESEMPENHO);
        assertThat(testSpmConfiguration.isGraficoDeTarefas()).isEqualTo(DEFAULT_GRAFICO_DE_TAREFAS);
        assertThat(testSpmConfiguration.isSenhaEmRecuperacao()).isEqualTo(DEFAULT_SENHA_EM_RECUPERACAO);
    }

    @Test
    @Transactional
    public void createSpmConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = spmConfigurationRepository.findAll().size();

        // Create the SpmConfiguration with an existing ID
        spmConfiguration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpmConfigurationMockMvc.perform(post("/api/spm-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spmConfiguration)))
            .andExpect(status().isBadRequest());

        // Validate the SpmConfiguration in the database
        List<SpmConfiguration> spmConfigurationList = spmConfigurationRepository.findAll();
        assertThat(spmConfigurationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllSpmConfigurations() throws Exception {
        // Initialize the database
        spmConfigurationRepository.saveAndFlush(spmConfiguration);

        // Get all the spmConfigurationList
        restSpmConfigurationMockMvc.perform(get("/api/spm-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(spmConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].filtro").value(hasItem(DEFAULT_FILTRO.toString())))
            .andExpect(jsonPath("$.[*].idioma").value(hasItem(DEFAULT_IDIOMA.toString())))
            .andExpect(jsonPath("$.[*].graficoDeEsforco").value(hasItem(DEFAULT_GRAFICO_DE_ESFORCO.booleanValue())))
            .andExpect(jsonPath("$.[*].graficoDeCustos").value(hasItem(DEFAULT_GRAFICO_DE_CUSTOS.booleanValue())))
            .andExpect(jsonPath("$.[*].graficoDeDesempenho").value(hasItem(DEFAULT_GRAFICO_DE_DESEMPENHO.booleanValue())))
            .andExpect(jsonPath("$.[*].graficoDeTarefas").value(hasItem(DEFAULT_GRAFICO_DE_TAREFAS.booleanValue())))
            .andExpect(jsonPath("$.[*].senhaEmRecuperacao").value(hasItem(DEFAULT_SENHA_EM_RECUPERACAO.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getSpmConfiguration() throws Exception {
        // Initialize the database
        spmConfigurationRepository.saveAndFlush(spmConfiguration);

        // Get the spmConfiguration
        restSpmConfigurationMockMvc.perform(get("/api/spm-configurations/{id}", spmConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(spmConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.filtro").value(DEFAULT_FILTRO.toString()))
            .andExpect(jsonPath("$.idioma").value(DEFAULT_IDIOMA.toString()))
            .andExpect(jsonPath("$.graficoDeEsforco").value(DEFAULT_GRAFICO_DE_ESFORCO.booleanValue()))
            .andExpect(jsonPath("$.graficoDeCustos").value(DEFAULT_GRAFICO_DE_CUSTOS.booleanValue()))
            .andExpect(jsonPath("$.graficoDeDesempenho").value(DEFAULT_GRAFICO_DE_DESEMPENHO.booleanValue()))
            .andExpect(jsonPath("$.graficoDeTarefas").value(DEFAULT_GRAFICO_DE_TAREFAS.booleanValue()))
            .andExpect(jsonPath("$.senhaEmRecuperacao").value(DEFAULT_SENHA_EM_RECUPERACAO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSpmConfiguration() throws Exception {
        // Get the spmConfiguration
        restSpmConfigurationMockMvc.perform(get("/api/spm-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpmConfiguration() throws Exception {
        // Initialize the database
        spmConfigurationRepository.saveAndFlush(spmConfiguration);

        int databaseSizeBeforeUpdate = spmConfigurationRepository.findAll().size();

        // Update the spmConfiguration
        SpmConfiguration updatedSpmConfiguration = spmConfigurationRepository.findById(spmConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedSpmConfiguration are not directly saved in db
        em.detach(updatedSpmConfiguration);
        updatedSpmConfiguration
            .filtro(UPDATED_FILTRO)
            .idioma(UPDATED_IDIOMA)
            .graficoDeEsforco(UPDATED_GRAFICO_DE_ESFORCO)
            .graficoDeCustos(UPDATED_GRAFICO_DE_CUSTOS)
            .graficoDeDesempenho(UPDATED_GRAFICO_DE_DESEMPENHO)
            .graficoDeTarefas(UPDATED_GRAFICO_DE_TAREFAS)
            .senhaEmRecuperacao(UPDATED_SENHA_EM_RECUPERACAO);

        restSpmConfigurationMockMvc.perform(put("/api/spm-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpmConfiguration)))
            .andExpect(status().isOk());

        // Validate the SpmConfiguration in the database
        List<SpmConfiguration> spmConfigurationList = spmConfigurationRepository.findAll();
        assertThat(spmConfigurationList).hasSize(databaseSizeBeforeUpdate);
        SpmConfiguration testSpmConfiguration = spmConfigurationList.get(spmConfigurationList.size() - 1);
        assertThat(testSpmConfiguration.getFiltro()).isEqualTo(UPDATED_FILTRO);
        assertThat(testSpmConfiguration.getIdioma()).isEqualTo(UPDATED_IDIOMA);
        assertThat(testSpmConfiguration.isGraficoDeEsforco()).isEqualTo(UPDATED_GRAFICO_DE_ESFORCO);
        assertThat(testSpmConfiguration.isGraficoDeCustos()).isEqualTo(UPDATED_GRAFICO_DE_CUSTOS);
        assertThat(testSpmConfiguration.isGraficoDeDesempenho()).isEqualTo(UPDATED_GRAFICO_DE_DESEMPENHO);
        assertThat(testSpmConfiguration.isGraficoDeTarefas()).isEqualTo(UPDATED_GRAFICO_DE_TAREFAS);
        assertThat(testSpmConfiguration.isSenhaEmRecuperacao()).isEqualTo(UPDATED_SENHA_EM_RECUPERACAO);
    }

    @Test
    @Transactional
    public void updateNonExistingSpmConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = spmConfigurationRepository.findAll().size();

        // Create the SpmConfiguration

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpmConfigurationMockMvc.perform(put("/api/spm-configurations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(spmConfiguration)))
            .andExpect(status().isBadRequest());

        // Validate the SpmConfiguration in the database
        List<SpmConfiguration> spmConfigurationList = spmConfigurationRepository.findAll();
        assertThat(spmConfigurationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpmConfiguration() throws Exception {
        // Initialize the database
        spmConfigurationRepository.saveAndFlush(spmConfiguration);

        int databaseSizeBeforeDelete = spmConfigurationRepository.findAll().size();

        // Delete the spmConfiguration
        restSpmConfigurationMockMvc.perform(delete("/api/spm-configurations/{id}", spmConfiguration.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SpmConfiguration> spmConfigurationList = spmConfigurationRepository.findAll();
        assertThat(spmConfigurationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SpmConfiguration.class);
        SpmConfiguration spmConfiguration1 = new SpmConfiguration();
        spmConfiguration1.setId(1L);
        SpmConfiguration spmConfiguration2 = new SpmConfiguration();
        spmConfiguration2.setId(spmConfiguration1.getId());
        assertThat(spmConfiguration1).isEqualTo(spmConfiguration2);
        spmConfiguration2.setId(2L);
        assertThat(spmConfiguration1).isNotEqualTo(spmConfiguration2);
        spmConfiguration1.setId(null);
        assertThat(spmConfiguration1).isNotEqualTo(spmConfiguration2);
    }
}
