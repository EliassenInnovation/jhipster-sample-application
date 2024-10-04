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

describe('SuggestedProducts e2e test', () => {
  const suggestedProductsPageUrl = '/suggested-products';
  const suggestedProductsPageUrlPattern = new RegExp('/suggested-products(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const suggestedProductsSample = {};

  let suggestedProducts;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/suggested-products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/suggested-products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/suggested-products/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (suggestedProducts) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/suggested-products/${suggestedProducts.id}`,
      }).then(() => {
        suggestedProducts = undefined;
      });
    }
  });

  it('SuggestedProducts menu should load SuggestedProducts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('suggested-products');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SuggestedProducts').should('exist');
    cy.url().should('match', suggestedProductsPageUrlPattern);
  });

  describe('SuggestedProducts page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(suggestedProductsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SuggestedProducts page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/suggested-products/new$'));
        cy.getEntityCreateUpdateHeading('SuggestedProducts');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suggestedProductsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/suggested-products',
          body: suggestedProductsSample,
        }).then(({ body }) => {
          suggestedProducts = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/suggested-products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [suggestedProducts],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(suggestedProductsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SuggestedProducts page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('suggestedProducts');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suggestedProductsPageUrlPattern);
      });

      it('edit button click should load edit SuggestedProducts page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SuggestedProducts');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suggestedProductsPageUrlPattern);
      });

      it('edit button click should load edit SuggestedProducts page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SuggestedProducts');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suggestedProductsPageUrlPattern);
      });

      it('last delete button click should delete instance of SuggestedProducts', () => {
        cy.intercept('GET', '/api/suggested-products/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('suggestedProducts').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', suggestedProductsPageUrlPattern);

        suggestedProducts = undefined;
      });
    });
  });

  describe('new SuggestedProducts page', () => {
    beforeEach(() => {
      cy.visit(`${suggestedProductsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SuggestedProducts');
    });

    it('should create an instance of SuggestedProducts', () => {
      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="isApprove"]`).should('not.be.checked');
      cy.get(`[data-cy="isApprove"]`).click();
      cy.get(`[data-cy="isApprove"]`).should('be.checked');

      cy.get(`[data-cy="productId"]`).type('19192');
      cy.get(`[data-cy="productId"]`).should('have.value', '19192');

      cy.get(`[data-cy="suggestedByDistrict"]`).type('20035');
      cy.get(`[data-cy="suggestedByDistrict"]`).should('have.value', '20035');

      cy.get(`[data-cy="suggestedByUserId"]`).type('22646');
      cy.get(`[data-cy="suggestedByUserId"]`).should('have.value', '22646');

      cy.get(`[data-cy="suggestedProductId"]`).type('16067');
      cy.get(`[data-cy="suggestedProductId"]`).should('have.value', '16067');

      cy.get(`[data-cy="suggestionDate"]`).type('2024-10-04');
      cy.get(`[data-cy="suggestionDate"]`).blur();
      cy.get(`[data-cy="suggestionDate"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="suggestionId"]`).type('26497');
      cy.get(`[data-cy="suggestionId"]`).should('have.value', '26497');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        suggestedProducts = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', suggestedProductsPageUrlPattern);
    });
  });
});
