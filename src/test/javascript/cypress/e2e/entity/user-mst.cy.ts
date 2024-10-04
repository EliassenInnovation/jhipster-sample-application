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

describe('UserMst e2e test', () => {
  const userMstPageUrl = '/user-mst';
  const userMstPageUrlPattern = new RegExp('/user-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userMstSample = {};

  let userMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-msts/${userMst.id}`,
      }).then(() => {
        userMst = undefined;
      });
    }
  });

  it('UserMsts menu should load UserMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserMst').should('exist');
    cy.url().should('match', userMstPageUrlPattern);
  });

  describe('UserMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-mst/new$'));
        cy.getEntityCreateUpdateHeading('UserMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-msts',
          body: userMstSample,
        }).then(({ body }) => {
          userMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [userMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userMstPageUrlPattern);
      });

      it('edit button click should load edit UserMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userMstPageUrlPattern);
      });

      it('edit button click should load edit UserMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userMstPageUrlPattern);
      });

      it('last delete button click should delete instance of UserMst', () => {
        cy.intercept('GET', '/api/user-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('userMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', userMstPageUrlPattern);

        userMst = undefined;
      });
    });
  });

  describe('new UserMst page', () => {
    beforeEach(() => {
      cy.visit(`${userMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserMst');
    });

    it('should create an instance of UserMst', () => {
      cy.get(`[data-cy="addressLine1"]`).type('harmful');
      cy.get(`[data-cy="addressLine1"]`).should('have.value', 'harmful');

      cy.get(`[data-cy="addressLine2"]`).type('heating');
      cy.get(`[data-cy="addressLine2"]`).should('have.value', 'heating');

      cy.get(`[data-cy="city"]`).type('West Carmela');
      cy.get(`[data-cy="city"]`).should('have.value', 'West Carmela');

      cy.get(`[data-cy="country"]`).type('Iran');
      cy.get(`[data-cy="country"]`).should('have.value', 'Iran');

      cy.get(`[data-cy="coverImage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="coverImage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="createBy"]`).type('17624');
      cy.get(`[data-cy="createBy"]`).should('have.value', '17624');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="email"]`).type('Esteban.Mertz@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Esteban.Mertz@gmail.com');

      cy.get(`[data-cy="firstName"]`).type('Trevor');
      cy.get(`[data-cy="firstName"]`).should('have.value', 'Trevor');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="lastName"]`).type('Stracke');
      cy.get(`[data-cy="lastName"]`).should('have.value', 'Stracke');

      cy.get(`[data-cy="manufacturerId"]`).type('21786');
      cy.get(`[data-cy="manufacturerId"]`).should('have.value', '21786');

      cy.get(`[data-cy="mobile"]`).type('cemetery near after');
      cy.get(`[data-cy="mobile"]`).should('have.value', 'cemetery near after');

      cy.get(`[data-cy="objectId"]`).type('pillory pish');
      cy.get(`[data-cy="objectId"]`).should('have.value', 'pillory pish');

      cy.get(`[data-cy="password"]`).type('actual designation');
      cy.get(`[data-cy="password"]`).should('have.value', 'actual designation');

      cy.get(`[data-cy="profileImage"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="profileImage"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="roleId"]`).type('14482');
      cy.get(`[data-cy="roleId"]`).should('have.value', '14482');

      cy.get(`[data-cy="schoolDistrictId"]`).type('31762');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '31762');

      cy.get(`[data-cy="state"]`).type('3478');
      cy.get(`[data-cy="state"]`).should('have.value', '3478');

      cy.get(`[data-cy="updatedBy"]`).type('25746');
      cy.get(`[data-cy="updatedBy"]`).should('have.value', '25746');

      cy.get(`[data-cy="updatedOn"]`).type('2024-10-03');
      cy.get(`[data-cy="updatedOn"]`).blur();
      cy.get(`[data-cy="updatedOn"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="userId"]`).type('26949');
      cy.get(`[data-cy="userId"]`).should('have.value', '26949');

      cy.get(`[data-cy="zipCode"]`).type('72692');
      cy.get(`[data-cy="zipCode"]`).should('have.value', '72692');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        userMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', userMstPageUrlPattern);
    });
  });
});
