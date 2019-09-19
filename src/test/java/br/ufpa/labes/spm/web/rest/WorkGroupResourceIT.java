package br.ufpa.labes.spm.web.rest;

import br.ufpa.labes.spm.SpmApp;
import br.ufpa.labes.spm.domain.WorkGroup;
import br.ufpa.labes.spm.repository.WorkGroupRepository;
import br.ufpa.labes.spm.service.WorkGroupService;
import br.ufpa.labes.spm.service.dto.WorkGroupDTO;
import br.ufpa.labes.spm.service.mapper.WorkGroupMapper;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static br.ufpa.labes.spm.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link WorkGroupResource} REST controller.
 */
@EmbeddedKafka
@SpringBootTest(classes = SpmApp.class)
public class WorkGroupResourceIT {

    private static final String DEFAULT_IDENT = "AAAAAAAAAA";
    private static final String UPDATED_IDENT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private WorkGroupRepository workGroupRepository;

    @Autowired
    private WorkGroupMapper workGroupMapper;

    @Autowired
    private WorkGroupService workGroupService;

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

    private MockMvc restWorkGroupMockMvc;

    private WorkGroup workGroup;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkGroupResource workGroupResource = new WorkGroupResource(workGroupService);
        this.restWorkGroupMockMvc = MockMvcBuilders.standaloneSetup(workGroupResource)
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
    public static WorkGroup createEntity(EntityManager em) {
        WorkGroup workGroup = new WorkGroup()
            .ident(DEFAULT_IDENT)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .active(DEFAULT_ACTIVE);
        return workGroup;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkGroup createUpdatedEntity(EntityManager em) {
        WorkGroup workGroup = new WorkGroup()
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        return workGroup;
    }

    @BeforeEach
    public void initTest() {
        workGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkGroup() throws Exception {
        int databaseSizeBeforeCreate = workGroupRepository.findAll().size();

        // Create the WorkGroup
        WorkGroupDTO workGroupDTO = workGroupMapper.toDto(workGroup);
        restWorkGroupMockMvc.perform(post("/api/work-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the WorkGroup in the database
        List<WorkGroup> workGroupList = workGroupRepository.findAll();
        assertThat(workGroupList).hasSize(databaseSizeBeforeCreate + 1);
        WorkGroup testWorkGroup = workGroupList.get(workGroupList.size() - 1);
        assertThat(testWorkGroup.getIdent()).isEqualTo(DEFAULT_IDENT);
        assertThat(testWorkGroup.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWorkGroup.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testWorkGroup.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createWorkGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workGroupRepository.findAll().size();

        // Create the WorkGroup with an existing ID
        workGroup.setId(1L);
        WorkGroupDTO workGroupDTO = workGroupMapper.toDto(workGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkGroupMockMvc.perform(post("/api/work-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkGroup in the database
        List<WorkGroup> workGroupList = workGroupRepository.findAll();
        assertThat(workGroupList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllWorkGroups() throws Exception {
        // Initialize the database
        workGroupRepository.saveAndFlush(workGroup);

        // Get all the workGroupList
        restWorkGroupMockMvc.perform(get("/api/work-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].ident").value(hasItem(DEFAULT_IDENT.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getWorkGroup() throws Exception {
        // Initialize the database
        workGroupRepository.saveAndFlush(workGroup);

        // Get the workGroup
        restWorkGroupMockMvc.perform(get("/api/work-groups/{id}", workGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workGroup.getId().intValue()))
            .andExpect(jsonPath("$.ident").value(DEFAULT_IDENT.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWorkGroup() throws Exception {
        // Get the workGroup
        restWorkGroupMockMvc.perform(get("/api/work-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkGroup() throws Exception {
        // Initialize the database
        workGroupRepository.saveAndFlush(workGroup);

        int databaseSizeBeforeUpdate = workGroupRepository.findAll().size();

        // Update the workGroup
        WorkGroup updatedWorkGroup = workGroupRepository.findById(workGroup.getId()).get();
        // Disconnect from session so that the updates on updatedWorkGroup are not directly saved in db
        em.detach(updatedWorkGroup);
        updatedWorkGroup
            .ident(UPDATED_IDENT)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .active(UPDATED_ACTIVE);
        WorkGroupDTO workGroupDTO = workGroupMapper.toDto(updatedWorkGroup);

        restWorkGroupMockMvc.perform(put("/api/work-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupDTO)))
            .andExpect(status().isOk());

        // Validate the WorkGroup in the database
        List<WorkGroup> workGroupList = workGroupRepository.findAll();
        assertThat(workGroupList).hasSize(databaseSizeBeforeUpdate);
        WorkGroup testWorkGroup = workGroupList.get(workGroupList.size() - 1);
        assertThat(testWorkGroup.getIdent()).isEqualTo(UPDATED_IDENT);
        assertThat(testWorkGroup.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWorkGroup.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testWorkGroup.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkGroup() throws Exception {
        int databaseSizeBeforeUpdate = workGroupRepository.findAll().size();

        // Create the WorkGroup
        WorkGroupDTO workGroupDTO = workGroupMapper.toDto(workGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkGroupMockMvc.perform(put("/api/work-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkGroup in the database
        List<WorkGroup> workGroupList = workGroupRepository.findAll();
        assertThat(workGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkGroup() throws Exception {
        // Initialize the database
        workGroupRepository.saveAndFlush(workGroup);

        int databaseSizeBeforeDelete = workGroupRepository.findAll().size();

        // Delete the workGroup
        restWorkGroupMockMvc.perform(delete("/api/work-groups/{id}", workGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkGroup> workGroupList = workGroupRepository.findAll();
        assertThat(workGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkGroup.class);
        WorkGroup workGroup1 = new WorkGroup();
        workGroup1.setId(1L);
        WorkGroup workGroup2 = new WorkGroup();
        workGroup2.setId(workGroup1.getId());
        assertThat(workGroup1).isEqualTo(workGroup2);
        workGroup2.setId(2L);
        assertThat(workGroup1).isNotEqualTo(workGroup2);
        workGroup1.setId(null);
        assertThat(workGroup1).isNotEqualTo(workGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkGroupDTO.class);
        WorkGroupDTO workGroupDTO1 = new WorkGroupDTO();
        workGroupDTO1.setId(1L);
        WorkGroupDTO workGroupDTO2 = new WorkGroupDTO();
        assertThat(workGroupDTO1).isNotEqualTo(workGroupDTO2);
        workGroupDTO2.setId(workGroupDTO1.getId());
        assertThat(workGroupDTO1).isEqualTo(workGroupDTO2);
        workGroupDTO2.setId(2L);
        assertThat(workGroupDTO1).isNotEqualTo(workGroupDTO2);
        workGroupDTO1.setId(null);
        assertThat(workGroupDTO1).isNotEqualTo(workGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(workGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(workGroupMapper.fromId(null)).isNull();
    }
}
