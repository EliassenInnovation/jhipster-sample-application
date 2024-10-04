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

describe('ErrorLog e2e test', () => {
  const errorLogPageUrl = '/error-log';
  const errorLogPageUrlPattern = new RegExp('/error-log(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const errorLogSample = {};

  let errorLog;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/error-logs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/error-logs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/error-logs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (errorLog) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/error-logs/${errorLog.id}`,
      }).then(() => {
        errorLog = undefined;
      });
    }
  });

  it('ErrorLogs menu should load ErrorLogs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('error-log');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ErrorLog').should('exist');
    cy.url().should('match', errorLogPageUrlPattern);
  });

  describe('ErrorLog page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(errorLogPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ErrorLog page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/error-log/new$'));
        cy.getEntityCreateUpdateHeading('ErrorLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', errorLogPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/error-logs',
          body: errorLogSample,
        }).then(({ body }) => {
          errorLog = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/error-logs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [errorLog],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(errorLogPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ErrorLog page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('errorLog');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', errorLogPageUrlPattern);
      });

      it('edit button click should load edit ErrorLog page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ErrorLog');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', errorLogPageUrlPattern);
      });

      it('edit button click should load edit ErrorLog page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ErrorLog');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', errorLogPageUrlPattern);
      });

      it('last delete button click should delete instance of ErrorLog', () => {
        cy.intercept('GET', '/api/error-logs/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('errorLog').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', errorLogPageUrlPattern);

        errorLog = undefined;
      });
    });
  });

  describe('new ErrorLog page', () => {
    beforeEach(() => {
      cy.visit(`${errorLogPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ErrorLog');
    });

    it('should create an instance of ErrorLog', () => {
      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="errorId"]`).type('22019');
      cy.get(`[data-cy="errorId"]`).should('have.value', '22019');

      cy.get(`[data-cy="errorMessage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="errorMessage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="errorPath"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="errorPath"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        errorLog = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', errorLogPageUrlPattern);
    });
  });
});
