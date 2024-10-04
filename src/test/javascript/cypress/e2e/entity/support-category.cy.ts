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

describe('SupportCategory e2e test', () => {
  const supportCategoryPageUrl = '/support-category';
  const supportCategoryPageUrlPattern = new RegExp('/support-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const supportCategorySample = {};

  let supportCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/support-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/support-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/support-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (supportCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/support-categories/${supportCategory.id}`,
      }).then(() => {
        supportCategory = undefined;
      });
    }
  });

  it('SupportCategories menu should load SupportCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('support-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SupportCategory').should('exist');
    cy.url().should('match', supportCategoryPageUrlPattern);
  });

  describe('SupportCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(supportCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SupportCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/support-category/new$'));
        cy.getEntityCreateUpdateHeading('SupportCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/support-categories',
          body: supportCategorySample,
        }).then(({ body }) => {
          supportCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/support-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [supportCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(supportCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SupportCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('supportCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportCategoryPageUrlPattern);
      });

      it('edit button click should load edit SupportCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupportCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportCategoryPageUrlPattern);
      });

      it('edit button click should load edit SupportCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupportCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of SupportCategory', () => {
        cy.intercept('GET', '/api/support-categories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('supportCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportCategoryPageUrlPattern);

        supportCategory = undefined;
      });
    });
  });

  describe('new SupportCategory page', () => {
    beforeEach(() => {
      cy.visit(`${supportCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SupportCategory');
    });

    it('should create an instance of SupportCategory', () => {
      cy.get(`[data-cy="supportCategoryId"]`).type('5246');
      cy.get(`[data-cy="supportCategoryId"]`).should('have.value', '5246');

      cy.get(`[data-cy="supportCategoryName"]`).type('cycle but treble');
      cy.get(`[data-cy="supportCategoryName"]`).should('have.value', 'cycle but treble');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        supportCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', supportCategoryPageUrlPattern);
    });
  });
});
