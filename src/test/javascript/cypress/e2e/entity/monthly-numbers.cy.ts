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

describe('MonthlyNumbers e2e test', () => {
  const monthlyNumbersPageUrl = '/monthly-numbers';
  const monthlyNumbersPageUrlPattern = new RegExp('/monthly-numbers(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const monthlyNumbersSample = {};

  let monthlyNumbers;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/monthly-numbers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/monthly-numbers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/monthly-numbers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (monthlyNumbers) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/monthly-numbers/${monthlyNumbers.id}`,
      }).then(() => {
        monthlyNumbers = undefined;
      });
    }
  });

  it('MonthlyNumbers menu should load MonthlyNumbers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('monthly-numbers');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('MonthlyNumbers').should('exist');
    cy.url().should('match', monthlyNumbersPageUrlPattern);
  });

  describe('MonthlyNumbers page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(monthlyNumbersPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create MonthlyNumbers page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/monthly-numbers/new$'));
        cy.getEntityCreateUpdateHeading('MonthlyNumbers');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyNumbersPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/monthly-numbers',
          body: monthlyNumbersSample,
        }).then(({ body }) => {
          monthlyNumbers = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/monthly-numbers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [monthlyNumbers],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(monthlyNumbersPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details MonthlyNumbers page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('monthlyNumbers');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyNumbersPageUrlPattern);
      });

      it('edit button click should load edit MonthlyNumbers page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MonthlyNumbers');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyNumbersPageUrlPattern);
      });

      it('edit button click should load edit MonthlyNumbers page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('MonthlyNumbers');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyNumbersPageUrlPattern);
      });

      it('last delete button click should delete instance of MonthlyNumbers', () => {
        cy.intercept('GET', '/api/monthly-numbers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('monthlyNumbers').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', monthlyNumbersPageUrlPattern);

        monthlyNumbers = undefined;
      });
    });
  });

  describe('new MonthlyNumbers page', () => {
    beforeEach(() => {
      cy.visit(`${monthlyNumbersPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('MonthlyNumbers');
    });

    it('should create an instance of MonthlyNumbers', () => {
      cy.get(`[data-cy="actualMonthId"]`).type('28090');
      cy.get(`[data-cy="actualMonthId"]`).should('have.value', '28090');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="enrollment"]`).type('390');
      cy.get(`[data-cy="enrollment"]`).should('have.value', '390');

      cy.get(`[data-cy="freeAndReducedPercent"]`).type('20519');
      cy.get(`[data-cy="freeAndReducedPercent"]`).should('have.value', '20519');

      cy.get(`[data-cy="iD"]`).type('2058');
      cy.get(`[data-cy="iD"]`).should('have.value', '2058');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="mealsServed"]`).type('19234');
      cy.get(`[data-cy="mealsServed"]`).should('have.value', '19234');

      cy.get(`[data-cy="modifiedOn"]`).type('2024-10-03');
      cy.get(`[data-cy="modifiedOn"]`).blur();
      cy.get(`[data-cy="modifiedOn"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="monthId"]`).type('24843');
      cy.get(`[data-cy="monthId"]`).should('have.value', '24843');

      cy.get(`[data-cy="numberOfDistricts"]`).type('32194');
      cy.get(`[data-cy="numberOfDistricts"]`).should('have.value', '32194');

      cy.get(`[data-cy="numberOfSites"]`).type('20946');
      cy.get(`[data-cy="numberOfSites"]`).should('have.value', '20946');

      cy.get(`[data-cy="regDate"]`).type('2024-10-04');
      cy.get(`[data-cy="regDate"]`).blur();
      cy.get(`[data-cy="regDate"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="schoolDistrictId"]`).type('5316');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '5316');

      cy.get(`[data-cy="year"]`).type('ack');
      cy.get(`[data-cy="year"]`).should('have.value', 'ack');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        monthlyNumbers = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', monthlyNumbersPageUrlPattern);
    });
  });
});
