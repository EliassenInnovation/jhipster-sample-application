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

describe('ProductH7New e2e test', () => {
  const productH7NewPageUrl = '/product-h-7-new';
  const productH7NewPageUrlPattern = new RegExp('/product-h-7-new(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productH7NewSample = {};

  let productH7New;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-h-7-news+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-h-7-news').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-h-7-news/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productH7New) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-h-7-news/${productH7New.id}`,
      }).then(() => {
        productH7New = undefined;
      });
    }
  });

  it('ProductH7News menu should load ProductH7News page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-h-7-new');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductH7New').should('exist');
    cy.url().should('match', productH7NewPageUrlPattern);
  });

  describe('ProductH7New page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productH7NewPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductH7New page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-h-7-new/new$'));
        cy.getEntityCreateUpdateHeading('ProductH7New');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7NewPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-h-7-news',
          body: productH7NewSample,
        }).then(({ body }) => {
          productH7New = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-h-7-news+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productH7New],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productH7NewPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductH7New page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productH7New');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7NewPageUrlPattern);
      });

      it('edit button click should load edit ProductH7New page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductH7New');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7NewPageUrlPattern);
      });

      it('edit button click should load edit ProductH7New page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductH7New');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7NewPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductH7New', () => {
        cy.intercept('GET', '/api/product-h-7-news/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productH7New').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productH7NewPageUrlPattern);

        productH7New = undefined;
      });
    });
  });

  describe('new ProductH7New page', () => {
    beforeEach(() => {
      cy.visit(`${productH7NewPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductH7New');
    });

    it('should create an instance of ProductH7New', () => {
      cy.get(`[data-cy="gtinUpc"]`).type('decouple mortar');
      cy.get(`[data-cy="gtinUpc"]`).should('have.value', 'decouple mortar');

      cy.get(`[data-cy="h7KeywordId"]`).type('23895');
      cy.get(`[data-cy="h7KeywordId"]`).should('have.value', '23895');

      cy.get(`[data-cy="iOCGroup"]`).type('at');
      cy.get(`[data-cy="iOCGroup"]`).should('have.value', 'at');

      cy.get(`[data-cy="productH7Id"]`).type('25872');
      cy.get(`[data-cy="productH7Id"]`).should('have.value', '25872');

      cy.get(`[data-cy="productId"]`).type('24423');
      cy.get(`[data-cy="productId"]`).should('have.value', '24423');

      cy.get(`[data-cy="productName"]`).type('midst geez conceptualize');
      cy.get(`[data-cy="productName"]`).should('have.value', 'midst geez conceptualize');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productH7New = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productH7NewPageUrlPattern);
    });
  });
});
