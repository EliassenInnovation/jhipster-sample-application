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

describe('LoginHistory e2e test', () => {
  const loginHistoryPageUrl = '/login-history';
  const loginHistoryPageUrlPattern = new RegExp('/login-history(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const loginHistorySample = {};

  let loginHistory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/login-histories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/login-histories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/login-histories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (loginHistory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/login-histories/${loginHistory.id}`,
      }).then(() => {
        loginHistory = undefined;
      });
    }
  });

  it('LoginHistories menu should load LoginHistories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('login-history');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('LoginHistory').should('exist');
    cy.url().should('match', loginHistoryPageUrlPattern);
  });

  describe('LoginHistory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(loginHistoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create LoginHistory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/login-history/new$'));
        cy.getEntityCreateUpdateHeading('LoginHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', loginHistoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/login-histories',
          body: loginHistorySample,
        }).then(({ body }) => {
          loginHistory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/login-histories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [loginHistory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(loginHistoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details LoginHistory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('loginHistory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', loginHistoryPageUrlPattern);
      });

      it('edit button click should load edit LoginHistory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoginHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', loginHistoryPageUrlPattern);
      });

      it('edit button click should load edit LoginHistory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('LoginHistory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', loginHistoryPageUrlPattern);
      });

      it('last delete button click should delete instance of LoginHistory', () => {
        cy.intercept('GET', '/api/login-histories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('loginHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', loginHistoryPageUrlPattern);

        loginHistory = undefined;
      });
    });
  });

  describe('new LoginHistory page', () => {
    beforeEach(() => {
      cy.visit(`${loginHistoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('LoginHistory');
    });

    it('should create an instance of LoginHistory', () => {
      cy.get(`[data-cy="forgotPin"]`).type('28357');
      cy.get(`[data-cy="forgotPin"]`).should('have.value', '28357');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="loginDate"]`).type('2024-10-04');
      cy.get(`[data-cy="loginDate"]`).blur();
      cy.get(`[data-cy="loginDate"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="loginLogId"]`).type('15469');
      cy.get(`[data-cy="loginLogId"]`).should('have.value', '15469');

      cy.get(`[data-cy="loginType"]`).type('however sashay pink');
      cy.get(`[data-cy="loginType"]`).should('have.value', 'however sashay pink');

      cy.get(`[data-cy="logOutDate"]`).type('2024-10-04');
      cy.get(`[data-cy="logOutDate"]`).blur();
      cy.get(`[data-cy="logOutDate"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="userId"]`).type('3958');
      cy.get(`[data-cy="userId"]`).should('have.value', '3958');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        loginHistory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', loginHistoryPageUrlPattern);
    });
  });
});
