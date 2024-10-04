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

describe('CommunityPost e2e test', () => {
  const communityPostPageUrl = '/community-post';
  const communityPostPageUrlPattern = new RegExp('/community-post(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const communityPostSample = {};

  let communityPost;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/community-posts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/community-posts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/community-posts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (communityPost) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/community-posts/${communityPost.id}`,
      }).then(() => {
        communityPost = undefined;
      });
    }
  });

  it('CommunityPosts menu should load CommunityPosts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('community-post');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CommunityPost').should('exist');
    cy.url().should('match', communityPostPageUrlPattern);
  });

  describe('CommunityPost page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(communityPostPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CommunityPost page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/community-post/new$'));
        cy.getEntityCreateUpdateHeading('CommunityPost');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/community-posts',
          body: communityPostSample,
        }).then(({ body }) => {
          communityPost = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/community-posts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [communityPost],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(communityPostPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CommunityPost page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('communityPost');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostPageUrlPattern);
      });

      it('edit button click should load edit CommunityPost page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityPost');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostPageUrlPattern);
      });

      it('edit button click should load edit CommunityPost page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityPost');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostPageUrlPattern);
      });

      it('last delete button click should delete instance of CommunityPost', () => {
        cy.intercept('GET', '/api/community-posts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('communityPost').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostPageUrlPattern);

        communityPost = undefined;
      });
    });
  });

  describe('new CommunityPost page', () => {
    beforeEach(() => {
      cy.visit(`${communityPostPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CommunityPost');
    });

    it('should create an instance of CommunityPost', () => {
      cy.get(`[data-cy="communityPostId"]`).type('79754f6b-2a13-4f77-ada5-9c312140600c');
      cy.get(`[data-cy="communityPostId"]`).invoke('val').should('match', new RegExp('79754f6b-2a13-4f77-ada5-9c312140600c'));

      cy.get(`[data-cy="createdBy"]`).type('18506');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '18506');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="date"]`).type('2024-10-04');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="lastUpdatedBy"]`).type('3086');
      cy.get(`[data-cy="lastUpdatedBy"]`).should('have.value', '3086');

      cy.get(`[data-cy="lastUpdatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="lastUpdatedOn"]`).blur();
      cy.get(`[data-cy="lastUpdatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="postTypeId"]`).type('28227');
      cy.get(`[data-cy="postTypeId"]`).should('have.value', '28227');

      cy.get(`[data-cy="privacyTypeId"]`).type('27109');
      cy.get(`[data-cy="privacyTypeId"]`).should('have.value', '27109');

      cy.get(`[data-cy="schoolDistrictId"]`).type('9036');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '9036');

      cy.get(`[data-cy="userId"]`).type('1816');
      cy.get(`[data-cy="userId"]`).should('have.value', '1816');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        communityPost = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', communityPostPageUrlPattern);
    });
  });
});
