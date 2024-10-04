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

describe('PrivacyType e2e test', () => {
  const privacyTypePageUrl = '/privacy-type';
  const privacyTypePageUrlPattern = new RegExp('/privacy-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const privacyTypeSample = {};

  let privacyType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/privacy-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/privacy-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/privacy-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (privacyType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/privacy-types/${privacyType.id}`,
      }).then(() => {
        privacyType = undefined;
      });
    }
  });

  it('PrivacyTypes menu should load PrivacyTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('privacy-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrivacyType').should('exist');
    cy.url().should('match', privacyTypePageUrlPattern);
  });

  describe('PrivacyType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(privacyTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PrivacyType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/privacy-type/new$'));
        cy.getEntityCreateUpdateHeading('PrivacyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', privacyTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/privacy-types',
          body: privacyTypeSample,
        }).then(({ body }) => {
          privacyType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/privacy-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [privacyType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(privacyTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PrivacyType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('privacyType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', privacyTypePageUrlPattern);
      });

      it('edit button click should load edit PrivacyType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PrivacyType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', privacyTypePageUrlPattern);
      });

      it('edit button click should load edit PrivacyType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PrivacyType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', privacyTypePageUrlPattern);
      });

      it('last delete button click should delete instance of PrivacyType', () => {
        cy.intercept('GET', '/api/privacy-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('privacyType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', privacyTypePageUrlPattern);

        privacyType = undefined;
      });
    });
  });

  describe('new PrivacyType page', () => {
    beforeEach(() => {
      cy.visit(`${privacyTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PrivacyType');
    });

    it('should create an instance of PrivacyType', () => {
      cy.get(`[data-cy="privacyTypeId"]`).type('13766');
      cy.get(`[data-cy="privacyTypeId"]`).should('have.value', '13766');

      cy.get(`[data-cy="privacyTypeName"]`).type('inferior');
      cy.get(`[data-cy="privacyTypeName"]`).should('have.value', 'inferior');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        privacyType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', privacyTypePageUrlPattern);
    });
  });
});
