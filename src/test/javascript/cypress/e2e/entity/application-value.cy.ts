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

describe('ApplicationValue e2e test', () => {
  const applicationValuePageUrl = '/application-value';
  const applicationValuePageUrlPattern = new RegExp('/application-value(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const applicationValueSample = {};

  let applicationValue;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/application-values+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/application-values').as('postEntityRequest');
    cy.intercept('DELETE', '/api/application-values/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (applicationValue) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/application-values/${applicationValue.id}`,
      }).then(() => {
        applicationValue = undefined;
      });
    }
  });

  it('ApplicationValues menu should load ApplicationValues page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('application-value');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ApplicationValue').should('exist');
    cy.url().should('match', applicationValuePageUrlPattern);
  });

  describe('ApplicationValue page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(applicationValuePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ApplicationValue page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/application-value/new$'));
        cy.getEntityCreateUpdateHeading('ApplicationValue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationValuePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/application-values',
          body: applicationValueSample,
        }).then(({ body }) => {
          applicationValue = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/application-values+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [applicationValue],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(applicationValuePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ApplicationValue page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('applicationValue');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationValuePageUrlPattern);
      });

      it('edit button click should load edit ApplicationValue page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationValue');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationValuePageUrlPattern);
      });

      it('edit button click should load edit ApplicationValue page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ApplicationValue');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationValuePageUrlPattern);
      });

      it('last delete button click should delete instance of ApplicationValue', () => {
        cy.intercept('GET', '/api/application-values/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('applicationValue').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', applicationValuePageUrlPattern);

        applicationValue = undefined;
      });
    });
  });

  describe('new ApplicationValue page', () => {
    beforeEach(() => {
      cy.visit(`${applicationValuePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ApplicationValue');
    });

    it('should create an instance of ApplicationValue', () => {
      cy.get(`[data-cy="applicationValueId"]`).type('22789');
      cy.get(`[data-cy="applicationValueId"]`).should('have.value', '22789');

      cy.get(`[data-cy="key"]`).type('behind certainly');
      cy.get(`[data-cy="key"]`).should('have.value', 'behind certainly');

      cy.get(`[data-cy="valueDate"]`).type('2024-10-04T16:31');
      cy.get(`[data-cy="valueDate"]`).blur();
      cy.get(`[data-cy="valueDate"]`).should('have.value', '2024-10-04T16:31');

      cy.get(`[data-cy="valueInt"]`).type('31145');
      cy.get(`[data-cy="valueInt"]`).should('have.value', '31145');

      cy.get(`[data-cy="valueLong"]`).type('7218');
      cy.get(`[data-cy="valueLong"]`).should('have.value', '7218');

      cy.get(`[data-cy="valueString"]`).type('vacantly');
      cy.get(`[data-cy="valueString"]`).should('have.value', 'vacantly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        applicationValue = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', applicationValuePageUrlPattern);
    });
  });
});
