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

describe('ProductsToUpdate e2e test', () => {
  const productsToUpdatePageUrl = '/products-to-update';
  const productsToUpdatePageUrlPattern = new RegExp('/products-to-update(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productsToUpdateSample = {};

  let productsToUpdate;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/products-to-updates+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/products-to-updates').as('postEntityRequest');
    cy.intercept('DELETE', '/api/products-to-updates/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productsToUpdate) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/products-to-updates/${productsToUpdate.id}`,
      }).then(() => {
        productsToUpdate = undefined;
      });
    }
  });

  it('ProductsToUpdates menu should load ProductsToUpdates page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('products-to-update');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductsToUpdate').should('exist');
    cy.url().should('match', productsToUpdatePageUrlPattern);
  });

  describe('ProductsToUpdate page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productsToUpdatePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductsToUpdate page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/products-to-update/new$'));
        cy.getEntityCreateUpdateHeading('ProductsToUpdate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productsToUpdatePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/products-to-updates',
          body: productsToUpdateSample,
        }).then(({ body }) => {
          productsToUpdate = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/products-to-updates+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productsToUpdate],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productsToUpdatePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductsToUpdate page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productsToUpdate');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productsToUpdatePageUrlPattern);
      });

      it('edit button click should load edit ProductsToUpdate page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductsToUpdate');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productsToUpdatePageUrlPattern);
      });

      it('edit button click should load edit ProductsToUpdate page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductsToUpdate');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productsToUpdatePageUrlPattern);
      });

      it('last delete button click should delete instance of ProductsToUpdate', () => {
        cy.intercept('GET', '/api/products-to-updates/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productsToUpdate').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productsToUpdatePageUrlPattern);

        productsToUpdate = undefined;
      });
    });
  });

  describe('new ProductsToUpdate page', () => {
    beforeEach(() => {
      cy.visit(`${productsToUpdatePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductsToUpdate');
    });

    it('should create an instance of ProductsToUpdate', () => {
      cy.get(`[data-cy="maxGLNCode"]`).type('babushka sense');
      cy.get(`[data-cy="maxGLNCode"]`).should('have.value', 'babushka sense');

      cy.get(`[data-cy="maxManufacturerID"]`).type('29514');
      cy.get(`[data-cy="maxManufacturerID"]`).should('have.value', '29514');

      cy.get(`[data-cy="productId"]`).type('25081');
      cy.get(`[data-cy="productId"]`).should('have.value', '25081');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productsToUpdate = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productsToUpdatePageUrlPattern);
    });
  });
});
