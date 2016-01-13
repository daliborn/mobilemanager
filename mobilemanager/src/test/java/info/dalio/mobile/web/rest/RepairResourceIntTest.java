package info.dalio.mobile.web.rest;

import info.dalio.mobile.Application;
import info.dalio.mobile.domain.Repair;
import info.dalio.mobile.repository.RepairRepository;
import info.dalio.mobile.repository.search.RepairSearchRepository;
import info.dalio.mobile.web.rest.dto.RepairDTO;
import info.dalio.mobile.web.rest.mapper.RepairMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import info.dalio.mobile.domain.enumeration.Brand;

/**
 * Test class for the RepairResource REST controller.
 *
 * @see RepairResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RepairResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_IMEI = "AAAAA";
    private static final String UPDATED_IMEI = "BBBBB";
    private static final String DEFAULT_SERIALNO = "AAAAA";
    private static final String UPDATED_SERIALNO = "BBBBB";


    private static final Brand DEFAULT_BRAND = Brand.Acer;
    private static final Brand UPDATED_BRAND = Brand.Alcatel;

    private static final ZonedDateTime DEFAULT_ENTRY_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_ENTRY_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ENTRY_DATE_STR = dateTimeFormatter.format(DEFAULT_ENTRY_DATE);

    private static final Boolean DEFAULT_CLOSED = false;
    private static final Boolean UPDATED_CLOSED = true;
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    @Inject
    private RepairRepository repairRepository;

    @Inject
    private RepairMapper repairMapper;

    @Inject
    private RepairSearchRepository repairSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRepairMockMvc;

    private Repair repair;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RepairResource repairResource = new RepairResource();
        ReflectionTestUtils.setField(repairResource, "repairSearchRepository", repairSearchRepository);
        ReflectionTestUtils.setField(repairResource, "repairRepository", repairRepository);
        ReflectionTestUtils.setField(repairResource, "repairMapper", repairMapper);
        this.restRepairMockMvc = MockMvcBuilders.standaloneSetup(repairResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        repair = new Repair();
        repair.setImei(DEFAULT_IMEI);
        repair.setSerialno(DEFAULT_SERIALNO);
        repair.setBrand(DEFAULT_BRAND);
        repair.setEntryDate(DEFAULT_ENTRY_DATE);
        repair.setClosed(DEFAULT_CLOSED);
        repair.setComment(DEFAULT_COMMENT);
        repair.setPrice(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void createRepair() throws Exception {
        int databaseSizeBeforeCreate = repairRepository.findAll().size();

        // Create the Repair
        RepairDTO repairDTO = repairMapper.repairToRepairDTO(repair);

        restRepairMockMvc.perform(post("/api/repairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
                .andExpect(status().isCreated());

        // Validate the Repair in the database
        List<Repair> repairs = repairRepository.findAll();
        assertThat(repairs).hasSize(databaseSizeBeforeCreate + 1);
        Repair testRepair = repairs.get(repairs.size() - 1);
        assertThat(testRepair.getImei()).isEqualTo(DEFAULT_IMEI);
        assertThat(testRepair.getSerialno()).isEqualTo(DEFAULT_SERIALNO);
        assertThat(testRepair.getBrand()).isEqualTo(DEFAULT_BRAND);
        assertThat(testRepair.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
        assertThat(testRepair.getClosed()).isEqualTo(DEFAULT_CLOSED);
        assertThat(testRepair.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testRepair.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    public void checkImeiIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairRepository.findAll().size();
        // set the field null
        repair.setImei(null);

        // Create the Repair, which fails.
        RepairDTO repairDTO = repairMapper.repairToRepairDTO(repair);

        restRepairMockMvc.perform(post("/api/repairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
                .andExpect(status().isBadRequest());

        List<Repair> repairs = repairRepository.findAll();
        assertThat(repairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSerialnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = repairRepository.findAll().size();
        // set the field null
        repair.setSerialno(null);

        // Create the Repair, which fails.
        RepairDTO repairDTO = repairMapper.repairToRepairDTO(repair);

        restRepairMockMvc.perform(post("/api/repairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
                .andExpect(status().isBadRequest());

        List<Repair> repairs = repairRepository.findAll();
        assertThat(repairs).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRepairs() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get all the repairs
        restRepairMockMvc.perform(get("/api/repairs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(repair.getId().intValue())))
                .andExpect(jsonPath("$.[*].imei").value(hasItem(DEFAULT_IMEI.toString())))
                .andExpect(jsonPath("$.[*].serialno").value(hasItem(DEFAULT_SERIALNO.toString())))
                .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND.toString())))
                .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE_STR)))
                .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.booleanValue())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())));
    }

    @Test
    @Transactional
    public void getRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

        // Get the repair
        restRepairMockMvc.perform(get("/api/repairs/{id}", repair.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(repair.getId().intValue()))
            .andExpect(jsonPath("$.imei").value(DEFAULT_IMEI.toString()))
            .andExpect(jsonPath("$.serialno").value(DEFAULT_SERIALNO.toString()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND.toString()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE_STR))
            .andExpect(jsonPath("$.closed").value(DEFAULT_CLOSED.booleanValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRepair() throws Exception {
        // Get the repair
        restRepairMockMvc.perform(get("/api/repairs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

		int databaseSizeBeforeUpdate = repairRepository.findAll().size();

        // Update the repair
        repair.setImei(UPDATED_IMEI);
        repair.setSerialno(UPDATED_SERIALNO);
        repair.setBrand(UPDATED_BRAND);
        repair.setEntryDate(UPDATED_ENTRY_DATE);
        repair.setClosed(UPDATED_CLOSED);
        repair.setComment(UPDATED_COMMENT);
        repair.setPrice(UPDATED_PRICE);
        RepairDTO repairDTO = repairMapper.repairToRepairDTO(repair);

        restRepairMockMvc.perform(put("/api/repairs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(repairDTO)))
                .andExpect(status().isOk());

        // Validate the Repair in the database
        List<Repair> repairs = repairRepository.findAll();
        assertThat(repairs).hasSize(databaseSizeBeforeUpdate);
        Repair testRepair = repairs.get(repairs.size() - 1);
        assertThat(testRepair.getImei()).isEqualTo(UPDATED_IMEI);
        assertThat(testRepair.getSerialno()).isEqualTo(UPDATED_SERIALNO);
        assertThat(testRepair.getBrand()).isEqualTo(UPDATED_BRAND);
        assertThat(testRepair.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
        assertThat(testRepair.getClosed()).isEqualTo(UPDATED_CLOSED);
        assertThat(testRepair.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testRepair.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    public void deleteRepair() throws Exception {
        // Initialize the database
        repairRepository.saveAndFlush(repair);

		int databaseSizeBeforeDelete = repairRepository.findAll().size();

        // Get the repair
        restRepairMockMvc.perform(delete("/api/repairs/{id}", repair.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Repair> repairs = repairRepository.findAll();
        assertThat(repairs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
