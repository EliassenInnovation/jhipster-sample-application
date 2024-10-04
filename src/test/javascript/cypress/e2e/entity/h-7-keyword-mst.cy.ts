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

describe('H7KeywordMst e2e test', () => {
  const h7KeywordMstPageUrl = '/h-7-keyword-mst';
  const h7KeywordMstPageUrlPattern = new RegExp('/h-7-keyword-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const h7KeywordMstSample = {};

  let h7KeywordMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/h-7-keyword-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/h-7-keyword-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/h-7-keyword-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (h7KeywordMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/h-7-keyword-msts/${h7KeywordMst.id}`,
      }).then(() => {
        h7KeywordMst = undefined;
      });
    }
  });

  it('H7KeywordMsts menu should load H7KeywordMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('h-7-keyword-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('H7KeywordMst').should('exist');
    cy.url().should('match', h7KeywordMstPageUrlPattern);
  });

  describe('H7KeywordMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(h7KeywordMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create H7KeywordMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/h-7-keyword-mst/new$'));
        cy.getEntityCreateUpdateHeading('H7KeywordMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', h7KeywordMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/h-7-keyword-msts',
          body: h7KeywordMstSample,
        }).then(({ body }) => {
          h7KeywordMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/h-7-keyword-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [h7KeywordMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(h7KeywordMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details H7KeywordMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('h7KeywordMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', h7KeywordMstPageUrlPattern);
      });

      it('edit button click should load edit H7KeywordMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('H7KeywordMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', h7KeywordMstPageUrlPattern);
      });

      it('edit button click should load edit H7KeywordMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('H7KeywordMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', h7KeywordMstPageUrlPattern);
      });

      it('last delete button click should delete instance of H7KeywordMst', () => {
        cy.intercept('GET', '/api/h-7-keyword-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('h7KeywordMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', h7KeywordMstPageUrlPattern);

        h7KeywordMst = undefined;
      });
    });
  });

  describe('new H7KeywordMst page', () => {
    beforeEach(() => {
      cy.visit(`${h7KeywordMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('H7KeywordMst');
    });

    it('should create an instance of H7KeywordMst', () => {
      cy.get(`[data-cy="h7Group"]`).type('to bah denitrify');
      cy.get(`[data-cy="h7Group"]`).should('have.value', 'to bah denitrify');

      cy.get(`[data-cy="h7Keyword"]`).type('ocelot');
      cy.get(`[data-cy="h7Keyword"]`).should('have.value', 'ocelot');

      cy.get(`[data-cy="h7keywordId"]`).type('20689');
      cy.get(`[data-cy="h7keywordId"]`).should('have.value', '20689');

      cy.get(`[data-cy="iocGroup"]`).type('misreport suddenly');
      cy.get(`[data-cy="iocGroup"]`).should('have.value', 'misreport suddenly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        h7KeywordMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', h7KeywordMstPageUrlPattern);
    });
  });
});
