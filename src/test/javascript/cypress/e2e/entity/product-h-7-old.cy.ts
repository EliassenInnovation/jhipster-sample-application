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

describe('ProductH7Old e2e test', () => {
  const productH7OldPageUrl = '/product-h-7-old';
  const productH7OldPageUrlPattern = new RegExp('/product-h-7-old(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productH7OldSample = {};

  let productH7Old;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-h-7-olds+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-h-7-olds').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-h-7-olds/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productH7Old) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-h-7-olds/${productH7Old.id}`,
      }).then(() => {
        productH7Old = undefined;
      });
    }
  });

  it('ProductH7Olds menu should load ProductH7Olds page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-h-7-old');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductH7Old').should('exist');
    cy.url().should('match', productH7OldPageUrlPattern);
  });

  describe('ProductH7Old page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productH7OldPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductH7Old page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-h-7-old/new$'));
        cy.getEntityCreateUpdateHeading('ProductH7Old');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7OldPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-h-7-olds',
          body: productH7OldSample,
        }).then(({ body }) => {
          productH7Old = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-h-7-olds+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productH7Old],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productH7OldPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductH7Old page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productH7Old');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7OldPageUrlPattern);
      });

      it('edit button click should load edit ProductH7Old page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductH7Old');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7OldPageUrlPattern);
      });

      it('edit button click should load edit ProductH7Old page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductH7Old');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7OldPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductH7Old', () => {
        cy.intercept('GET', '/api/product-h-7-olds/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productH7Old').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7OldPageUrlPattern);

        productH7Old = undefined;
      });
    });
  });

  describe('new ProductH7Old page', () => {
    beforeEach(() => {
      cy.visit(`${productH7OldPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductH7Old');
    });

    it('should create an instance of ProductH7Old', () => {
      cy.get(`[data-cy="gtinUpc"]`).type('or regarding twine');
      cy.get(`[data-cy="gtinUpc"]`).should('have.value', 'or regarding twine');

      cy.get(`[data-cy="h7KeywordId"]`).type('16870');
      cy.get(`[data-cy="h7KeywordId"]`).should('have.value', '16870');

      cy.get(`[data-cy="iOCGroup"]`).type('not');
      cy.get(`[data-cy="iOCGroup"]`).should('have.value', 'not');

      cy.get(`[data-cy="productH7Id"]`).type('8909');
      cy.get(`[data-cy="productH7Id"]`).should('have.value', '8909');

      cy.get(`[data-cy="productId"]`).type('24924');
      cy.get(`[data-cy="productId"]`).should('have.value', '24924');

      cy.get(`[data-cy="productName"]`).type('yippee who');
      cy.get(`[data-cy="productName"]`).should('have.value', 'yippee who');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productH7Old = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productH7OldPageUrlPattern);
    });
  });
});
