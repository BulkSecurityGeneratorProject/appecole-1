package ma.xavion.appecole.web.rest;

import ma.xavion.appecole.AppEcoleApp;

import ma.xavion.appecole.domain.Niveau;
import ma.xavion.appecole.repository.NiveauRepository;
import ma.xavion.appecole.repository.search.NiveauSearchRepository;
import ma.xavion.appecole.service.NiveauService;
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
 * Test class for the NiveauResource REST controller.
 *
 * @see NiveauResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AppEcoleApp.class)
public class NiveauResourceIntTest {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private NiveauRepository niveauRepository;

    @Autowired
    private NiveauService niveauService;

    /**
     * This repository is mocked in the ma.xavion.appecole.repository.search test package.
     *
     * @see ma.xavion.appecole.repository.search.NiveauSearchRepositoryMockConfiguration
     */
    @Autowired
    private NiveauSearchRepository mockNiveauSearchRepository;

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

    private MockMvc restNiveauMockMvc;

    private Niveau niveau;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NiveauResource niveauResource = new NiveauResource(niveauService);
        this.restNiveauMockMvc = MockMvcBuilders.standaloneSetup(niveauResource)
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
    public static Niveau createEntity(EntityManager em) {
        Niveau niveau = new Niveau()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION);
        return niveau;
    }

    @Before
    public void initTest() {
        niveau = createEntity(em);
    }

    @Test
    @Transactional
    public void createNiveau() throws Exception {
        int databaseSizeBeforeCreate = niveauRepository.findAll().size();

        // Create the Niveau
        restNiveauMockMvc.perform(post("/api/niveaus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(niveau)))
            .andExpect(status().isCreated());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeCreate + 1);
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testNiveau.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(1)).save(testNiveau);
    }

    @Test
    @Transactional
    public void createNiveauWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = niveauRepository.findAll().size();

        // Create the Niveau with an existing ID
        niveau.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNiveauMockMvc.perform(post("/api/niveaus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(niveau)))
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeCreate);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(0)).save(niveau);
    }

    @Test
    @Transactional
    public void getAllNiveaus() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get all the niveauList
        restNiveauMockMvc.perform(get("/api/niveaus?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getNiveau() throws Exception {
        // Initialize the database
        niveauRepository.saveAndFlush(niveau);

        // Get the niveau
        restNiveauMockMvc.perform(get("/api/niveaus/{id}", niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(niveau.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNiveau() throws Exception {
        // Get the niveau
        restNiveauMockMvc.perform(get("/api/niveaus/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNiveau() throws Exception {
        // Initialize the database
        niveauService.save(niveau);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockNiveauSearchRepository);

        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();

        // Update the niveau
        Niveau updatedNiveau = niveauRepository.findById(niveau.getId()).get();
        // Disconnect from session so that the updates on updatedNiveau are not directly saved in db
        em.detach(updatedNiveau);
        updatedNiveau
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION);

        restNiveauMockMvc.perform(put("/api/niveaus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNiveau)))
            .andExpect(status().isOk());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);
        Niveau testNiveau = niveauList.get(niveauList.size() - 1);
        assertThat(testNiveau.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testNiveau.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(1)).save(testNiveau);
    }

    @Test
    @Transactional
    public void updateNonExistingNiveau() throws Exception {
        int databaseSizeBeforeUpdate = niveauRepository.findAll().size();

        // Create the Niveau

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNiveauMockMvc.perform(put("/api/niveaus")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(niveau)))
            .andExpect(status().isBadRequest());

        // Validate the Niveau in the database
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(0)).save(niveau);
    }

    @Test
    @Transactional
    public void deleteNiveau() throws Exception {
        // Initialize the database
        niveauService.save(niveau);

        int databaseSizeBeforeDelete = niveauRepository.findAll().size();

        // Delete the niveau
        restNiveauMockMvc.perform(delete("/api/niveaus/{id}", niveau.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Niveau> niveauList = niveauRepository.findAll();
        assertThat(niveauList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Niveau in Elasticsearch
        verify(mockNiveauSearchRepository, times(1)).deleteById(niveau.getId());
    }

    @Test
    @Transactional
    public void searchNiveau() throws Exception {
        // Initialize the database
        niveauService.save(niveau);
        when(mockNiveauSearchRepository.search(queryStringQuery("id:" + niveau.getId())))
            .thenReturn(Collections.singletonList(niveau));
        // Search the niveau
        restNiveauMockMvc.perform(get("/api/_search/niveaus?query=id:" + niveau.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(niveau.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Niveau.class);
        Niveau niveau1 = new Niveau();
        niveau1.setId(1L);
        Niveau niveau2 = new Niveau();
        niveau2.setId(niveau1.getId());
        assertThat(niveau1).isEqualTo(niveau2);
        niveau2.setId(2L);
        assertThat(niveau1).isNotEqualTo(niveau2);
        niveau1.setId(null);
        assertThat(niveau1).isNotEqualTo(niveau2);
    }
}
