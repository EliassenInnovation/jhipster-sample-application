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

describe('ReplacedProducts e2e test', () => {
  const replacedProductsPageUrl = '/replaced-products';
  const replacedProductsPageUrlPattern = new RegExp('/replaced-products(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const replacedProductsSample = {};

  let replacedProducts;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/replaced-products+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/replaced-products').as('postEntityRequest');
    cy.intercept('DELETE', '/api/replaced-products/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (replacedProducts) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/replaced-products/${replacedProducts.id}`,
      }).then(() => {
        replacedProducts = undefined;
      });
    }
  });

  it('ReplacedProducts menu should load ReplacedProducts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('replaced-products');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReplacedProducts').should('exist');
    cy.url().should('match', replacedProductsPageUrlPattern);
  });

  describe('ReplacedProducts page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(replacedProductsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReplacedProducts page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/replaced-products/new$'));
        cy.getEntityCreateUpdateHeading('ReplacedProducts');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', replacedProductsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/replaced-products',
          body: replacedProductsSample,
        }).then(({ body }) => {
          replacedProducts = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/replaced-products+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [replacedProducts],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(replacedProductsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ReplacedProducts page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('replacedProducts');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', replacedProductsPageUrlPattern);
      });

      it('edit button click should load edit ReplacedProducts page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReplacedProducts');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', replacedProductsPageUrlPattern);
      });

      it('edit button click should load edit ReplacedProducts page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReplacedProducts');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', replacedProductsPageUrlPattern);
      });

      it('last delete button click should delete instance of ReplacedProducts', () => {
        cy.intercept('GET', '/api/replaced-products/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('replacedProducts').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', replacedProductsPageUrlPattern);

        replacedProducts = undefined;
      });
    });
  });

  describe('new ReplacedProducts page', () => {
    beforeEach(() => {
      cy.visit(`${replacedProductsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReplacedProducts');
    });

    it('should create an instance of ReplacedProducts', () => {
      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="productId"]`).type('26089');
      cy.get(`[data-cy="productId"]`).should('have.value', '26089');

      cy.get(`[data-cy="replacedByUserId"]`).type('10719');
      cy.get(`[data-cy="replacedByUserId"]`).should('have.value', '10719');

      cy.get(`[data-cy="replacedId"]`).type('32745');
      cy.get(`[data-cy="replacedId"]`).should('have.value', '32745');

      cy.get(`[data-cy="replacedProductId"]`).type('20655');
      cy.get(`[data-cy="replacedProductId"]`).should('have.value', '20655');

      cy.get(`[data-cy="replacementDate"]`).type('2024-10-04');
      cy.get(`[data-cy="replacementDate"]`).blur();
      cy.get(`[data-cy="replacementDate"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="schoolDistrictId"]`).type('22676');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '22676');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        replacedProducts = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', replacedProductsPageUrlPattern);
    });
  });
});
