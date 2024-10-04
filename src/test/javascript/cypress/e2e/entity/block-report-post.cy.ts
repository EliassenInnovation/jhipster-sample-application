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

describe('BlockReportPost e2e test', () => {
  const blockReportPostPageUrl = '/block-report-post';
  const blockReportPostPageUrlPattern = new RegExp('/block-report-post(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const blockReportPostSample = {};

  let blockReportPost;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/block-report-posts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/block-report-posts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/block-report-posts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (blockReportPost) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/block-report-posts/${blockReportPost.id}`,
      }).then(() => {
        blockReportPost = undefined;
      });
    }
  });

  it('BlockReportPosts menu should load BlockReportPosts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('block-report-post');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BlockReportPost').should('exist');
    cy.url().should('match', blockReportPostPageUrlPattern);
  });

  describe('BlockReportPost page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(blockReportPostPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BlockReportPost page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/block-report-post/new$'));
        cy.getEntityCreateUpdateHeading('BlockReportPost');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', blockReportPostPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/block-report-posts',
          body: blockReportPostSample,
        }).then(({ body }) => {
          blockReportPost = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/block-report-posts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [blockReportPost],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(blockReportPostPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BlockReportPost page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('blockReportPost');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', blockReportPostPageUrlPattern);
      });

      it('edit button click should load edit BlockReportPost page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BlockReportPost');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', blockReportPostPageUrlPattern);
      });

      it('edit button click should load edit BlockReportPost page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BlockReportPost');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', blockReportPostPageUrlPattern);
      });

      it('last delete button click should delete instance of BlockReportPost', () => {
        cy.intercept('GET', '/api/block-report-posts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('blockReportPost').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', blockReportPostPageUrlPattern);

        blockReportPost = undefined;
      });
    });
  });

  describe('new BlockReportPost page', () => {
    beforeEach(() => {
      cy.visit(`${blockReportPostPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BlockReportPost');
    });

    it('should create an instance of BlockReportPost', () => {
      cy.get(`[data-cy="blockCategories"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="blockCategories"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="blockingReason"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="blockingReason"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="postBlockReportId"]`).type('ccc6dc23-eb5e-45a2-b391-9fac32acb379');
      cy.get(`[data-cy="postBlockReportId"]`).invoke('val').should('match', new RegExp('ccc6dc23-eb5e-45a2-b391-9fac32acb379'));

      cy.get(`[data-cy="postId"]`).type('232e706b-446b-4418-9b13-f4b3d2bdb2a5');
      cy.get(`[data-cy="postId"]`).invoke('val').should('match', new RegExp('232e706b-446b-4418-9b13-f4b3d2bdb2a5'));

      cy.get(`[data-cy="postType"]`).type('syringe below');
      cy.get(`[data-cy="postType"]`).should('have.value', 'syringe below');

      cy.get(`[data-cy="requestedBy"]`).type('21008');
      cy.get(`[data-cy="requestedBy"]`).should('have.value', '21008');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        blockReportPost = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', blockReportPostPageUrlPattern);
    });
  });
});
