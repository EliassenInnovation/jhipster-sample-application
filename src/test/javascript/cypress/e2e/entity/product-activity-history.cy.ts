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

describe('ProductActivityHistory e2e test', () => {
  const productActivityHistoryPageUrl = '/product-activity-history';
  const productActivityHistoryPageUrlPattern = new RegExp('/product-activity-history(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productActivityHistorySample = {};

  let productActivityHistory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-activity-histories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-activity-histories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-activity-histories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productActivityHistory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-activity-histories/${productActivityHistory.id}`,
      }).then(() => {
        productActivityHistory = undefined;
      });
    }
  });

  it('ProductActivityHistories menu should load ProductActivityHistories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-activity-history');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductActivityHistory').should('exist');
    cy.url().should('match', productActivityHistoryPageUrlPattern);
  });

  describe('ProductActivityHistory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productActivityHistoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductActivityHistory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-activity-history/new$'));
        cy.getEntityCreateUpdateHeading('ProductActivityHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productActivityHistoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-activity-histories',
          body: productActivityHistorySample,
        }).then(({ body }) => {
          productActivityHistory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-activity-histories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productActivityHistory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productActivityHistoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductActivityHistory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productActivityHistory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productActivityHistoryPageUrlPattern);
      });

      it('edit button click should load edit ProductActivityHistory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductActivityHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productActivityHistoryPageUrlPattern);
      });

      it('edit button click should load edit ProductActivityHistory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductActivityHistory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productActivityHistoryPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductActivityHistory', () => {
        cy.intercept('GET', '/api/product-activity-histories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productActivityHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productActivityHistoryPageUrlPattern);

        productActivityHistory = undefined;
      });
    });
  });

  describe('new ProductActivityHistory page', () => {
    beforeEach(() => {
      cy.visit(`${productActivityHistoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductActivityHistory');
    });

    it('should create an instance of ProductActivityHistory', () => {
      cy.get(`[data-cy="activityId"]`).type('23617');
      cy.get(`[data-cy="activityId"]`).should('have.value', '23617');

      cy.get(`[data-cy="activityType"]`).type('greedily');
      cy.get(`[data-cy="activityType"]`).should('have.value', 'greedily');

      cy.get(`[data-cy="createdBy"]`).type('28988');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '28988');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="date"]`).type('2024-10-04');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="productId"]`).type('26880');
      cy.get(`[data-cy="productId"]`).should('have.value', '26880');

      cy.get(`[data-cy="suggestedProductId"]`).type('25218');
      cy.get(`[data-cy="suggestedProductId"]`).should('have.value', '25218');

      cy.get(`[data-cy="updatedBy"]`).type('225');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '225');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productActivityHistory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productActivityHistoryPageUrlPattern);
    });
  });
});
