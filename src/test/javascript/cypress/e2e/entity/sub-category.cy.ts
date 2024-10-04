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

describe('SubCategory e2e test', () => {
  const subCategoryPageUrl = '/sub-category';
  const subCategoryPageUrlPattern = new RegExp('/sub-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const subCategorySample = {};

  let subCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/sub-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sub-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sub-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (subCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sub-categories/${subCategory.id}`,
      }).then(() => {
        subCategory = undefined;
      });
    }
  });

  it('SubCategories menu should load SubCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sub-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubCategory').should('exist');
    cy.url().should('match', subCategoryPageUrlPattern);
  });

  describe('SubCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sub-category/new$'));
        cy.getEntityCreateUpdateHeading('SubCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sub-categories',
          body: subCategorySample,
        }).then(({ body }) => {
          subCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sub-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [subCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(subCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SubCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subCategoryPageUrlPattern);
      });

      it('edit button click should load edit SubCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subCategoryPageUrlPattern);
      });

      it('edit button click should load edit SubCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of SubCategory', () => {
        cy.intercept('GET', '/api/sub-categories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('subCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subCategoryPageUrlPattern);

        subCategory = undefined;
      });
    });
  });

  describe('new SubCategory page', () => {
    beforeEach(() => {
      cy.visit(`${subCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubCategory');
    });

    it('should create an instance of SubCategory', () => {
      cy.get(`[data-cy="categoryId"]`).type('24413');
      cy.get(`[data-cy="categoryId"]`).should('have.value', '24413');

      cy.get(`[data-cy="createdBy"]`).type('22935');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '22935');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="subCategoryCode"]`).type('object outside quinoa');
      cy.get(`[data-cy="subCategoryCode"]`).should('have.value', 'object outside quinoa');

      cy.get(`[data-cy="subCategoryId"]`).type('31373');
      cy.get(`[data-cy="subCategoryId"]`).should('have.value', '31373');

      cy.get(`[data-cy="subCategoryName"]`).type('swear');
      cy.get(`[data-cy="subCategoryName"]`).should('have.value', 'swear');

      cy.get(`[data-cy="updatedBy"]`).type('25990');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '25990');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        subCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', subCategoryPageUrlPattern);
    });
  });
});
