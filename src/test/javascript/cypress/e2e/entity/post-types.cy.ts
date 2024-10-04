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

describe('PostTypes e2e test', () => {
  const postTypesPageUrl = '/post-types';
  const postTypesPageUrlPattern = new RegExp('/post-types(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const postTypesSample = {};

  let postTypes;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/post-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/post-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/post-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (postTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/post-types/${postTypes.id}`,
      }).then(() => {
        postTypes = undefined;
      });
    }
  });

  it('PostTypes menu should load PostTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('post-types');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PostTypes').should('exist');
    cy.url().should('match', postTypesPageUrlPattern);
  });

  describe('PostTypes page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(postTypesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PostTypes page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/post-types/new$'));
        cy.getEntityCreateUpdateHeading('PostTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postTypesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/post-types',
          body: postTypesSample,
        }).then(({ body }) => {
          postTypes = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/post-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [postTypes],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(postTypesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PostTypes page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('postTypes');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postTypesPageUrlPattern);
      });

      it('edit button click should load edit PostTypes page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postTypesPageUrlPattern);
      });

      it('edit button click should load edit PostTypes page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PostTypes');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postTypesPageUrlPattern);
      });

      it('last delete button click should delete instance of PostTypes', () => {
        cy.intercept('GET', '/api/post-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('postTypes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', postTypesPageUrlPattern);

        postTypes = undefined;
      });
    });
  });

  describe('new PostTypes page', () => {
    beforeEach(() => {
      cy.visit(`${postTypesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PostTypes');
    });

    it('should create an instance of PostTypes', () => {
      cy.get(`[data-cy="createdBy"]`).type('7a135477-99e1-4ab6-8bb1-ac35a4a5aa3d');
      cy.get(`[data-cy="createdBy"]`).invoke('val').should('match', new RegExp('7a135477-99e1-4ab6-8bb1-ac35a4a5aa3d'));

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="lastUpdatedBy"]`).type('7c294654-9314-4fd6-8685-072c26145ec6');
      cy.get(`[data-cy="lastUpdatedBy"]`).invoke('val').should('match', new RegExp('7c294654-9314-4fd6-8685-072c26145ec6'));

      cy.get(`[data-cy="lastUpdatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="lastUpdatedOn"]`).blur();
      cy.get(`[data-cy="lastUpdatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="postType"]`).type('following developing');
      cy.get(`[data-cy="postType"]`).should('have.value', 'following developing');

      cy.get(`[data-cy="postTypeId"]`).type('6648');
      cy.get(`[data-cy="postTypeId"]`).should('have.value', '6648');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        postTypes = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', postTypesPageUrlPattern);
    });
  });
});
