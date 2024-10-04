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

describe('ProductBeforeApprove e2e test', () => {
  const productBeforeApprovePageUrl = '/product-before-approve';
  const productBeforeApprovePageUrlPattern = new RegExp('/product-before-approve(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productBeforeApproveSample = {};

  let productBeforeApprove;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-before-approves+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-before-approves').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-before-approves/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productBeforeApprove) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-before-approves/${productBeforeApprove.id}`,
      }).then(() => {
        productBeforeApprove = undefined;
      });
    }
  });

  it('ProductBeforeApproves menu should load ProductBeforeApproves page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-before-approve');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductBeforeApprove').should('exist');
    cy.url().should('match', productBeforeApprovePageUrlPattern);
  });

  describe('ProductBeforeApprove page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productBeforeApprovePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductBeforeApprove page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-before-approve/new$'));
        cy.getEntityCreateUpdateHeading('ProductBeforeApprove');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productBeforeApprovePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-before-approves',
          body: productBeforeApproveSample,
        }).then(({ body }) => {
          productBeforeApprove = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-before-approves+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productBeforeApprove],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productBeforeApprovePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductBeforeApprove page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productBeforeApprove');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productBeforeApprovePageUrlPattern);
      });

      it('edit button click should load edit ProductBeforeApprove page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductBeforeApprove');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productBeforeApprovePageUrlPattern);
      });

      it('edit button click should load edit ProductBeforeApprove page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductBeforeApprove');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productBeforeApprovePageUrlPattern);
      });

      it('last delete button click should delete instance of ProductBeforeApprove', () => {
        cy.intercept('GET', '/api/product-before-approves/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productBeforeApprove').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productBeforeApprovePageUrlPattern);

        productBeforeApprove = undefined;
      });
    });
  });

  describe('new ProductBeforeApprove page', () => {
    beforeEach(() => {
      cy.visit(`${productBeforeApprovePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductBeforeApprove');
    });

    it('should create an instance of ProductBeforeApprove', () => {
      cy.get(`[data-cy="addedSugar"]`).type('12636.77');
      cy.get(`[data-cy="addedSugar"]`).should('have.value', '12636.77');

      cy.get(`[data-cy="addedSugarUom"]`).type('ha going');
      cy.get(`[data-cy="addedSugarUom"]`).should('have.value', 'ha going');

      cy.get(`[data-cy="allergenKeywords"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="allergenKeywords"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="brandName"]`).type('psst coaxingly sophisticated');
      cy.get(`[data-cy="brandName"]`).should('have.value', 'psst coaxingly sophisticated');

      cy.get(`[data-cy="calories"]`).type('22335.88');
      cy.get(`[data-cy="calories"]`).should('have.value', '22335.88');

      cy.get(`[data-cy="caloriesUom"]`).type('nucleotidase');
      cy.get(`[data-cy="caloriesUom"]`).should('have.value', 'nucleotidase');

      cy.get(`[data-cy="carbohydrates"]`).type('7315.22');
      cy.get(`[data-cy="carbohydrates"]`).should('have.value', '7315.22');

      cy.get(`[data-cy="carbohydratesUom"]`).type('moralise arrange');
      cy.get(`[data-cy="carbohydratesUom"]`).should('have.value', 'moralise arrange');

      cy.get(`[data-cy="categoryId"]`).type('16357');
      cy.get(`[data-cy="categoryId"]`).should('have.value', '16357');

      cy.get(`[data-cy="cholesterol"]`).type('19356.6');
      cy.get(`[data-cy="cholesterol"]`).should('have.value', '19356.6');

      cy.get(`[data-cy="cholesterolUOM"]`).type('euphonium from sit');
      cy.get(`[data-cy="cholesterolUOM"]`).should('have.value', 'euphonium from sit');

      cy.get(`[data-cy="createdBy"]`).type('9484');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '9484');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="dietaryFiber"]`).type('12564.06');
      cy.get(`[data-cy="dietaryFiber"]`).should('have.value', '12564.06');

      cy.get(`[data-cy="dietaryFiberUom"]`).type('righteously ha pluck');
      cy.get(`[data-cy="dietaryFiberUom"]`).should('have.value', 'righteously ha pluck');

      cy.get(`[data-cy="distributorId"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="distributorId"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="gTIN"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="gTIN"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="ingredients"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="ingredients"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="iocCategoryId"]`).type('24029');
      cy.get(`[data-cy="iocCategoryId"]`).should('have.value', '24029');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="isMerge"]`).should('not.be.checked');
      cy.get(`[data-cy="isMerge"]`).click();
      cy.get(`[data-cy="isMerge"]`).should('be.checked');

      cy.get(`[data-cy="manufacturerId"]`).type('28213');
      cy.get(`[data-cy="manufacturerId"]`).should('have.value', '28213');

      cy.get(`[data-cy="manufacturerProductCode"]`).type('brr ouch');
      cy.get(`[data-cy="manufacturerProductCode"]`).should('have.value', 'brr ouch');

      cy.get(`[data-cy="mergeDate"]`).type('2024-10-04');
      cy.get(`[data-cy="mergeDate"]`).blur();
      cy.get(`[data-cy="mergeDate"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="productId"]`).type('19194');
      cy.get(`[data-cy="productId"]`).should('have.value', '19194');

      cy.get(`[data-cy="productLabelPdfUrl"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="productLabelPdfUrl"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="productName"]`).type('ouch');
      cy.get(`[data-cy="productName"]`).should('have.value', 'ouch');

      cy.get(`[data-cy="protein"]`).type('18958.33');
      cy.get(`[data-cy="protein"]`).should('have.value', '18958.33');

      cy.get(`[data-cy="proteinUom"]`).type('interior priesthood');
      cy.get(`[data-cy="proteinUom"]`).should('have.value', 'interior priesthood');

      cy.get(`[data-cy="saturatedFat"]`).type('20885.21');
      cy.get(`[data-cy="saturatedFat"]`).should('have.value', '20885.21');

      cy.get(`[data-cy="serving"]`).type('27554.86');
      cy.get(`[data-cy="serving"]`).should('have.value', '27554.86');

      cy.get(`[data-cy="servingUom"]`).type('but');
      cy.get(`[data-cy="servingUom"]`).should('have.value', 'but');

      cy.get(`[data-cy="sodium"]`).type('9753.8');
      cy.get(`[data-cy="sodium"]`).should('have.value', '9753.8');

      cy.get(`[data-cy="sodiumUom"]`).type('worth enfold casement');
      cy.get(`[data-cy="sodiumUom"]`).should('have.value', 'worth enfold casement');

      cy.get(`[data-cy="storageTypeId"]`).type('12557');
      cy.get(`[data-cy="storageTypeId"]`).should('have.value', '12557');

      cy.get(`[data-cy="subCategoryId"]`).type('29764');
      cy.get(`[data-cy="subCategoryId"]`).should('have.value', '29764');

      cy.get(`[data-cy="sugar"]`).type('4460.8');
      cy.get(`[data-cy="sugar"]`).should('have.value', '4460.8');

      cy.get(`[data-cy="sugarUom"]`).type('honestly');
      cy.get(`[data-cy="sugarUom"]`).should('have.value', 'honestly');

      cy.get(`[data-cy="totalFat"]`).type('8954.52');
      cy.get(`[data-cy="totalFat"]`).should('have.value', '8954.52');

      cy.get(`[data-cy="transFat"]`).type('23529.77');
      cy.get(`[data-cy="transFat"]`).should('have.value', '23529.77');

      cy.get(`[data-cy="uPC"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="uPC"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="updatedBy"]`).type('31671');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '31671');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="vendor"]`).type('inasmuch');
      cy.get(`[data-cy="vendor"]`).should('have.value', 'inasmuch');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productBeforeApprove = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productBeforeApprovePageUrlPattern);
    });
  });
});
