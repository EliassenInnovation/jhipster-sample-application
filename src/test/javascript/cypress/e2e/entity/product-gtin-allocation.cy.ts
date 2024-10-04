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

describe('ProductGtinAllocation e2e test', () => {
  const productGtinAllocationPageUrl = '/product-gtin-allocation';
  const productGtinAllocationPageUrlPattern = new RegExp('/product-gtin-allocation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productGtinAllocationSample = {};

  let productGtinAllocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-gtin-allocations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-gtin-allocations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-gtin-allocations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productGtinAllocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-gtin-allocations/${productGtinAllocation.id}`,
      }).then(() => {
        productGtinAllocation = undefined;
      });
    }
  });

  it('ProductGtinAllocations menu should load ProductGtinAllocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-gtin-allocation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductGtinAllocation').should('exist');
    cy.url().should('match', productGtinAllocationPageUrlPattern);
  });

  describe('ProductGtinAllocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productGtinAllocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductGtinAllocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-gtin-allocation/new$'));
        cy.getEntityCreateUpdateHeading('ProductGtinAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productGtinAllocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-gtin-allocations',
          body: productGtinAllocationSample,
        }).then(({ body }) => {
          productGtinAllocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-gtin-allocations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productGtinAllocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productGtinAllocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductGtinAllocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productGtinAllocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productGtinAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductGtinAllocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductGtinAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productGtinAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductGtinAllocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductGtinAllocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productGtinAllocationPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductGtinAllocation', () => {
        cy.intercept('GET', '/api/product-gtin-allocations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productGtinAllocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productGtinAllocationPageUrlPattern);

        productGtinAllocation = undefined;
      });
    });
  });

  describe('new ProductGtinAllocation page', () => {
    beforeEach(() => {
      cy.visit(`${productGtinAllocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductGtinAllocation');
    });

    it('should create an instance of ProductGtinAllocation', () => {
      cy.get(`[data-cy="createdBy"]`).type('27624');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '27624');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="gTIN"]`).type('daughter inasmuch');
      cy.get(`[data-cy="gTIN"]`).should('have.value', 'daughter inasmuch');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="productGtinId"]`).type('20346');
      cy.get(`[data-cy="productGtinId"]`).should('have.value', '20346');

      cy.get(`[data-cy="productId"]`).type('11883');
      cy.get(`[data-cy="productId"]`).should('have.value', '11883');

      cy.get(`[data-cy="updatedBy"]`).type('22760');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '22760');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productGtinAllocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productGtinAllocationPageUrlPattern);
    });
  });
});
