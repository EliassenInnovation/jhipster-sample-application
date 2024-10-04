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

describe('USDAUpdateMst e2e test', () => {
  const uSDAUpdateMstPageUrl = '/usda-update-mst';
  const uSDAUpdateMstPageUrlPattern = new RegExp('/usda-update-mst(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const uSDAUpdateMstSample = {};

  let uSDAUpdateMst;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/usda-update-msts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/usda-update-msts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/usda-update-msts/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (uSDAUpdateMst) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/usda-update-msts/${uSDAUpdateMst.id}`,
      }).then(() => {
        uSDAUpdateMst = undefined;
      });
    }
  });

  it('USDAUpdateMsts menu should load USDAUpdateMsts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('usda-update-mst');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('USDAUpdateMst').should('exist');
    cy.url().should('match', uSDAUpdateMstPageUrlPattern);
  });

  describe('USDAUpdateMst page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(uSDAUpdateMstPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create USDAUpdateMst page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/usda-update-mst/new$'));
        cy.getEntityCreateUpdateHeading('USDAUpdateMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uSDAUpdateMstPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/usda-update-msts',
          body: uSDAUpdateMstSample,
        }).then(({ body }) => {
          uSDAUpdateMst = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/usda-update-msts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [uSDAUpdateMst],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(uSDAUpdateMstPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details USDAUpdateMst page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('uSDAUpdateMst');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uSDAUpdateMstPageUrlPattern);
      });

      it('edit button click should load edit USDAUpdateMst page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('USDAUpdateMst');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uSDAUpdateMstPageUrlPattern);
      });

      it('edit button click should load edit USDAUpdateMst page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('USDAUpdateMst');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uSDAUpdateMstPageUrlPattern);
      });

      it('last delete button click should delete instance of USDAUpdateMst', () => {
        cy.intercept('GET', '/api/usda-update-msts/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('uSDAUpdateMst').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', uSDAUpdateMstPageUrlPattern);

        uSDAUpdateMst = undefined;
      });
    });
  });

  describe('new USDAUpdateMst page', () => {
    beforeEach(() => {
      cy.visit(`${uSDAUpdateMstPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('USDAUpdateMst');
    });

    it('should create an instance of USDAUpdateMst', () => {
      cy.get(`[data-cy="addedSugarsgperServing"]`).type('scarily deafening um');
      cy.get(`[data-cy="addedSugarsgperServing"]`).should('have.value', 'scarily deafening um');

      cy.get(`[data-cy="allAllergens"]`).type('accidentally enhance kooky');
      cy.get(`[data-cy="allAllergens"]`).should('have.value', 'accidentally enhance kooky');

      cy.get(`[data-cy="brandName"]`).type('stake thoughtfully idolized');
      cy.get(`[data-cy="brandName"]`).should('have.value', 'stake thoughtfully idolized');

      cy.get(`[data-cy="calciumCamgperServing"]`).type('22266.27');
      cy.get(`[data-cy="calciumCamgperServing"]`).should('have.value', '22266.27');

      cy.get(`[data-cy="calorieskcalperServing"]`).type('victoriously although');
      cy.get(`[data-cy="calorieskcalperServing"]`).should('have.value', 'victoriously although');

      cy.get(`[data-cy="cholesterolmgperServing"]`).type('another even parallel');
      cy.get(`[data-cy="cholesterolmgperServing"]`).should('have.value', 'another even parallel');

      cy.get(`[data-cy="cNCredited"]`).type('innocently geez collaborate');
      cy.get(`[data-cy="cNCredited"]`).should('have.value', 'innocently geez collaborate');

      cy.get(`[data-cy="cNCrediting"]`).type('unsung incidentally especially');
      cy.get(`[data-cy="cNCrediting"]`).should('have.value', 'unsung incidentally especially');

      cy.get(`[data-cy="cNExpirationDate"]`).type('velvety late');
      cy.get(`[data-cy="cNExpirationDate"]`).should('have.value', 'velvety late');

      cy.get(`[data-cy="cNLabelDocument"]`).type('finally cruelly');
      cy.get(`[data-cy="cNLabelDocument"]`).should('have.value', 'finally cruelly');

      cy.get(`[data-cy="cNLabelStatement"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="cNLabelStatement"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="cNProductIdentification"]`).type('tag');
      cy.get(`[data-cy="cNProductIdentification"]`).should('have.value', 'tag');

      cy.get(`[data-cy="cNQualified"]`).type('colossal frenetically');
      cy.get(`[data-cy="cNQualified"]`).should('have.value', 'colossal frenetically');

      cy.get(`[data-cy="cNQualifierCode"]`).type('passport');
      cy.get(`[data-cy="cNQualifierCode"]`).should('have.value', 'passport');

      cy.get(`[data-cy="dietaryFibergperServing"]`).type('octave knuckle yuck');
      cy.get(`[data-cy="dietaryFibergperServing"]`).should('have.value', 'octave knuckle yuck');

      cy.get(`[data-cy="downloaded"]`).type('2024-10-03');
      cy.get(`[data-cy="downloaded"]`).blur();
      cy.get(`[data-cy="downloaded"]`).should('have.value', '2024-10-03');

      cy.get(`[data-cy="eggs"]`).type('oof pharmacopoeia');
      cy.get(`[data-cy="eggs"]`).should('have.value', 'oof pharmacopoeia');

      cy.get(`[data-cy="fish"]`).type('definite impressive economise');
      cy.get(`[data-cy="fish"]`).should('have.value', 'definite impressive economise');

      cy.get(`[data-cy="foodCategory"]`).type('whispered');
      cy.get(`[data-cy="foodCategory"]`).should('have.value', 'whispered');

      cy.get(`[data-cy="functionalname"]`).type('govern');
      cy.get(`[data-cy="functionalname"]`).should('have.value', 'govern');

      cy.get(`[data-cy="gtin"]`).type('judgementally meh');
      cy.get(`[data-cy="gtin"]`).should('have.value', 'judgementally meh');

      cy.get(`[data-cy="halal"]`).type('nor boo blah');
      cy.get(`[data-cy="halal"]`).should('have.value', 'nor boo blah');

      cy.get(`[data-cy="hierarchicalPlacement"]`).type('couch');
      cy.get(`[data-cy="hierarchicalPlacement"]`).should('have.value', 'couch');

      cy.get(`[data-cy="informationProvider"]`).type('citizen');
      cy.get(`[data-cy="informationProvider"]`).should('have.value', 'citizen');

      cy.get(`[data-cy="ingredientsenglish"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="ingredientsenglish"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="ironFemgperServing"]`).type('restfully solemnly thoroughly');
      cy.get(`[data-cy="ironFemgperServing"]`).should('have.value', 'restfully solemnly thoroughly');

      cy.get(`[data-cy="kosher"]`).type('that whoever');
      cy.get(`[data-cy="kosher"]`).should('have.value', 'that whoever');

      cy.get(`[data-cy="lastupdated"]`).type('2024-10-04');
      cy.get(`[data-cy="lastupdated"]`).blur();
      cy.get(`[data-cy="lastupdated"]`).should('have.value', '2024-10-04');

      cy.get(`[data-cy="longdescription"]`).type('woefully woot indeed');
      cy.get(`[data-cy="longdescription"]`).should('have.value', 'woefully woot indeed');

      cy.get(`[data-cy="milk"]`).type('tightly');
      cy.get(`[data-cy="milk"]`).should('have.value', 'tightly');

      cy.get(`[data-cy="nutrientFormatTypeCodeReferenceCode"]`).type('amidst');
      cy.get(`[data-cy="nutrientFormatTypeCodeReferenceCode"]`).should('have.value', 'amidst');

      cy.get(`[data-cy="nutrientQuantityBasisTypeCode"]`).type('closely phew');
      cy.get(`[data-cy="nutrientQuantityBasisTypeCode"]`).should('have.value', 'closely phew');

      cy.get(`[data-cy="nutrientQuantityBasisUnitofMeasure"]`).type('far aching drat');
      cy.get(`[data-cy="nutrientQuantityBasisUnitofMeasure"]`).should('have.value', 'far aching drat');

      cy.get(`[data-cy="nutrientQuantityBasisValue"]`).type('imagineer boo');
      cy.get(`[data-cy="nutrientQuantityBasisValue"]`).should('have.value', 'imagineer boo');

      cy.get(`[data-cy="nutrientsperservingcalculatedfrombymeasurereportedamount"]`).should('not.be.checked');
      cy.get(`[data-cy="nutrientsperservingcalculatedfrombymeasurereportedamount"]`).click();
      cy.get(`[data-cy="nutrientsperservingcalculatedfrombymeasurereportedamount"]`).should('be.checked');

      cy.get(`[data-cy="peanuts"]`).type('righteously gadzooks release');
      cy.get(`[data-cy="peanuts"]`).should('have.value', 'righteously gadzooks release');

      cy.get(`[data-cy="pFSCreditableIngredientTypeCode"]`).type('opposite ack');
      cy.get(`[data-cy="pFSCreditableIngredientTypeCode"]`).should('have.value', 'opposite ack');

      cy.get(`[data-cy="pFSDocument"]`).type('godfather');
      cy.get(`[data-cy="pFSDocument"]`).should('have.value', 'godfather');

      cy.get(`[data-cy="pFSTotalCreditableIngredientAmount"]`).type('the circa');
      cy.get(`[data-cy="pFSTotalCreditableIngredientAmount"]`).should('have.value', 'the circa');

      cy.get(`[data-cy="pFSTotalPortionWeight"]`).type('helplessly yuppify ferociously');
      cy.get(`[data-cy="pFSTotalPortionWeight"]`).should('have.value', 'helplessly yuppify ferociously');

      cy.get(`[data-cy="potassiumKmgperServing"]`).type('gadzooks rusty despite');
      cy.get(`[data-cy="potassiumKmgperServing"]`).should('have.value', 'gadzooks rusty despite');

      cy.get(`[data-cy="preparationStateCode"]`).type('inwardly internal indeed');
      cy.get(`[data-cy="preparationStateCode"]`).should('have.value', 'inwardly internal indeed');

      cy.get(`[data-cy="productFormulationStatement"]`).type('briefly over psst');
      cy.get(`[data-cy="productFormulationStatement"]`).should('have.value', 'briefly over psst');

      cy.get(`[data-cy="proteingperServing"]`).type('hmph gee pack');
      cy.get(`[data-cy="proteingperServing"]`).should('have.value', 'hmph gee pack');

      cy.get(`[data-cy="saturatedFatgperServing"]`).type('2907.49');
      cy.get(`[data-cy="saturatedFatgperServing"]`).should('have.value', '2907.49');

      cy.get(`[data-cy="servingDescription"]`).type('what satisfy');
      cy.get(`[data-cy="servingDescription"]`).should('have.value', 'what satisfy');

      cy.get(`[data-cy="servingSize"]`).type('dental garage reward');
      cy.get(`[data-cy="servingSize"]`).should('have.value', 'dental garage reward');

      cy.get(`[data-cy="servingUnitofMeasure"]`).type('elegantly');
      cy.get(`[data-cy="servingUnitofMeasure"]`).should('have.value', 'elegantly');

      cy.get(`[data-cy="servingsPerCase"]`).type('whereas phooey');
      cy.get(`[data-cy="servingsPerCase"]`).should('have.value', 'whereas phooey');

      cy.get(`[data-cy="sesame"]`).type('waltz elevation');
      cy.get(`[data-cy="sesame"]`).should('have.value', 'waltz elevation');

      cy.get(`[data-cy="shellfish"]`).type('boiling');
      cy.get(`[data-cy="shellfish"]`).should('have.value', 'boiling');

      cy.get(`[data-cy="shortdescription"]`).type('kinase yum about');
      cy.get(`[data-cy="shortdescription"]`).should('have.value', 'kinase yum about');

      cy.get(`[data-cy="sodiummgperServing"]`).type('slink aha excitable');
      cy.get(`[data-cy="sodiummgperServing"]`).should('have.value', 'slink aha excitable');

      cy.get(`[data-cy="soybeans"]`).type('triumphantly stall');
      cy.get(`[data-cy="soybeans"]`).should('have.value', 'triumphantly stall');

      cy.get(`[data-cy="sugarsgperServing"]`).type('tensely');
      cy.get(`[data-cy="sugarsgperServing"]`).should('have.value', 'tensely');

      cy.get(`[data-cy="totalCarbohydrategperServing"]`).type('18627.5');
      cy.get(`[data-cy="totalCarbohydrategperServing"]`).should('have.value', '18627.5');

      cy.get(`[data-cy="totalFatgperServing"]`).type('fragrant knowingly');
      cy.get(`[data-cy="totalFatgperServing"]`).should('have.value', 'fragrant knowingly');

      cy.get(`[data-cy="tradeChannels"]`).type('ew unique orchid');
      cy.get(`[data-cy="tradeChannels"]`).should('have.value', 'ew unique orchid');

      cy.get(`[data-cy="transFatgperServing"]`).type('question');
      cy.get(`[data-cy="transFatgperServing"]`).should('have.value', 'question');

      cy.get(`[data-cy="treeNuts"]`).type('phew gleefully');
      cy.get(`[data-cy="treeNuts"]`).should('have.value', 'phew gleefully');

      cy.get(`[data-cy="uSDAFoodsMaterialCode"]`).type('when whoa larva');
      cy.get(`[data-cy="uSDAFoodsMaterialCode"]`).should('have.value', 'when whoa larva');

      cy.get(`[data-cy="uSDAFoodsProductsDescription"]`).type('narrate wisecrack');
      cy.get(`[data-cy="uSDAFoodsProductsDescription"]`).should('have.value', 'narrate wisecrack');

      cy.get(`[data-cy="vendorName"]`).type('woot concerning');
      cy.get(`[data-cy="vendorName"]`).should('have.value', 'woot concerning');

      cy.get(`[data-cy="vendorID"]`).type('subexpression realistic');
      cy.get(`[data-cy="vendorID"]`).should('have.value', 'subexpression realistic');

      cy.get(`[data-cy="vitaminDmcgperServing"]`).type('innocently than');
      cy.get(`[data-cy="vitaminDmcgperServing"]`).should('have.value', 'innocently than');

      cy.get(`[data-cy="wheat"]`).type('dowse duh');
      cy.get(`[data-cy="wheat"]`).should('have.value', 'dowse duh');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        uSDAUpdateMst = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', uSDAUpdateMstPageUrlPattern);
    });
  });
});
