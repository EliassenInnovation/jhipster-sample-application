package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProductMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProductMst;
import com.mycompany.myapp.repository.ProductMstRepository;
import com.mycompany.myapp.repository.search.ProductMstSearchRepository;
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
 * Integration tests for the {@link ProductMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProductMstResourceIT {

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

    private static final String DEFAULT_G_TIN = "AAAAAAAAAA";
    private static final String UPDATED_G_TIN = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTS = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTS = "BBBBBBBBBB";

    private static final Integer DEFAULT_I_OC_CATEGORY_ID = 1;
    private static final Integer UPDATED_I_OC_CATEGORY_ID = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_MERGE = false;
    private static final Boolean UPDATED_IS_MERGE = true;

    private static final Boolean DEFAULT_IS_ONE_WORLD_SYNC_PRODUCT = false;
    private static final Boolean UPDATED_IS_ONE_WORLD_SYNC_PRODUCT = true;

    private static final Integer DEFAULT_MANUFACTURER_ID = 1;
    private static final Integer UPDATED_MANUFACTURER_ID = 2;

    private static final String DEFAULT_MANUFACTURER_PRODUCT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_PRODUCT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER_TEXT_1_WS = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_TEXT_1_WS = "BBBBBBBBBB";

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

    private static final String DEFAULT_U_PCGTIN = "AAAAAAAAAA";
    private static final String UPDATED_U_PCGTIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_UPDATED_BY = 1;
    private static final Integer UPDATED_UPDATED_BY = 2;

    private static final LocalDate DEFAULT_UPDATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/product-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/product-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProductMstRepository productMstRepository;

    @Autowired
    private ProductMstSearchRepository productMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMstMockMvc;

    private ProductMst productMst;

    private ProductMst insertedProductMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductMst createEntity() {
        return new ProductMst()
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
            .gTIN(DEFAULT_G_TIN)
            .ingredients(DEFAULT_INGREDIENTS)
            .iOCCategoryId(DEFAULT_I_OC_CATEGORY_ID)
            .isActive(DEFAULT_IS_ACTIVE)
            .isMerge(DEFAULT_IS_MERGE)
            .isOneWorldSyncProduct(DEFAULT_IS_ONE_WORLD_SYNC_PRODUCT)
            .manufacturerId(DEFAULT_MANUFACTURER_ID)
            .manufacturerProductCode(DEFAULT_MANUFACTURER_PRODUCT_CODE)
            .manufacturerText1Ws(DEFAULT_MANUFACTURER_TEXT_1_WS)
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
            .uPCGTIN(DEFAULT_U_PCGTIN)
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
    public static ProductMst createUpdatedEntity() {
        return new ProductMst()
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
            .gTIN(UPDATED_G_TIN)
            .ingredients(UPDATED_INGREDIENTS)
            .iOCCategoryId(UPDATED_I_OC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .isMerge(UPDATED_IS_MERGE)
            .isOneWorldSyncProduct(UPDATED_IS_ONE_WORLD_SYNC_PRODUCT)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerProductCode(UPDATED_MANUFACTURER_PRODUCT_CODE)
            .manufacturerText1Ws(UPDATED_MANUFACTURER_TEXT_1_WS)
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
            .uPCGTIN(UPDATED_U_PCGTIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .vendor(UPDATED_VENDOR);
    }

    @BeforeEach
    public void initTest() {
        productMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProductMst != null) {
            productMstRepository.delete(insertedProductMst);
            productMstSearchRepository.delete(insertedProductMst);
            insertedProductMst = null;
        }
    }

    @Test
    @Transactional
    void createProductMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        // Create the ProductMst
        var returnedProductMst = om.readValue(
            restProductMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProductMst.class
        );

        // Validate the ProductMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertProductMstUpdatableFieldsEquals(returnedProductMst, getPersistedProductMst(returnedProductMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedProductMst = returnedProductMst;
    }

    @Test
    @Transactional
    void createProductMstWithExistingId() throws Exception {
        // Create the ProductMst with an existing ID
        productMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMst)))
            .andExpect(status().isBadRequest());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllProductMsts() throws Exception {
        // Initialize the database
        insertedProductMst = productMstRepository.saveAndFlush(productMst);

        // Get all the productMstList
        restProductMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productMst.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS.toString())))
            .andExpect(jsonPath("$.[*].iOCCategoryId").value(hasItem(DEFAULT_I_OC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isMerge").value(hasItem(DEFAULT_IS_MERGE.booleanValue())))
            .andExpect(jsonPath("$.[*].isOneWorldSyncProduct").value(hasItem(DEFAULT_IS_ONE_WORLD_SYNC_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].manufacturerProductCode").value(hasItem(DEFAULT_MANUFACTURER_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].manufacturerText1Ws").value(hasItem(DEFAULT_MANUFACTURER_TEXT_1_WS)))
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
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)))
            .andExpect(jsonPath("$.[*].uPCGTIN").value(hasItem(DEFAULT_U_PCGTIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)));
    }

    @Test
    @Transactional
    void getProductMst() throws Exception {
        // Initialize the database
        insertedProductMst = productMstRepository.saveAndFlush(productMst);

        // Get the productMst
        restProductMstMockMvc
            .perform(get(ENTITY_API_URL_ID, productMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productMst.getId().intValue()))
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
            .andExpect(jsonPath("$.gTIN").value(DEFAULT_G_TIN))
            .andExpect(jsonPath("$.ingredients").value(DEFAULT_INGREDIENTS.toString()))
            .andExpect(jsonPath("$.iOCCategoryId").value(DEFAULT_I_OC_CATEGORY_ID))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isMerge").value(DEFAULT_IS_MERGE.booleanValue()))
            .andExpect(jsonPath("$.isOneWorldSyncProduct").value(DEFAULT_IS_ONE_WORLD_SYNC_PRODUCT.booleanValue()))
            .andExpect(jsonPath("$.manufacturerId").value(DEFAULT_MANUFACTURER_ID))
            .andExpect(jsonPath("$.manufacturerProductCode").value(DEFAULT_MANUFACTURER_PRODUCT_CODE))
            .andExpect(jsonPath("$.manufacturerText1Ws").value(DEFAULT_MANUFACTURER_TEXT_1_WS))
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
            .andExpect(jsonPath("$.uPC").value(DEFAULT_U_PC))
            .andExpect(jsonPath("$.uPCGTIN").value(DEFAULT_U_PCGTIN))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedOn").value(DEFAULT_UPDATED_ON.toString()))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR));
    }

    @Test
    @Transactional
    void getNonExistingProductMst() throws Exception {
        // Get the productMst
        restProductMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductMst() throws Exception {
        // Initialize the database
        insertedProductMst = productMstRepository.saveAndFlush(productMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        productMstSearchRepository.save(productMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());

        // Update the productMst
        ProductMst updatedProductMst = productMstRepository.findById(productMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProductMst are not directly saved in db
        em.detach(updatedProductMst);
        updatedProductMst
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
            .gTIN(UPDATED_G_TIN)
            .ingredients(UPDATED_INGREDIENTS)
            .iOCCategoryId(UPDATED_I_OC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .isMerge(UPDATED_IS_MERGE)
            .isOneWorldSyncProduct(UPDATED_IS_ONE_WORLD_SYNC_PRODUCT)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerProductCode(UPDATED_MANUFACTURER_PRODUCT_CODE)
            .manufacturerText1Ws(UPDATED_MANUFACTURER_TEXT_1_WS)
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
            .uPCGTIN(UPDATED_U_PCGTIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .vendor(UPDATED_VENDOR);

        restProductMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProductMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedProductMst))
            )
            .andExpect(status().isOk());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProductMstToMatchAllProperties(updatedProductMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ProductMst> productMstSearchList = Streamable.of(productMstSearchRepository.findAll()).toList();
                ProductMst testProductMstSearch = productMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertProductMstAllPropertiesEquals(testProductMstSearch, updatedProductMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingProductMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        productMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productMst.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        productMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(productMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        productMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(productMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateProductMstWithPatch() throws Exception {
        // Initialize the database
        insertedProductMst = productMstRepository.saveAndFlush(productMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMst using partial update
        ProductMst partialUpdatedProductMst = new ProductMst();
        partialUpdatedProductMst.setId(productMst.getId());

        partialUpdatedProductMst
            .addedSugar(UPDATED_ADDED_SUGAR)
            .addedSugarUom(UPDATED_ADDED_SUGAR_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .carbohydratesUom(UPDATED_CARBOHYDRATES_UOM)
            .cholesterol(UPDATED_CHOLESTEROL)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdOn(UPDATED_CREATED_ON)
            .description(UPDATED_DESCRIPTION)
            .gTIN(UPDATED_G_TIN)
            .ingredients(UPDATED_INGREDIENTS)
            .iOCCategoryId(UPDATED_I_OC_CATEGORY_ID)
            .manufacturerProductCode(UPDATED_MANUFACTURER_PRODUCT_CODE)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .protein(UPDATED_PROTEIN)
            .serving(UPDATED_SERVING)
            .sodium(UPDATED_SODIUM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .sugarUom(UPDATED_SUGAR_UOM)
            .transFat(UPDATED_TRANS_FAT)
            .uPC(UPDATED_U_PC)
            .uPCGTIN(UPDATED_U_PCGTIN);

        restProductMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductMst))
            )
            .andExpect(status().isOk());

        // Validate the ProductMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductMstUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProductMst, productMst),
            getPersistedProductMst(productMst)
        );
    }

    @Test
    @Transactional
    void fullUpdateProductMstWithPatch() throws Exception {
        // Initialize the database
        insertedProductMst = productMstRepository.saveAndFlush(productMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the productMst using partial update
        ProductMst partialUpdatedProductMst = new ProductMst();
        partialUpdatedProductMst.setId(productMst.getId());

        partialUpdatedProductMst
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
            .gTIN(UPDATED_G_TIN)
            .ingredients(UPDATED_INGREDIENTS)
            .iOCCategoryId(UPDATED_I_OC_CATEGORY_ID)
            .isActive(UPDATED_IS_ACTIVE)
            .isMerge(UPDATED_IS_MERGE)
            .isOneWorldSyncProduct(UPDATED_IS_ONE_WORLD_SYNC_PRODUCT)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerProductCode(UPDATED_MANUFACTURER_PRODUCT_CODE)
            .manufacturerText1Ws(UPDATED_MANUFACTURER_TEXT_1_WS)
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
            .uPCGTIN(UPDATED_U_PCGTIN)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedOn(UPDATED_UPDATED_ON)
            .vendor(UPDATED_VENDOR);

        restProductMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProductMst))
            )
            .andExpect(status().isOk());

        // Validate the ProductMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProductMstUpdatableFieldsEquals(partialUpdatedProductMst, getPersistedProductMst(partialUpdatedProductMst));
    }

    @Test
    @Transactional
    void patchNonExistingProductMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        productMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        productMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(productMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        productMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(productMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteProductMst() throws Exception {
        // Initialize the database
        insertedProductMst = productMstRepository.saveAndFlush(productMst);
        productMstRepository.save(productMst);
        productMstSearchRepository.save(productMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the productMst
        restProductMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, productMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(productMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchProductMst() throws Exception {
        // Initialize the database
        insertedProductMst = productMstRepository.saveAndFlush(productMst);
        productMstSearchRepository.save(productMst);

        // Search the productMst
        restProductMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + productMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productMst.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS.toString())))
            .andExpect(jsonPath("$.[*].iOCCategoryId").value(hasItem(DEFAULT_I_OC_CATEGORY_ID)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isMerge").value(hasItem(DEFAULT_IS_MERGE.booleanValue())))
            .andExpect(jsonPath("$.[*].isOneWorldSyncProduct").value(hasItem(DEFAULT_IS_ONE_WORLD_SYNC_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].manufacturerProductCode").value(hasItem(DEFAULT_MANUFACTURER_PRODUCT_CODE)))
            .andExpect(jsonPath("$.[*].manufacturerText1Ws").value(hasItem(DEFAULT_MANUFACTURER_TEXT_1_WS)))
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
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)))
            .andExpect(jsonPath("$.[*].uPCGTIN").value(hasItem(DEFAULT_U_PCGTIN)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedOn").value(hasItem(DEFAULT_UPDATED_ON.toString())))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)));
    }

    protected long getRepositoryCount() {
        return productMstRepository.count();
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

    protected ProductMst getPersistedProductMst(ProductMst productMst) {
        return productMstRepository.findById(productMst.getId()).orElseThrow();
    }

    protected void assertPersistedProductMstToMatchAllProperties(ProductMst expectedProductMst) {
        assertProductMstAllPropertiesEquals(expectedProductMst, getPersistedProductMst(expectedProductMst));
    }

    protected void assertPersistedProductMstToMatchUpdatableProperties(ProductMst expectedProductMst) {
        assertProductMstAllUpdatablePropertiesEquals(expectedProductMst, getPersistedProductMst(expectedProductMst));
    }
}
