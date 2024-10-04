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

describe('ProductUpcAllocation e2e test', () => {
  const productUpcAllocationPageUrl = '/product-upc-allocation';
  const productUpcAllocationPageUrlPattern = new RegExp('/product-upc-allocation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productUpcAllocationSample = {};

  let productUpcAllocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-upc-allocations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-upc-allocations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-upc-allocations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productUpcAllocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-upc-allocations/${productUpcAllocation.id}`,
      }).then(() => {
        productUpcAllocation = undefined;
      });
    }
  });

  it('ProductUpcAllocations menu should load ProductUpcAllocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-upc-allocation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductUpcAllocation').should('exist');
    cy.url().should('match', productUpcAllocationPageUrlPattern);
  });

  describe('ProductUpcAllocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productUpcAllocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductUpcAllocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-upc-allocation/new$'));
        cy.getEntityCreateUpdateHeading('ProductUpcAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productUpcAllocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-upc-allocations',
          body: productUpcAllocationSample,
        }).then(({ body }) => {
          productUpcAllocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-upc-allocations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productUpcAllocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productUpcAllocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductUpcAllocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productUpcAllocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productUpcAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductUpcAllocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductUpcAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productUpcAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductUpcAllocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductUpcAllocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productUpcAllocationPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductUpcAllocation', () => {
        cy.intercept('GET', '/api/product-upc-allocations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productUpcAllocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productUpcAllocationPageUrlPattern);

        productUpcAllocation = undefined;
      });
    });
  });

  describe('new ProductUpcAllocation page', () => {
    beforeEach(() => {
      cy.visit(`${productUpcAllocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductUpcAllocation');
    });

    it('should create an instance of ProductUpcAllocation', () => {
      cy.get(`[data-cy="createdBy"]`).type('5982');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '5982');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="productId"]`).type('5722');
      cy.get(`[data-cy="productId"]`).should('have.value', '5722');

      cy.get(`[data-cy="productUpcId"]`).type('25869');
      cy.get(`[data-cy="productUpcId"]`).should('have.value', '25869');

      cy.get(`[data-cy="uPC"]`).type('unpleasant properly partially');
      cy.get(`[data-cy="uPC"]`).should('have.value', 'unpleasant properly partially');

      cy.get(`[data-cy="updatedBy"]`).type('2519');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '2519');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-03');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-03');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productUpcAllocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productUpcAllocationPageUrlPattern);
    });
  });
});
