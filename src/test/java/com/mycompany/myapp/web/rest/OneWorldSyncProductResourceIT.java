package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.OneWorldSyncProductAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.OneWorldSyncProduct;
import com.mycompany.myapp.repository.OneWorldSyncProductRepository;
import com.mycompany.myapp.repository.search.OneWorldSyncProductSearchRepository;
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
 * Integration tests for the {@link OneWorldSyncProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OneWorldSyncProductResourceIT {

    private static final String DEFAULT_ADDED_SUGARS = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_SUGARS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDED_SUGAR_UOM = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_SUGAR_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_ALLERGEN_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_ALLERGEN_KEYWORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_ALLERGENS = 1;
    private static final Integer UPDATED_ALLERGENS = 2;

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CALORIES = "AAAAAAAAAA";
    private static final String UPDATED_CALORIES = "BBBBBBBBBB";

    private static final String DEFAULT_CALORIES_UOM = "AAAAAAAAAA";
    private static final String UPDATED_CALORIES_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_CARBOHYDRATES = "AAAAAAAAAA";
    private static final String UPDATED_CARBOHYDRATES = "BBBBBBBBBB";

    private static final String DEFAULT_CARBOHYDRATES_UOM = "AAAAAAAAAA";
    private static final String UPDATED_CARBOHYDRATES_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CHOLESTEROL = "AAAAAAAAAA";
    private static final String UPDATED_CHOLESTEROL = "BBBBBBBBBB";

    private static final String DEFAULT_CHOLESTEROL_UOM = "AAAAAAAAAA";
    private static final String UPDATED_CHOLESTEROL_UOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DATA_FORM = "AAAAAAAAAA";
    private static final String UPDATED_DATA_FORM = "BBBBBBBBBB";

    private static final String DEFAULT_DIETARY_FIBER = "AAAAAAAAAA";
    private static final String UPDATED_DIETARY_FIBER = "BBBBBBBBBB";

    private static final String DEFAULT_DIETARY_FIBER_UOM = "AAAAAAAAAA";
    private static final String UPDATED_DIETARY_FIBER_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRIBUTOR = "AAAAAAAAAA";
    private static final String UPDATED_DISTRIBUTOR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DO_NOT_CONSIDER_PRODUCT = false;
    private static final Boolean UPDATED_DO_NOT_CONSIDER_PRODUCT = true;

    private static final String DEFAULT_EXTENDED_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_EXTENDED_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_G_LN_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_G_LN_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_G_TIN = "AAAAAAAAAA";
    private static final String UPDATED_G_TIN = "BBBBBBBBBB";

    private static final Integer DEFAULT_H_7 = 1;
    private static final Integer UPDATED_H_7 = 2;

    private static final String DEFAULT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTS = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_APPROVE = false;
    private static final Boolean UPDATED_IS_APPROVE = true;

    private static final Boolean DEFAULT_IS_MERGE = false;
    private static final Boolean UPDATED_IS_MERGE = true;

    private static final Boolean DEFAULT_IS_PRODUCT_SYNC = false;
    private static final Boolean UPDATED_IS_PRODUCT_SYNC = true;

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final Integer DEFAULT_MANUFACTURER_ID = 1;
    private static final Integer UPDATED_MANUFACTURER_ID = 2;

    private static final String DEFAULT_MANUFACTURER_TEXT_1_WS = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER_TEXT_1_WS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_MODIFIED_ON = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MODIFIED_ON = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_PRODUCT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PRODUCT_ID = 1L;
    private static final Long UPDATED_PRODUCT_ID = 2L;

    private static final String DEFAULT_PRODUCT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROTEIN = "AAAAAAAAAA";
    private static final String UPDATED_PROTEIN = "BBBBBBBBBB";

    private static final String DEFAULT_PROTEIN_UOM = "AAAAAAAAAA";
    private static final String UPDATED_PROTEIN_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_SATURATED_FAT = "AAAAAAAAAA";
    private static final String UPDATED_SATURATED_FAT = "BBBBBBBBBB";

    private static final String DEFAULT_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_SERVING_UOM = "AAAAAAAAAA";
    private static final String UPDATED_SERVING_UOM = "BBBBBBBBBB";

    private static final String DEFAULT_SODIUM = "AAAAAAAAAA";
    private static final String UPDATED_SODIUM = "BBBBBBBBBB";

    private static final String DEFAULT_SODIUM_UOM = "AAAAAAAAAA";
    private static final String UPDATED_SODIUM_UOM = "BBBBBBBBBB";

    private static final Integer DEFAULT_STORAGE_TYPE_ID = 1;
    private static final Integer UPDATED_STORAGE_TYPE_ID = 2;

    private static final String DEFAULT_STORAGE_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_STORAGE_TYPE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_CATEGORY_1_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY_1_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_CATEGORY_2_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SUB_CATEGORY_2_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUGAR = "AAAAAAAAAA";
    private static final String UPDATED_SUGAR = "BBBBBBBBBB";

    private static final String DEFAULT_SUGAR_UOM = "AAAAAAAAAA";
    private static final String UPDATED_SUGAR_UOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SYNC_EFFECTIVE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SYNC_EFFECTIVE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SYNC_HEADER_LAST_CHANGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SYNC_HEADER_LAST_CHANGE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SYNC_ITEM_REFERENCE_ID = "AAAAAAAAAA";
    private static final String UPDATED_SYNC_ITEM_REFERENCE_ID = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SYNC_LAST_CHANGE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SYNC_LAST_CHANGE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SYNC_PUBLICATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SYNC_PUBLICATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TOTAL_FAT = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_FAT = "BBBBBBBBBB";

    private static final String DEFAULT_TRANS_FAT = "AAAAAAAAAA";
    private static final String UPDATED_TRANS_FAT = "BBBBBBBBBB";

    private static final String DEFAULT_U_PC = "AAAAAAAAAA";
    private static final String UPDATED_U_PC = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/one-world-sync-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/one-world-sync-products/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OneWorldSyncProductRepository oneWorldSyncProductRepository;

    @Autowired
    private OneWorldSyncProductSearchRepository oneWorldSyncProductSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOneWorldSyncProductMockMvc;

    private OneWorldSyncProduct oneWorldSyncProduct;

    private OneWorldSyncProduct insertedOneWorldSyncProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OneWorldSyncProduct createEntity() {
        return new OneWorldSyncProduct()
            .addedSugars(DEFAULT_ADDED_SUGARS)
            .addedSugarUom(DEFAULT_ADDED_SUGAR_UOM)
            .allergenKeyword(DEFAULT_ALLERGEN_KEYWORD)
            .allergens(DEFAULT_ALLERGENS)
            .brandName(DEFAULT_BRAND_NAME)
            .calories(DEFAULT_CALORIES)
            .caloriesUom(DEFAULT_CALORIES_UOM)
            .carbohydrates(DEFAULT_CARBOHYDRATES)
            .carbohydratesUom(DEFAULT_CARBOHYDRATES_UOM)
            .categoryName(DEFAULT_CATEGORY_NAME)
            .cholesterol(DEFAULT_CHOLESTEROL)
            .cholesterolUOM(DEFAULT_CHOLESTEROL_UOM)
            .createdOn(DEFAULT_CREATED_ON)
            .dataForm(DEFAULT_DATA_FORM)
            .dietaryFiber(DEFAULT_DIETARY_FIBER)
            .dietaryFiberUom(DEFAULT_DIETARY_FIBER_UOM)
            .distributor(DEFAULT_DISTRIBUTOR)
            .doNotConsiderProduct(DEFAULT_DO_NOT_CONSIDER_PRODUCT)
            .extendedModel(DEFAULT_EXTENDED_MODEL)
            .gLNNumber(DEFAULT_G_LN_NUMBER)
            .gTIN(DEFAULT_G_TIN)
            .h7(DEFAULT_H_7)
            .image(DEFAULT_IMAGE)
            .ingredients(DEFAULT_INGREDIENTS)
            .isActive(DEFAULT_IS_ACTIVE)
            .isApprove(DEFAULT_IS_APPROVE)
            .isMerge(DEFAULT_IS_MERGE)
            .isProductSync(DEFAULT_IS_PRODUCT_SYNC)
            .manufacturer(DEFAULT_MANUFACTURER)
            .manufacturerId(DEFAULT_MANUFACTURER_ID)
            .manufacturerText1Ws(DEFAULT_MANUFACTURER_TEXT_1_WS)
            .modifiedOn(DEFAULT_MODIFIED_ON)
            .productDescription(DEFAULT_PRODUCT_DESCRIPTION)
            .productId(DEFAULT_PRODUCT_ID)
            .productName(DEFAULT_PRODUCT_NAME)
            .protein(DEFAULT_PROTEIN)
            .proteinUom(DEFAULT_PROTEIN_UOM)
            .saturatedFat(DEFAULT_SATURATED_FAT)
            .serving(DEFAULT_SERVING)
            .servingUom(DEFAULT_SERVING_UOM)
            .sodium(DEFAULT_SODIUM)
            .sodiumUom(DEFAULT_SODIUM_UOM)
            .storageTypeId(DEFAULT_STORAGE_TYPE_ID)
            .storageTypeName(DEFAULT_STORAGE_TYPE_NAME)
            .subCategory1Name(DEFAULT_SUB_CATEGORY_1_NAME)
            .subCategory2Name(DEFAULT_SUB_CATEGORY_2_NAME)
            .sugar(DEFAULT_SUGAR)
            .sugarUom(DEFAULT_SUGAR_UOM)
            .syncEffective(DEFAULT_SYNC_EFFECTIVE)
            .syncHeaderLastChange(DEFAULT_SYNC_HEADER_LAST_CHANGE)
            .syncItemReferenceId(DEFAULT_SYNC_ITEM_REFERENCE_ID)
            .syncLastChange(DEFAULT_SYNC_LAST_CHANGE)
            .syncPublication(DEFAULT_SYNC_PUBLICATION)
            .totalFat(DEFAULT_TOTAL_FAT)
            .transFat(DEFAULT_TRANS_FAT)
            .uPC(DEFAULT_U_PC)
            .vendor(DEFAULT_VENDOR);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OneWorldSyncProduct createUpdatedEntity() {
        return new OneWorldSyncProduct()
            .addedSugars(UPDATED_ADDED_SUGARS)
            .addedSugarUom(UPDATED_ADDED_SUGAR_UOM)
            .allergenKeyword(UPDATED_ALLERGEN_KEYWORD)
            .allergens(UPDATED_ALLERGENS)
            .brandName(UPDATED_BRAND_NAME)
            .calories(UPDATED_CALORIES)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .carbohydratesUom(UPDATED_CARBOHYDRATES_UOM)
            .categoryName(UPDATED_CATEGORY_NAME)
            .cholesterol(UPDATED_CHOLESTEROL)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdOn(UPDATED_CREATED_ON)
            .dataForm(UPDATED_DATA_FORM)
            .dietaryFiber(UPDATED_DIETARY_FIBER)
            .dietaryFiberUom(UPDATED_DIETARY_FIBER_UOM)
            .distributor(UPDATED_DISTRIBUTOR)
            .doNotConsiderProduct(UPDATED_DO_NOT_CONSIDER_PRODUCT)
            .extendedModel(UPDATED_EXTENDED_MODEL)
            .gLNNumber(UPDATED_G_LN_NUMBER)
            .gTIN(UPDATED_G_TIN)
            .h7(UPDATED_H_7)
            .image(UPDATED_IMAGE)
            .ingredients(UPDATED_INGREDIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .isApprove(UPDATED_IS_APPROVE)
            .isMerge(UPDATED_IS_MERGE)
            .isProductSync(UPDATED_IS_PRODUCT_SYNC)
            .manufacturer(UPDATED_MANUFACTURER)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerText1Ws(UPDATED_MANUFACTURER_TEXT_1_WS)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .protein(UPDATED_PROTEIN)
            .proteinUom(UPDATED_PROTEIN_UOM)
            .saturatedFat(UPDATED_SATURATED_FAT)
            .serving(UPDATED_SERVING)
            .servingUom(UPDATED_SERVING_UOM)
            .sodium(UPDATED_SODIUM)
            .sodiumUom(UPDATED_SODIUM_UOM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .storageTypeName(UPDATED_STORAGE_TYPE_NAME)
            .subCategory1Name(UPDATED_SUB_CATEGORY_1_NAME)
            .subCategory2Name(UPDATED_SUB_CATEGORY_2_NAME)
            .sugar(UPDATED_SUGAR)
            .sugarUom(UPDATED_SUGAR_UOM)
            .syncEffective(UPDATED_SYNC_EFFECTIVE)
            .syncHeaderLastChange(UPDATED_SYNC_HEADER_LAST_CHANGE)
            .syncItemReferenceId(UPDATED_SYNC_ITEM_REFERENCE_ID)
            .syncLastChange(UPDATED_SYNC_LAST_CHANGE)
            .syncPublication(UPDATED_SYNC_PUBLICATION)
            .totalFat(UPDATED_TOTAL_FAT)
            .transFat(UPDATED_TRANS_FAT)
            .uPC(UPDATED_U_PC)
            .vendor(UPDATED_VENDOR);
    }

    @BeforeEach
    public void initTest() {
        oneWorldSyncProduct = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOneWorldSyncProduct != null) {
            oneWorldSyncProductRepository.delete(insertedOneWorldSyncProduct);
            oneWorldSyncProductSearchRepository.delete(insertedOneWorldSyncProduct);
            insertedOneWorldSyncProduct = null;
        }
    }

    @Test
    @Transactional
    void createOneWorldSyncProduct() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        // Create the OneWorldSyncProduct
        var returnedOneWorldSyncProduct = om.readValue(
            restOneWorldSyncProductMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oneWorldSyncProduct)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OneWorldSyncProduct.class
        );

        // Validate the OneWorldSyncProduct in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertOneWorldSyncProductUpdatableFieldsEquals(
            returnedOneWorldSyncProduct,
            getPersistedOneWorldSyncProduct(returnedOneWorldSyncProduct)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedOneWorldSyncProduct = returnedOneWorldSyncProduct;
    }

    @Test
    @Transactional
    void createOneWorldSyncProductWithExistingId() throws Exception {
        // Create the OneWorldSyncProduct with an existing ID
        oneWorldSyncProduct.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOneWorldSyncProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oneWorldSyncProduct)))
            .andExpect(status().isBadRequest());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllOneWorldSyncProducts() throws Exception {
        // Initialize the database
        insertedOneWorldSyncProduct = oneWorldSyncProductRepository.saveAndFlush(oneWorldSyncProduct);

        // Get all the oneWorldSyncProductList
        restOneWorldSyncProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oneWorldSyncProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedSugars").value(hasItem(DEFAULT_ADDED_SUGARS)))
            .andExpect(jsonPath("$.[*].addedSugarUom").value(hasItem(DEFAULT_ADDED_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].allergenKeyword").value(hasItem(DEFAULT_ALLERGEN_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].allergens").value(hasItem(DEFAULT_ALLERGENS)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].calories").value(hasItem(DEFAULT_CALORIES)))
            .andExpect(jsonPath("$.[*].caloriesUom").value(hasItem(DEFAULT_CALORIES_UOM)))
            .andExpect(jsonPath("$.[*].carbohydrates").value(hasItem(DEFAULT_CARBOHYDRATES)))
            .andExpect(jsonPath("$.[*].carbohydratesUom").value(hasItem(DEFAULT_CARBOHYDRATES_UOM)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].cholesterol").value(hasItem(DEFAULT_CHOLESTEROL)))
            .andExpect(jsonPath("$.[*].cholesterolUOM").value(hasItem(DEFAULT_CHOLESTEROL_UOM)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].dataForm").value(hasItem(DEFAULT_DATA_FORM)))
            .andExpect(jsonPath("$.[*].dietaryFiber").value(hasItem(DEFAULT_DIETARY_FIBER)))
            .andExpect(jsonPath("$.[*].dietaryFiberUom").value(hasItem(DEFAULT_DIETARY_FIBER_UOM)))
            .andExpect(jsonPath("$.[*].distributor").value(hasItem(DEFAULT_DISTRIBUTOR)))
            .andExpect(jsonPath("$.[*].doNotConsiderProduct").value(hasItem(DEFAULT_DO_NOT_CONSIDER_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].extendedModel").value(hasItem(DEFAULT_EXTENDED_MODEL.toString())))
            .andExpect(jsonPath("$.[*].gLNNumber").value(hasItem(DEFAULT_G_LN_NUMBER)))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].h7").value(hasItem(DEFAULT_H_7)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isApprove").value(hasItem(DEFAULT_IS_APPROVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isMerge").value(hasItem(DEFAULT_IS_MERGE.booleanValue())))
            .andExpect(jsonPath("$.[*].isProductSync").value(hasItem(DEFAULT_IS_PRODUCT_SYNC.booleanValue())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].manufacturerText1Ws").value(hasItem(DEFAULT_MANUFACTURER_TEXT_1_WS)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].protein").value(hasItem(DEFAULT_PROTEIN)))
            .andExpect(jsonPath("$.[*].proteinUom").value(hasItem(DEFAULT_PROTEIN_UOM)))
            .andExpect(jsonPath("$.[*].saturatedFat").value(hasItem(DEFAULT_SATURATED_FAT)))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING)))
            .andExpect(jsonPath("$.[*].servingUom").value(hasItem(DEFAULT_SERVING_UOM)))
            .andExpect(jsonPath("$.[*].sodium").value(hasItem(DEFAULT_SODIUM)))
            .andExpect(jsonPath("$.[*].sodiumUom").value(hasItem(DEFAULT_SODIUM_UOM)))
            .andExpect(jsonPath("$.[*].storageTypeId").value(hasItem(DEFAULT_STORAGE_TYPE_ID)))
            .andExpect(jsonPath("$.[*].storageTypeName").value(hasItem(DEFAULT_STORAGE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].subCategory1Name").value(hasItem(DEFAULT_SUB_CATEGORY_1_NAME)))
            .andExpect(jsonPath("$.[*].subCategory2Name").value(hasItem(DEFAULT_SUB_CATEGORY_2_NAME)))
            .andExpect(jsonPath("$.[*].sugar").value(hasItem(DEFAULT_SUGAR)))
            .andExpect(jsonPath("$.[*].sugarUom").value(hasItem(DEFAULT_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].syncEffective").value(hasItem(DEFAULT_SYNC_EFFECTIVE.toString())))
            .andExpect(jsonPath("$.[*].syncHeaderLastChange").value(hasItem(DEFAULT_SYNC_HEADER_LAST_CHANGE.toString())))
            .andExpect(jsonPath("$.[*].syncItemReferenceId").value(hasItem(DEFAULT_SYNC_ITEM_REFERENCE_ID)))
            .andExpect(jsonPath("$.[*].syncLastChange").value(hasItem(DEFAULT_SYNC_LAST_CHANGE.toString())))
            .andExpect(jsonPath("$.[*].syncPublication").value(hasItem(DEFAULT_SYNC_PUBLICATION.toString())))
            .andExpect(jsonPath("$.[*].totalFat").value(hasItem(DEFAULT_TOTAL_FAT)))
            .andExpect(jsonPath("$.[*].transFat").value(hasItem(DEFAULT_TRANS_FAT)))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)));
    }

    @Test
    @Transactional
    void getOneWorldSyncProduct() throws Exception {
        // Initialize the database
        insertedOneWorldSyncProduct = oneWorldSyncProductRepository.saveAndFlush(oneWorldSyncProduct);

        // Get the oneWorldSyncProduct
        restOneWorldSyncProductMockMvc
            .perform(get(ENTITY_API_URL_ID, oneWorldSyncProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oneWorldSyncProduct.getId().intValue()))
            .andExpect(jsonPath("$.addedSugars").value(DEFAULT_ADDED_SUGARS))
            .andExpect(jsonPath("$.addedSugarUom").value(DEFAULT_ADDED_SUGAR_UOM))
            .andExpect(jsonPath("$.allergenKeyword").value(DEFAULT_ALLERGEN_KEYWORD.toString()))
            .andExpect(jsonPath("$.allergens").value(DEFAULT_ALLERGENS))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.calories").value(DEFAULT_CALORIES))
            .andExpect(jsonPath("$.caloriesUom").value(DEFAULT_CALORIES_UOM))
            .andExpect(jsonPath("$.carbohydrates").value(DEFAULT_CARBOHYDRATES))
            .andExpect(jsonPath("$.carbohydratesUom").value(DEFAULT_CARBOHYDRATES_UOM))
            .andExpect(jsonPath("$.categoryName").value(DEFAULT_CATEGORY_NAME))
            .andExpect(jsonPath("$.cholesterol").value(DEFAULT_CHOLESTEROL))
            .andExpect(jsonPath("$.cholesterolUOM").value(DEFAULT_CHOLESTEROL_UOM))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
            .andExpect(jsonPath("$.dataForm").value(DEFAULT_DATA_FORM))
            .andExpect(jsonPath("$.dietaryFiber").value(DEFAULT_DIETARY_FIBER))
            .andExpect(jsonPath("$.dietaryFiberUom").value(DEFAULT_DIETARY_FIBER_UOM))
            .andExpect(jsonPath("$.distributor").value(DEFAULT_DISTRIBUTOR))
            .andExpect(jsonPath("$.doNotConsiderProduct").value(DEFAULT_DO_NOT_CONSIDER_PRODUCT.booleanValue()))
            .andExpect(jsonPath("$.extendedModel").value(DEFAULT_EXTENDED_MODEL.toString()))
            .andExpect(jsonPath("$.gLNNumber").value(DEFAULT_G_LN_NUMBER))
            .andExpect(jsonPath("$.gTIN").value(DEFAULT_G_TIN))
            .andExpect(jsonPath("$.h7").value(DEFAULT_H_7))
            .andExpect(jsonPath("$.image").value(DEFAULT_IMAGE.toString()))
            .andExpect(jsonPath("$.ingredients").value(DEFAULT_INGREDIENTS.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.isApprove").value(DEFAULT_IS_APPROVE.booleanValue()))
            .andExpect(jsonPath("$.isMerge").value(DEFAULT_IS_MERGE.booleanValue()))
            .andExpect(jsonPath("$.isProductSync").value(DEFAULT_IS_PRODUCT_SYNC.booleanValue()))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER.toString()))
            .andExpect(jsonPath("$.manufacturerId").value(DEFAULT_MANUFACTURER_ID))
            .andExpect(jsonPath("$.manufacturerText1Ws").value(DEFAULT_MANUFACTURER_TEXT_1_WS))
            .andExpect(jsonPath("$.modifiedOn").value(DEFAULT_MODIFIED_ON.toString()))
            .andExpect(jsonPath("$.productDescription").value(DEFAULT_PRODUCT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID.intValue()))
            .andExpect(jsonPath("$.productName").value(DEFAULT_PRODUCT_NAME.toString()))
            .andExpect(jsonPath("$.protein").value(DEFAULT_PROTEIN))
            .andExpect(jsonPath("$.proteinUom").value(DEFAULT_PROTEIN_UOM))
            .andExpect(jsonPath("$.saturatedFat").value(DEFAULT_SATURATED_FAT))
            .andExpect(jsonPath("$.serving").value(DEFAULT_SERVING))
            .andExpect(jsonPath("$.servingUom").value(DEFAULT_SERVING_UOM))
            .andExpect(jsonPath("$.sodium").value(DEFAULT_SODIUM))
            .andExpect(jsonPath("$.sodiumUom").value(DEFAULT_SODIUM_UOM))
            .andExpect(jsonPath("$.storageTypeId").value(DEFAULT_STORAGE_TYPE_ID))
            .andExpect(jsonPath("$.storageTypeName").value(DEFAULT_STORAGE_TYPE_NAME.toString()))
            .andExpect(jsonPath("$.subCategory1Name").value(DEFAULT_SUB_CATEGORY_1_NAME))
            .andExpect(jsonPath("$.subCategory2Name").value(DEFAULT_SUB_CATEGORY_2_NAME))
            .andExpect(jsonPath("$.sugar").value(DEFAULT_SUGAR))
            .andExpect(jsonPath("$.sugarUom").value(DEFAULT_SUGAR_UOM))
            .andExpect(jsonPath("$.syncEffective").value(DEFAULT_SYNC_EFFECTIVE.toString()))
            .andExpect(jsonPath("$.syncHeaderLastChange").value(DEFAULT_SYNC_HEADER_LAST_CHANGE.toString()))
            .andExpect(jsonPath("$.syncItemReferenceId").value(DEFAULT_SYNC_ITEM_REFERENCE_ID))
            .andExpect(jsonPath("$.syncLastChange").value(DEFAULT_SYNC_LAST_CHANGE.toString()))
            .andExpect(jsonPath("$.syncPublication").value(DEFAULT_SYNC_PUBLICATION.toString()))
            .andExpect(jsonPath("$.totalFat").value(DEFAULT_TOTAL_FAT))
            .andExpect(jsonPath("$.transFat").value(DEFAULT_TRANS_FAT))
            .andExpect(jsonPath("$.uPC").value(DEFAULT_U_PC))
            .andExpect(jsonPath("$.vendor").value(DEFAULT_VENDOR));
    }

    @Test
    @Transactional
    void getNonExistingOneWorldSyncProduct() throws Exception {
        // Get the oneWorldSyncProduct
        restOneWorldSyncProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOneWorldSyncProduct() throws Exception {
        // Initialize the database
        insertedOneWorldSyncProduct = oneWorldSyncProductRepository.saveAndFlush(oneWorldSyncProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        oneWorldSyncProductSearchRepository.save(oneWorldSyncProduct);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());

        // Update the oneWorldSyncProduct
        OneWorldSyncProduct updatedOneWorldSyncProduct = oneWorldSyncProductRepository.findById(oneWorldSyncProduct.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOneWorldSyncProduct are not directly saved in db
        em.detach(updatedOneWorldSyncProduct);
        updatedOneWorldSyncProduct
            .addedSugars(UPDATED_ADDED_SUGARS)
            .addedSugarUom(UPDATED_ADDED_SUGAR_UOM)
            .allergenKeyword(UPDATED_ALLERGEN_KEYWORD)
            .allergens(UPDATED_ALLERGENS)
            .brandName(UPDATED_BRAND_NAME)
            .calories(UPDATED_CALORIES)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .carbohydratesUom(UPDATED_CARBOHYDRATES_UOM)
            .categoryName(UPDATED_CATEGORY_NAME)
            .cholesterol(UPDATED_CHOLESTEROL)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdOn(UPDATED_CREATED_ON)
            .dataForm(UPDATED_DATA_FORM)
            .dietaryFiber(UPDATED_DIETARY_FIBER)
            .dietaryFiberUom(UPDATED_DIETARY_FIBER_UOM)
            .distributor(UPDATED_DISTRIBUTOR)
            .doNotConsiderProduct(UPDATED_DO_NOT_CONSIDER_PRODUCT)
            .extendedModel(UPDATED_EXTENDED_MODEL)
            .gLNNumber(UPDATED_G_LN_NUMBER)
            .gTIN(UPDATED_G_TIN)
            .h7(UPDATED_H_7)
            .image(UPDATED_IMAGE)
            .ingredients(UPDATED_INGREDIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .isApprove(UPDATED_IS_APPROVE)
            .isMerge(UPDATED_IS_MERGE)
            .isProductSync(UPDATED_IS_PRODUCT_SYNC)
            .manufacturer(UPDATED_MANUFACTURER)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerText1Ws(UPDATED_MANUFACTURER_TEXT_1_WS)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .protein(UPDATED_PROTEIN)
            .proteinUom(UPDATED_PROTEIN_UOM)
            .saturatedFat(UPDATED_SATURATED_FAT)
            .serving(UPDATED_SERVING)
            .servingUom(UPDATED_SERVING_UOM)
            .sodium(UPDATED_SODIUM)
            .sodiumUom(UPDATED_SODIUM_UOM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .storageTypeName(UPDATED_STORAGE_TYPE_NAME)
            .subCategory1Name(UPDATED_SUB_CATEGORY_1_NAME)
            .subCategory2Name(UPDATED_SUB_CATEGORY_2_NAME)
            .sugar(UPDATED_SUGAR)
            .sugarUom(UPDATED_SUGAR_UOM)
            .syncEffective(UPDATED_SYNC_EFFECTIVE)
            .syncHeaderLastChange(UPDATED_SYNC_HEADER_LAST_CHANGE)
            .syncItemReferenceId(UPDATED_SYNC_ITEM_REFERENCE_ID)
            .syncLastChange(UPDATED_SYNC_LAST_CHANGE)
            .syncPublication(UPDATED_SYNC_PUBLICATION)
            .totalFat(UPDATED_TOTAL_FAT)
            .transFat(UPDATED_TRANS_FAT)
            .uPC(UPDATED_U_PC)
            .vendor(UPDATED_VENDOR);

        restOneWorldSyncProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOneWorldSyncProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedOneWorldSyncProduct))
            )
            .andExpect(status().isOk());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOneWorldSyncProductToMatchAllProperties(updatedOneWorldSyncProduct);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OneWorldSyncProduct> oneWorldSyncProductSearchList = Streamable.of(
                    oneWorldSyncProductSearchRepository.findAll()
                ).toList();
                OneWorldSyncProduct testOneWorldSyncProductSearch = oneWorldSyncProductSearchList.get(searchDatabaseSizeAfter - 1);

                assertOneWorldSyncProductAllPropertiesEquals(testOneWorldSyncProductSearch, updatedOneWorldSyncProduct);
            });
    }

    @Test
    @Transactional
    void putNonExistingOneWorldSyncProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        oneWorldSyncProduct.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOneWorldSyncProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oneWorldSyncProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oneWorldSyncProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchOneWorldSyncProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        oneWorldSyncProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneWorldSyncProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(oneWorldSyncProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOneWorldSyncProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        oneWorldSyncProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneWorldSyncProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(oneWorldSyncProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateOneWorldSyncProductWithPatch() throws Exception {
        // Initialize the database
        insertedOneWorldSyncProduct = oneWorldSyncProductRepository.saveAndFlush(oneWorldSyncProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oneWorldSyncProduct using partial update
        OneWorldSyncProduct partialUpdatedOneWorldSyncProduct = new OneWorldSyncProduct();
        partialUpdatedOneWorldSyncProduct.setId(oneWorldSyncProduct.getId());

        partialUpdatedOneWorldSyncProduct
            .brandName(UPDATED_BRAND_NAME)
            .calories(UPDATED_CALORIES)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdOn(UPDATED_CREATED_ON)
            .dataForm(UPDATED_DATA_FORM)
            .dietaryFiberUom(UPDATED_DIETARY_FIBER_UOM)
            .distributor(UPDATED_DISTRIBUTOR)
            .doNotConsiderProduct(UPDATED_DO_NOT_CONSIDER_PRODUCT)
            .gTIN(UPDATED_G_TIN)
            .h7(UPDATED_H_7)
            .image(UPDATED_IMAGE)
            .isActive(UPDATED_IS_ACTIVE)
            .isApprove(UPDATED_IS_APPROVE)
            .isMerge(UPDATED_IS_MERGE)
            .isProductSync(UPDATED_IS_PRODUCT_SYNC)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerText1Ws(UPDATED_MANUFACTURER_TEXT_1_WS)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .productId(UPDATED_PRODUCT_ID)
            .protein(UPDATED_PROTEIN)
            .proteinUom(UPDATED_PROTEIN_UOM)
            .serving(UPDATED_SERVING)
            .servingUom(UPDATED_SERVING_UOM)
            .sodium(UPDATED_SODIUM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .subCategory1Name(UPDATED_SUB_CATEGORY_1_NAME)
            .syncItemReferenceId(UPDATED_SYNC_ITEM_REFERENCE_ID)
            .syncPublication(UPDATED_SYNC_PUBLICATION)
            .uPC(UPDATED_U_PC)
            .vendor(UPDATED_VENDOR);

        restOneWorldSyncProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOneWorldSyncProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOneWorldSyncProduct))
            )
            .andExpect(status().isOk());

        // Validate the OneWorldSyncProduct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOneWorldSyncProductUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOneWorldSyncProduct, oneWorldSyncProduct),
            getPersistedOneWorldSyncProduct(oneWorldSyncProduct)
        );
    }

    @Test
    @Transactional
    void fullUpdateOneWorldSyncProductWithPatch() throws Exception {
        // Initialize the database
        insertedOneWorldSyncProduct = oneWorldSyncProductRepository.saveAndFlush(oneWorldSyncProduct);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the oneWorldSyncProduct using partial update
        OneWorldSyncProduct partialUpdatedOneWorldSyncProduct = new OneWorldSyncProduct();
        partialUpdatedOneWorldSyncProduct.setId(oneWorldSyncProduct.getId());

        partialUpdatedOneWorldSyncProduct
            .addedSugars(UPDATED_ADDED_SUGARS)
            .addedSugarUom(UPDATED_ADDED_SUGAR_UOM)
            .allergenKeyword(UPDATED_ALLERGEN_KEYWORD)
            .allergens(UPDATED_ALLERGENS)
            .brandName(UPDATED_BRAND_NAME)
            .calories(UPDATED_CALORIES)
            .caloriesUom(UPDATED_CALORIES_UOM)
            .carbohydrates(UPDATED_CARBOHYDRATES)
            .carbohydratesUom(UPDATED_CARBOHYDRATES_UOM)
            .categoryName(UPDATED_CATEGORY_NAME)
            .cholesterol(UPDATED_CHOLESTEROL)
            .cholesterolUOM(UPDATED_CHOLESTEROL_UOM)
            .createdOn(UPDATED_CREATED_ON)
            .dataForm(UPDATED_DATA_FORM)
            .dietaryFiber(UPDATED_DIETARY_FIBER)
            .dietaryFiberUom(UPDATED_DIETARY_FIBER_UOM)
            .distributor(UPDATED_DISTRIBUTOR)
            .doNotConsiderProduct(UPDATED_DO_NOT_CONSIDER_PRODUCT)
            .extendedModel(UPDATED_EXTENDED_MODEL)
            .gLNNumber(UPDATED_G_LN_NUMBER)
            .gTIN(UPDATED_G_TIN)
            .h7(UPDATED_H_7)
            .image(UPDATED_IMAGE)
            .ingredients(UPDATED_INGREDIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .isApprove(UPDATED_IS_APPROVE)
            .isMerge(UPDATED_IS_MERGE)
            .isProductSync(UPDATED_IS_PRODUCT_SYNC)
            .manufacturer(UPDATED_MANUFACTURER)
            .manufacturerId(UPDATED_MANUFACTURER_ID)
            .manufacturerText1Ws(UPDATED_MANUFACTURER_TEXT_1_WS)
            .modifiedOn(UPDATED_MODIFIED_ON)
            .productDescription(UPDATED_PRODUCT_DESCRIPTION)
            .productId(UPDATED_PRODUCT_ID)
            .productName(UPDATED_PRODUCT_NAME)
            .protein(UPDATED_PROTEIN)
            .proteinUom(UPDATED_PROTEIN_UOM)
            .saturatedFat(UPDATED_SATURATED_FAT)
            .serving(UPDATED_SERVING)
            .servingUom(UPDATED_SERVING_UOM)
            .sodium(UPDATED_SODIUM)
            .sodiumUom(UPDATED_SODIUM_UOM)
            .storageTypeId(UPDATED_STORAGE_TYPE_ID)
            .storageTypeName(UPDATED_STORAGE_TYPE_NAME)
            .subCategory1Name(UPDATED_SUB_CATEGORY_1_NAME)
            .subCategory2Name(UPDATED_SUB_CATEGORY_2_NAME)
            .sugar(UPDATED_SUGAR)
            .sugarUom(UPDATED_SUGAR_UOM)
            .syncEffective(UPDATED_SYNC_EFFECTIVE)
            .syncHeaderLastChange(UPDATED_SYNC_HEADER_LAST_CHANGE)
            .syncItemReferenceId(UPDATED_SYNC_ITEM_REFERENCE_ID)
            .syncLastChange(UPDATED_SYNC_LAST_CHANGE)
            .syncPublication(UPDATED_SYNC_PUBLICATION)
            .totalFat(UPDATED_TOTAL_FAT)
            .transFat(UPDATED_TRANS_FAT)
            .uPC(UPDATED_U_PC)
            .vendor(UPDATED_VENDOR);

        restOneWorldSyncProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOneWorldSyncProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOneWorldSyncProduct))
            )
            .andExpect(status().isOk());

        // Validate the OneWorldSyncProduct in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOneWorldSyncProductUpdatableFieldsEquals(
            partialUpdatedOneWorldSyncProduct,
            getPersistedOneWorldSyncProduct(partialUpdatedOneWorldSyncProduct)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOneWorldSyncProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        oneWorldSyncProduct.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOneWorldSyncProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oneWorldSyncProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oneWorldSyncProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOneWorldSyncProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        oneWorldSyncProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneWorldSyncProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(oneWorldSyncProduct))
            )
            .andExpect(status().isBadRequest());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOneWorldSyncProduct() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        oneWorldSyncProduct.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOneWorldSyncProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(oneWorldSyncProduct)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OneWorldSyncProduct in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteOneWorldSyncProduct() throws Exception {
        // Initialize the database
        insertedOneWorldSyncProduct = oneWorldSyncProductRepository.saveAndFlush(oneWorldSyncProduct);
        oneWorldSyncProductRepository.save(oneWorldSyncProduct);
        oneWorldSyncProductSearchRepository.save(oneWorldSyncProduct);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the oneWorldSyncProduct
        restOneWorldSyncProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, oneWorldSyncProduct.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(oneWorldSyncProductSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchOneWorldSyncProduct() throws Exception {
        // Initialize the database
        insertedOneWorldSyncProduct = oneWorldSyncProductRepository.saveAndFlush(oneWorldSyncProduct);
        oneWorldSyncProductSearchRepository.save(oneWorldSyncProduct);

        // Search the oneWorldSyncProduct
        restOneWorldSyncProductMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + oneWorldSyncProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oneWorldSyncProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedSugars").value(hasItem(DEFAULT_ADDED_SUGARS)))
            .andExpect(jsonPath("$.[*].addedSugarUom").value(hasItem(DEFAULT_ADDED_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].allergenKeyword").value(hasItem(DEFAULT_ALLERGEN_KEYWORD.toString())))
            .andExpect(jsonPath("$.[*].allergens").value(hasItem(DEFAULT_ALLERGENS)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].calories").value(hasItem(DEFAULT_CALORIES)))
            .andExpect(jsonPath("$.[*].caloriesUom").value(hasItem(DEFAULT_CALORIES_UOM)))
            .andExpect(jsonPath("$.[*].carbohydrates").value(hasItem(DEFAULT_CARBOHYDRATES)))
            .andExpect(jsonPath("$.[*].carbohydratesUom").value(hasItem(DEFAULT_CARBOHYDRATES_UOM)))
            .andExpect(jsonPath("$.[*].categoryName").value(hasItem(DEFAULT_CATEGORY_NAME)))
            .andExpect(jsonPath("$.[*].cholesterol").value(hasItem(DEFAULT_CHOLESTEROL)))
            .andExpect(jsonPath("$.[*].cholesterolUOM").value(hasItem(DEFAULT_CHOLESTEROL_UOM)))
            .andExpect(jsonPath("$.[*].createdOn").value(hasItem(DEFAULT_CREATED_ON.toString())))
            .andExpect(jsonPath("$.[*].dataForm").value(hasItem(DEFAULT_DATA_FORM)))
            .andExpect(jsonPath("$.[*].dietaryFiber").value(hasItem(DEFAULT_DIETARY_FIBER)))
            .andExpect(jsonPath("$.[*].dietaryFiberUom").value(hasItem(DEFAULT_DIETARY_FIBER_UOM)))
            .andExpect(jsonPath("$.[*].distributor").value(hasItem(DEFAULT_DISTRIBUTOR)))
            .andExpect(jsonPath("$.[*].doNotConsiderProduct").value(hasItem(DEFAULT_DO_NOT_CONSIDER_PRODUCT.booleanValue())))
            .andExpect(jsonPath("$.[*].extendedModel").value(hasItem(DEFAULT_EXTENDED_MODEL.toString())))
            .andExpect(jsonPath("$.[*].gLNNumber").value(hasItem(DEFAULT_G_LN_NUMBER)))
            .andExpect(jsonPath("$.[*].gTIN").value(hasItem(DEFAULT_G_TIN)))
            .andExpect(jsonPath("$.[*].h7").value(hasItem(DEFAULT_H_7)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(DEFAULT_IMAGE.toString())))
            .andExpect(jsonPath("$.[*].ingredients").value(hasItem(DEFAULT_INGREDIENTS.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isApprove").value(hasItem(DEFAULT_IS_APPROVE.booleanValue())))
            .andExpect(jsonPath("$.[*].isMerge").value(hasItem(DEFAULT_IS_MERGE.booleanValue())))
            .andExpect(jsonPath("$.[*].isProductSync").value(hasItem(DEFAULT_IS_PRODUCT_SYNC.booleanValue())))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER.toString())))
            .andExpect(jsonPath("$.[*].manufacturerId").value(hasItem(DEFAULT_MANUFACTURER_ID)))
            .andExpect(jsonPath("$.[*].manufacturerText1Ws").value(hasItem(DEFAULT_MANUFACTURER_TEXT_1_WS)))
            .andExpect(jsonPath("$.[*].modifiedOn").value(hasItem(DEFAULT_MODIFIED_ON.toString())))
            .andExpect(jsonPath("$.[*].productDescription").value(hasItem(DEFAULT_PRODUCT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID.intValue())))
            .andExpect(jsonPath("$.[*].productName").value(hasItem(DEFAULT_PRODUCT_NAME.toString())))
            .andExpect(jsonPath("$.[*].protein").value(hasItem(DEFAULT_PROTEIN)))
            .andExpect(jsonPath("$.[*].proteinUom").value(hasItem(DEFAULT_PROTEIN_UOM)))
            .andExpect(jsonPath("$.[*].saturatedFat").value(hasItem(DEFAULT_SATURATED_FAT)))
            .andExpect(jsonPath("$.[*].serving").value(hasItem(DEFAULT_SERVING)))
            .andExpect(jsonPath("$.[*].servingUom").value(hasItem(DEFAULT_SERVING_UOM)))
            .andExpect(jsonPath("$.[*].sodium").value(hasItem(DEFAULT_SODIUM)))
            .andExpect(jsonPath("$.[*].sodiumUom").value(hasItem(DEFAULT_SODIUM_UOM)))
            .andExpect(jsonPath("$.[*].storageTypeId").value(hasItem(DEFAULT_STORAGE_TYPE_ID)))
            .andExpect(jsonPath("$.[*].storageTypeName").value(hasItem(DEFAULT_STORAGE_TYPE_NAME.toString())))
            .andExpect(jsonPath("$.[*].subCategory1Name").value(hasItem(DEFAULT_SUB_CATEGORY_1_NAME)))
            .andExpect(jsonPath("$.[*].subCategory2Name").value(hasItem(DEFAULT_SUB_CATEGORY_2_NAME)))
            .andExpect(jsonPath("$.[*].sugar").value(hasItem(DEFAULT_SUGAR)))
            .andExpect(jsonPath("$.[*].sugarUom").value(hasItem(DEFAULT_SUGAR_UOM)))
            .andExpect(jsonPath("$.[*].syncEffective").value(hasItem(DEFAULT_SYNC_EFFECTIVE.toString())))
            .andExpect(jsonPath("$.[*].syncHeaderLastChange").value(hasItem(DEFAULT_SYNC_HEADER_LAST_CHANGE.toString())))
            .andExpect(jsonPath("$.[*].syncItemReferenceId").value(hasItem(DEFAULT_SYNC_ITEM_REFERENCE_ID)))
            .andExpect(jsonPath("$.[*].syncLastChange").value(hasItem(DEFAULT_SYNC_LAST_CHANGE.toString())))
            .andExpect(jsonPath("$.[*].syncPublication").value(hasItem(DEFAULT_SYNC_PUBLICATION.toString())))
            .andExpect(jsonPath("$.[*].totalFat").value(hasItem(DEFAULT_TOTAL_FAT)))
            .andExpect(jsonPath("$.[*].transFat").value(hasItem(DEFAULT_TRANS_FAT)))
            .andExpect(jsonPath("$.[*].uPC").value(hasItem(DEFAULT_U_PC)))
            .andExpect(jsonPath("$.[*].vendor").value(hasItem(DEFAULT_VENDOR)));
    }

    protected long getRepositoryCount() {
        return oneWorldSyncProductRepository.count();
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

    protected OneWorldSyncProduct getPersistedOneWorldSyncProduct(OneWorldSyncProduct oneWorldSyncProduct) {
        return oneWorldSyncProductRepository.findById(oneWorldSyncProduct.getId()).orElseThrow();
    }

    protected void assertPersistedOneWorldSyncProductToMatchAllProperties(OneWorldSyncProduct expectedOneWorldSyncProduct) {
        assertOneWorldSyncProductAllPropertiesEquals(
            expectedOneWorldSyncProduct,
            getPersistedOneWorldSyncProduct(expectedOneWorldSyncProduct)
        );
    }

    protected void assertPersistedOneWorldSyncProductToMatchUpdatableProperties(OneWorldSyncProduct expectedOneWorldSyncProduct) {
        assertOneWorldSyncProductAllUpdatablePropertiesEquals(
            expectedOneWorldSyncProduct,
            getPersistedOneWorldSyncProduct(expectedOneWorldSyncProduct)
        );
    }
}
