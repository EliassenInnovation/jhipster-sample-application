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

describe('ProductImageBeforeApprove e2e test', () => {
  const productImageBeforeApprovePageUrl = '/product-image-before-approve';
  const productImageBeforeApprovePageUrlPattern = new RegExp('/product-image-before-approve(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const productImageBeforeApproveSample = {};

  let productImageBeforeApprove;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/product-image-before-approves+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/product-image-before-approves').as('postEntityRequest');
    cy.intercept('DELETE', '/api/product-image-before-approves/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (productImageBeforeApprove) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/product-image-before-approves/${productImageBeforeApprove.id}`,
      }).then(() => {
        productImageBeforeApprove = undefined;
      });
    }
  });

  it('ProductImageBeforeApproves menu should load ProductImageBeforeApproves page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('product-image-before-approve');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProductImageBeforeApprove').should('exist');
    cy.url().should('match', productImageBeforeApprovePageUrlPattern);
  });

  describe('ProductImageBeforeApprove page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(productImageBeforeApprovePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProductImageBeforeApprove page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/product-image-before-approve/new$'));
        cy.getEntityCreateUpdateHeading('ProductImageBeforeApprove');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImageBeforeApprovePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/product-image-before-approves',
          body: productImageBeforeApproveSample,
        }).then(({ body }) => {
          productImageBeforeApprove = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/product-image-before-approves+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [productImageBeforeApprove],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(productImageBeforeApprovePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProductImageBeforeApprove page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('productImageBeforeApprove');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImageBeforeApprovePageUrlPattern);
      });

      it('edit button click should load edit ProductImageBeforeApprove page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductImageBeforeApprove');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImageBeforeApprovePageUrlPattern);
      });

      it('edit button click should load edit ProductImageBeforeApprove page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProductImageBeforeApprove');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImageBeforeApprovePageUrlPattern);
      });

      it('last delete button click should delete instance of ProductImageBeforeApprove', () => {
        cy.intercept('GET', '/api/product-image-before-approves/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('productImageBeforeApprove').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', productImageBeforeApprovePageUrlPattern);

        productImageBeforeApprove = undefined;
      });
    });
  });

  describe('new ProductImageBeforeApprove page', () => {
    beforeEach(() => {
      cy.visit(`${productImageBeforeApprovePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProductImageBeforeApprove');
    });

    it('should create an instance of ProductImageBeforeApprove', () => {
      cy.get(`[data-cy="createdBy"]`).type('5402');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '5402');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="imageURL"]`).type('huzzah');
      cy.get(`[data-cy="imageURL"]`).should('have.value', 'huzzah');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="productId"]`).type('18411');
      cy.get(`[data-cy="productId"]`).should('have.value', '18411');

      cy.get(`[data-cy="productImageId"]`).type('27695');
      cy.get(`[data-cy="productImageId"]`).should('have.value', '27695');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        productImageBeforeApprove = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', productImageBeforeApprovePageUrlPattern);
    });
  });
});
