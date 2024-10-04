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

describe('CommunityLikeMst e2e test', () => {
  const communityLikeMstPageUrl = '/community-like-mst';
  const communityLikeMstPageUrlPattern = new RegExp('/community-like-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const communityLikeMstSample = {};

  let communityLikeMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/community-like-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/community-like-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/community-like-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (communityLikeMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/community-like-msts/${communityLikeMst.id}`,
      }).then(() => {
        communityLikeMst = undefined;
      });
    }
  });

  it('CommunityLikeMsts menu should load CommunityLikeMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('community-like-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CommunityLikeMst').should('exist');
    cy.url().should('match', communityLikeMstPageUrlPattern);
  });

  describe('CommunityLikeMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(communityLikeMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CommunityLikeMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/community-like-mst/new$'));
        cy.getEntityCreateUpdateHeading('CommunityLikeMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityLikeMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/community-like-msts',
          body: communityLikeMstSample,
        }).then(({ body }) => {
          communityLikeMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/community-like-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [communityLikeMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(communityLikeMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CommunityLikeMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('communityLikeMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityLikeMstPageUrlPattern);
      });

      it('edit button click should load edit CommunityLikeMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityLikeMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityLikeMstPageUrlPattern);
      });

      it('edit button click should load edit CommunityLikeMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityLikeMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityLikeMstPageUrlPattern);
      });

      it('last delete button click should delete instance of CommunityLikeMst', () => {
        cy.intercept('GET', '/api/community-like-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('communityLikeMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityLikeMstPageUrlPattern);

        communityLikeMst = undefined;
      });
    });
  });

  describe('new CommunityLikeMst page', () => {
    beforeEach(() => {
      cy.visit(`${communityLikeMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CommunityLikeMst');
    });

    it('should create an instance of CommunityLikeMst', () => {
      cy.get(`[data-cy="communityLikeId"]`).type('6540b557-713b-4a47-ac6e-17b944825a70');
      cy.get(`[data-cy="communityLikeId"]`).invoke('val').should('match', new RegExp('6540b557-713b-4a47-ac6e-17b944825a70'));

      cy.get(`[data-cy="communityPostId"]`).type('f17ec3f6-79f4-4025-8c32-779403b75066');
      cy.get(`[data-cy="communityPostId"]`).invoke('val').should('match', new RegExp('f17ec3f6-79f4-4025-8c32-779403b75066'));

      cy.get(`[data-cy="createdOn"]`).type('2024-10-03');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="isLiked"]`).should('not.be.checked');
      cy.get(`[data-cy="isLiked"]`).click();
      cy.get(`[data-cy="isLiked"]`).should('be.checked');

      cy.get(`[data-cy="likedByUserId"]`).type('25272');
      cy.get(`[data-cy="likedByUserId"]`).should('have.value', '25272');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        communityLikeMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', communityLikeMstPageUrlPattern);
    });
  });
});
