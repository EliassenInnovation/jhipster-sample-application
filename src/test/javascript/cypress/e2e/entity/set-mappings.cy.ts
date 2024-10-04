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

describe('SetMappings e2e test', () => {
  const setMappingsPageUrl = '/set-mappings';
  const setMappingsPageUrlPattern = new RegExp('/set-mappings(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const setMappingsSample = {};

  let setMappings;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/set-mappings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/set-mappings').as('postEntityRequest');
    cy.intercept('DELETE', '/api/set-mappings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (setMappings) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/set-mappings/${setMappings.id}`,
      }).then(() => {
        setMappings = undefined;
      });
    }
  });

  it('SetMappings menu should load SetMappings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('set-mappings');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SetMappings').should('exist');
    cy.url().should('match', setMappingsPageUrlPattern);
  });

  describe('SetMappings page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(setMappingsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SetMappings page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/set-mappings/new$'));
        cy.getEntityCreateUpdateHeading('SetMappings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', setMappingsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/set-mappings',
          body: setMappingsSample,
        }).then(({ body }) => {
          setMappings = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/set-mappings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [setMappings],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(setMappingsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SetMappings page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('setMappings');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', setMappingsPageUrlPattern);
      });

      it('edit button click should load edit SetMappings page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SetMappings');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', setMappingsPageUrlPattern);
      });

      it('edit button click should load edit SetMappings page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SetMappings');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', setMappingsPageUrlPattern);
      });

      it('last delete button click should delete instance of SetMappings', () => {
        cy.intercept('GET', '/api/set-mappings/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('setMappings').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', setMappingsPageUrlPattern);

        setMappings = undefined;
      });
    });
  });

  describe('new SetMappings page', () => {
    beforeEach(() => {
      cy.visit(`${setMappingsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SetMappings');
    });

    it('should create an instance of SetMappings', () => {
      cy.get(`[data-cy="iD"]`).type('12437');
      cy.get(`[data-cy="iD"]`).should('have.value', '12437');

      cy.get(`[data-cy="oneWorldValue"]`).type('vice');
      cy.get(`[data-cy="oneWorldValue"]`).should('have.value', 'vice');

      cy.get(`[data-cy="productValue"]`).type('sense deprave clamour');
      cy.get(`[data-cy="productValue"]`).should('have.value', 'sense deprave clamour');

      cy.get(`[data-cy="setName"]`).type('demobilise boohoo pace');
      cy.get(`[data-cy="setName"]`).should('have.value', 'demobilise boohoo pace');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        setMappings = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', setMappingsPageUrlPattern);
    });
  });
});
