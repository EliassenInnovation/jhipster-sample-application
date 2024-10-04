package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductBeforeApproveAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductBeforeApprove;
import com.mycompany.myapp.repository.ProductBeforeApproveRepository;
import com.mycompany.myapp.repository.search.ProductBeforeApproveSearchRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductBeforeApproveResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductBeforeApproveResourceIT {

    private static final Double DEFAULT_ADDED_SUGAR = 1D;
    private static final Double UPDATED_ADDED_SUGAR = 2D;

    private static final String DEFAULT_ADDED_SUGAR_UOM = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_SUGAR_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_ALLERGEN_KEYWORDS = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGEN_KEYWORDS = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CALORIES = 1D;
    private static final Double UPDATED_CALORIES = 2D;

    private static final String DEFAULT_CALORIES_UOM = "AAAAAAAAAA";
    private static final String UPDATED_CALORIES_UOM = "BBBBBBBBBB";

    private static final Double DEFAULT_CARBOHYDRATES = 1D;
    private static final Double UPDATED_CARBOHYDRATES = 2D;

    private static final String DEFAULT_CARBOHYDRATES_UOM = "AAAAAAAAAA";
    private static final String UPDATED_CARBOHYDRATES_UOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_CATEGORY_ID = 1;
    private static final Integer UPDATED_CATEGORY_ID = 2;

    private static final Double DEFAULT_CHOLESTEROL = 1D;
    private static final Double UPDATED_CHOLESTEROL = 2D;

    private static final String DEFAULT_CHOLESTEROL_UOM = "AAAAAAAAAA";
    private static final String UPDATED_CHOLESTEROL_UOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREATED_BY = 1;
    private static final Integer UPDATED_CREATED_BY = 2;

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_DIETARY_FIBER = 1D;
    private static final Double UPDATED_DIETARY_FIBER = 2D;

    private static final String DEFAULT_DIETARY_FIBER_UOM = "AAAAAAAAAA";
    private static final String UPDATED_DIETARY_FIBER_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRIBUTOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_DISTRIBUTOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_G_TIN = "AAAAAAAAAA";
    private static final String UPDATED_G_TIN = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTS = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_IOC_CATEGORY_ID = 1;
    private static final Integer UPDATED_IOC_CATEGORY_ID = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_MERGE = false;
    private static final Boolean UPDATED_IS_MERGE = true;

    private static final Integer DEFAULT_MANUFACTURER_ID = 1;
    private static final Integer UPDATED_MANUFACTURER_ID = 2;

    private static final String DEFAULT_MANUFACTURER_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_PRODUCT_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MERGE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MERGE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_PRODUCT_LABEL_PDF_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_LABEL_PDF_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_PROTEIN = 1D;
    private static final Double UPDATED_PROTEIN = 2D;

    private static final String DEFAULT_PROTEIN_UOM = "AAAAAAAAAA";
    private static final String UPDATED_PROTEIN_UOM = "BBBBBBBBBB";

    private static final Double DEFAULT_SATURATED_FAT = 1D;
    private static final Double UPDATED_SATURATED_FAT = 2D;

    private static final Double DEFAULT_SERVING = 1D;
    private static final Double UPDATED_SERVING = 2D;

    private static final String DEFAULT_SERVING_UOM = "AAAAAAAAAA";
    private static final String UPDATED_SERVING_UOM = "BBBBBBBBBB";

    private static final Double DEFAULT_SODIUM = 1D;
    private static final Double UPDATED_SODIUM = 2D;

    private static final String DEFAULT_SODIUM_UOM = "AAAAAAAAAA";
    private static final String UPDATED_SODIUM_UOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_STORAGE_TYPE_ID = 1;
    private static final Integer UPDATED_STORAGE_TYPE_ID = 2;

    private static final Integer DEFAULT_SUB_CATEGORY_ID = 1;
    private static final Integer UPDATED_SUB_CATEGORY_ID = 2;

    private static final Double DEFAULT_SUGAR = 1D;
    private static final Double UPDATED_SUGAR = 2D;

    private static final String DEFAULT_SUGAR_UOM = "AAAAAAAAAA";
    private static final String UPDATED_SUGAR_UOM = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_FAT = 1D;
    private static final Double UPDATED_TOTAL_FAT = 2D;

    private static final Double DEFAULT_TRANS_FAT = 1D;
    private static final Double UPDATED_TRANS_FAT = 2D;

    private static final String DEFAULT_U_PC = "AAAAAAAAAA";
    private static final String UPDATED_U_PC = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-before-approves";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-before-approves/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductBeforeApproveRepository productBeforeApproveRepository;

    @Autowired
    private ProductBeforeApproveSearchRepository productBeforeApproveSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductBeforeApproveMockMvc;

    private ProductBeforeApprove productBeforeApprove;

    private ProductBeforeApprove insertedProductBeforeApprove;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBeforeApprove createEntity() {
        return new ProductBeforeApprove()
            .addedSugar(DEFAULT_ADDED_SUGAR)
            .addedSugarUom(DEFAULT_ADDED_SUGAR_UOM)
            .allergenKeywords(DEFAULT_ALLERGEN_KEYWORDS)
            .brandName(DEFAULT_BRAND_NAME)
            .calories(DEFAULT_CALORIES)
            .caloriesUom(DEFAULT_CALORIES_UOM)
            .carbohydrates(DEFAULT_CARBOHYDRATES)
            .carbohydratesUom(DEFAULT_CARBOHYDRATES_UOM)
            .categoryId(DEFAULT_CATEGORY_ID)
            .cholesterol(DEFAULT_CHOLESTEROL)
            .cholesterolUOM(DEFAULT_CHOLESTEROL_UOM)
            .createdBy(DEFAULT_CREATED_BY)
            .createdOn(DEFAULT_CREATED_ON)
            .description(DEFAULT_DESCRIPTION)
            .dietaryFiber(DEFAULT_DIETARY_FIBER)
            .dietaryFiberUom(DEFAULT_DIETARY_FIBER_UOM)
            .distributorId(DEFAULT_DISTRIBUTOR_ID)
            .gTIN(DEFAULT_G_TIN)
            .ingredients(DEFAULT_INGREDIENTS)
            .iocCategoryId(DEFAULT_IOC_CATEGORY_ID)
            .isActive(DEFAULT_IS_ACTIVE)
            .isMerge(DEFAULT_IS_MERGE)
            .manufacturerId(DEFAULT_MANUFACTURER_ID)
            .manufacturerProductCode(DEFAULT_MANUFACTURER_PRODUCT_CODE)
            .mergeDate(DEFAULT_MERGE_DATE)
            .productId(DEFAULT_PRODUCT_ID)
            .productLabelPdfUrl(DEFAULT_PRODUCT_LABEL_PDF_URL)
            .productName(DEFAULT_PRODUCT_NAME)
            .protein(DEFAULT_PROTEIN)
            .proteinUom(DEFAULT_PROTEIN_UOM)
            .saturatedFat(DEFAULT_SATURATED_FAT)
            .serving(DEFAULT_SERVING)
            .servingUom(DEFAULT_SERVING_UOM)
            .sodium(DEFAULT_SODIUM)
            .sodiumUom(DEFAULT_SODIUM_UOM)
            .storageTypeId(DEFAULT_STORAGE_TYPE_ID)
            .subCategoryId(DEFAULT_SUB_CATEGORY_ID)
            .sugar(DEFAULT_SUGAR)
            .sugarUom(DEFAULT_SUGAR_UOM)
            .totalFat(DEFAULT_TOTAL_FAT)
            .transFat(DEFAULT_TRANS_FAT)
            .uPC(DEFAULT_U_PC)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedOn(DEFAULT_UPDATED_ON)
            .vendor(DEFAULT_VENDOR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductBeforeApprove createUpdatedEntity() {
        return new ProductBeforeApprove()
            .addedSugar(UPDATED_ADDED_SUGAR)
            .addedSugarUom(UPDATED_ADDED_SUGAR_UOM)
            .allergenKeywords(UPDATED_ALLERGEN_KEYWORDS)
            .brandName(UPDATED_BRAND_NAME)
            .calories(UPDATED_CALORIES)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .carbohydratesUom(UPDATED_CARBOHYDRATES_UOM)
            .categoryId(UPDATED_CATEGORY_ID)
            .cholesterol(UPDATED_CHOLESTEROL)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .description(UPDATED_DESCRIPTION)
            .dietaryFiber(UPDATED_DIETARY_FIBER)
            .dietaryFiberUom(UPDATED_DIETARY_FIBER_UOM)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .gTIN(UPDATED_G_TIN)
            .ingredients(UPDATED_INGREDIENTS)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .isMerge(UPDATED_IS_MERGE)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerProductCode(UPDATED_MANUFACTURER_PRODUCT_CODE)
            .mergeDate(UPDATED_MERGE_DATE)
            .productId(UPDATED_PRODUCT_ID)
            .productLabelPdfUrl(UPDATED_PRODUCT_LABEL_PDF_URL)
            .productName(UPDATED_PRODUCT_NAME)
            .protein(UPDATED_PROTEIN)
            .proteinUom(UPDATED_PROTEIN_UOM)
            .saturatedFat(UPDATED_SATURATED_FAT)
            .serving(UPDATED_SERVING)
            .servingUom(UPDATED_SERVING_UOM)
            .sodium(UPDATED_SODIUM)
            .sodiumUom(UPDATED_SODIUM_UOM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .subCategoryId(UPDATED_SUB_CATEGORY_ID)
            .sugar(UPDATED_SUGAR)
            .sugarUom(UPDATED_SUGAR_UOM)
            .totalFat(UPDATED_TOTAL_FAT)
            .transFat(UPDATED_TRANS_FAT)
            .uPC(UPDATED_U_PC)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .vendor(UPDATED_VENDOR);
    }

    @BeforeEach
    public void initTest() {
        productBeforeApprove = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductBeforeApprove != null) {
            productBeforeApproveRepository.delete(insertedProductBeforeApprove);
            productBeforeApproveSearchRepository.delete(insertedProductBeforeApprove);
            insertedProductBeforeApprove = null;
        }
    }

    @Test
    @Transactional
    void createProductBeforeApprove() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        // Create the ProductBeforeApprove
        var returnedProductBeforeApprove = om.readValue(
            restProductBeforeApproveMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeforeApprove)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductBeforeApprove.class
        );

        // Validate the ProductBeforeApprove in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductBeforeApproveUpdatableFieldsEquals(
            returnedProductBeforeApprove,
            getPersistedProductBeforeApprove(returnedProductBeforeApprove)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductBeforeApprove = returnedProductBeforeApprove;
    }

    @Test
    @Transactional
    void createProductBeforeApproveWithExistingId() throws Exception {
        // Create the ProductBeforeApprove with an existing ID
        productBeforeApprove.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductBeforeApproveMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeforeApprove)))
            .andExpect(status().isBadRequest());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductBeforeApproves() throws Exception {
        // Initialize the database
        insertedProductBeforeApprove = productBeforeApproveRepository.saveAndFlush(productBeforeApprove);

        // Get all the productBeforeApproveList
        restProductBeforeApproveMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productBeforeApprove.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedSugar").value(hasItem(DEFAULT_ADDED_SUGAR.doubleValue())))
            .andExpect(jsonPath("$.[*].addedSugarUom").value(hasItem(DEFAULT_ADDED_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].allergenKeywords").value(hasItem(DEFAULT_ALLERGEN_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].calories").value(hasItem(DEFAULT_CALORIES.doubleValue())))
            .andExpect(jsonPath("$.[*].caloriesUom").value(hasItem(DEFAULT_CALORIES_UOM)))
            .andExpect(jsonPath("$.[*].carbohydrates").value(hasItem(DEFAULT_CARBOHYDRATES.doubleValue())))
            .andExpect(jsonPath("$.[*].carbohydratesUom").value(hasItem(DEFAULT_CARBOHYDRATES_UOM)))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].cholesterol").value(hasItem(DEFAULT_CHOLESTEROL.doubleValue())))
            .andExpect(jsonPath("$.[*].cholesterolUOM").value(hasItem(DEFAULT_CHOLESTEROL_UOM)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dietaryFiber").value(hasItem(DEFAULT_DIETARY_FIBER.doubleValue())))
            .andExpect(jsonPath("$.[*].dietaryFiberUom").value(hasItem(DEFAULT_DIETARY_FIBER_UOM)))
            .andExpect(jsonPath("$.[*].distributorId").value(hasItem(DEFAULT_DISTRIBUTOR_ID.toString())))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN.toString())))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS.toString())))
            .andExpect(jsonPath("$.[*].iocCategoryId").value(hasItem(DEFAULT_IOC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isMerge").value(hasItem(DEFAULT_IS_MERGE.booleanValue())))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].manufacturerProductCode").value(hasItem(DEFAULT_MANUFACTURER_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].mergeDate").value(hasItem(DEFAULT_MERGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productLabelPdfUrl").value(hasItem(DEFAULT_PRODUCT_LABEL_PDF_URL.toString())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].protein").value(hasItem(DEFAULT_PROTEIN.doubleValue())))
            .andExpect(jsonPath("$.[*].proteinUom").value(hasItem(DEFAULT_PROTEIN_UOM)))
            .andExpect(jsonPath("$.[*].saturatedFat").value(hasItem(DEFAULT_SATURATED_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].servingUom").value(hasItem(DEFAULT_SERVING_UOM)))
            .andExpect(jsonPath("$.[*].sodium").value(hasItem(DEFAULT_SODIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].sodiumUom").value(hasItem(DEFAULT_SODIUM_UOM)))
            .andExpect(jsonPath("$.[*].storageTypeId").value(hasItem(DEFAULT_STORAGE_TYPE_ID)))
            .andExpect(jsonPath("$.[*].subCategoryId").value(hasItem(DEFAULT_SUB_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].sugar").value(hasItem(DEFAULT_SUGAR.doubleValue())))
            .andExpect(jsonPath("$.[*].sugarUom").value(hasItem(DEFAULT_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].totalFat").value(hasItem(DEFAULT_TOTAL_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].transFat").value(hasItem(DEFAULT_TRANS_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)));
    }

    @Test
    @Transactional
    void getProductBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductBeforeApprove = productBeforeApproveRepository.saveAndFlush(productBeforeApprove);

        // Get the productBeforeApprove
        restProductBeforeApproveMockMvc
            .perform(get(ENTITY_API_URL_ID, productBeforeApprove.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productBeforeApprove.getId().intValue()))
            .andExpect(jsonPath("$.addedSugar").value(DEFAULT_ADDED_SUGAR.doubleValue()))
            .andExpect(jsonPath("$.addedSugarUom").value(DEFAULT_ADDED_SUGAR_UOM))
            .andExpect(jsonPath("$.allergenKeywords").value(DEFAULT_ALLERGEN_KEYWORDS.toString()))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.calories").value(DEFAULT_CALORIES.doubleValue()))
            .andExpect(jsonPath("$.caloriesUom").value(DEFAULT_CALORIES_UOM))
            .andExpect(jsonPath("$.carbohydrates").value(DEFAULT_CARBOHYDRATES.doubleValue()))
            .andExpect(jsonPath("$.carbohydratesUom").value(DEFAULT_CARBOHYDRATES_UOM))
            .andExpect(jsonPath("$.categoryId").value(DEFAULT_CATEGORY_ID))
            .andExpect(jsonPath("$.cholesterol").value(DEFAULT_CHOLESTEROL.doubleValue()))
            .andExpect(jsonPath("$.cholesterolUOM").value(DEFAULT_CHOLESTEROL_UOM))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dietaryFiber").value(DEFAULT_DIETARY_FIBER.doubleValue()))
            .andExpect(jsonPath("$.dietaryFiberUom").value(DEFAULT_DIETARY_FIBER_UOM))
            .andExpect(jsonPath("$.distributorId").value(DEFAULT_DISTRIBUTOR_ID.toString()))
            .andExpect(jsonPath("$.gTIN").value(DEFAULT_G_TIN.toString()))
            .andExpect(jsonPath("$.ingredients").value(DEFAULT_INGREDIENTS.toString()))
            .andExpect(jsonPath("$.iocCategoryId").value(DEFAULT_IOC_CATEGORY_ID))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isMerge").value(DEFAULT_IS_MERGE.booleanValue()))
            .andExpect(jsonPath("$.manufacturerId").value(DEFAULT_MANUFACTURER_ID))
            .andExpect(jsonPath("$.manufacturerProductCode").value(DEFAULT_MANUFACTURER_PRODUCT_CODE))
            .andExpect(jsonPath("$.mergeDate").value(DEFAULT_MERGE_DATE.toString()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productLabelPdfUrl").value(DEFAULT_PRODUCT_LABEL_PDF_URL.toString()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME))
            .andExpect(jsonPath("$.protein").value(DEFAULT_PROTEIN.doubleValue()))
            .andExpect(jsonPath("$.proteinUom").value(DEFAULT_PROTEIN_UOM))
            .andExpect(jsonPath("$.saturatedFat").value(DEFAULT_SATURATED_FAT.doubleValue()))
            .andExpect(jsonPath("$.serving").value(DEFAULT_SERVING.doubleValue()))
            .andExpect(jsonPath("$.servingUom").value(DEFAULT_SERVING_UOM))
            .andExpect(jsonPath("$.sodium").value(DEFAULT_SODIUM.doubleValue()))
            .andExpect(jsonPath("$.sodiumUom").value(DEFAULT_SODIUM_UOM))
            .andExpect(jsonPath("$.storageTypeId").value(DEFAULT_STORAGE_TYPE_ID))
            .andExpect(jsonPath("$.subCategoryId").value(DEFAULT_SUB_CATEGORY_ID))
            .andExpect(jsonPath("$.sugar").value(DEFAULT_SUGAR.doubleValue()))
            .andExpect(jsonPath("$.sugarUom").value(DEFAULT_SUGAR_UOM))
            .andExpect(jsonPath("$.totalFat").value(DEFAULT_TOTAL_FAT.doubleValue()))
            .andExpect(jsonPath("$.transFat").value(DEFAULT_TRANS_FAT.doubleValue()))
            .andExpect(jsonPath("$.uPC").value(DEFAULT_U_PC.toString()))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR));
    }

    @Test
    @Transactional
    void getNonExistingProductBeforeApprove() throws Exception {
        // Get the productBeforeApprove
        restProductBeforeApproveMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductBeforeApprove = productBeforeApproveRepository.saveAndFlush(productBeforeApprove);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productBeforeApproveSearchRepository.save(productBeforeApprove);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());

        // Update the productBeforeApprove
        ProductBeforeApprove updatedProductBeforeApprove = productBeforeApproveRepository
            .findById(productBeforeApprove.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedProductBeforeApprove are not directly saved in db
        em.detach(updatedProductBeforeApprove);
        updatedProductBeforeApprove
            .addedSugar(UPDATED_ADDED_SUGAR)
            .addedSugarUom(UPDATED_ADDED_SUGAR_UOM)
            .allergenKeywords(UPDATED_ALLERGEN_KEYWORDS)
            .brandName(UPDATED_BRAND_NAME)
            .calories(UPDATED_CALORIES)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .carbohydratesUom(UPDATED_CARBOHYDRATES_UOM)
            .categoryId(UPDATED_CATEGORY_ID)
            .cholesterol(UPDATED_CHOLESTEROL)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .description(UPDATED_DESCRIPTION)
            .dietaryFiber(UPDATED_DIETARY_FIBER)
            .dietaryFiberUom(UPDATED_DIETARY_FIBER_UOM)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .gTIN(UPDATED_G_TIN)
            .ingredients(UPDATED_INGREDIENTS)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .isMerge(UPDATED_IS_MERGE)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerProductCode(UPDATED_MANUFACTURER_PRODUCT_CODE)
            .mergeDate(UPDATED_MERGE_DATE)
            .productId(UPDATED_PRODUCT_ID)
            .productLabelPdfUrl(UPDATED_PRODUCT_LABEL_PDF_URL)
            .productName(UPDATED_PRODUCT_NAME)
            .protein(UPDATED_PROTEIN)
            .proteinUom(UPDATED_PROTEIN_UOM)
            .saturatedFat(UPDATED_SATURATED_FAT)
            .serving(UPDATED_SERVING)
            .servingUom(UPDATED_SERVING_UOM)
            .sodium(UPDATED_SODIUM)
            .sodiumUom(UPDATED_SODIUM_UOM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .subCategoryId(UPDATED_SUB_CATEGORY_ID)
            .sugar(UPDATED_SUGAR)
            .sugarUom(UPDATED_SUGAR_UOM)
            .totalFat(UPDATED_TOTAL_FAT)
            .transFat(UPDATED_TRANS_FAT)
            .uPC(UPDATED_U_PC)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .vendor(UPDATED_VENDOR);

        restProductBeforeApproveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductBeforeApprove.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductBeforeApprove))
            )
            .andExpect(status().isOk());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductBeforeApproveToMatchAllProperties(updatedProductBeforeApprove);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductBeforeApprove> productBeforeApproveSearchList = Streamable.of(
                    productBeforeApproveSearchRepository.findAll()
                ).toList();
                ProductBeforeApprove testProductBeforeApproveSearch = productBeforeApproveSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductBeforeApproveAllPropertiesEquals(testProductBeforeApproveSearch, updatedProductBeforeApprove);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        productBeforeApprove.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBeforeApproveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productBeforeApprove.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        productBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBeforeApproveMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        productBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBeforeApproveMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productBeforeApprove)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductBeforeApproveWithPatch() throws Exception {
        // Initialize the database
        insertedProductBeforeApprove = productBeforeApproveRepository.saveAndFlush(productBeforeApprove);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productBeforeApprove using partial update
        ProductBeforeApprove partialUpdatedProductBeforeApprove = new ProductBeforeApprove();
        partialUpdatedProductBeforeApprove.setId(productBeforeApprove.getId());

        partialUpdatedProductBeforeApprove
            .allergenKeywords(UPDATED_ALLERGEN_KEYWORDS)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .description(UPDATED_DESCRIPTION)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .ingredients(UPDATED_INGREDIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .mergeDate(UPDATED_MERGE_DATE)
            .productId(UPDATED_PRODUCT_ID)
            .sugarUom(UPDATED_SUGAR_UOM)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .vendor(UPDATED_VENDOR);

        restProductBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductBeforeApprove.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductBeforeApprove))
            )
            .andExpect(status().isOk());

        // Validate the ProductBeforeApprove in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductBeforeApproveUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductBeforeApprove, productBeforeApprove),
            getPersistedProductBeforeApprove(productBeforeApprove)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductBeforeApproveWithPatch() throws Exception {
        // Initialize the database
        insertedProductBeforeApprove = productBeforeApproveRepository.saveAndFlush(productBeforeApprove);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productBeforeApprove using partial update
        ProductBeforeApprove partialUpdatedProductBeforeApprove = new ProductBeforeApprove();
        partialUpdatedProductBeforeApprove.setId(productBeforeApprove.getId());

        partialUpdatedProductBeforeApprove
            .addedSugar(UPDATED_ADDED_SUGAR)
            .addedSugarUom(UPDATED_ADDED_SUGAR_UOM)
            .allergenKeywords(UPDATED_ALLERGEN_KEYWORDS)
            .brandName(UPDATED_BRAND_NAME)
            .calories(UPDATED_CALORIES)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .carbohydratesUom(UPDATED_CARBOHYDRATES_UOM)
            .categoryId(UPDATED_CATEGORY_ID)
            .cholesterol(UPDATED_CHOLESTEROL)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdBy(UPDATED_CREATED_BY)
            .createdOn(UPDATED_CREATED_ON)
            .description(UPDATED_DESCRIPTION)
            .dietaryFiber(UPDATED_DIETARY_FIBER)
            .dietaryFiberUom(UPDATED_DIETARY_FIBER_UOM)
            .distributorId(UPDATED_DISTRIBUTOR_ID)
            .gTIN(UPDATED_G_TIN)
            .ingredients(UPDATED_INGREDIENTS)
            .iocCategoryId(UPDATED_IOC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .isMerge(UPDATED_IS_MERGE)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerProductCode(UPDATED_MANUFACTURER_PRODUCT_CODE)
            .mergeDate(UPDATED_MERGE_DATE)
            .productId(UPDATED_PRODUCT_ID)
            .productLabelPdfUrl(UPDATED_PRODUCT_LABEL_PDF_URL)
            .productName(UPDATED_PRODUCT_NAME)
            .protein(UPDATED_PROTEIN)
            .proteinUom(UPDATED_PROTEIN_UOM)
            .saturatedFat(UPDATED_SATURATED_FAT)
            .serving(UPDATED_SERVING)
            .servingUom(UPDATED_SERVING_UOM)
            .sodium(UPDATED_SODIUM)
            .sodiumUom(UPDATED_SODIUM_UOM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .subCategoryId(UPDATED_SUB_CATEGORY_ID)
            .sugar(UPDATED_SUGAR)
            .sugarUom(UPDATED_SUGAR_UOM)
            .totalFat(UPDATED_TOTAL_FAT)
            .transFat(UPDATED_TRANS_FAT)
            .uPC(UPDATED_U_PC)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .vendor(UPDATED_VENDOR);

        restProductBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductBeforeApprove.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductBeforeApprove))
            )
            .andExpect(status().isOk());

        // Validate the ProductBeforeApprove in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductBeforeApproveUpdatableFieldsEquals(
            partialUpdatedProductBeforeApprove,
            getPersistedProductBeforeApprove(partialUpdatedProductBeforeApprove)
        );
    }

    @Test
    @Transactional
    void patchNonExistingProductBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        productBeforeApprove.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productBeforeApprove.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        productBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBeforeApproveMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productBeforeApprove))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductBeforeApprove() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        productBeforeApprove.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductBeforeApproveMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productBeforeApprove)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductBeforeApprove in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductBeforeApprove = productBeforeApproveRepository.saveAndFlush(productBeforeApprove);
        productBeforeApproveRepository.save(productBeforeApprove);
        productBeforeApproveSearchRepository.save(productBeforeApprove);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productBeforeApprove
        restProductBeforeApproveMockMvc
            .perform(delete(ENTITY_API_URL_ID, productBeforeApprove.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productBeforeApproveSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductBeforeApprove() throws Exception {
        // Initialize the database
        insertedProductBeforeApprove = productBeforeApproveRepository.saveAndFlush(productBeforeApprove);
        productBeforeApproveSearchRepository.save(productBeforeApprove);

        // Search the productBeforeApprove
        restProductBeforeApproveMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productBeforeApprove.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productBeforeApprove.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedSugar").value(hasItem(DEFAULT_ADDED_SUGAR.doubleValue())))
            .andExpect(jsonPath("$.[*].addedSugarUom").value(hasItem(DEFAULT_ADDED_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].allergenKeywords").value(hasItem(DEFAULT_ALLERGEN_KEYWORDS.toString())))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].calories").value(hasItem(DEFAULT_CALORIES.doubleValue())))
            .andExpect(jsonPath("$.[*].caloriesUom").value(hasItem(DEFAULT_CALORIES_UOM)))
            .andExpect(jsonPath("$.[*].carbohydrates").value(hasItem(DEFAULT_CARBOHYDRATES.doubleValue())))
            .andExpect(jsonPath("$.[*].carbohydratesUom").value(hasItem(DEFAULT_CARBOHYDRATES_UOM)))
            .andExpect(jsonPath("$.[*].categoryId").value(hasItem(DEFAULT_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].cholesterol").value(hasItem(DEFAULT_CHOLESTEROL.doubleValue())))
            .andExpect(jsonPath("$.[*].cholesterolUOM").value(hasItem(DEFAULT_CHOLESTEROL_UOM)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dietaryFiber").value(hasItem(DEFAULT_DIETARY_FIBER.doubleValue())))
            .andExpect(jsonPath("$.[*].dietaryFiberUom").value(hasItem(DEFAULT_DIETARY_FIBER_UOM)))
            .andExpect(jsonPath("$.[*].distributorId").value(hasItem(DEFAULT_DISTRIBUTOR_ID.toString())))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN.toString())))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS.toString())))
            .andExpect(jsonPath("$.[*].iocCategoryId").value(hasItem(DEFAULT_IOC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isMerge").value(hasItem(DEFAULT_IS_MERGE.booleanValue())))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].manufacturerProductCode").value(hasItem(DEFAULT_MANUFACTURER_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].mergeDate").value(hasItem(DEFAULT_MERGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productLabelPdfUrl").value(hasItem(DEFAULT_PRODUCT_LABEL_PDF_URL.toString())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME)))
            .andExpect(jsonPath("$.[*].protein").value(hasItem(DEFAULT_PROTEIN.doubleValue())))
            .andExpect(jsonPath("$.[*].proteinUom").value(hasItem(DEFAULT_PROTEIN_UOM)))
            .andExpect(jsonPath("$.[*].saturatedFat").value(hasItem(DEFAULT_SATURATED_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].servingUom").value(hasItem(DEFAULT_SERVING_UOM)))
            .andExpect(jsonPath("$.[*].sodium").value(hasItem(DEFAULT_SODIUM.doubleValue())))
            .andExpect(jsonPath("$.[*].sodiumUom").value(hasItem(DEFAULT_SODIUM_UOM)))
            .andExpect(jsonPath("$.[*].storageTypeId").value(hasItem(DEFAULT_STORAGE_TYPE_ID)))
            .andExpect(jsonPath("$.[*].subCategoryId").value(hasItem(DEFAULT_SUB_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].sugar").value(hasItem(DEFAULT_SUGAR.doubleValue())))
            .andExpect(jsonPath("$.[*].sugarUom").value(hasItem(DEFAULT_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].totalFat").value(hasItem(DEFAULT_TOTAL_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].transFat").value(hasItem(DEFAULT_TRANS_FAT.doubleValue())))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC.toString())))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)));
    }

    protected long getRepositoryCount() {
        return productBeforeApproveRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProductBeforeApprove getPersistedProductBeforeApprove(ProductBeforeApprove productBeforeApprove) {
        return productBeforeApproveRepository.findById(productBeforeApprove.getId()).orElseThrow();
    }

    protected void assertPersistedProductBeforeApproveToMatchAllProperties(ProductBeforeApprove expectedProductBeforeApprove) {
        assertProductBeforeApproveAllPropertiesEquals(
            expectedProductBeforeApprove,
            getPersistedProductBeforeApprove(expectedProductBeforeApprove)
        );
    }

    protected void assertPersistedProductBeforeApproveToMatchUpdatableProperties(ProductBeforeApprove expectedProductBeforeApprove) {
        assertProductBeforeApproveAllUpdatablePropertiesEquals(
            expectedProductBeforeApprove,
            getPersistedProductBeforeApprove(expectedProductBeforeApprove)
        );
    }
}
