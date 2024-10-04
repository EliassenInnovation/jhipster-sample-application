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

describe('UserDistrictAllocation e2e test', () => {
  const userDistrictAllocationPageUrl = '/user-district-allocation';
  const userDistrictAllocationPageUrlPattern = new RegExp('/user-district-allocation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userDistrictAllocationSample = {};

  let userDistrictAllocation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-district-allocations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-district-allocations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-district-allocations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userDistrictAllocation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-district-allocations/${userDistrictAllocation.id}`,
      }).then(() => {
        userDistrictAllocation = undefined;
      });
    }
  });

  it('UserDistrictAllocations menu should load UserDistrictAllocations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-district-allocation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserDistrictAllocation').should('exist');
    cy.url().should('match', userDistrictAllocationPageUrlPattern);
  });

  describe('UserDistrictAllocation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userDistrictAllocationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserDistrictAllocation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-district-allocation/new$'));
        cy.getEntityCreateUpdateHeading('UserDistrictAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDistrictAllocationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-district-allocations',
          body: userDistrictAllocationSample,
        }).then(({ body }) => {
          userDistrictAllocation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-district-allocations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [userDistrictAllocation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userDistrictAllocationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserDistrictAllocation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userDistrictAllocation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDistrictAllocationPageUrlPattern);
      });

      it('edit button click should load edit UserDistrictAllocation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserDistrictAllocation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDistrictAllocationPageUrlPattern);
      });

      it('edit button click should load edit UserDistrictAllocation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserDistrictAllocation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDistrictAllocationPageUrlPattern);
      });

      it('last delete button click should delete instance of UserDistrictAllocation', () => {
        cy.intercept('GET', '/api/user-district-allocations/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userDistrictAllocation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userDistrictAllocationPageUrlPattern);

        userDistrictAllocation = undefined;
      });
    });
  });

  describe('new UserDistrictAllocation page', () => {
    beforeEach(() => {
      cy.visit(`${userDistrictAllocationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserDistrictAllocation');
    });

    it('should create an instance of UserDistrictAllocation', () => {
      cy.get(`[data-cy="createdBy"]`).type('3011');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '3011');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-03');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="isAllocated"]`).should('not.be.checked');
      cy.get(`[data-cy="isAllocated"]`).click();
      cy.get(`[data-cy="isAllocated"]`).should('be.checked');

      cy.get(`[data-cy="schoolDistrictId"]`).type('17184');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '17184');

      cy.get(`[data-cy="updatedBy"]`).type('4618');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '4618');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="userDistrictAllocationId"]`).type('e06b9092-c218-4a9c-be34-4225e7d7a66f');
      cy.get(`[data-cy="userDistrictAllocationId"]`).invoke('val').should('match', new RegExp('e06b9092-c218-4a9c-be34-4225e7d7a66f'));

      cy.get(`[data-cy="userId"]`).type('21142');
      cy.get(`[data-cy="userId"]`).should('have.value', '21142');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userDistrictAllocation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userDistrictAllocationPageUrlPattern);
    });
  });
});
