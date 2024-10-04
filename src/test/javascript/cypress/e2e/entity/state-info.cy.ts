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

describe('StateInfo e2e test', () => {
  const stateInfoPageUrl = '/state-info';
  const stateInfoPageUrlPattern = new RegExp('/state-info(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const stateInfoSample = {};

  let stateInfo;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/state-infos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/state-infos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/state-infos/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (stateInfo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/state-infos/${stateInfo.id}`,
      }).then(() => {
        stateInfo = undefined;
      });
    }
  });

  it('StateInfos menu should load StateInfos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('state-info');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StateInfo').should('exist');
    cy.url().should('match', stateInfoPageUrlPattern);
  });

  describe('StateInfo page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(stateInfoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StateInfo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/state-info/new$'));
        cy.getEntityCreateUpdateHeading('StateInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateInfoPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/state-infos',
          body: stateInfoSample,
        }).then(({ body }) => {
          stateInfo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/state-infos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [stateInfo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(stateInfoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StateInfo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('stateInfo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateInfoPageUrlPattern);
      });

      it('edit button click should load edit StateInfo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StateInfo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateInfoPageUrlPattern);
      });

      it('edit button click should load edit StateInfo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StateInfo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateInfoPageUrlPattern);
      });

      it('last delete button click should delete instance of StateInfo', () => {
        cy.intercept('GET', '/api/state-infos/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('stateInfo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', stateInfoPageUrlPattern);

        stateInfo = undefined;
      });
    });
  });

  describe('new StateInfo page', () => {
    beforeEach(() => {
      cy.visit(`${stateInfoPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StateInfo');
    });

    it('should create an instance of StateInfo', () => {
      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="stateId"]`).type('9529');
      cy.get(`[data-cy="stateId"]`).should('have.value', '9529');

      cy.get(`[data-cy="stateName"]`).type('low boo');
      cy.get(`[data-cy="stateName"]`).should('have.value', 'low boo');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        stateInfo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', stateInfoPageUrlPattern);
    });
  });
});
