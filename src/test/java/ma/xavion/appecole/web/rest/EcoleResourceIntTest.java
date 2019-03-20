package ma.xavion.appecole.web.rest;

import ma.xavion.appecole.AppEcoleApp;

import ma.xavion.appecole.domain.Ecole;
import ma.xavion.appecole.repository.EcoleRepository;
import ma.xavion.appecole.repository.search.EcoleSearchRepository;
import ma.xavion.appecole.service.EcoleService;
import ma.xavion.appecole.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static ma.xavion.appecole.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EcoleResource REST controller.
 *
 * @see EcoleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppEcoleApp.class)
public class EcoleResourceIntTest {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    @Autowired
    private EcoleRepository ecoleRepository;

    @Autowired
    private EcoleService ecoleService;

    /**
     * This repository is mocked in the ma.xavion.appecole.repository.search test package.
     *
     * @see ma.xavion.appecole.repository.search.EcoleSearchRepositoryMockConfiguration
     */
    @Autowired
    private EcoleSearchRepository mockEcoleSearchRepository;

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

    private MockMvc restEcoleMockMvc;

    private Ecole ecole;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EcoleResource ecoleResource = new EcoleResource(ecoleService);
        this.restEcoleMockMvc = MockMvcBuilders.standaloneSetup(ecoleResource)
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
    public static Ecole createEntity(EntityManager em) {
        Ecole ecole = new Ecole()
            .nom(DEFAULT_NOM)
            .adresse(DEFAULT_ADRESSE)
            .ville(DEFAULT_VILLE)
            .telephone(DEFAULT_TELEPHONE);
        return ecole;
    }

    @Before
    public void initTest() {
        ecole = createEntity(em);
    }

    @Test
    @Transactional
    public void createEcole() throws Exception {
        int databaseSizeBeforeCreate = ecoleRepository.findAll().size();

        // Create the Ecole
        restEcoleMockMvc.perform(post("/api/ecoles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isCreated());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeCreate + 1);
        Ecole testEcole = ecoleList.get(ecoleList.size() - 1);
        assertThat(testEcole.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEcole.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testEcole.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testEcole.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);

        // Validate the Ecole in Elasticsearch
        verify(mockEcoleSearchRepository, times(1)).save(testEcole);
    }

    @Test
    @Transactional
    public void createEcoleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ecoleRepository.findAll().size();

        // Create the Ecole with an existing ID
        ecole.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEcoleMockMvc.perform(post("/api/ecoles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isBadRequest());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ecole in Elasticsearch
        verify(mockEcoleSearchRepository, times(0)).save(ecole);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecoleRepository.findAll().size();
        // set the field null
        ecole.setNom(null);

        // Create the Ecole, which fails.

        restEcoleMockMvc.perform(post("/api/ecoles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isBadRequest());

        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEcoles() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        // Get all the ecoleList
        restEcoleMockMvc.perform(get("/api/ecoles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecole.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE.toString())));
    }
    
    @Test
    @Transactional
    public void getEcole() throws Exception {
        // Initialize the database
        ecoleRepository.saveAndFlush(ecole);

        // Get the ecole
        restEcoleMockMvc.perform(get("/api/ecoles/{id}", ecole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ecole.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEcole() throws Exception {
        // Get the ecole
        restEcoleMockMvc.perform(get("/api/ecoles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEcole() throws Exception {
        // Initialize the database
        ecoleService.save(ecole);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockEcoleSearchRepository);

        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();

        // Update the ecole
        Ecole updatedEcole = ecoleRepository.findById(ecole.getId()).get();
        // Disconnect from session so that the updates on updatedEcole are not directly saved in db
        em.detach(updatedEcole);
        updatedEcole
            .nom(UPDATED_NOM)
            .adresse(UPDATED_ADRESSE)
            .ville(UPDATED_VILLE)
            .telephone(UPDATED_TELEPHONE);

        restEcoleMockMvc.perform(put("/api/ecoles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEcole)))
            .andExpect(status().isOk());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);
        Ecole testEcole = ecoleList.get(ecoleList.size() - 1);
        assertThat(testEcole.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEcole.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEcole.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testEcole.getTelephone()).isEqualTo(UPDATED_TELEPHONE);

        // Validate the Ecole in Elasticsearch
        verify(mockEcoleSearchRepository, times(1)).save(testEcole);
    }

    @Test
    @Transactional
    public void updateNonExistingEcole() throws Exception {
        int databaseSizeBeforeUpdate = ecoleRepository.findAll().size();

        // Create the Ecole

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcoleMockMvc.perform(put("/api/ecoles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ecole)))
            .andExpect(status().isBadRequest());

        // Validate the Ecole in the database
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ecole in Elasticsearch
        verify(mockEcoleSearchRepository, times(0)).save(ecole);
    }

    @Test
    @Transactional
    public void deleteEcole() throws Exception {
        // Initialize the database
        ecoleService.save(ecole);

        int databaseSizeBeforeDelete = ecoleRepository.findAll().size();

        // Delete the ecole
        restEcoleMockMvc.perform(delete("/api/ecoles/{id}", ecole.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ecole> ecoleList = ecoleRepository.findAll();
        assertThat(ecoleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ecole in Elasticsearch
        verify(mockEcoleSearchRepository, times(1)).deleteById(ecole.getId());
    }

    @Test
    @Transactional
    public void searchEcole() throws Exception {
        // Initialize the database
        ecoleService.save(ecole);
        when(mockEcoleSearchRepository.search(queryStringQuery("id:" + ecole.getId())))
            .thenReturn(Collections.singletonList(ecole));
        // Search the ecole
        restEcoleMockMvc.perform(get("/api/_search/ecoles?query=id:" + ecole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecole.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ecole.class);
        Ecole ecole1 = new Ecole();
        ecole1.setId(1L);
        Ecole ecole2 = new Ecole();
        ecole2.setId(ecole1.getId());
        assertThat(ecole1).isEqualTo(ecole2);
        ecole2.setId(2L);
        assertThat(ecole1).isNotEqualTo(ecole2);
        ecole1.setId(null);
        assertThat(ecole1).isNotEqualTo(ecole2);
    }
}
