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

describe('StorageTypes e2e test', () => {
  const storageTypesPageUrl = '/storage-types';
  const storageTypesPageUrlPattern = new RegExp('/storage-types(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const storageTypesSample = {};

  let storageTypes;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/storage-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/storage-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/storage-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (storageTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/storage-types/${storageTypes.id}`,
      }).then(() => {
        storageTypes = undefined;
      });
    }
  });

  it('StorageTypes menu should load StorageTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('storage-types');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('StorageTypes').should('exist');
    cy.url().should('match', storageTypesPageUrlPattern);
  });

  describe('StorageTypes page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(storageTypesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create StorageTypes page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/storage-types/new$'));
        cy.getEntityCreateUpdateHeading('StorageTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageTypesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/storage-types',
          body: storageTypesSample,
        }).then(({ body }) => {
          storageTypes = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/storage-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [storageTypes],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(storageTypesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details StorageTypes page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('storageTypes');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageTypesPageUrlPattern);
      });

      it('edit button click should load edit StorageTypes page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StorageTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageTypesPageUrlPattern);
      });

      it('edit button click should load edit StorageTypes page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('StorageTypes');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageTypesPageUrlPattern);
      });

      it('last delete button click should delete instance of StorageTypes', () => {
        cy.intercept('GET', '/api/storage-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('storageTypes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', storageTypesPageUrlPattern);

        storageTypes = undefined;
      });
    });
  });

  describe('new StorageTypes page', () => {
    beforeEach(() => {
      cy.visit(`${storageTypesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('StorageTypes');
    });

    it('should create an instance of StorageTypes', () => {
      cy.get(`[data-cy="storageTypeId"]`).type('140');
      cy.get(`[data-cy="storageTypeId"]`).should('have.value', '140');

      cy.get(`[data-cy="storageTypeName"]`).type('availability suddenly cautiously');
      cy.get(`[data-cy="storageTypeName"]`).should('have.value', 'availability suddenly cautiously');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        storageTypes = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', storageTypesPageUrlPattern);
    });
  });
});
