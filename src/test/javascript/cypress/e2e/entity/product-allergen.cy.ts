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

describe('ProductAllergen e2e test', () => {
  const productAllergenPageUrl = '/product-allergen';
  const productAllergenPageUrlPattern = new RegExp('/product-allergen(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productAllergenSample = {};

  let productAllergen;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-allergens+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-allergens').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-allergens/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productAllergen) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-allergens/${productAllergen.id}`,
      }).then(() => {
        productAllergen = undefined;
      });
    }
  });

  it('ProductAllergens menu should load ProductAllergens page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-allergen');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductAllergen').should('exist');
    cy.url().should('match', productAllergenPageUrlPattern);
  });

  describe('ProductAllergen page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productAllergenPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductAllergen page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-allergen/new$'));
        cy.getEntityCreateUpdateHeading('ProductAllergen');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-allergens',
          body: productAllergenSample,
        }).then(({ body }) => {
          productAllergen = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-allergens+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productAllergen],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productAllergenPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductAllergen page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productAllergen');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenPageUrlPattern);
      });

      it('edit button click should load edit ProductAllergen page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAllergen');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenPageUrlPattern);
      });

      it('edit button click should load edit ProductAllergen page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAllergen');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductAllergen', () => {
        cy.intercept('GET', '/api/product-allergens/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productAllergen').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenPageUrlPattern);

        productAllergen = undefined;
      });
    });
  });

  describe('new ProductAllergen page', () => {
    beforeEach(() => {
      cy.visit(`${productAllergenPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductAllergen');
    });

    it('should create an instance of ProductAllergen', () => {
      cy.get(`[data-cy="allergenId"]`).type('20371');
      cy.get(`[data-cy="allergenId"]`).should('have.value', '20371');

      cy.get(`[data-cy="allergenGroup"]`).type('less');
      cy.get(`[data-cy="allergenGroup"]`).should('have.value', 'less');

      cy.get(`[data-cy="allergenName"]`).type('yum odd plugin');
      cy.get(`[data-cy="allergenName"]`).should('have.value', 'yum odd plugin');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="gTIN"]`).type('quip');
      cy.get(`[data-cy="gTIN"]`).should('have.value', 'quip');

      cy.get(`[data-cy="gTINUPC"]`).type('afore amid');
      cy.get(`[data-cy="gTINUPC"]`).should('have.value', 'afore amid');

      cy.get(`[data-cy="productAllergenId"]`).type('29407');
      cy.get(`[data-cy="productAllergenId"]`).should('have.value', '29407');

      cy.get(`[data-cy="productId"]`).type('20185');
      cy.get(`[data-cy="productId"]`).should('have.value', '20185');

      cy.get(`[data-cy="uPC"]`).type('handover cauliflower excepting');
      cy.get(`[data-cy="uPC"]`).should('have.value', 'handover cauliflower excepting');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productAllergen = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productAllergenPageUrlPattern);
    });
  });
});
