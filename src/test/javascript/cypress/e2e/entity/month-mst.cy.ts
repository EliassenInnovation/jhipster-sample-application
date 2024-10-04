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

describe('MonthMst e2e test', () => {
  const monthMstPageUrl = '/month-mst';
  const monthMstPageUrlPattern = new RegExp('/month-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const monthMstSample = {};

  let monthMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/month-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/month-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/month-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (monthMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/month-msts/${monthMst.id}`,
      }).then(() => {
        monthMst = undefined;
      });
    }
  });

  it('MonthMsts menu should load MonthMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('month-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MonthMst').should('exist');
    cy.url().should('match', monthMstPageUrlPattern);
  });

  describe('MonthMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(monthMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MonthMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/month-mst/new$'));
        cy.getEntityCreateUpdateHeading('MonthMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/month-msts',
          body: monthMstSample,
        }).then(({ body }) => {
          monthMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/month-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [monthMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(monthMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MonthMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('monthMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthMstPageUrlPattern);
      });

      it('edit button click should load edit MonthMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MonthMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthMstPageUrlPattern);
      });

      it('edit button click should load edit MonthMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MonthMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthMstPageUrlPattern);
      });

      it('last delete button click should delete instance of MonthMst', () => {
        cy.intercept('GET', '/api/month-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('monthMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthMstPageUrlPattern);

        monthMst = undefined;
      });
    });
  });

  describe('new MonthMst page', () => {
    beforeEach(() => {
      cy.visit(`${monthMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MonthMst');
    });

    it('should create an instance of MonthMst', () => {
      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="monthID"]`).type('5726');
      cy.get(`[data-cy="monthID"]`).should('have.value', '5726');

      cy.get(`[data-cy="monthName"]`).type('amidst');
      cy.get(`[data-cy="monthName"]`).should('have.value', 'amidst');

      cy.get(`[data-cy="year"]`).type('why');
      cy.get(`[data-cy="year"]`).should('have.value', 'why');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        monthMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', monthMstPageUrlPattern);
    });
  });
});
