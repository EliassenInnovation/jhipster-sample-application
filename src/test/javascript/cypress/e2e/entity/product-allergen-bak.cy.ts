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

describe('ProductAllergenBak e2e test', () => {
  const productAllergenBakPageUrl = '/product-allergen-bak';
  const productAllergenBakPageUrlPattern = new RegExp('/product-allergen-bak(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productAllergenBakSample = {};

  let productAllergenBak;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-allergen-baks+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-allergen-baks').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-allergen-baks/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productAllergenBak) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-allergen-baks/${productAllergenBak.id}`,
      }).then(() => {
        productAllergenBak = undefined;
      });
    }
  });

  it('ProductAllergenBaks menu should load ProductAllergenBaks page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-allergen-bak');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductAllergenBak').should('exist');
    cy.url().should('match', productAllergenBakPageUrlPattern);
  });

  describe('ProductAllergenBak page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productAllergenBakPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductAllergenBak page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-allergen-bak/new$'));
        cy.getEntityCreateUpdateHeading('ProductAllergenBak');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenBakPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-allergen-baks',
          body: productAllergenBakSample,
        }).then(({ body }) => {
          productAllergenBak = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-allergen-baks+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productAllergenBak],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productAllergenBakPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductAllergenBak page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productAllergenBak');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenBakPageUrlPattern);
      });

      it('edit button click should load edit ProductAllergenBak page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAllergenBak');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenBakPageUrlPattern);
      });

      it('edit button click should load edit ProductAllergenBak page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductAllergenBak');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenBakPageUrlPattern);
      });

      it('last delete button click should delete instance of ProductAllergenBak', () => {
        cy.intercept('GET', '/api/product-allergen-baks/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productAllergenBak').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productAllergenBakPageUrlPattern);

        productAllergenBak = undefined;
      });
    });
  });

  describe('new ProductAllergenBak page', () => {
    beforeEach(() => {
      cy.visit(`${productAllergenBakPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductAllergenBak');
    });

    it('should create an instance of ProductAllergenBak', () => {
      cy.get(`[data-cy="allergenId"]`).type('9342');
      cy.get(`[data-cy="allergenId"]`).should('have.value', '9342');

      cy.get(`[data-cy="allergenGroup"]`).type('appropriate citizen');
      cy.get(`[data-cy="allergenGroup"]`).should('have.value', 'appropriate citizen');

      cy.get(`[data-cy="allergenName"]`).type('barracks');
      cy.get(`[data-cy="allergenName"]`).should('have.value', 'barracks');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="gTIN"]`).type('provided');
      cy.get(`[data-cy="gTIN"]`).should('have.value', 'provided');

      cy.get(`[data-cy="gTINUPC"]`).type('whoever recount around');
      cy.get(`[data-cy="gTINUPC"]`).should('have.value', 'whoever recount around');

      cy.get(`[data-cy="productAllergenId"]`).type('11526');
      cy.get(`[data-cy="productAllergenId"]`).should('have.value', '11526');

      cy.get(`[data-cy="productId"]`).type('3132');
      cy.get(`[data-cy="productId"]`).should('have.value', '3132');

      cy.get(`[data-cy="uPC"]`).type('stable ick');
      cy.get(`[data-cy="uPC"]`).should('have.value', 'stable ick');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productAllergenBak = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productAllergenBakPageUrlPattern);
    });
  });
});
