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

describe('IocCategory e2e test', () => {
  const iocCategoryPageUrl = '/ioc-category';
  const iocCategoryPageUrlPattern = new RegExp('/ioc-category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const iocCategorySample = {};

  let iocCategory;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ioc-categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ioc-categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ioc-categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (iocCategory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ioc-categories/${iocCategory.id}`,
      }).then(() => {
        iocCategory = undefined;
      });
    }
  });

  it('IocCategories menu should load IocCategories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ioc-category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('IocCategory').should('exist');
    cy.url().should('match', iocCategoryPageUrlPattern);
  });

  describe('IocCategory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(iocCategoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create IocCategory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ioc-category/new$'));
        cy.getEntityCreateUpdateHeading('IocCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', iocCategoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ioc-categories',
          body: iocCategorySample,
        }).then(({ body }) => {
          iocCategory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ioc-categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [iocCategory],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(iocCategoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details IocCategory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('iocCategory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', iocCategoryPageUrlPattern);
      });

      it('edit button click should load edit IocCategory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IocCategory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', iocCategoryPageUrlPattern);
      });

      it('edit button click should load edit IocCategory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('IocCategory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', iocCategoryPageUrlPattern);
      });

      it('last delete button click should delete instance of IocCategory', () => {
        cy.intercept('GET', '/api/ioc-categories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('iocCategory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', iocCategoryPageUrlPattern);

        iocCategory = undefined;
      });
    });
  });

  describe('new IocCategory page', () => {
    beforeEach(() => {
      cy.visit(`${iocCategoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('IocCategory');
    });

    it('should create an instance of IocCategory', () => {
      cy.get(`[data-cy="iocCategoryColor"]`).type('focalise');
      cy.get(`[data-cy="iocCategoryColor"]`).should('have.value', 'focalise');

      cy.get(`[data-cy="iocCategoryId"]`).type('15989');
      cy.get(`[data-cy="iocCategoryId"]`).should('have.value', '15989');

      cy.get(`[data-cy="iocCategoryName"]`).type('gee');
      cy.get(`[data-cy="iocCategoryName"]`).should('have.value', 'gee');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        iocCategory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', iocCategoryPageUrlPattern);
    });
  });
});
