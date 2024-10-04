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

describe('RoleMst e2e test', () => {
  const roleMstPageUrl = '/role-mst';
  const roleMstPageUrlPattern = new RegExp('/role-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const roleMstSample = {};

  let roleMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/role-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/role-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/role-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (roleMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/role-msts/${roleMst.id}`,
      }).then(() => {
        roleMst = undefined;
      });
    }
  });

  it('RoleMsts menu should load RoleMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('role-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RoleMst').should('exist');
    cy.url().should('match', roleMstPageUrlPattern);
  });

  describe('RoleMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(roleMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RoleMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/role-mst/new$'));
        cy.getEntityCreateUpdateHeading('RoleMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', roleMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/role-msts',
          body: roleMstSample,
        }).then(({ body }) => {
          roleMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/role-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [roleMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(roleMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RoleMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('roleMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', roleMstPageUrlPattern);
      });

      it('edit button click should load edit RoleMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RoleMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', roleMstPageUrlPattern);
      });

      it('edit button click should load edit RoleMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RoleMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', roleMstPageUrlPattern);
      });

      it('last delete button click should delete instance of RoleMst', () => {
        cy.intercept('GET', '/api/role-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('roleMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', roleMstPageUrlPattern);

        roleMst = undefined;
      });
    });
  });

  describe('new RoleMst page', () => {
    beforeEach(() => {
      cy.visit(`${roleMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RoleMst');
    });

    it('should create an instance of RoleMst', () => {
      cy.get(`[data-cy="createdBy"]`).type('3536');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '3536');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="parentRoleId"]`).type('16973');
      cy.get(`[data-cy="parentRoleId"]`).should('have.value', '16973');

      cy.get(`[data-cy="roleId"]`).type('8716');
      cy.get(`[data-cy="roleId"]`).should('have.value', '8716');

      cy.get(`[data-cy="roleName"]`).type('apologise why likely');
      cy.get(`[data-cy="roleName"]`).should('have.value', 'apologise why likely');

      cy.get(`[data-cy="updatedBy"]`).type('31955');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '31955');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-04');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        roleMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', roleMstPageUrlPattern);
    });
  });
});
