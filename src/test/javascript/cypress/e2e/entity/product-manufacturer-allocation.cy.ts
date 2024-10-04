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

describe('ProductManufacturerAllocation e2e test', () => {
  const productManufacturerAllocationPageUrl = '/product-manufacturer-allocation';
  const productManufacturerAllocationPageUrlPattern = new RegExp('/product-manufacturer-allocation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productManufacturerAllocationSample = {};

  let productManufacturerAllocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-manufacturer-allocations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-manufacturer-allocations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-manufacturer-allocations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productManufacturerAllocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-manufacturer-allocations/${productManufacturerAllocation.id}`,
      }).then(() => {
        productManufacturerAllocation = undefined;
      });
    }
  });

  it('ProductManufacturerAllocations menu should load ProductManufacturerAllocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-manufacturer-allocation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductManufacturerAllocation').should('exist');
    cy.url().should('match', productManufacturerAllocationPageUrlPattern);
  });

  describe('ProductManufacturerAllocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productManufacturerAllocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductManufacturerAllocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-manufacturer-allocation/new$'));
        cy.getEntityCreateUpdateHeading('ProductManufacturerAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productManufacturerAllocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-manufacturer-allocations',
          body: productManufacturerAllocationSample,
        }).then(({ body }) => {
          productManufacturerAllocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-manufacturer-allocations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productManufacturerAllocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productManufacturerAllocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductManufacturerAllocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productManufacturerAllocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productManufacturerAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductManufacturerAllocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductManufacturerAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productManufacturerAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductManufacturerAllocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductManufacturerAllocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productManufacturerAllocationPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductManufacturerAllocation', () => {
        cy.intercept('GET', '/api/product-manufacturer-allocations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productManufacturerAllocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productManufacturerAllocationPageUrlPattern);

        productManufacturerAllocation = undefined;
      });
    });
  });

  describe('new ProductManufacturerAllocation page', () => {
    beforeEach(() => {
      cy.visit(`${productManufacturerAllocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductManufacturerAllocation');
    });

    it('should create an instance of ProductManufacturerAllocation', () => {
      cy.get(`[data-cy="createdBy"]`).type('14604');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '14604');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isAllocated"]`).should('not.be.checked');
      cy.get(`[data-cy="isAllocated"]`).click();
      cy.get(`[data-cy="isAllocated"]`).should('be.checked');

      cy.get(`[data-cy="manufactureId"]`).type('13211');
      cy.get(`[data-cy="manufactureId"]`).should('have.value', '13211');

      cy.get(`[data-cy="productId"]`).type('30775');
      cy.get(`[data-cy="productId"]`).should('have.value', '30775');

      cy.get(`[data-cy="productManufacturerAllocationId"]`).type('bbd9d211-30e8-4f3b-9ff8-8868e026fe77');
      cy.get(`[data-cy="productManufacturerAllocationId"]`)
        .invoke('val')
        .should('match', new RegExp('bbd9d211-30e8-4f3b-9ff8-8868e026fe77'));

      cy.get(`[data-cy="updatedBy"]`).type('1713');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '1713');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productManufacturerAllocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productManufacturerAllocationPageUrlPattern);
    });
  });
});
