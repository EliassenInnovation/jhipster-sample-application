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

describe('SupportTicketTransaction e2e test', () => {
  const supportTicketTransactionPageUrl = '/support-ticket-transaction';
  const supportTicketTransactionPageUrlPattern = new RegExp('/support-ticket-transaction(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const supportTicketTransactionSample = {};

  let supportTicketTransaction;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/support-ticket-transactions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/support-ticket-transactions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/support-ticket-transactions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (supportTicketTransaction) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/support-ticket-transactions/${supportTicketTransaction.id}`,
      }).then(() => {
        supportTicketTransaction = undefined;
      });
    }
  });

  it('SupportTicketTransactions menu should load SupportTicketTransactions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('support-ticket-transaction');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SupportTicketTransaction').should('exist');
    cy.url().should('match', supportTicketTransactionPageUrlPattern);
  });

  describe('SupportTicketTransaction page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(supportTicketTransactionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SupportTicketTransaction page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/support-ticket-transaction/new$'));
        cy.getEntityCreateUpdateHeading('SupportTicketTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketTransactionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/support-ticket-transactions',
          body: supportTicketTransactionSample,
        }).then(({ body }) => {
          supportTicketTransaction = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/support-ticket-transactions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [supportTicketTransaction],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(supportTicketTransactionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SupportTicketTransaction page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('supportTicketTransaction');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketTransactionPageUrlPattern);
      });

      it('edit button click should load edit SupportTicketTransaction page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupportTicketTransaction');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketTransactionPageUrlPattern);
      });

      it('edit button click should load edit SupportTicketTransaction page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupportTicketTransaction');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketTransactionPageUrlPattern);
      });

      it('last delete button click should delete instance of SupportTicketTransaction', () => {
        cy.intercept('GET', '/api/support-ticket-transactions/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('supportTicketTransaction').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketTransactionPageUrlPattern);

        supportTicketTransaction = undefined;
      });
    });
  });

  describe('new SupportTicketTransaction page', () => {
    beforeEach(() => {
      cy.visit(`${supportTicketTransactionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SupportTicketTransaction');
    });

    it('should create an instance of SupportTicketTransaction', () => {
      cy.get(`[data-cy="createdBy"]`).type('29985');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '29985');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="description"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="description"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="fileExtension"]`).type('notwithstanding once');
      cy.get(`[data-cy="fileExtension"]`).should('have.value', 'notwithstanding once');

      cy.get(`[data-cy="fileName"]`).type('self-assured slather');
      cy.get(`[data-cy="fileName"]`).should('have.value', 'self-assured slather');

      cy.get(`[data-cy="filePath"]`).type('provided ah');
      cy.get(`[data-cy="filePath"]`).should('have.value', 'provided ah');

      cy.get(`[data-cy="fileSize"]`).type('29218');
      cy.get(`[data-cy="fileSize"]`).should('have.value', '29218');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="isSentByFigSupport"]`).should('not.be.checked');
      cy.get(`[data-cy="isSentByFigSupport"]`).click();
      cy.get(`[data-cy="isSentByFigSupport"]`).should('be.checked');

      cy.get(`[data-cy="lastUpdatedBy"]`).type('24825');
      cy.get(`[data-cy="lastUpdatedBy"]`).should('have.value', '24825');

      cy.get(`[data-cy="lastUpdatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="lastUpdatedOn"]`).blur();
      cy.get(`[data-cy="lastUpdatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="ticketId"]`).type('18103');
      cy.get(`[data-cy="ticketId"]`).should('have.value', '18103');

      cy.get(`[data-cy="ticketTransactionId"]`).type('19383');
      cy.get(`[data-cy="ticketTransactionId"]`).should('have.value', '19383');

      cy.get(`[data-cy="userId"]`).type('26470');
      cy.get(`[data-cy="userId"]`).should('have.value', '26470');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        supportTicketTransaction = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', supportTicketTransactionPageUrlPattern);
    });
  });
});
