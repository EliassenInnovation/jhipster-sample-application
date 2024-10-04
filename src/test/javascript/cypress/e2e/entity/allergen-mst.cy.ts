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

describe('AllergenMst e2e test', () => {
  const allergenMstPageUrl = '/allergen-mst';
  const allergenMstPageUrlPattern = new RegExp('/allergen-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const allergenMstSample = {};

  let allergenMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/allergen-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/allergen-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/allergen-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (allergenMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/allergen-msts/${allergenMst.id}`,
      }).then(() => {
        allergenMst = undefined;
      });
    }
  });

  it('AllergenMsts menu should load AllergenMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('allergen-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AllergenMst').should('exist');
    cy.url().should('match', allergenMstPageUrlPattern);
  });

  describe('AllergenMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(allergenMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AllergenMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/allergen-mst/new$'));
        cy.getEntityCreateUpdateHeading('AllergenMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', allergenMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/allergen-msts',
          body: allergenMstSample,
        }).then(({ body }) => {
          allergenMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/allergen-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [allergenMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(allergenMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AllergenMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('allergenMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', allergenMstPageUrlPattern);
      });

      it('edit button click should load edit AllergenMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AllergenMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', allergenMstPageUrlPattern);
      });

      it('edit button click should load edit AllergenMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AllergenMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', allergenMstPageUrlPattern);
      });

      it('last delete button click should delete instance of AllergenMst', () => {
        cy.intercept('GET', '/api/allergen-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('allergenMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', allergenMstPageUrlPattern);

        allergenMst = undefined;
      });
    });
  });

  describe('new AllergenMst page', () => {
    beforeEach(() => {
      cy.visit(`${allergenMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AllergenMst');
    });

    it('should create an instance of AllergenMst', () => {
      cy.get(`[data-cy="allergenGroup"]`).type('consequently brr');
      cy.get(`[data-cy="allergenGroup"]`).should('have.value', 'consequently brr');

      cy.get(`[data-cy="allergenId"]`).type('15998');
      cy.get(`[data-cy="allergenId"]`).should('have.value', '15998');

      cy.get(`[data-cy="allergenName"]`).type('gadzooks');
      cy.get(`[data-cy="allergenName"]`).should('have.value', 'gadzooks');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        allergenMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', allergenMstPageUrlPattern);
    });
  });
});
