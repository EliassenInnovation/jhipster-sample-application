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

describe('SupportTicketMst e2e test', () => {
  const supportTicketMstPageUrl = '/support-ticket-mst';
  const supportTicketMstPageUrlPattern = new RegExp('/support-ticket-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const supportTicketMstSample = {};

  let supportTicketMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/support-ticket-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/support-ticket-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/support-ticket-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (supportTicketMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/support-ticket-msts/${supportTicketMst.id}`,
      }).then(() => {
        supportTicketMst = undefined;
      });
    }
  });

  it('SupportTicketMsts menu should load SupportTicketMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('support-ticket-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SupportTicketMst').should('exist');
    cy.url().should('match', supportTicketMstPageUrlPattern);
  });

  describe('SupportTicketMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(supportTicketMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SupportTicketMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/support-ticket-mst/new$'));
        cy.getEntityCreateUpdateHeading('SupportTicketMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/support-ticket-msts',
          body: supportTicketMstSample,
        }).then(({ body }) => {
          supportTicketMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/support-ticket-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [supportTicketMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(supportTicketMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SupportTicketMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('supportTicketMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketMstPageUrlPattern);
      });

      it('edit button click should load edit SupportTicketMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupportTicketMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketMstPageUrlPattern);
      });

      it('edit button click should load edit SupportTicketMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SupportTicketMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketMstPageUrlPattern);
      });

      it('last delete button click should delete instance of SupportTicketMst', () => {
        cy.intercept('GET', '/api/support-ticket-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('supportTicketMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', supportTicketMstPageUrlPattern);

        supportTicketMst = undefined;
      });
    });
  });

  describe('new SupportTicketMst page', () => {
    beforeEach(() => {
      cy.visit(`${supportTicketMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SupportTicketMst');
    });

    it('should create an instance of SupportTicketMst', () => {
      cy.get(`[data-cy="createdBy"]`).type('2288');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '2288');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-03');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="email"]`).type('Jillian_Abernathy@hotmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Jillian_Abernathy@hotmail.com');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="isWithOutLogin"]`).should('not.be.checked');
      cy.get(`[data-cy="isWithOutLogin"]`).click();
      cy.get(`[data-cy="isWithOutLogin"]`).should('be.checked');

      cy.get(`[data-cy="lastUpdatedBy"]`).type('20105');
      cy.get(`[data-cy="lastUpdatedBy"]`).should('have.value', '20105');

      cy.get(`[data-cy="lastUpdatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="lastUpdatedOn"]`).blur();
      cy.get(`[data-cy="lastUpdatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="priority"]`).type('throughout an typeface');
      cy.get(`[data-cy="priority"]`).should('have.value', 'throughout an typeface');

      cy.get(`[data-cy="schoolDistrictId"]`).type('16482');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '16482');

      cy.get(`[data-cy="status"]`).type('anenst save roasted');
      cy.get(`[data-cy="status"]`).should('have.value', 'anenst save roasted');

      cy.get(`[data-cy="subject"]`).type('which');
      cy.get(`[data-cy="subject"]`).should('have.value', 'which');

      cy.get(`[data-cy="supportCategoryId"]`).type('3449');
      cy.get(`[data-cy="supportCategoryId"]`).should('have.value', '3449');

      cy.get(`[data-cy="ticketId"]`).type('1016');
      cy.get(`[data-cy="ticketId"]`).should('have.value', '1016');

      cy.get(`[data-cy="ticketReferenceNumber"]`).type('28042');
      cy.get(`[data-cy="ticketReferenceNumber"]`).should('have.value', '28042');

      cy.get(`[data-cy="userId"]`).type('8145');
      cy.get(`[data-cy="userId"]`).should('have.value', '8145');

      cy.get(`[data-cy="userName"]`).type('inasmuch from');
      cy.get(`[data-cy="userName"]`).should('have.value', 'inasmuch from');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        supportTicketMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', supportTicketMstPageUrlPattern);
    });
  });
});
