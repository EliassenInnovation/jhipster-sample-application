package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.USDAUpdateMstAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.USDAUpdateMst;
import com.mycompany.myapp.repository.USDAUpdateMstRepository;
import com.mycompany.myapp.repository.search.USDAUpdateMstSearchRepository;
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
 * Integration tests for the {@link USDAUpdateMstResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class USDAUpdateMstResourceIT {

    private static final String DEFAULT_ADDED_SUGARSGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_ADDED_SUGARSGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_ALL_ALLERGENS = "AAAAAAAAAA";
    private static final String UPDATED_ALL_ALLERGENS = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CALCIUM_CAMGPER_SERVING = 1D;
    private static final Double UPDATED_CALCIUM_CAMGPER_SERVING = 2D;

    private static final String DEFAULT_CALORIESKCALPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_CALORIESKCALPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_CHOLESTEROLMGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_CHOLESTEROLMGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_CREDITED = "AAAAAAAAAA";
    private static final String UPDATED_C_N_CREDITED = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_CREDITING = "AAAAAAAAAA";
    private static final String UPDATED_C_N_CREDITING = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_EXPIRATION_DATE = "AAAAAAAAAA";
    private static final String UPDATED_C_N_EXPIRATION_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_LABEL_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_C_N_LABEL_DOCUMENT = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_LABEL_STATEMENT = "AAAAAAAAAA";
    private static final String UPDATED_C_N_LABEL_STATEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_PRODUCT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_C_N_PRODUCT_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_QUALIFIED = "AAAAAAAAAA";
    private static final String UPDATED_C_N_QUALIFIED = "BBBBBBBBBB";

    private static final String DEFAULT_C_N_QUALIFIER_CODE = "AAAAAAAAAA";
    private static final String UPDATED_C_N_QUALIFIER_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DIETARY_FIBERGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_DIETARY_FIBERGPER_SERVING = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DOWNLOADED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DOWNLOADED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_EGGS = "AAAAAAAAAA";
    private static final String UPDATED_EGGS = "BBBBBBBBBB";

    private static final String DEFAULT_FISH = "AAAAAAAAAA";
    private static final String UPDATED_FISH = "BBBBBBBBBB";

    private static final String DEFAULT_FOOD_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_FOOD_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_FUNCTIONALNAME = "AAAAAAAAAA";
    private static final String UPDATED_FUNCTIONALNAME = "BBBBBBBBBB";

    private static final String DEFAULT_GTIN = "AAAAAAAAAA";
    private static final String UPDATED_GTIN = "BBBBBBBBBB";

    private static final String DEFAULT_HALAL = "AAAAAAAAAA";
    private static final String UPDATED_HALAL = "BBBBBBBBBB";

    private static final String DEFAULT_HIERARCHICAL_PLACEMENT = "AAAAAAAAAA";
    private static final String UPDATED_HIERARCHICAL_PLACEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_INFORMATION_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_INFORMATION_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENTSENGLISH = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENTSENGLISH = "BBBBBBBBBB";

    private static final String DEFAULT_IRON_FEMGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_IRON_FEMGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_KOSHER = "AAAAAAAAAA";
    private static final String UPDATED_KOSHER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_LASTUPDATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_LASTUPDATED = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LONGDESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LONGDESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_MILK = "AAAAAAAAAA";
    private static final String UPDATED_MILK = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRIENT_QUANTITY_BASIS_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_NUTRIENT_QUANTITY_BASIS_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE = "AAAAAAAAAA";
    private static final String UPDATED_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRIENT_QUANTITY_BASIS_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_NUTRIENT_QUANTITY_BASIS_VALUE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT = false;
    private static final Boolean UPDATED_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT = true;

    private static final String DEFAULT_PEANUTS = "AAAAAAAAAA";
    private static final String UPDATED_PEANUTS = "BBBBBBBBBB";

    private static final String DEFAULT_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_P_FS_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_P_FS_DOCUMENT = "BBBBBBBBBB";

    private static final String DEFAULT_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT = "AAAAAAAAAA";
    private static final String UPDATED_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_P_FS_TOTAL_PORTION_WEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_P_FS_TOTAL_PORTION_WEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_POTASSIUM_KMGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_POTASSIUM_KMGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_PREPARATION_STATE_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PREPARATION_STATE_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT_FORMULATION_STATEMENT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_FORMULATION_STATEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_PROTEINGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_PROTEINGPER_SERVING = "BBBBBBBBBB";

    private static final Double DEFAULT_SATURATED_FATGPER_SERVING = 1D;
    private static final Double UPDATED_SATURATED_FATGPER_SERVING = 2D;

    private static final String DEFAULT_SERVING_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SERVING_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SERVING_SIZE = "AAAAAAAAAA";
    private static final String UPDATED_SERVING_SIZE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVING_UNITOF_MEASURE = "AAAAAAAAAA";
    private static final String UPDATED_SERVING_UNITOF_MEASURE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVINGS_PER_CASE = "AAAAAAAAAA";
    private static final String UPDATED_SERVINGS_PER_CASE = "BBBBBBBBBB";

    private static final String DEFAULT_SESAME = "AAAAAAAAAA";
    private static final String UPDATED_SESAME = "BBBBBBBBBB";

    private static final String DEFAULT_SHELLFISH = "AAAAAAAAAA";
    private static final String UPDATED_SHELLFISH = "BBBBBBBBBB";

    private static final String DEFAULT_SHORTDESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORTDESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SODIUMMGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_SODIUMMGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_SOYBEANS = "AAAAAAAAAA";
    private static final String UPDATED_SOYBEANS = "BBBBBBBBBB";

    private static final String DEFAULT_SUGARSGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_SUGARSGPER_SERVING = "BBBBBBBBBB";

    private static final Double DEFAULT_TOTAL_CARBOHYDRATEGPER_SERVING = 1D;
    private static final Double UPDATED_TOTAL_CARBOHYDRATEGPER_SERVING = 2D;

    private static final String DEFAULT_TOTAL_FATGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_TOTAL_FATGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_TRADE_CHANNELS = "AAAAAAAAAA";
    private static final String UPDATED_TRADE_CHANNELS = "BBBBBBBBBB";

    private static final String DEFAULT_TRANS_FATGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_TRANS_FATGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_TREE_NUTS = "AAAAAAAAAA";
    private static final String UPDATED_TREE_NUTS = "BBBBBBBBBB";

    private static final String DEFAULT_U_SDA_FOODS_MATERIAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_U_SDA_FOODS_MATERIAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_U_SDA_FOODS_PRODUCTS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_U_SDA_FOODS_PRODUCTS_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VENDOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_VENDOR_ID = "BBBBBBBBBB";

    private static final String DEFAULT_VITAMIN_DMCGPER_SERVING = "AAAAAAAAAA";
    private static final String UPDATED_VITAMIN_DMCGPER_SERVING = "BBBBBBBBBB";

    private static final String DEFAULT_WHEAT = "AAAAAAAAAA";
    private static final String UPDATED_WHEAT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/usda-update-msts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/usda-update-msts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private USDAUpdateMstRepository uSDAUpdateMstRepository;

    @Autowired
    private USDAUpdateMstSearchRepository uSDAUpdateMstSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUSDAUpdateMstMockMvc;

    private USDAUpdateMst uSDAUpdateMst;

    private USDAUpdateMst insertedUSDAUpdateMst;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static USDAUpdateMst createEntity() {
        return new USDAUpdateMst()
            .addedSugarsgperServing(DEFAULT_ADDED_SUGARSGPER_SERVING)
            .allAllergens(DEFAULT_ALL_ALLERGENS)
            .brandName(DEFAULT_BRAND_NAME)
            .calciumCamgperServing(DEFAULT_CALCIUM_CAMGPER_SERVING)
            .calorieskcalperServing(DEFAULT_CALORIESKCALPER_SERVING)
            .cholesterolmgperServing(DEFAULT_CHOLESTEROLMGPER_SERVING)
            .cNCredited(DEFAULT_C_N_CREDITED)
            .cNCrediting(DEFAULT_C_N_CREDITING)
            .cNExpirationDate(DEFAULT_C_N_EXPIRATION_DATE)
            .cNLabelDocument(DEFAULT_C_N_LABEL_DOCUMENT)
            .cNLabelStatement(DEFAULT_C_N_LABEL_STATEMENT)
            .cNProductIdentification(DEFAULT_C_N_PRODUCT_IDENTIFICATION)
            .cNQualified(DEFAULT_C_N_QUALIFIED)
            .cNQualifierCode(DEFAULT_C_N_QUALIFIER_CODE)
            .dietaryFibergperServing(DEFAULT_DIETARY_FIBERGPER_SERVING)
            .downloaded(DEFAULT_DOWNLOADED)
            .eggs(DEFAULT_EGGS)
            .fish(DEFAULT_FISH)
            .foodCategory(DEFAULT_FOOD_CATEGORY)
            .functionalname(DEFAULT_FUNCTIONALNAME)
            .gtin(DEFAULT_GTIN)
            .halal(DEFAULT_HALAL)
            .hierarchicalPlacement(DEFAULT_HIERARCHICAL_PLACEMENT)
            .informationProvider(DEFAULT_INFORMATION_PROVIDER)
            .ingredientsenglish(DEFAULT_INGREDIENTSENGLISH)
            .ironFemgperServing(DEFAULT_IRON_FEMGPER_SERVING)
            .kosher(DEFAULT_KOSHER)
            .lastupdated(DEFAULT_LASTUPDATED)
            .longdescription(DEFAULT_LONGDESCRIPTION)
            .milk(DEFAULT_MILK)
            .nutrientFormatTypeCodeReferenceCode(DEFAULT_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE)
            .nutrientQuantityBasisTypeCode(DEFAULT_NUTRIENT_QUANTITY_BASIS_TYPE_CODE)
            .nutrientQuantityBasisUnitofMeasure(DEFAULT_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE)
            .nutrientQuantityBasisValue(DEFAULT_NUTRIENT_QUANTITY_BASIS_VALUE)
            .nutrientsperservingcalculatedfrombymeasurereportedamount(DEFAULT_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT)
            .peanuts(DEFAULT_PEANUTS)
            .pFSCreditableIngredientTypeCode(DEFAULT_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE)
            .pFSDocument(DEFAULT_P_FS_DOCUMENT)
            .pFSTotalCreditableIngredientAmount(DEFAULT_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT)
            .pFSTotalPortionWeight(DEFAULT_P_FS_TOTAL_PORTION_WEIGHT)
            .potassiumKmgperServing(DEFAULT_POTASSIUM_KMGPER_SERVING)
            .preparationStateCode(DEFAULT_PREPARATION_STATE_CODE)
            .productFormulationStatement(DEFAULT_PRODUCT_FORMULATION_STATEMENT)
            .proteingperServing(DEFAULT_PROTEINGPER_SERVING)
            .saturatedFatgperServing(DEFAULT_SATURATED_FATGPER_SERVING)
            .servingDescription(DEFAULT_SERVING_DESCRIPTION)
            .servingSize(DEFAULT_SERVING_SIZE)
            .servingUnitofMeasure(DEFAULT_SERVING_UNITOF_MEASURE)
            .servingsPerCase(DEFAULT_SERVINGS_PER_CASE)
            .sesame(DEFAULT_SESAME)
            .shellfish(DEFAULT_SHELLFISH)
            .shortdescription(DEFAULT_SHORTDESCRIPTION)
            .sodiummgperServing(DEFAULT_SODIUMMGPER_SERVING)
            .soybeans(DEFAULT_SOYBEANS)
            .sugarsgperServing(DEFAULT_SUGARSGPER_SERVING)
            .totalCarbohydrategperServing(DEFAULT_TOTAL_CARBOHYDRATEGPER_SERVING)
            .totalFatgperServing(DEFAULT_TOTAL_FATGPER_SERVING)
            .tradeChannels(DEFAULT_TRADE_CHANNELS)
            .transFatgperServing(DEFAULT_TRANS_FATGPER_SERVING)
            .treeNuts(DEFAULT_TREE_NUTS)
            .uSDAFoodsMaterialCode(DEFAULT_U_SDA_FOODS_MATERIAL_CODE)
            .uSDAFoodsProductsDescription(DEFAULT_U_SDA_FOODS_PRODUCTS_DESCRIPTION)
            .vendorName(DEFAULT_VENDOR_NAME)
            .vendorID(DEFAULT_VENDOR_ID)
            .vitaminDmcgperServing(DEFAULT_VITAMIN_DMCGPER_SERVING)
            .wheat(DEFAULT_WHEAT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static USDAUpdateMst createUpdatedEntity() {
        return new USDAUpdateMst()
            .addedSugarsgperServing(UPDATED_ADDED_SUGARSGPER_SERVING)
            .allAllergens(UPDATED_ALL_ALLERGENS)
            .brandName(UPDATED_BRAND_NAME)
            .calciumCamgperServing(UPDATED_CALCIUM_CAMGPER_SERVING)
            .calorieskcalperServing(UPDATED_CALORIESKCALPER_SERVING)
            .cholesterolmgperServing(UPDATED_CHOLESTEROLMGPER_SERVING)
            .cNCredited(UPDATED_C_N_CREDITED)
            .cNCrediting(UPDATED_C_N_CREDITING)
            .cNExpirationDate(UPDATED_C_N_EXPIRATION_DATE)
            .cNLabelDocument(UPDATED_C_N_LABEL_DOCUMENT)
            .cNLabelStatement(UPDATED_C_N_LABEL_STATEMENT)
            .cNProductIdentification(UPDATED_C_N_PRODUCT_IDENTIFICATION)
            .cNQualified(UPDATED_C_N_QUALIFIED)
            .cNQualifierCode(UPDATED_C_N_QUALIFIER_CODE)
            .dietaryFibergperServing(UPDATED_DIETARY_FIBERGPER_SERVING)
            .downloaded(UPDATED_DOWNLOADED)
            .eggs(UPDATED_EGGS)
            .fish(UPDATED_FISH)
            .foodCategory(UPDATED_FOOD_CATEGORY)
            .functionalname(UPDATED_FUNCTIONALNAME)
            .gtin(UPDATED_GTIN)
            .halal(UPDATED_HALAL)
            .hierarchicalPlacement(UPDATED_HIERARCHICAL_PLACEMENT)
            .informationProvider(UPDATED_INFORMATION_PROVIDER)
            .ingredientsenglish(UPDATED_INGREDIENTSENGLISH)
            .ironFemgperServing(UPDATED_IRON_FEMGPER_SERVING)
            .kosher(UPDATED_KOSHER)
            .lastupdated(UPDATED_LASTUPDATED)
            .longdescription(UPDATED_LONGDESCRIPTION)
            .milk(UPDATED_MILK)
            .nutrientFormatTypeCodeReferenceCode(UPDATED_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE)
            .nutrientQuantityBasisTypeCode(UPDATED_NUTRIENT_QUANTITY_BASIS_TYPE_CODE)
            .nutrientQuantityBasisUnitofMeasure(UPDATED_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE)
            .nutrientQuantityBasisValue(UPDATED_NUTRIENT_QUANTITY_BASIS_VALUE)
            .nutrientsperservingcalculatedfrombymeasurereportedamount(UPDATED_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT)
            .peanuts(UPDATED_PEANUTS)
            .pFSCreditableIngredientTypeCode(UPDATED_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE)
            .pFSDocument(UPDATED_P_FS_DOCUMENT)
            .pFSTotalCreditableIngredientAmount(UPDATED_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT)
            .pFSTotalPortionWeight(UPDATED_P_FS_TOTAL_PORTION_WEIGHT)
            .potassiumKmgperServing(UPDATED_POTASSIUM_KMGPER_SERVING)
            .preparationStateCode(UPDATED_PREPARATION_STATE_CODE)
            .productFormulationStatement(UPDATED_PRODUCT_FORMULATION_STATEMENT)
            .proteingperServing(UPDATED_PROTEINGPER_SERVING)
            .saturatedFatgperServing(UPDATED_SATURATED_FATGPER_SERVING)
            .servingDescription(UPDATED_SERVING_DESCRIPTION)
            .servingSize(UPDATED_SERVING_SIZE)
            .servingUnitofMeasure(UPDATED_SERVING_UNITOF_MEASURE)
            .servingsPerCase(UPDATED_SERVINGS_PER_CASE)
            .sesame(UPDATED_SESAME)
            .shellfish(UPDATED_SHELLFISH)
            .shortdescription(UPDATED_SHORTDESCRIPTION)
            .sodiummgperServing(UPDATED_SODIUMMGPER_SERVING)
            .soybeans(UPDATED_SOYBEANS)
            .sugarsgperServing(UPDATED_SUGARSGPER_SERVING)
            .totalCarbohydrategperServing(UPDATED_TOTAL_CARBOHYDRATEGPER_SERVING)
            .totalFatgperServing(UPDATED_TOTAL_FATGPER_SERVING)
            .tradeChannels(UPDATED_TRADE_CHANNELS)
            .transFatgperServing(UPDATED_TRANS_FATGPER_SERVING)
            .treeNuts(UPDATED_TREE_NUTS)
            .uSDAFoodsMaterialCode(UPDATED_U_SDA_FOODS_MATERIAL_CODE)
            .uSDAFoodsProductsDescription(UPDATED_U_SDA_FOODS_PRODUCTS_DESCRIPTION)
            .vendorName(UPDATED_VENDOR_NAME)
            .vendorID(UPDATED_VENDOR_ID)
            .vitaminDmcgperServing(UPDATED_VITAMIN_DMCGPER_SERVING)
            .wheat(UPDATED_WHEAT);
    }

    @BeforeEach
    public void initTest() {
        uSDAUpdateMst = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUSDAUpdateMst != null) {
            uSDAUpdateMstRepository.delete(insertedUSDAUpdateMst);
            uSDAUpdateMstSearchRepository.delete(insertedUSDAUpdateMst);
            insertedUSDAUpdateMst = null;
        }
    }

    @Test
    @Transactional
    void createUSDAUpdateMst() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        // Create the USDAUpdateMst
        var returnedUSDAUpdateMst = om.readValue(
            restUSDAUpdateMstMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uSDAUpdateMst)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            USDAUpdateMst.class
        );

        // Validate the USDAUpdateMst in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUSDAUpdateMstUpdatableFieldsEquals(returnedUSDAUpdateMst, getPersistedUSDAUpdateMst(returnedUSDAUpdateMst));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedUSDAUpdateMst = returnedUSDAUpdateMst;
    }

    @Test
    @Transactional
    void createUSDAUpdateMstWithExistingId() throws Exception {
        // Create the USDAUpdateMst with an existing ID
        uSDAUpdateMst.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restUSDAUpdateMstMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uSDAUpdateMst)))
            .andExpect(status().isBadRequest());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllUSDAUpdateMsts() throws Exception {
        // Initialize the database
        insertedUSDAUpdateMst = uSDAUpdateMstRepository.saveAndFlush(uSDAUpdateMst);

        // Get all the uSDAUpdateMstList
        restUSDAUpdateMstMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uSDAUpdateMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedSugarsgperServing").value(hasItem(DEFAULT_ADDED_SUGARSGPER_SERVING)))
            .andExpect(jsonPath("$.[*].allAllergens").value(hasItem(DEFAULT_ALL_ALLERGENS)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].calciumCamgperServing").value(hasItem(DEFAULT_CALCIUM_CAMGPER_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].calorieskcalperServing").value(hasItem(DEFAULT_CALORIESKCALPER_SERVING)))
            .andExpect(jsonPath("$.[*].cholesterolmgperServing").value(hasItem(DEFAULT_CHOLESTEROLMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].cNCredited").value(hasItem(DEFAULT_C_N_CREDITED)))
            .andExpect(jsonPath("$.[*].cNCrediting").value(hasItem(DEFAULT_C_N_CREDITING)))
            .andExpect(jsonPath("$.[*].cNExpirationDate").value(hasItem(DEFAULT_C_N_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.[*].cNLabelDocument").value(hasItem(DEFAULT_C_N_LABEL_DOCUMENT)))
            .andExpect(jsonPath("$.[*].cNLabelStatement").value(hasItem(DEFAULT_C_N_LABEL_STATEMENT.toString())))
            .andExpect(jsonPath("$.[*].cNProductIdentification").value(hasItem(DEFAULT_C_N_PRODUCT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].cNQualified").value(hasItem(DEFAULT_C_N_QUALIFIED)))
            .andExpect(jsonPath("$.[*].cNQualifierCode").value(hasItem(DEFAULT_C_N_QUALIFIER_CODE)))
            .andExpect(jsonPath("$.[*].dietaryFibergperServing").value(hasItem(DEFAULT_DIETARY_FIBERGPER_SERVING)))
            .andExpect(jsonPath("$.[*].downloaded").value(hasItem(DEFAULT_DOWNLOADED.toString())))
            .andExpect(jsonPath("$.[*].eggs").value(hasItem(DEFAULT_EGGS)))
            .andExpect(jsonPath("$.[*].fish").value(hasItem(DEFAULT_FISH)))
            .andExpect(jsonPath("$.[*].foodCategory").value(hasItem(DEFAULT_FOOD_CATEGORY)))
            .andExpect(jsonPath("$.[*].functionalname").value(hasItem(DEFAULT_FUNCTIONALNAME)))
            .andExpect(jsonPath("$.[*].gtin").value(hasItem(DEFAULT_GTIN)))
            .andExpect(jsonPath("$.[*].halal").value(hasItem(DEFAULT_HALAL)))
            .andExpect(jsonPath("$.[*].hierarchicalPlacement").value(hasItem(DEFAULT_HIERARCHICAL_PLACEMENT)))
            .andExpect(jsonPath("$.[*].informationProvider").value(hasItem(DEFAULT_INFORMATION_PROVIDER)))
            .andExpect(jsonPath("$.[*].ingredientsenglish").value(hasItem(DEFAULT_INGREDIENTSENGLISH.toString())))
            .andExpect(jsonPath("$.[*].ironFemgperServing").value(hasItem(DEFAULT_IRON_FEMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].kosher").value(hasItem(DEFAULT_KOSHER)))
            .andExpect(jsonPath("$.[*].lastupdated").value(hasItem(DEFAULT_LASTUPDATED.toString())))
            .andExpect(jsonPath("$.[*].longdescription").value(hasItem(DEFAULT_LONGDESCRIPTION)))
            .andExpect(jsonPath("$.[*].milk").value(hasItem(DEFAULT_MILK)))
            .andExpect(
                jsonPath("$.[*].nutrientFormatTypeCodeReferenceCode").value(hasItem(DEFAULT_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE))
            )
            .andExpect(jsonPath("$.[*].nutrientQuantityBasisTypeCode").value(hasItem(DEFAULT_NUTRIENT_QUANTITY_BASIS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].nutrientQuantityBasisUnitofMeasure").value(hasItem(DEFAULT_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE)))
            .andExpect(jsonPath("$.[*].nutrientQuantityBasisValue").value(hasItem(DEFAULT_NUTRIENT_QUANTITY_BASIS_VALUE)))
            .andExpect(
                jsonPath("$.[*].nutrientsperservingcalculatedfrombymeasurereportedamount").value(
                    hasItem(DEFAULT_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT.booleanValue())
                )
            )
            .andExpect(jsonPath("$.[*].peanuts").value(hasItem(DEFAULT_PEANUTS)))
            .andExpect(jsonPath("$.[*].pFSCreditableIngredientTypeCode").value(hasItem(DEFAULT_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].pFSDocument").value(hasItem(DEFAULT_P_FS_DOCUMENT)))
            .andExpect(jsonPath("$.[*].pFSTotalCreditableIngredientAmount").value(hasItem(DEFAULT_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT)))
            .andExpect(jsonPath("$.[*].pFSTotalPortionWeight").value(hasItem(DEFAULT_P_FS_TOTAL_PORTION_WEIGHT)))
            .andExpect(jsonPath("$.[*].potassiumKmgperServing").value(hasItem(DEFAULT_POTASSIUM_KMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].preparationStateCode").value(hasItem(DEFAULT_PREPARATION_STATE_CODE)))
            .andExpect(jsonPath("$.[*].productFormulationStatement").value(hasItem(DEFAULT_PRODUCT_FORMULATION_STATEMENT)))
            .andExpect(jsonPath("$.[*].proteingperServing").value(hasItem(DEFAULT_PROTEINGPER_SERVING)))
            .andExpect(jsonPath("$.[*].saturatedFatgperServing").value(hasItem(DEFAULT_SATURATED_FATGPER_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].servingDescription").value(hasItem(DEFAULT_SERVING_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].servingSize").value(hasItem(DEFAULT_SERVING_SIZE)))
            .andExpect(jsonPath("$.[*].servingUnitofMeasure").value(hasItem(DEFAULT_SERVING_UNITOF_MEASURE)))
            .andExpect(jsonPath("$.[*].servingsPerCase").value(hasItem(DEFAULT_SERVINGS_PER_CASE)))
            .andExpect(jsonPath("$.[*].sesame").value(hasItem(DEFAULT_SESAME)))
            .andExpect(jsonPath("$.[*].shellfish").value(hasItem(DEFAULT_SHELLFISH)))
            .andExpect(jsonPath("$.[*].shortdescription").value(hasItem(DEFAULT_SHORTDESCRIPTION)))
            .andExpect(jsonPath("$.[*].sodiummgperServing").value(hasItem(DEFAULT_SODIUMMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].soybeans").value(hasItem(DEFAULT_SOYBEANS)))
            .andExpect(jsonPath("$.[*].sugarsgperServing").value(hasItem(DEFAULT_SUGARSGPER_SERVING)))
            .andExpect(jsonPath("$.[*].totalCarbohydrategperServing").value(hasItem(DEFAULT_TOTAL_CARBOHYDRATEGPER_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].totalFatgperServing").value(hasItem(DEFAULT_TOTAL_FATGPER_SERVING)))
            .andExpect(jsonPath("$.[*].tradeChannels").value(hasItem(DEFAULT_TRADE_CHANNELS)))
            .andExpect(jsonPath("$.[*].transFatgperServing").value(hasItem(DEFAULT_TRANS_FATGPER_SERVING)))
            .andExpect(jsonPath("$.[*].treeNuts").value(hasItem(DEFAULT_TREE_NUTS)))
            .andExpect(jsonPath("$.[*].uSDAFoodsMaterialCode").value(hasItem(DEFAULT_U_SDA_FOODS_MATERIAL_CODE)))
            .andExpect(jsonPath("$.[*].uSDAFoodsProductsDescription").value(hasItem(DEFAULT_U_SDA_FOODS_PRODUCTS_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].vendorID").value(hasItem(DEFAULT_VENDOR_ID)))
            .andExpect(jsonPath("$.[*].vitaminDmcgperServing").value(hasItem(DEFAULT_VITAMIN_DMCGPER_SERVING)))
            .andExpect(jsonPath("$.[*].wheat").value(hasItem(DEFAULT_WHEAT)));
    }

    @Test
    @Transactional
    void getUSDAUpdateMst() throws Exception {
        // Initialize the database
        insertedUSDAUpdateMst = uSDAUpdateMstRepository.saveAndFlush(uSDAUpdateMst);

        // Get the uSDAUpdateMst
        restUSDAUpdateMstMockMvc
            .perform(get(ENTITY_API_URL_ID, uSDAUpdateMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uSDAUpdateMst.getId().intValue()))
            .andExpect(jsonPath("$.addedSugarsgperServing").value(DEFAULT_ADDED_SUGARSGPER_SERVING))
            .andExpect(jsonPath("$.allAllergens").value(DEFAULT_ALL_ALLERGENS))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.calciumCamgperServing").value(DEFAULT_CALCIUM_CAMGPER_SERVING.doubleValue()))
            .andExpect(jsonPath("$.calorieskcalperServing").value(DEFAULT_CALORIESKCALPER_SERVING))
            .andExpect(jsonPath("$.cholesterolmgperServing").value(DEFAULT_CHOLESTEROLMGPER_SERVING))
            .andExpect(jsonPath("$.cNCredited").value(DEFAULT_C_N_CREDITED))
            .andExpect(jsonPath("$.cNCrediting").value(DEFAULT_C_N_CREDITING))
            .andExpect(jsonPath("$.cNExpirationDate").value(DEFAULT_C_N_EXPIRATION_DATE))
            .andExpect(jsonPath("$.cNLabelDocument").value(DEFAULT_C_N_LABEL_DOCUMENT))
            .andExpect(jsonPath("$.cNLabelStatement").value(DEFAULT_C_N_LABEL_STATEMENT.toString()))
            .andExpect(jsonPath("$.cNProductIdentification").value(DEFAULT_C_N_PRODUCT_IDENTIFICATION))
            .andExpect(jsonPath("$.cNQualified").value(DEFAULT_C_N_QUALIFIED))
            .andExpect(jsonPath("$.cNQualifierCode").value(DEFAULT_C_N_QUALIFIER_CODE))
            .andExpect(jsonPath("$.dietaryFibergperServing").value(DEFAULT_DIETARY_FIBERGPER_SERVING))
            .andExpect(jsonPath("$.downloaded").value(DEFAULT_DOWNLOADED.toString()))
            .andExpect(jsonPath("$.eggs").value(DEFAULT_EGGS))
            .andExpect(jsonPath("$.fish").value(DEFAULT_FISH))
            .andExpect(jsonPath("$.foodCategory").value(DEFAULT_FOOD_CATEGORY))
            .andExpect(jsonPath("$.functionalname").value(DEFAULT_FUNCTIONALNAME))
            .andExpect(jsonPath("$.gtin").value(DEFAULT_GTIN))
            .andExpect(jsonPath("$.halal").value(DEFAULT_HALAL))
            .andExpect(jsonPath("$.hierarchicalPlacement").value(DEFAULT_HIERARCHICAL_PLACEMENT))
            .andExpect(jsonPath("$.informationProvider").value(DEFAULT_INFORMATION_PROVIDER))
            .andExpect(jsonPath("$.ingredientsenglish").value(DEFAULT_INGREDIENTSENGLISH.toString()))
            .andExpect(jsonPath("$.ironFemgperServing").value(DEFAULT_IRON_FEMGPER_SERVING))
            .andExpect(jsonPath("$.kosher").value(DEFAULT_KOSHER))
            .andExpect(jsonPath("$.lastupdated").value(DEFAULT_LASTUPDATED.toString()))
            .andExpect(jsonPath("$.longdescription").value(DEFAULT_LONGDESCRIPTION))
            .andExpect(jsonPath("$.milk").value(DEFAULT_MILK))
            .andExpect(jsonPath("$.nutrientFormatTypeCodeReferenceCode").value(DEFAULT_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE))
            .andExpect(jsonPath("$.nutrientQuantityBasisTypeCode").value(DEFAULT_NUTRIENT_QUANTITY_BASIS_TYPE_CODE))
            .andExpect(jsonPath("$.nutrientQuantityBasisUnitofMeasure").value(DEFAULT_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE))
            .andExpect(jsonPath("$.nutrientQuantityBasisValue").value(DEFAULT_NUTRIENT_QUANTITY_BASIS_VALUE))
            .andExpect(
                jsonPath("$.nutrientsperservingcalculatedfrombymeasurereportedamount").value(
                    DEFAULT_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT.booleanValue()
                )
            )
            .andExpect(jsonPath("$.peanuts").value(DEFAULT_PEANUTS))
            .andExpect(jsonPath("$.pFSCreditableIngredientTypeCode").value(DEFAULT_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE))
            .andExpect(jsonPath("$.pFSDocument").value(DEFAULT_P_FS_DOCUMENT))
            .andExpect(jsonPath("$.pFSTotalCreditableIngredientAmount").value(DEFAULT_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT))
            .andExpect(jsonPath("$.pFSTotalPortionWeight").value(DEFAULT_P_FS_TOTAL_PORTION_WEIGHT))
            .andExpect(jsonPath("$.potassiumKmgperServing").value(DEFAULT_POTASSIUM_KMGPER_SERVING))
            .andExpect(jsonPath("$.preparationStateCode").value(DEFAULT_PREPARATION_STATE_CODE))
            .andExpect(jsonPath("$.productFormulationStatement").value(DEFAULT_PRODUCT_FORMULATION_STATEMENT))
            .andExpect(jsonPath("$.proteingperServing").value(DEFAULT_PROTEINGPER_SERVING))
            .andExpect(jsonPath("$.saturatedFatgperServing").value(DEFAULT_SATURATED_FATGPER_SERVING.doubleValue()))
            .andExpect(jsonPath("$.servingDescription").value(DEFAULT_SERVING_DESCRIPTION))
            .andExpect(jsonPath("$.servingSize").value(DEFAULT_SERVING_SIZE))
            .andExpect(jsonPath("$.servingUnitofMeasure").value(DEFAULT_SERVING_UNITOF_MEASURE))
            .andExpect(jsonPath("$.servingsPerCase").value(DEFAULT_SERVINGS_PER_CASE))
            .andExpect(jsonPath("$.sesame").value(DEFAULT_SESAME))
            .andExpect(jsonPath("$.shellfish").value(DEFAULT_SHELLFISH))
            .andExpect(jsonPath("$.shortdescription").value(DEFAULT_SHORTDESCRIPTION))
            .andExpect(jsonPath("$.sodiummgperServing").value(DEFAULT_SODIUMMGPER_SERVING))
            .andExpect(jsonPath("$.soybeans").value(DEFAULT_SOYBEANS))
            .andExpect(jsonPath("$.sugarsgperServing").value(DEFAULT_SUGARSGPER_SERVING))
            .andExpect(jsonPath("$.totalCarbohydrategperServing").value(DEFAULT_TOTAL_CARBOHYDRATEGPER_SERVING.doubleValue()))
            .andExpect(jsonPath("$.totalFatgperServing").value(DEFAULT_TOTAL_FATGPER_SERVING))
            .andExpect(jsonPath("$.tradeChannels").value(DEFAULT_TRADE_CHANNELS))
            .andExpect(jsonPath("$.transFatgperServing").value(DEFAULT_TRANS_FATGPER_SERVING))
            .andExpect(jsonPath("$.treeNuts").value(DEFAULT_TREE_NUTS))
            .andExpect(jsonPath("$.uSDAFoodsMaterialCode").value(DEFAULT_U_SDA_FOODS_MATERIAL_CODE))
            .andExpect(jsonPath("$.uSDAFoodsProductsDescription").value(DEFAULT_U_SDA_FOODS_PRODUCTS_DESCRIPTION))
            .andExpect(jsonPath("$.vendorName").value(DEFAULT_VENDOR_NAME))
            .andExpect(jsonPath("$.vendorID").value(DEFAULT_VENDOR_ID))
            .andExpect(jsonPath("$.vitaminDmcgperServing").value(DEFAULT_VITAMIN_DMCGPER_SERVING))
            .andExpect(jsonPath("$.wheat").value(DEFAULT_WHEAT));
    }

    @Test
    @Transactional
    void getNonExistingUSDAUpdateMst() throws Exception {
        // Get the uSDAUpdateMst
        restUSDAUpdateMstMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUSDAUpdateMst() throws Exception {
        // Initialize the database
        insertedUSDAUpdateMst = uSDAUpdateMstRepository.saveAndFlush(uSDAUpdateMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        uSDAUpdateMstSearchRepository.save(uSDAUpdateMst);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());

        // Update the uSDAUpdateMst
        USDAUpdateMst updatedUSDAUpdateMst = uSDAUpdateMstRepository.findById(uSDAUpdateMst.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUSDAUpdateMst are not directly saved in db
        em.detach(updatedUSDAUpdateMst);
        updatedUSDAUpdateMst
            .addedSugarsgperServing(UPDATED_ADDED_SUGARSGPER_SERVING)
            .allAllergens(UPDATED_ALL_ALLERGENS)
            .brandName(UPDATED_BRAND_NAME)
            .calciumCamgperServing(UPDATED_CALCIUM_CAMGPER_SERVING)
            .calorieskcalperServing(UPDATED_CALORIESKCALPER_SERVING)
            .cholesterolmgperServing(UPDATED_CHOLESTEROLMGPER_SERVING)
            .cNCredited(UPDATED_C_N_CREDITED)
            .cNCrediting(UPDATED_C_N_CREDITING)
            .cNExpirationDate(UPDATED_C_N_EXPIRATION_DATE)
            .cNLabelDocument(UPDATED_C_N_LABEL_DOCUMENT)
            .cNLabelStatement(UPDATED_C_N_LABEL_STATEMENT)
            .cNProductIdentification(UPDATED_C_N_PRODUCT_IDENTIFICATION)
            .cNQualified(UPDATED_C_N_QUALIFIED)
            .cNQualifierCode(UPDATED_C_N_QUALIFIER_CODE)
            .dietaryFibergperServing(UPDATED_DIETARY_FIBERGPER_SERVING)
            .downloaded(UPDATED_DOWNLOADED)
            .eggs(UPDATED_EGGS)
            .fish(UPDATED_FISH)
            .foodCategory(UPDATED_FOOD_CATEGORY)
            .functionalname(UPDATED_FUNCTIONALNAME)
            .gtin(UPDATED_GTIN)
            .halal(UPDATED_HALAL)
            .hierarchicalPlacement(UPDATED_HIERARCHICAL_PLACEMENT)
            .informationProvider(UPDATED_INFORMATION_PROVIDER)
            .ingredientsenglish(UPDATED_INGREDIENTSENGLISH)
            .ironFemgperServing(UPDATED_IRON_FEMGPER_SERVING)
            .kosher(UPDATED_KOSHER)
            .lastupdated(UPDATED_LASTUPDATED)
            .longdescription(UPDATED_LONGDESCRIPTION)
            .milk(UPDATED_MILK)
            .nutrientFormatTypeCodeReferenceCode(UPDATED_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE)
            .nutrientQuantityBasisTypeCode(UPDATED_NUTRIENT_QUANTITY_BASIS_TYPE_CODE)
            .nutrientQuantityBasisUnitofMeasure(UPDATED_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE)
            .nutrientQuantityBasisValue(UPDATED_NUTRIENT_QUANTITY_BASIS_VALUE)
            .nutrientsperservingcalculatedfrombymeasurereportedamount(UPDATED_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT)
            .peanuts(UPDATED_PEANUTS)
            .pFSCreditableIngredientTypeCode(UPDATED_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE)
            .pFSDocument(UPDATED_P_FS_DOCUMENT)
            .pFSTotalCreditableIngredientAmount(UPDATED_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT)
            .pFSTotalPortionWeight(UPDATED_P_FS_TOTAL_PORTION_WEIGHT)
            .potassiumKmgperServing(UPDATED_POTASSIUM_KMGPER_SERVING)
            .preparationStateCode(UPDATED_PREPARATION_STATE_CODE)
            .productFormulationStatement(UPDATED_PRODUCT_FORMULATION_STATEMENT)
            .proteingperServing(UPDATED_PROTEINGPER_SERVING)
            .saturatedFatgperServing(UPDATED_SATURATED_FATGPER_SERVING)
            .servingDescription(UPDATED_SERVING_DESCRIPTION)
            .servingSize(UPDATED_SERVING_SIZE)
            .servingUnitofMeasure(UPDATED_SERVING_UNITOF_MEASURE)
            .servingsPerCase(UPDATED_SERVINGS_PER_CASE)
            .sesame(UPDATED_SESAME)
            .shellfish(UPDATED_SHELLFISH)
            .shortdescription(UPDATED_SHORTDESCRIPTION)
            .sodiummgperServing(UPDATED_SODIUMMGPER_SERVING)
            .soybeans(UPDATED_SOYBEANS)
            .sugarsgperServing(UPDATED_SUGARSGPER_SERVING)
            .totalCarbohydrategperServing(UPDATED_TOTAL_CARBOHYDRATEGPER_SERVING)
            .totalFatgperServing(UPDATED_TOTAL_FATGPER_SERVING)
            .tradeChannels(UPDATED_TRADE_CHANNELS)
            .transFatgperServing(UPDATED_TRANS_FATGPER_SERVING)
            .treeNuts(UPDATED_TREE_NUTS)
            .uSDAFoodsMaterialCode(UPDATED_U_SDA_FOODS_MATERIAL_CODE)
            .uSDAFoodsProductsDescription(UPDATED_U_SDA_FOODS_PRODUCTS_DESCRIPTION)
            .vendorName(UPDATED_VENDOR_NAME)
            .vendorID(UPDATED_VENDOR_ID)
            .vitaminDmcgperServing(UPDATED_VITAMIN_DMCGPER_SERVING)
            .wheat(UPDATED_WHEAT);

        restUSDAUpdateMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUSDAUpdateMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUSDAUpdateMst))
            )
            .andExpect(status().isOk());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUSDAUpdateMstToMatchAllProperties(updatedUSDAUpdateMst);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<USDAUpdateMst> uSDAUpdateMstSearchList = Streamable.of(uSDAUpdateMstSearchRepository.findAll()).toList();
                USDAUpdateMst testUSDAUpdateMstSearch = uSDAUpdateMstSearchList.get(searchDatabaseSizeAfter - 1);

                assertUSDAUpdateMstAllPropertiesEquals(testUSDAUpdateMstSearch, updatedUSDAUpdateMst);
            });
    }

    @Test
    @Transactional
    void putNonExistingUSDAUpdateMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        uSDAUpdateMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUSDAUpdateMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uSDAUpdateMst.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uSDAUpdateMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchUSDAUpdateMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        uSDAUpdateMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUSDAUpdateMstMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(uSDAUpdateMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUSDAUpdateMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        uSDAUpdateMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUSDAUpdateMstMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(uSDAUpdateMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateUSDAUpdateMstWithPatch() throws Exception {
        // Initialize the database
        insertedUSDAUpdateMst = uSDAUpdateMstRepository.saveAndFlush(uSDAUpdateMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uSDAUpdateMst using partial update
        USDAUpdateMst partialUpdatedUSDAUpdateMst = new USDAUpdateMst();
        partialUpdatedUSDAUpdateMst.setId(uSDAUpdateMst.getId());

        partialUpdatedUSDAUpdateMst
            .addedSugarsgperServing(UPDATED_ADDED_SUGARSGPER_SERVING)
            .cholesterolmgperServing(UPDATED_CHOLESTEROLMGPER_SERVING)
            .cNLabelDocument(UPDATED_C_N_LABEL_DOCUMENT)
            .cNLabelStatement(UPDATED_C_N_LABEL_STATEMENT)
            .fish(UPDATED_FISH)
            .functionalname(UPDATED_FUNCTIONALNAME)
            .ingredientsenglish(UPDATED_INGREDIENTSENGLISH)
            .kosher(UPDATED_KOSHER)
            .nutrientQuantityBasisUnitofMeasure(UPDATED_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE)
            .nutrientQuantityBasisValue(UPDATED_NUTRIENT_QUANTITY_BASIS_VALUE)
            .pFSDocument(UPDATED_P_FS_DOCUMENT)
            .pFSTotalCreditableIngredientAmount(UPDATED_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT)
            .pFSTotalPortionWeight(UPDATED_P_FS_TOTAL_PORTION_WEIGHT)
            .potassiumKmgperServing(UPDATED_POTASSIUM_KMGPER_SERVING)
            .proteingperServing(UPDATED_PROTEINGPER_SERVING)
            .saturatedFatgperServing(UPDATED_SATURATED_FATGPER_SERVING)
            .servingUnitofMeasure(UPDATED_SERVING_UNITOF_MEASURE)
            .sesame(UPDATED_SESAME)
            .sugarsgperServing(UPDATED_SUGARSGPER_SERVING)
            .totalCarbohydrategperServing(UPDATED_TOTAL_CARBOHYDRATEGPER_SERVING)
            .uSDAFoodsProductsDescription(UPDATED_U_SDA_FOODS_PRODUCTS_DESCRIPTION)
            .vendorName(UPDATED_VENDOR_NAME)
            .vendorID(UPDATED_VENDOR_ID)
            .wheat(UPDATED_WHEAT);

        restUSDAUpdateMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUSDAUpdateMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUSDAUpdateMst))
            )
            .andExpect(status().isOk());

        // Validate the USDAUpdateMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUSDAUpdateMstUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUSDAUpdateMst, uSDAUpdateMst),
            getPersistedUSDAUpdateMst(uSDAUpdateMst)
        );
    }

    @Test
    @Transactional
    void fullUpdateUSDAUpdateMstWithPatch() throws Exception {
        // Initialize the database
        insertedUSDAUpdateMst = uSDAUpdateMstRepository.saveAndFlush(uSDAUpdateMst);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the uSDAUpdateMst using partial update
        USDAUpdateMst partialUpdatedUSDAUpdateMst = new USDAUpdateMst();
        partialUpdatedUSDAUpdateMst.setId(uSDAUpdateMst.getId());

        partialUpdatedUSDAUpdateMst
            .addedSugarsgperServing(UPDATED_ADDED_SUGARSGPER_SERVING)
            .allAllergens(UPDATED_ALL_ALLERGENS)
            .brandName(UPDATED_BRAND_NAME)
            .calciumCamgperServing(UPDATED_CALCIUM_CAMGPER_SERVING)
            .calorieskcalperServing(UPDATED_CALORIESKCALPER_SERVING)
            .cholesterolmgperServing(UPDATED_CHOLESTEROLMGPER_SERVING)
            .cNCredited(UPDATED_C_N_CREDITED)
            .cNCrediting(UPDATED_C_N_CREDITING)
            .cNExpirationDate(UPDATED_C_N_EXPIRATION_DATE)
            .cNLabelDocument(UPDATED_C_N_LABEL_DOCUMENT)
            .cNLabelStatement(UPDATED_C_N_LABEL_STATEMENT)
            .cNProductIdentification(UPDATED_C_N_PRODUCT_IDENTIFICATION)
            .cNQualified(UPDATED_C_N_QUALIFIED)
            .cNQualifierCode(UPDATED_C_N_QUALIFIER_CODE)
            .dietaryFibergperServing(UPDATED_DIETARY_FIBERGPER_SERVING)
            .downloaded(UPDATED_DOWNLOADED)
            .eggs(UPDATED_EGGS)
            .fish(UPDATED_FISH)
            .foodCategory(UPDATED_FOOD_CATEGORY)
            .functionalname(UPDATED_FUNCTIONALNAME)
            .gtin(UPDATED_GTIN)
            .halal(UPDATED_HALAL)
            .hierarchicalPlacement(UPDATED_HIERARCHICAL_PLACEMENT)
            .informationProvider(UPDATED_INFORMATION_PROVIDER)
            .ingredientsenglish(UPDATED_INGREDIENTSENGLISH)
            .ironFemgperServing(UPDATED_IRON_FEMGPER_SERVING)
            .kosher(UPDATED_KOSHER)
            .lastupdated(UPDATED_LASTUPDATED)
            .longdescription(UPDATED_LONGDESCRIPTION)
            .milk(UPDATED_MILK)
            .nutrientFormatTypeCodeReferenceCode(UPDATED_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE)
            .nutrientQuantityBasisTypeCode(UPDATED_NUTRIENT_QUANTITY_BASIS_TYPE_CODE)
            .nutrientQuantityBasisUnitofMeasure(UPDATED_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE)
            .nutrientQuantityBasisValue(UPDATED_NUTRIENT_QUANTITY_BASIS_VALUE)
            .nutrientsperservingcalculatedfrombymeasurereportedamount(UPDATED_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT)
            .peanuts(UPDATED_PEANUTS)
            .pFSCreditableIngredientTypeCode(UPDATED_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE)
            .pFSDocument(UPDATED_P_FS_DOCUMENT)
            .pFSTotalCreditableIngredientAmount(UPDATED_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT)
            .pFSTotalPortionWeight(UPDATED_P_FS_TOTAL_PORTION_WEIGHT)
            .potassiumKmgperServing(UPDATED_POTASSIUM_KMGPER_SERVING)
            .preparationStateCode(UPDATED_PREPARATION_STATE_CODE)
            .productFormulationStatement(UPDATED_PRODUCT_FORMULATION_STATEMENT)
            .proteingperServing(UPDATED_PROTEINGPER_SERVING)
            .saturatedFatgperServing(UPDATED_SATURATED_FATGPER_SERVING)
            .servingDescription(UPDATED_SERVING_DESCRIPTION)
            .servingSize(UPDATED_SERVING_SIZE)
            .servingUnitofMeasure(UPDATED_SERVING_UNITOF_MEASURE)
            .servingsPerCase(UPDATED_SERVINGS_PER_CASE)
            .sesame(UPDATED_SESAME)
            .shellfish(UPDATED_SHELLFISH)
            .shortdescription(UPDATED_SHORTDESCRIPTION)
            .sodiummgperServing(UPDATED_SODIUMMGPER_SERVING)
            .soybeans(UPDATED_SOYBEANS)
            .sugarsgperServing(UPDATED_SUGARSGPER_SERVING)
            .totalCarbohydrategperServing(UPDATED_TOTAL_CARBOHYDRATEGPER_SERVING)
            .totalFatgperServing(UPDATED_TOTAL_FATGPER_SERVING)
            .tradeChannels(UPDATED_TRADE_CHANNELS)
            .transFatgperServing(UPDATED_TRANS_FATGPER_SERVING)
            .treeNuts(UPDATED_TREE_NUTS)
            .uSDAFoodsMaterialCode(UPDATED_U_SDA_FOODS_MATERIAL_CODE)
            .uSDAFoodsProductsDescription(UPDATED_U_SDA_FOODS_PRODUCTS_DESCRIPTION)
            .vendorName(UPDATED_VENDOR_NAME)
            .vendorID(UPDATED_VENDOR_ID)
            .vitaminDmcgperServing(UPDATED_VITAMIN_DMCGPER_SERVING)
            .wheat(UPDATED_WHEAT);

        restUSDAUpdateMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUSDAUpdateMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUSDAUpdateMst))
            )
            .andExpect(status().isOk());

        // Validate the USDAUpdateMst in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUSDAUpdateMstUpdatableFieldsEquals(partialUpdatedUSDAUpdateMst, getPersistedUSDAUpdateMst(partialUpdatedUSDAUpdateMst));
    }

    @Test
    @Transactional
    void patchNonExistingUSDAUpdateMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        uSDAUpdateMst.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUSDAUpdateMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uSDAUpdateMst.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uSDAUpdateMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUSDAUpdateMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        uSDAUpdateMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUSDAUpdateMstMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(uSDAUpdateMst))
            )
            .andExpect(status().isBadRequest());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUSDAUpdateMst() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        uSDAUpdateMst.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUSDAUpdateMstMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(uSDAUpdateMst)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the USDAUpdateMst in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteUSDAUpdateMst() throws Exception {
        // Initialize the database
        insertedUSDAUpdateMst = uSDAUpdateMstRepository.saveAndFlush(uSDAUpdateMst);
        uSDAUpdateMstRepository.save(uSDAUpdateMst);
        uSDAUpdateMstSearchRepository.save(uSDAUpdateMst);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the uSDAUpdateMst
        restUSDAUpdateMstMockMvc
            .perform(delete(ENTITY_API_URL_ID, uSDAUpdateMst.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(uSDAUpdateMstSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchUSDAUpdateMst() throws Exception {
        // Initialize the database
        insertedUSDAUpdateMst = uSDAUpdateMstRepository.saveAndFlush(uSDAUpdateMst);
        uSDAUpdateMstSearchRepository.save(uSDAUpdateMst);

        // Search the uSDAUpdateMst
        restUSDAUpdateMstMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + uSDAUpdateMst.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uSDAUpdateMst.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedSugarsgperServing").value(hasItem(DEFAULT_ADDED_SUGARSGPER_SERVING)))
            .andExpect(jsonPath("$.[*].allAllergens").value(hasItem(DEFAULT_ALL_ALLERGENS)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].calciumCamgperServing").value(hasItem(DEFAULT_CALCIUM_CAMGPER_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].calorieskcalperServing").value(hasItem(DEFAULT_CALORIESKCALPER_SERVING)))
            .andExpect(jsonPath("$.[*].cholesterolmgperServing").value(hasItem(DEFAULT_CHOLESTEROLMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].cNCredited").value(hasItem(DEFAULT_C_N_CREDITED)))
            .andExpect(jsonPath("$.[*].cNCrediting").value(hasItem(DEFAULT_C_N_CREDITING)))
            .andExpect(jsonPath("$.[*].cNExpirationDate").value(hasItem(DEFAULT_C_N_EXPIRATION_DATE)))
            .andExpect(jsonPath("$.[*].cNLabelDocument").value(hasItem(DEFAULT_C_N_LABEL_DOCUMENT)))
            .andExpect(jsonPath("$.[*].cNLabelStatement").value(hasItem(DEFAULT_C_N_LABEL_STATEMENT.toString())))
            .andExpect(jsonPath("$.[*].cNProductIdentification").value(hasItem(DEFAULT_C_N_PRODUCT_IDENTIFICATION)))
            .andExpect(jsonPath("$.[*].cNQualified").value(hasItem(DEFAULT_C_N_QUALIFIED)))
            .andExpect(jsonPath("$.[*].cNQualifierCode").value(hasItem(DEFAULT_C_N_QUALIFIER_CODE)))
            .andExpect(jsonPath("$.[*].dietaryFibergperServing").value(hasItem(DEFAULT_DIETARY_FIBERGPER_SERVING)))
            .andExpect(jsonPath("$.[*].downloaded").value(hasItem(DEFAULT_DOWNLOADED.toString())))
            .andExpect(jsonPath("$.[*].eggs").value(hasItem(DEFAULT_EGGS)))
            .andExpect(jsonPath("$.[*].fish").value(hasItem(DEFAULT_FISH)))
            .andExpect(jsonPath("$.[*].foodCategory").value(hasItem(DEFAULT_FOOD_CATEGORY)))
            .andExpect(jsonPath("$.[*].functionalname").value(hasItem(DEFAULT_FUNCTIONALNAME)))
            .andExpect(jsonPath("$.[*].gtin").value(hasItem(DEFAULT_GTIN)))
            .andExpect(jsonPath("$.[*].halal").value(hasItem(DEFAULT_HALAL)))
            .andExpect(jsonPath("$.[*].hierarchicalPlacement").value(hasItem(DEFAULT_HIERARCHICAL_PLACEMENT)))
            .andExpect(jsonPath("$.[*].informationProvider").value(hasItem(DEFAULT_INFORMATION_PROVIDER)))
            .andExpect(jsonPath("$.[*].ingredientsenglish").value(hasItem(DEFAULT_INGREDIENTSENGLISH.toString())))
            .andExpect(jsonPath("$.[*].ironFemgperServing").value(hasItem(DEFAULT_IRON_FEMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].kosher").value(hasItem(DEFAULT_KOSHER)))
            .andExpect(jsonPath("$.[*].lastupdated").value(hasItem(DEFAULT_LASTUPDATED.toString())))
            .andExpect(jsonPath("$.[*].longdescription").value(hasItem(DEFAULT_LONGDESCRIPTION)))
            .andExpect(jsonPath("$.[*].milk").value(hasItem(DEFAULT_MILK)))
            .andExpect(
                jsonPath("$.[*].nutrientFormatTypeCodeReferenceCode").value(hasItem(DEFAULT_NUTRIENT_FORMAT_TYPE_CODE_REFERENCE_CODE))
            )
            .andExpect(jsonPath("$.[*].nutrientQuantityBasisTypeCode").value(hasItem(DEFAULT_NUTRIENT_QUANTITY_BASIS_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].nutrientQuantityBasisUnitofMeasure").value(hasItem(DEFAULT_NUTRIENT_QUANTITY_BASIS_UNITOF_MEASURE)))
            .andExpect(jsonPath("$.[*].nutrientQuantityBasisValue").value(hasItem(DEFAULT_NUTRIENT_QUANTITY_BASIS_VALUE)))
            .andExpect(
                jsonPath("$.[*].nutrientsperservingcalculatedfrombymeasurereportedamount").value(
                    hasItem(DEFAULT_NUTRIENTSPERSERVINGCALCULATEDFROMBYMEASUREREPORTEDAMOUNT.booleanValue())
                )
            )
            .andExpect(jsonPath("$.[*].peanuts").value(hasItem(DEFAULT_PEANUTS)))
            .andExpect(jsonPath("$.[*].pFSCreditableIngredientTypeCode").value(hasItem(DEFAULT_P_FS_CREDITABLE_INGREDIENT_TYPE_CODE)))
            .andExpect(jsonPath("$.[*].pFSDocument").value(hasItem(DEFAULT_P_FS_DOCUMENT)))
            .andExpect(jsonPath("$.[*].pFSTotalCreditableIngredientAmount").value(hasItem(DEFAULT_P_FS_TOTAL_CREDITABLE_INGREDIENT_AMOUNT)))
            .andExpect(jsonPath("$.[*].pFSTotalPortionWeight").value(hasItem(DEFAULT_P_FS_TOTAL_PORTION_WEIGHT)))
            .andExpect(jsonPath("$.[*].potassiumKmgperServing").value(hasItem(DEFAULT_POTASSIUM_KMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].preparationStateCode").value(hasItem(DEFAULT_PREPARATION_STATE_CODE)))
            .andExpect(jsonPath("$.[*].productFormulationStatement").value(hasItem(DEFAULT_PRODUCT_FORMULATION_STATEMENT)))
            .andExpect(jsonPath("$.[*].proteingperServing").value(hasItem(DEFAULT_PROTEINGPER_SERVING)))
            .andExpect(jsonPath("$.[*].saturatedFatgperServing").value(hasItem(DEFAULT_SATURATED_FATGPER_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].servingDescription").value(hasItem(DEFAULT_SERVING_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].servingSize").value(hasItem(DEFAULT_SERVING_SIZE)))
            .andExpect(jsonPath("$.[*].servingUnitofMeasure").value(hasItem(DEFAULT_SERVING_UNITOF_MEASURE)))
            .andExpect(jsonPath("$.[*].servingsPerCase").value(hasItem(DEFAULT_SERVINGS_PER_CASE)))
            .andExpect(jsonPath("$.[*].sesame").value(hasItem(DEFAULT_SESAME)))
            .andExpect(jsonPath("$.[*].shellfish").value(hasItem(DEFAULT_SHELLFISH)))
            .andExpect(jsonPath("$.[*].shortdescription").value(hasItem(DEFAULT_SHORTDESCRIPTION)))
            .andExpect(jsonPath("$.[*].sodiummgperServing").value(hasItem(DEFAULT_SODIUMMGPER_SERVING)))
            .andExpect(jsonPath("$.[*].soybeans").value(hasItem(DEFAULT_SOYBEANS)))
            .andExpect(jsonPath("$.[*].sugarsgperServing").value(hasItem(DEFAULT_SUGARSGPER_SERVING)))
            .andExpect(jsonPath("$.[*].totalCarbohydrategperServing").value(hasItem(DEFAULT_TOTAL_CARBOHYDRATEGPER_SERVING.doubleValue())))
            .andExpect(jsonPath("$.[*].totalFatgperServing").value(hasItem(DEFAULT_TOTAL_FATGPER_SERVING)))
            .andExpect(jsonPath("$.[*].tradeChannels").value(hasItem(DEFAULT_TRADE_CHANNELS)))
            .andExpect(jsonPath("$.[*].transFatgperServing").value(hasItem(DEFAULT_TRANS_FATGPER_SERVING)))
            .andExpect(jsonPath("$.[*].treeNuts").value(hasItem(DEFAULT_TREE_NUTS)))
            .andExpect(jsonPath("$.[*].uSDAFoodsMaterialCode").value(hasItem(DEFAULT_U_SDA_FOODS_MATERIAL_CODE)))
            .andExpect(jsonPath("$.[*].uSDAFoodsProductsDescription").value(hasItem(DEFAULT_U_SDA_FOODS_PRODUCTS_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].vendorName").value(hasItem(DEFAULT_VENDOR_NAME)))
            .andExpect(jsonPath("$.[*].vendorID").value(hasItem(DEFAULT_VENDOR_ID)))
            .andExpect(jsonPath("$.[*].vitaminDmcgperServing").value(hasItem(DEFAULT_VITAMIN_DMCGPER_SERVING)))
            .andExpect(jsonPath("$.[*].wheat").value(hasItem(DEFAULT_WHEAT)));
    }

    protected long getRepositoryCount() {
        return uSDAUpdateMstRepository.count();
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

    protected USDAUpdateMst getPersistedUSDAUpdateMst(USDAUpdateMst uSDAUpdateMst) {
        return uSDAUpdateMstRepository.findById(uSDAUpdateMst.getId()).orElseThrow();
    }

    protected void assertPersistedUSDAUpdateMstToMatchAllProperties(USDAUpdateMst expectedUSDAUpdateMst) {
        assertUSDAUpdateMstAllPropertiesEquals(expectedUSDAUpdateMst, getPersistedUSDAUpdateMst(expectedUSDAUpdateMst));
    }

    protected void assertPersistedUSDAUpdateMstToMatchUpdatableProperties(USDAUpdateMst expectedUSDAUpdateMst) {
        assertUSDAUpdateMstAllUpdatablePropertiesEquals(expectedUSDAUpdateMst, getPersistedUSDAUpdateMst(expectedUSDAUpdateMst));
    }
}
