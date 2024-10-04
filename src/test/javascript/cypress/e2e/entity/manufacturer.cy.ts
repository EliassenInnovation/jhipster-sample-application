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

describe('Manufacturer e2e test', () => {
  const manufacturerPageUrl = '/manufacturer';
  const manufacturerPageUrlPattern = new RegExp('/manufacturer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const manufacturerSample = {};

  let manufacturer;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/manufacturers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/manufacturers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/manufacturers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (manufacturer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/manufacturers/${manufacturer.id}`,
      }).then(() => {
        manufacturer = undefined;
      });
    }
  });

  it('Manufacturers menu should load Manufacturers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('manufacturer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Manufacturer').should('exist');
    cy.url().should('match', manufacturerPageUrlPattern);
  });

  describe('Manufacturer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(manufacturerPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Manufacturer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/manufacturer/new$'));
        cy.getEntityCreateUpdateHeading('Manufacturer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manufacturerPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/manufacturers',
          body: manufacturerSample,
        }).then(({ body }) => {
          manufacturer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/manufacturers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [manufacturer],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(manufacturerPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Manufacturer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('manufacturer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manufacturerPageUrlPattern);
      });

      it('edit button click should load edit Manufacturer page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Manufacturer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manufacturerPageUrlPattern);
      });

      it('edit button click should load edit Manufacturer page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Manufacturer');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manufacturerPageUrlPattern);
      });

      it('last delete button click should delete instance of Manufacturer', () => {
        cy.intercept('GET', '/api/manufacturers/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('manufacturer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', manufacturerPageUrlPattern);

        manufacturer = undefined;
      });
    });
  });

  describe('new Manufacturer page', () => {
    beforeEach(() => {
      cy.visit(`${manufacturerPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Manufacturer');
    });

    it('should create an instance of Manufacturer', () => {
      cy.get(`[data-cy="createdBy"]`).type('13975');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '13975');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="email"]`).type('Destany.Davis70@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Destany.Davis70@gmail.com');

      cy.get(`[data-cy="firstName"]`).type('Sydni');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Sydni');

      cy.get(`[data-cy="glnNumber"]`).type('very when honestly');
      cy.get(`[data-cy="glnNumber"]`).should('have.value', 'very when honestly');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="lastName"]`).type('Goldner');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Goldner');

      cy.get(`[data-cy="manufacturer"]`).type('wasabi which damp');
      cy.get(`[data-cy="manufacturer"]`).should('have.value', 'wasabi which damp');

      cy.get(`[data-cy="manufacturerId"]`).type('18155');
      cy.get(`[data-cy="manufacturerId"]`).should('have.value', '18155');

      cy.get(`[data-cy="password"]`).type('barring plait');
      cy.get(`[data-cy="password"]`).should('have.value', 'barring plait');

      cy.get(`[data-cy="phoneNumber"]`).type('nervously till');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'nervously till');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        manufacturer = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', manufacturerPageUrlPattern);
    });
  });
});
