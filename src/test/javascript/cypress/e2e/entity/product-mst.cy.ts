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

describe('ProductMst e2e test', () => {
  const productMstPageUrl = '/product-mst';
  const productMstPageUrlPattern = new RegExp('/product-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productMstSample = {};

  let productMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-msts/${productMst.id}`,
      }).then(() => {
        productMst = undefined;
      });
    }
  });

  it('ProductMsts menu should load ProductMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductMst').should('exist');
    cy.url().should('match', productMstPageUrlPattern);
  });

  describe('ProductMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-mst/new$'));
        cy.getEntityCreateUpdateHeading('ProductMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-msts',
          body: productMstSample,
        }).then(({ body }) => {
          productMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productMstPageUrlPattern);
      });

      it('edit button click should load edit ProductMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productMstPageUrlPattern);
      });

      it('edit button click should load edit ProductMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productMstPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductMst', () => {
        cy.intercept('GET', '/api/product-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productMstPageUrlPattern);

        productMst = undefined;
      });
    });
  });

  describe('new ProductMst page', () => {
    beforeEach(() => {
      cy.visit(`${productMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductMst');
    });

    it('should create an instance of ProductMst', () => {
      cy.get(`[data-cy="addedSugar"]`).type('23298.98');
      cy.get(`[data-cy="addedSugar"]`).should('have.value', '23298.98');

      cy.get(`[data-cy="addedSugarUom"]`).type('phew swelter rich');
      cy.get(`[data-cy="addedSugarUom"]`).should('have.value', 'phew swelter rich');

      cy.get(`[data-cy="allergenKeywords"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="allergenKeywords"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="brandName"]`).type('replacement qualified');
      cy.get(`[data-cy="brandName"]`).should('have.value', 'replacement qualified');

      cy.get(`[data-cy="calories"]`).type('7811.91');
      cy.get(`[data-cy="calories"]`).should('have.value', '7811.91');

      cy.get(`[data-cy="caloriesUom"]`).type('both voluntarily daily');
      cy.get(`[data-cy="caloriesUom"]`).should('have.value', 'both voluntarily daily');

      cy.get(`[data-cy="carbohydrates"]`).type('32041.95');
      cy.get(`[data-cy="carbohydrates"]`).should('have.value', '32041.95');

      cy.get(`[data-cy="carbohydratesUom"]`).type('likewise during wherever');
      cy.get(`[data-cy="carbohydratesUom"]`).should('have.value', 'likewise during wherever');

      cy.get(`[data-cy="categoryId"]`).type('6697');
      cy.get(`[data-cy="categoryId"]`).should('have.value', '6697');

      cy.get(`[data-cy="cholesterol"]`).type('1417.67');
      cy.get(`[data-cy="cholesterol"]`).should('have.value', '1417.67');

      cy.get(`[data-cy="cholesterolUOM"]`).type('whenever');
      cy.get(`[data-cy="cholesterolUOM"]`).should('have.value', 'whenever');

      cy.get(`[data-cy="createdBy"]`).type('12116');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '12116');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="dietaryFiber"]`).type('15169.34');
      cy.get(`[data-cy="dietaryFiber"]`).should('have.value', '15169.34');

      cy.get(`[data-cy="dietaryFiberUom"]`).type('extra-large after');
      cy.get(`[data-cy="dietaryFiberUom"]`).should('have.value', 'extra-large after');

      cy.get(`[data-cy="gTIN"]`).type('scarcely');
      cy.get(`[data-cy="gTIN"]`).should('have.value', 'scarcely');

      cy.get(`[data-cy="ingredients"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="ingredients"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="iOCCategoryId"]`).type('6623');
      cy.get(`[data-cy="iOCCategoryId"]`).should('have.value', '6623');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="isMerge"]`).should('not.be.checked');
      cy.get(`[data-cy="isMerge"]`).click();
      cy.get(`[data-cy="isMerge"]`).should('be.checked');

      cy.get(`[data-cy="isOneWorldSyncProduct"]`).should('not.be.checked');
      cy.get(`[data-cy="isOneWorldSyncProduct"]`).click();
      cy.get(`[data-cy="isOneWorldSyncProduct"]`).should('be.checked');

      cy.get(`[data-cy="manufacturerId"]`).type('28168');
      cy.get(`[data-cy="manufacturerId"]`).should('have.value', '28168');

      cy.get(`[data-cy="manufacturerProductCode"]`).type('generally');
      cy.get(`[data-cy="manufacturerProductCode"]`).should('have.value', 'generally');

      cy.get(`[data-cy="manufacturerText1Ws"]`).type('trash condense wallop');
      cy.get(`[data-cy="manufacturerText1Ws"]`).should('have.value', 'trash condense wallop');

      cy.get(`[data-cy="mergeDate"]`).type('2024-10-04');
      cy.get(`[data-cy="mergeDate"]`).blur();
      cy.get(`[data-cy="mergeDate"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="productId"]`).type('22954');
      cy.get(`[data-cy="productId"]`).should('have.value', '22954');

      cy.get(`[data-cy="productLabelPdfUrl"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="productLabelPdfUrl"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="productName"]`).type('reconsideration');
      cy.get(`[data-cy="productName"]`).should('have.value', 'reconsideration');

      cy.get(`[data-cy="protein"]`).type('8403.01');
      cy.get(`[data-cy="protein"]`).should('have.value', '8403.01');

      cy.get(`[data-cy="proteinUom"]`).type('um');
      cy.get(`[data-cy="proteinUom"]`).should('have.value', 'um');

      cy.get(`[data-cy="saturatedFat"]`).type('21546.92');
      cy.get(`[data-cy="saturatedFat"]`).should('have.value', '21546.92');

      cy.get(`[data-cy="serving"]`).type('25892.35');
      cy.get(`[data-cy="serving"]`).should('have.value', '25892.35');

      cy.get(`[data-cy="servingUom"]`).type('enraged');
      cy.get(`[data-cy="servingUom"]`).should('have.value', 'enraged');

      cy.get(`[data-cy="sodium"]`).type('4496.53');
      cy.get(`[data-cy="sodium"]`).should('have.value', '4496.53');

      cy.get(`[data-cy="sodiumUom"]`).type('powerless arrogantly actually');
      cy.get(`[data-cy="sodiumUom"]`).should('have.value', 'powerless arrogantly actually');

      cy.get(`[data-cy="storageTypeId"]`).type('12134');
      cy.get(`[data-cy="storageTypeId"]`).should('have.value', '12134');

      cy.get(`[data-cy="subCategoryId"]`).type('12785');
      cy.get(`[data-cy="subCategoryId"]`).should('have.value', '12785');

      cy.get(`[data-cy="sugar"]`).type('11712.91');
      cy.get(`[data-cy="sugar"]`).should('have.value', '11712.91');

      cy.get(`[data-cy="sugarUom"]`).type('thorny importance');
      cy.get(`[data-cy="sugarUom"]`).should('have.value', 'thorny importance');

      cy.get(`[data-cy="totalFat"]`).type('15521.94');
      cy.get(`[data-cy="totalFat"]`).should('have.value', '15521.94');

      cy.get(`[data-cy="transFat"]`).type('12637.91');
      cy.get(`[data-cy="transFat"]`).should('have.value', '12637.91');

      cy.get(`[data-cy="uPC"]`).type('grandpa greedily eggplant');
      cy.get(`[data-cy="uPC"]`).should('have.value', 'grandpa greedily eggplant');

      cy.get(`[data-cy="uPCGTIN"]`).type('intelligent wetly forearm');
      cy.get(`[data-cy="uPCGTIN"]`).should('have.value', 'intelligent wetly forearm');

      cy.get(`[data-cy="updatedBy"]`).type('28564');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '28564');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="vendor"]`).type('a unselfish');
      cy.get(`[data-cy="vendor"]`).should('have.value', 'a unselfish');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productMstPageUrlPattern);
    });
  });
});
