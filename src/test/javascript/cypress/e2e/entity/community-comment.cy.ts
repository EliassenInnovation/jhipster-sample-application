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

describe('CommunityComment e2e test', () => {
  const communityCommentPageUrl = '/community-comment';
  const communityCommentPageUrlPattern = new RegExp('/community-comment(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const communityCommentSample = {};

  let communityComment;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/community-comments+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/community-comments').as('postEntityRequest');
    cy.intercept('DELETE', '/api/community-comments/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (communityComment) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/community-comments/${communityComment.id}`,
      }).then(() => {
        communityComment = undefined;
      });
    }
  });

  it('CommunityComments menu should load CommunityComments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('community-comment');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CommunityComment').should('exist');
    cy.url().should('match', communityCommentPageUrlPattern);
  });

  describe('CommunityComment page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(communityCommentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CommunityComment page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/community-comment/new$'));
        cy.getEntityCreateUpdateHeading('CommunityComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityCommentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/community-comments',
          body: communityCommentSample,
        }).then(({ body }) => {
          communityComment = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/community-comments+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [communityComment],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(communityCommentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CommunityComment page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('communityComment');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityCommentPageUrlPattern);
      });

      it('edit button click should load edit CommunityComment page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityComment');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityCommentPageUrlPattern);
      });

      it('edit button click should load edit CommunityComment page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityComment');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityCommentPageUrlPattern);
      });

      it('last delete button click should delete instance of CommunityComment', () => {
        cy.intercept('GET', '/api/community-comments/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('communityComment').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityCommentPageUrlPattern);

        communityComment = undefined;
      });
    });
  });

  describe('new CommunityComment page', () => {
    beforeEach(() => {
      cy.visit(`${communityCommentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CommunityComment');
    });

    it('should create an instance of CommunityComment', () => {
      cy.get(`[data-cy="comment"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="comment"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="commentByUserId"]`).type('968');
      cy.get(`[data-cy="commentByUserId"]`).should('have.value', '968');

      cy.get(`[data-cy="communityCommentId"]`).type('3ab79377-6c4e-405b-968c-a8bf51964eec');
      cy.get(`[data-cy="communityCommentId"]`).invoke('val').should('match', new RegExp('3ab79377-6c4e-405b-968c-a8bf51964eec'));

      cy.get(`[data-cy="communityPostId"]`).type('1ea8a8de-16f8-443f-84c4-539e0fea5d74');
      cy.get(`[data-cy="communityPostId"]`).invoke('val').should('match', new RegExp('1ea8a8de-16f8-443f-84c4-539e0fea5d74'));

      cy.get(`[data-cy="createdOn"]`).type('2024-10-03');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="lastUpdatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="lastUpdatedOn"]`).blur();
      cy.get(`[data-cy="lastUpdatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        communityComment = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', communityCommentPageUrlPattern);
    });
  });
});
