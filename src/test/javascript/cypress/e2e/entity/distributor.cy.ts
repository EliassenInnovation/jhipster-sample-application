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

describe('Distributor e2e test', () => {
  const distributorPageUrl = '/distributor';
  const distributorPageUrlPattern = new RegExp('/distributor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const distributorSample = {};

  let distributor;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/distributors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/distributors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/distributors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (distributor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/distributors/${distributor.id}`,
      }).then(() => {
        distributor = undefined;
      });
    }
  });

  it('Distributors menu should load Distributors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('distributor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Distributor').should('exist');
    cy.url().should('match', distributorPageUrlPattern);
  });

  describe('Distributor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(distributorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Distributor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/distributor/new$'));
        cy.getEntityCreateUpdateHeading('Distributor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distributorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/distributors',
          body: distributorSample,
        }).then(({ body }) => {
          distributor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/distributors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [distributor],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(distributorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Distributor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('distributor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distributorPageUrlPattern);
      });

      it('edit button click should load edit Distributor page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Distributor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distributorPageUrlPattern);
      });

      it('edit button click should load edit Distributor page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Distributor');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distributorPageUrlPattern);
      });

      it('last delete button click should delete instance of Distributor', () => {
        cy.intercept('GET', '/api/distributors/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('distributor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', distributorPageUrlPattern);

        distributor = undefined;
      });
    });
  });

  describe('new Distributor page', () => {
    beforeEach(() => {
      cy.visit(`${distributorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Distributor');
    });

    it('should create an instance of Distributor', () => {
      cy.get(`[data-cy="createdBy"]`).type('29982');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '29982');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="distributorCode"]`).type('likely very beyond');
      cy.get(`[data-cy="distributorCode"]`).should('have.value', 'likely very beyond');

      cy.get(`[data-cy="distributorID"]`).type('22075');
      cy.get(`[data-cy="distributorID"]`).should('have.value', '22075');

      cy.get(`[data-cy="distributorName"]`).type('ah which');
      cy.get(`[data-cy="distributorName"]`).should('have.value', 'ah which');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="updatedBy"]`).type('8003');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '8003');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        distributor = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', distributorPageUrlPattern);
    });
  });
});
