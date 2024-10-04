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

describe('ProductChangeHistory e2e test', () => {
  const productChangeHistoryPageUrl = '/product-change-history';
  const productChangeHistoryPageUrlPattern = new RegExp('/product-change-history(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productChangeHistorySample = {};

  let productChangeHistory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-change-histories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-change-histories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-change-histories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productChangeHistory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-change-histories/${productChangeHistory.id}`,
      }).then(() => {
        productChangeHistory = undefined;
      });
    }
  });

  it('ProductChangeHistories menu should load ProductChangeHistories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-change-history');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductChangeHistory').should('exist');
    cy.url().should('match', productChangeHistoryPageUrlPattern);
  });

  describe('ProductChangeHistory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productChangeHistoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductChangeHistory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-change-history/new$'));
        cy.getEntityCreateUpdateHeading('ProductChangeHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productChangeHistoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-change-histories',
          body: productChangeHistorySample,
        }).then(({ body }) => {
          productChangeHistory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-change-histories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productChangeHistory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productChangeHistoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductChangeHistory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productChangeHistory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productChangeHistoryPageUrlPattern);
      });

      it('edit button click should load edit ProductChangeHistory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductChangeHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productChangeHistoryPageUrlPattern);
      });

      it('edit button click should load edit ProductChangeHistory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductChangeHistory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productChangeHistoryPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductChangeHistory', () => {
        cy.intercept('GET', '/api/product-change-histories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productChangeHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productChangeHistoryPageUrlPattern);

        productChangeHistory = undefined;
      });
    });
  });

  describe('new ProductChangeHistory page', () => {
    beforeEach(() => {
      cy.visit(`${productChangeHistoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductChangeHistory');
    });

    it('should create an instance of ProductChangeHistory', () => {
      cy.get(`[data-cy="createdBy"]`).type('23385');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '23385');

      cy.get(`[data-cy="dateCreated"]`).type('2024-10-04');
      cy.get(`[data-cy="dateCreated"]`).blur();
      cy.get(`[data-cy="dateCreated"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="historyId"]`).type('6bfafefe-91b3-47c5-a328-fd7914731e69');
      cy.get(`[data-cy="historyId"]`).invoke('val').should('match', new RegExp('6bfafefe-91b3-47c5-a328-fd7914731e69'));

      cy.get(`[data-cy="iocCategoryId"]`).type('27414');
      cy.get(`[data-cy="iocCategoryId"]`).should('have.value', '27414');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="productId"]`).type('29215');
      cy.get(`[data-cy="productId"]`).should('have.value', '29215');

      cy.get(`[data-cy="schoolDistrictId"]`).type('22379');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '22379');

      cy.get(`[data-cy="selectionType"]`).type('maestro readmit toaster');
      cy.get(`[data-cy="selectionType"]`).should('have.value', 'maestro readmit toaster');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productChangeHistory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productChangeHistoryPageUrlPattern);
    });
  });
});
