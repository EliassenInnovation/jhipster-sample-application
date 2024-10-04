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

describe('ProductH7 e2e test', () => {
  const productH7PageUrl = '/product-h-7';
  const productH7PageUrlPattern = new RegExp('/product-h-7(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productH7Sample = {};

  let productH7;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-h-7-s+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-h-7-s').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-h-7-s/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productH7) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-h-7-s/${productH7.id}`,
      }).then(() => {
        productH7 = undefined;
      });
    }
  });

  it('ProductH7s menu should load ProductH7s page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-h-7');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductH7').should('exist');
    cy.url().should('match', productH7PageUrlPattern);
  });

  describe('ProductH7 page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productH7PageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductH7 page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-h-7/new$'));
        cy.getEntityCreateUpdateHeading('ProductH7');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7PageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-h-7-s',
          body: productH7Sample,
        }).then(({ body }) => {
          productH7 = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-h-7-s+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productH7],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productH7PageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductH7 page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productH7');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7PageUrlPattern);
      });

      it('edit button click should load edit ProductH7 page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductH7');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7PageUrlPattern);
      });

      it('edit button click should load edit ProductH7 page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductH7');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7PageUrlPattern);
      });

      it('last delete button click should delete instance of ProductH7', () => {
        cy.intercept('GET', '/api/product-h-7-s/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productH7').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7PageUrlPattern);

        productH7 = undefined;
      });
    });
  });

  describe('new ProductH7 page', () => {
    beforeEach(() => {
      cy.visit(`${productH7PageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductH7');
    });

    it('should create an instance of ProductH7', () => {
      cy.get(`[data-cy="gtinUpc"]`).type('provided quietly bah');
      cy.get(`[data-cy="gtinUpc"]`).should('have.value', 'provided quietly bah');

      cy.get(`[data-cy="h7KeywordId"]`).type('6518');
      cy.get(`[data-cy="h7KeywordId"]`).should('have.value', '6518');

      cy.get(`[data-cy="iOCGroup"]`).type('substantiate gosh weary');
      cy.get(`[data-cy="iOCGroup"]`).should('have.value', 'substantiate gosh weary');

      cy.get(`[data-cy="productH7Id"]`).type('30682');
      cy.get(`[data-cy="productH7Id"]`).should('have.value', '30682');

      cy.get(`[data-cy="productId"]`).type('20376');
      cy.get(`[data-cy="productId"]`).should('have.value', '20376');

      cy.get(`[data-cy="productName"]`).type('jogging infamous straight');
      cy.get(`[data-cy="productName"]`).should('have.value', 'jogging infamous straight');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productH7 = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productH7PageUrlPattern);
    });
  });
});
