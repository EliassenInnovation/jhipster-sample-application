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

describe('ProductDistributorAllocation e2e test', () => {
  const productDistributorAllocationPageUrl = '/product-distributor-allocation';
  const productDistributorAllocationPageUrlPattern = new RegExp('/product-distributor-allocation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productDistributorAllocationSample = {};

  let productDistributorAllocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-distributor-allocations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-distributor-allocations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-distributor-allocations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productDistributorAllocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-distributor-allocations/${productDistributorAllocation.id}`,
      }).then(() => {
        productDistributorAllocation = undefined;
      });
    }
  });

  it('ProductDistributorAllocations menu should load ProductDistributorAllocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-distributor-allocation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductDistributorAllocation').should('exist');
    cy.url().should('match', productDistributorAllocationPageUrlPattern);
  });

  describe('ProductDistributorAllocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productDistributorAllocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductDistributorAllocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-distributor-allocation/new$'));
        cy.getEntityCreateUpdateHeading('ProductDistributorAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistributorAllocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-distributor-allocations',
          body: productDistributorAllocationSample,
        }).then(({ body }) => {
          productDistributorAllocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-distributor-allocations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productDistributorAllocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productDistributorAllocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductDistributorAllocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productDistributorAllocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistributorAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductDistributorAllocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductDistributorAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistributorAllocationPageUrlPattern);
      });

      it('edit button click should load edit ProductDistributorAllocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductDistributorAllocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistributorAllocationPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductDistributorAllocation', () => {
        cy.intercept('GET', '/api/product-distributor-allocations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productDistributorAllocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productDistributorAllocationPageUrlPattern);

        productDistributorAllocation = undefined;
      });
    });
  });

  describe('new ProductDistributorAllocation page', () => {
    beforeEach(() => {
      cy.visit(`${productDistributorAllocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductDistributorAllocation');
    });

    it('should create an instance of ProductDistributorAllocation', () => {
      cy.get(`[data-cy="createdBy"]`).type('23639');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '23639');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="distributorId"]`).type('19369');
      cy.get(`[data-cy="distributorId"]`).should('have.value', '19369');

      cy.get(`[data-cy="isAllocated"]`).should('not.be.checked');
      cy.get(`[data-cy="isAllocated"]`).click();
      cy.get(`[data-cy="isAllocated"]`).should('be.checked');

      cy.get(`[data-cy="productDistributorAllocationId"]`).type('e5f686f4-65e2-422b-982f-26ca102fa342');
      cy.get(`[data-cy="productDistributorAllocationId"]`)
        .invoke('val')
        .should('match', new RegExp('e5f686f4-65e2-422b-982f-26ca102fa342'));

      cy.get(`[data-cy="productId"]`).type('27224');
      cy.get(`[data-cy="productId"]`).should('have.value', '27224');

      cy.get(`[data-cy="updatedBy"]`).type('30071');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '30071');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productDistributorAllocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productDistributorAllocationPageUrlPattern);
    });
  });
});
