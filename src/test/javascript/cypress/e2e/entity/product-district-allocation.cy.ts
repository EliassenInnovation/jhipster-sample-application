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

describe('ProductDistrictAllocation e2e test', () => {
  const productDistrictAllocationPageUrl = '/product-district-allocation';
  const productDistrictAllocationPageUrlPattern = new RegExp('/product-district-allocation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productDistrictAllocationSample = {};

  let productDistrictAllocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-district-allocations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-district-allocations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-district-allocations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productDistrictAllocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-district-allocations/${productDistrictAllocation.id}`,
      }).then(() => {
        productDistrictAllocation = undefined;
      });
    }
  });

  it('ProductDistrictAllocations menu should load ProductDistrictAllocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-district-allocation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductDistrictAllocation').should('exist');
    cy.url().should('match', productDistrictAllocationPageUrlPattern);
  });

  describe('ProductDistrictAllocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productDistrictAllocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductDistrictAllocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-district-allocation/new$'));
        cy.getEntityCreateUpdateHeading('ProductDistrictAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistrictAllocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-district-allocations',
          body: productDistrictAllocationSample,
        }).then(({ body }) => {
          productDistrictAllocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-district-allocations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productDistrictAllocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productDistrictAllocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductDistrictAllocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productDistrictAllocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistrictAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductDistrictAllocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductDistrictAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistrictAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductDistrictAllocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductDistrictAllocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistrictAllocationPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductDistrictAllocation', () => {
        cy.intercept('GET', '/api/product-district-allocations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productDistrictAllocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistrictAllocationPageUrlPattern);

        productDistrictAllocation = undefined;
      });
    });
  });

  describe('new ProductDistrictAllocation page', () => {
    beforeEach(() => {
      cy.visit(`${productDistrictAllocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductDistrictAllocation');
    });

    it('should create an instance of ProductDistrictAllocation', () => {
      cy.get(`[data-cy="createdBy"]`).type('17332');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '17332');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isAllocated"]`).should('not.be.checked');
      cy.get(`[data-cy="isAllocated"]`).click();
      cy.get(`[data-cy="isAllocated"]`).should('be.checked');

      cy.get(`[data-cy="productDistrictAllocationId"]`).type('2c78b018-aef2-4374-a85f-95acace48dfb');
      cy.get(`[data-cy="productDistrictAllocationId"]`).invoke('val').should('match', new RegExp('2c78b018-aef2-4374-a85f-95acace48dfb'));

      cy.get(`[data-cy="productId"]`).type('23961');
      cy.get(`[data-cy="productId"]`).should('have.value', '23961');

      cy.get(`[data-cy="schoolDistrictId"]`).type('563');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '563');

      cy.get(`[data-cy="updatedBy"]`).type('24117');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '24117');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productDistrictAllocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productDistrictAllocationPageUrlPattern);
    });
  });
});
