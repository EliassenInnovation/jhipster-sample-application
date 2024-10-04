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

describe('CommunityPostTransactions e2e test', () => {
  const communityPostTransactionsPageUrl = '/community-post-transactions';
  const communityPostTransactionsPageUrlPattern = new RegExp('/community-post-transactions(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const communityPostTransactionsSample = {};

  let communityPostTransactions;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/community-post-transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/community-post-transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/community-post-transactions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (communityPostTransactions) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/community-post-transactions/${communityPostTransactions.id}`,
      }).then(() => {
        communityPostTransactions = undefined;
      });
    }
  });

  it('CommunityPostTransactions menu should load CommunityPostTransactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('community-post-transactions');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CommunityPostTransactions').should('exist');
    cy.url().should('match', communityPostTransactionsPageUrlPattern);
  });

  describe('CommunityPostTransactions page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(communityPostTransactionsPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CommunityPostTransactions page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/community-post-transactions/new$'));
        cy.getEntityCreateUpdateHeading('CommunityPostTransactions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostTransactionsPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/community-post-transactions',
          body: communityPostTransactionsSample,
        }).then(({ body }) => {
          communityPostTransactions = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/community-post-transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [communityPostTransactions],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(communityPostTransactionsPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CommunityPostTransactions page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('communityPostTransactions');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostTransactionsPageUrlPattern);
      });

      it('edit button click should load edit CommunityPostTransactions page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityPostTransactions');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostTransactionsPageUrlPattern);
      });

      it('edit button click should load edit CommunityPostTransactions page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CommunityPostTransactions');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostTransactionsPageUrlPattern);
      });

      it('last delete button click should delete instance of CommunityPostTransactions', () => {
        cy.intercept('GET', '/api/community-post-transactions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('communityPostTransactions').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', communityPostTransactionsPageUrlPattern);

        communityPostTransactions = undefined;
      });
    });
  });

  describe('new CommunityPostTransactions page', () => {
    beforeEach(() => {
      cy.visit(`${communityPostTransactionsPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CommunityPostTransactions');
    });

    it('should create an instance of CommunityPostTransactions', () => {
      cy.get(`[data-cy="attachmentUrl"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="attachmentUrl"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="communityPostId"]`).type('9ca1145d-a0ff-422e-98e5-48f97b9fa2a8');
      cy.get(`[data-cy="communityPostId"]`).invoke('val').should('match', new RegExp('9ca1145d-a0ff-422e-98e5-48f97b9fa2a8'));

      cy.get(`[data-cy="communityPostTransactionId"]`).type('6a278f24-2559-4f0c-ab24-4f0ae55ffaad');
      cy.get(`[data-cy="communityPostTransactionId"]`).invoke('val').should('match', new RegExp('6a278f24-2559-4f0c-ab24-4f0ae55ffaad'));

      cy.get(`[data-cy="createdBy"]`).type('7796');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '7796');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="lastUpdatedBy"]`).type('17103');
      cy.get(`[data-cy="lastUpdatedBy"]`).should('have.value', '17103');

      cy.get(`[data-cy="lastUpdatedOn"]`).type('2024-10-03');
      cy.get(`[data-cy="lastUpdatedOn"]`).blur();
      cy.get(`[data-cy="lastUpdatedOn"]`).should('have.value', '2024-10-03');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        communityPostTransactions = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', communityPostTransactionsPageUrlPattern);
    });
  });
});
