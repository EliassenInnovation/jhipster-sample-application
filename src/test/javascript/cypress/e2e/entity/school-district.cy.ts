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

describe('SchoolDistrict e2e test', () => {
  const schoolDistrictPageUrl = '/school-district';
  const schoolDistrictPageUrlPattern = new RegExp('/school-district(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const schoolDistrictSample = {};

  let schoolDistrict;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/school-districts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/school-districts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/school-districts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (schoolDistrict) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/school-districts/${schoolDistrict.id}`,
      }).then(() => {
        schoolDistrict = undefined;
      });
    }
  });

  it('SchoolDistricts menu should load SchoolDistricts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('school-district');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SchoolDistrict').should('exist');
    cy.url().should('match', schoolDistrictPageUrlPattern);
  });

  describe('SchoolDistrict page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(schoolDistrictPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SchoolDistrict page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/school-district/new$'));
        cy.getEntityCreateUpdateHeading('SchoolDistrict');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', schoolDistrictPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/school-districts',
          body: schoolDistrictSample,
        }).then(({ body }) => {
          schoolDistrict = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/school-districts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [schoolDistrict],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(schoolDistrictPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details SchoolDistrict page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('schoolDistrict');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', schoolDistrictPageUrlPattern);
      });

      it('edit button click should load edit SchoolDistrict page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SchoolDistrict');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', schoolDistrictPageUrlPattern);
      });

      it('edit button click should load edit SchoolDistrict page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SchoolDistrict');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', schoolDistrictPageUrlPattern);
      });

      it('last delete button click should delete instance of SchoolDistrict', () => {
        cy.intercept('GET', '/api/school-districts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('schoolDistrict').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', schoolDistrictPageUrlPattern);

        schoolDistrict = undefined;
      });
    });
  });

  describe('new SchoolDistrict page', () => {
    beforeEach(() => {
      cy.visit(`${schoolDistrictPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SchoolDistrict');
    });

    it('should create an instance of SchoolDistrict', () => {
      cy.get(`[data-cy="city"]`).type('Margaritaburgh');
      cy.get(`[data-cy="city"]`).should('have.value', 'Margaritaburgh');

      cy.get(`[data-cy="contractCompany"]`).type('inasmuch');
      cy.get(`[data-cy="contractCompany"]`).should('have.value', 'inasmuch');

      cy.get(`[data-cy="country"]`).type('Lithuania');
      cy.get(`[data-cy="country"]`).should('have.value', 'Lithuania');

      cy.get(`[data-cy="createdBy"]`).type('18153');
      cy.get(`[data-cy="createdBy"]`).should('have.value', '18153');

      cy.get(`[data-cy="createdOn"]`).type('2024-10-04');
      cy.get(`[data-cy="createdOn"]`).blur();
      cy.get(`[data-cy="createdOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="districtLogo"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="districtLogo"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="districtName"]`).type('video');
      cy.get(`[data-cy="districtName"]`).should('have.value', 'video');

      cy.get(`[data-cy="email"]`).type('Tyson_Osinski71@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Tyson_Osinski71@gmail.com');

      cy.get(`[data-cy="foodServiceOptions"]`).type('oily with');
      cy.get(`[data-cy="foodServiceOptions"]`).should('have.value', 'oily with');

      cy.get(`[data-cy="isActive"]`).should('not.be.checked');
      cy.get(`[data-cy="isActive"]`).click();
      cy.get(`[data-cy="isActive"]`).should('be.checked');

      cy.get(`[data-cy="lastUpdatedBy"]`).type('13189');
      cy.get(`[data-cy="lastUpdatedBy"]`).should('have.value', '13189');

      cy.get(`[data-cy="lastUpdatedOn"]`).type('2024-10-04');
      cy.get(`[data-cy="lastUpdatedOn"]`).blur();
      cy.get(`[data-cy="lastUpdatedOn"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="phoneNumber"]`).type('plain huzzah');
      cy.get(`[data-cy="phoneNumber"]`).should('have.value', 'plain huzzah');

      cy.get(`[data-cy="schoolDistrictId"]`).type('11545');
      cy.get(`[data-cy="schoolDistrictId"]`).should('have.value', '11545');

      cy.get(`[data-cy="siteCode"]`).type('oddball');
      cy.get(`[data-cy="siteCode"]`).should('have.value', 'oddball');

      cy.get(`[data-cy="state"]`).type('4390');
      cy.get(`[data-cy="state"]`).should('have.value', '4390');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        schoolDistrict = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', schoolDistrictPageUrlPattern);
    });
  });
});
