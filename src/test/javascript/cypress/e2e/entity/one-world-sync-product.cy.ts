import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('OneWorldSyncProduct e2e test', () => {
  const oneWorldSyncProductPageUrl = '/one-world-sync-product';
  const oneWorldSyncProductPageUrlPattern = new RegExp('/one-world-sync-product(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const oneWorldSyncProductSample = {};

  let oneWorldSyncProduct;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/one-world-sync-products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/one-world-sync-products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/one-world-sync-products/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (oneWorldSyncProduct) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/one-world-sync-products/${oneWorldSyncProduct.id}`,
      }).then(() => {
        oneWorldSyncProduct = undefined;
      });
    }
  });

  it('OneWorldSyncProducts menu should load OneWorldSyncProducts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('one-world-sync-product');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('OneWorldSyncProduct').should('exist');
    cy.url().should('match', oneWorldSyncProductPageUrlPattern);
  });

  describe('OneWorldSyncProduct page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(oneWorldSyncProductPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create OneWorldSyncProduct page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/one-world-sync-product/new$'));
        cy.getEntityCreateUpdateHeading('OneWorldSyncProduct');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', oneWorldSyncProductPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/one-world-sync-products',
          body: oneWorldSyncProductSample,
        }).then(({ body }) => {
          oneWorldSyncProduct = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/one-world-sync-products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [oneWorldSyncProduct],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(oneWorldSyncProductPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details OneWorldSyncProduct page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('oneWorldSyncProduct');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', oneWorldSyncProductPageUrlPattern);
      });

      it('edit button click should load edit OneWorldSyncProduct page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OneWorldSyncProduct');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', oneWorldSyncProductPageUrlPattern);
      });

      it('edit button click should load edit OneWorldSyncProduct page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('OneWorldSyncProduct');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', oneWorldSyncProductPageUrlPattern);
      });

      it('last delete button click should delete instance of OneWorldSyncProduct', () => {
        cy.intercept('GET', '/api/one-world-sync-products/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('oneWorldSyncProduct').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', oneWorldSyncProductPageUrlPattern);

        oneWorldSyncProduct = undefined;
      });
    });
  });

  describe('new OneWorldSyncProduct page', () => {
    beforeEach(() => {
      cy.visit(`${oneWorldSyncProductPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('OneWorldSyncProduct');
    });

    it('should create an instance of OneWorldSyncProduct', () => {
      cy.get(`[data-cy="addedSugars"]`).type('at');
      cy.get(`[data-cy="addedSugars"]`).should('have.value', 'at');

      cy.get(`[data-cy="addedSugarUom"]`).type('amidst mammoth');
      cy.get(`[data-cy="addedSugarUom"]`).should('have.value', 'amidst mammoth');

      cy.get(`[data-cy="allergenKeyword"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="allergenKeyword"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="allergens"]`).type('28367');
      cy.get(`[data-cy="allergens"]`).should('have.value', '28367');

      cy.get(`[data-cy="brandName"]`).type('after');
      cy.get(`[data-cy="brandName"]`).should('have.value', 'after');

      cy.get(`[data-cy="calories"]`).type('behest phooey gosh');
      cy.get(`[data-cy="calories"]`).should('have.value', 'behest phooey gosh');

      cy.get(`[data-cy="caloriesUom"]`).type('old-fashioned');
      cy.get(`[data-cy="caloriesUom"]`).should('have.value', 'old-fashioned');

      cy.get(`[data-cy="carbohydrates"]`).type('er delight regularly');
      cy.get(`[data-cy="carbohydrates"]`).should('have.value', 'er delight regularly');

      cy.get(`[data-cy="carbohydratesUom"]`).type('pfft before');
      cy.get(`[data-cy="carbohydratesUom"]`).should('have.value', 'pfft before');

      cy.get(`[data-cy="categoryName"]`).type('yawn though');
      cy.get(`[data-cy="categoryName"]`).should('have.value', 'yawn though');

      cy.get(`[data-cy="cholesterol"]`).type('break when um');
      cy.get(`[data-cy="cholesterol"]`).should('have.value', 'break when um');

      cy.get(`[data-cy="cholesterolUOM"]`).type('determined');
      cy.get(`[data-cy="cholesterolUOM"]`).should('have.value', 'determined');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-03');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="dataForm"]`).type('pfft strictly');
      cy.get(`[data-cy="dataForm"]`).should('have.value', 'pfft strictly');

      cy.get(`[data-cy="dietaryFiber"]`).type('before bah');
      cy.get(`[data-cy="dietaryFiber"]`).should('have.value', 'before bah');

      cy.get(`[data-cy="dietaryFiberUom"]`).type('whenever');
      cy.get(`[data-cy="dietaryFiberUom"]`).should('have.value', 'whenever');

      cy.get(`[data-cy="distributor"]`).type('owlishly sophisticated');
      cy.get(`[data-cy="distributor"]`).should('have.value', 'owlishly sophisticated');

      cy.get(`[data-cy="doNotConsiderProduct"]`).should('not.be.checked');
      cy.get(`[data-cy="doNotConsiderProduct"]`).click();
      cy.get(`[data-cy="doNotConsiderProduct"]`).should('be.checked');

      cy.get(`[data-cy="extendedModel"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="extendedModel"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="gLNNumber"]`).type('mad hype extremely');
      cy.get(`[data-cy="gLNNumber"]`).should('have.value', 'mad hype extremely');

      cy.get(`[data-cy="gTIN"]`).type('oof deceivingly consequently');
      cy.get(`[data-cy="gTIN"]`).should('have.value', 'oof deceivingly consequently');

      cy.get(`[data-cy="h7"]`).type('3759');
      cy.get(`[data-cy="h7"]`).should('have.value', '3759');

      cy.get(`[data-cy="image"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="image"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="ingredients"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="ingredients"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="isApprove"]`).should('not.be.checked');
      cy.get(`[data-cy="isApprove"]`).click();
      cy.get(`[data-cy="isApprove"]`).should('be.checked');

      cy.get(`[data-cy="isMerge"]`).should('not.be.checked');
      cy.get(`[data-cy="isMerge"]`).click();
      cy.get(`[data-cy="isMerge"]`).should('be.checked');

      cy.get(`[data-cy="isProductSync"]`).should('not.be.checked');
      cy.get(`[data-cy="isProductSync"]`).click();
      cy.get(`[data-cy="isProductSync"]`).should('be.checked');

      cy.get(`[data-cy="manufacturer"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="manufacturer"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="manufacturerId"]`).type('24378');
      cy.get(`[data-cy="manufacturerId"]`).should('have.value', '24378');

      cy.get(`[data-cy="manufacturerText1Ws"]`).type('nicely worriedly coal');
      cy.get(`[data-cy="manufacturerText1Ws"]`).should('have.value', 'nicely worriedly coal');

      cy.get(`[data-cy="modifiedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="modifiedOn"]`).blur();
      cy.get(`[data-cy="modifiedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="productDescription"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="productDescription"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="productId"]`).type('8450');
      cy.get(`[data-cy="productId"]`).should('have.value', '8450');

      cy.get(`[data-cy="productName"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="productName"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="protein"]`).type('contrail');
      cy.get(`[data-cy="protein"]`).should('have.value', 'contrail');

      cy.get(`[data-cy="proteinUom"]`).type('meanwhile');
      cy.get(`[data-cy="proteinUom"]`).should('have.value', 'meanwhile');

      cy.get(`[data-cy="saturatedFat"]`).type('tighten splosh');
      cy.get(`[data-cy="saturatedFat"]`).should('have.value', 'tighten splosh');

      cy.get(`[data-cy="serving"]`).type('second wash traditionalism');
      cy.get(`[data-cy="serving"]`).should('have.value', 'second wash traditionalism');

      cy.get(`[data-cy="servingUom"]`).type('till');
      cy.get(`[data-cy="servingUom"]`).should('have.value', 'till');

      cy.get(`[data-cy="sodium"]`).type('boohoo who elementary');
      cy.get(`[data-cy="sodium"]`).should('have.value', 'boohoo who elementary');

      cy.get(`[data-cy="sodiumUom"]`).type('likewise back represent');
      cy.get(`[data-cy="sodiumUom"]`).should('have.value', 'likewise back represent');

      cy.get(`[data-cy="storageTypeId"]`).type('25936');
      cy.get(`[data-cy="storageTypeId"]`).should('have.value', '25936');

      cy.get(`[data-cy="storageTypeName"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="storageTypeName"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="subCategory1Name"]`).type('gee');
      cy.get(`[data-cy="subCategory1Name"]`).should('have.value', 'gee');

      cy.get(`[data-cy="subCategory2Name"]`).type('who oof');
      cy.get(`[data-cy="subCategory2Name"]`).should('have.value', 'who oof');

      cy.get(`[data-cy="sugar"]`).type('waist waft');
      cy.get(`[data-cy="sugar"]`).should('have.value', 'waist waft');

      cy.get(`[data-cy="sugarUom"]`).type('best');
      cy.get(`[data-cy="sugarUom"]`).should('have.value', 'best');

      cy.get(`[data-cy="syncEffective"]`).type('2024-10-04');
      cy.get(`[data-cy="syncEffective"]`).blur();
      cy.get(`[data-cy="syncEffective"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="syncHeaderLastChange"]`).type('2024-10-04');
      cy.get(`[data-cy="syncHeaderLastChange"]`).blur();
      cy.get(`[data-cy="syncHeaderLastChange"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="syncItemReferenceId"]`).type('upright worse reproachfully');
      cy.get(`[data-cy="syncItemReferenceId"]`).should('have.value', 'upright worse reproachfully');

      cy.get(`[data-cy="syncLastChange"]`).type('2024-10-04');
      cy.get(`[data-cy="syncLastChange"]`).blur();
      cy.get(`[data-cy="syncLastChange"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="syncPublication"]`).type('2024-10-04');
      cy.get(`[data-cy="syncPublication"]`).blur();
      cy.get(`[data-cy="syncPublication"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="totalFat"]`).type('offensively fruitful');
      cy.get(`[data-cy="totalFat"]`).should('have.value', 'offensively fruitful');

      cy.get(`[data-cy="transFat"]`).type('thankfully sweetly');
      cy.get(`[data-cy="transFat"]`).should('have.value', 'thankfully sweetly');

      cy.get(`[data-cy="uPC"]`).type('whoa pasta spectacles');
      cy.get(`[data-cy="uPC"]`).should('have.value', 'whoa pasta spectacles');

      cy.get(`[data-cy="vendor"]`).type('zowie unethically assail');
      cy.get(`[data-cy="vendor"]`).should('have.value', 'zowie unethically assail');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        oneWorldSyncProduct = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', oneWorldSyncProductPageUrlPattern);
    });
  });
});
