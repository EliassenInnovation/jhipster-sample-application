import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './usda-update-mst.reducer';

export const USDAUpdateMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const uSDAUpdateMstEntity = useAppSelector(state => state.uSDAUpdateMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uSDAUpdateMstDetailsHeading">
          <Translate contentKey="dboApp.uSDAUpdateMst.detail.title">USDAUpdateMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.id}</dd>
          <dt>
            <span id="addedSugarsgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.addedSugarsgperServing">Added Sugarsgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.addedSugarsgperServing}</dd>
          <dt>
            <span id="allAllergens">
              <Translate contentKey="dboApp.uSDAUpdateMst.allAllergens">All Allergens</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.allAllergens}</dd>
          <dt>
            <span id="brandName">
              <Translate contentKey="dboApp.uSDAUpdateMst.brandName">Brand Name</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.brandName}</dd>
          <dt>
            <span id="calciumCamgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.calciumCamgperServing">Calcium Camgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.calciumCamgperServing}</dd>
          <dt>
            <span id="calorieskcalperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.calorieskcalperServing">Calorieskcalper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.calorieskcalperServing}</dd>
          <dt>
            <span id="cholesterolmgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.cholesterolmgperServing">Cholesterolmgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cholesterolmgperServing}</dd>
          <dt>
            <span id="cNCredited">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNCredited">C N Credited</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNCredited}</dd>
          <dt>
            <span id="cNCrediting">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNCrediting">C N Crediting</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNCrediting}</dd>
          <dt>
            <span id="cNExpirationDate">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNExpirationDate">C N Expiration Date</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNExpirationDate}</dd>
          <dt>
            <span id="cNLabelDocument">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNLabelDocument">C N Label Document</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNLabelDocument}</dd>
          <dt>
            <span id="cNLabelStatement">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNLabelStatement">C N Label Statement</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNLabelStatement}</dd>
          <dt>
            <span id="cNProductIdentification">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNProductIdentification">C N Product Identification</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNProductIdentification}</dd>
          <dt>
            <span id="cNQualified">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNQualified">C N Qualified</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNQualified}</dd>
          <dt>
            <span id="cNQualifierCode">
              <Translate contentKey="dboApp.uSDAUpdateMst.cNQualifierCode">C N Qualifier Code</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.cNQualifierCode}</dd>
          <dt>
            <span id="dietaryFibergperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.dietaryFibergperServing">Dietary Fibergper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.dietaryFibergperServing}</dd>
          <dt>
            <span id="downloaded">
              <Translate contentKey="dboApp.uSDAUpdateMst.downloaded">Downloaded</Translate>
            </span>
          </dt>
          <dd>
            {uSDAUpdateMstEntity.downloaded ? (
              <TextFormat value={uSDAUpdateMstEntity.downloaded} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="eggs">
              <Translate contentKey="dboApp.uSDAUpdateMst.eggs">Eggs</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.eggs}</dd>
          <dt>
            <span id="fish">
              <Translate contentKey="dboApp.uSDAUpdateMst.fish">Fish</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.fish}</dd>
          <dt>
            <span id="foodCategory">
              <Translate contentKey="dboApp.uSDAUpdateMst.foodCategory">Food Category</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.foodCategory}</dd>
          <dt>
            <span id="functionalname">
              <Translate contentKey="dboApp.uSDAUpdateMst.functionalname">Functionalname</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.functionalname}</dd>
          <dt>
            <span id="gtin">
              <Translate contentKey="dboApp.uSDAUpdateMst.gtin">Gtin</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.gtin}</dd>
          <dt>
            <span id="halal">
              <Translate contentKey="dboApp.uSDAUpdateMst.halal">Halal</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.halal}</dd>
          <dt>
            <span id="hierarchicalPlacement">
              <Translate contentKey="dboApp.uSDAUpdateMst.hierarchicalPlacement">Hierarchical Placement</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.hierarchicalPlacement}</dd>
          <dt>
            <span id="informationProvider">
              <Translate contentKey="dboApp.uSDAUpdateMst.informationProvider">Information Provider</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.informationProvider}</dd>
          <dt>
            <span id="ingredientsenglish">
              <Translate contentKey="dboApp.uSDAUpdateMst.ingredientsenglish">Ingredientsenglish</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.ingredientsenglish}</dd>
          <dt>
            <span id="ironFemgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.ironFemgperServing">Iron Femgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.ironFemgperServing}</dd>
          <dt>
            <span id="kosher">
              <Translate contentKey="dboApp.uSDAUpdateMst.kosher">Kosher</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.kosher}</dd>
          <dt>
            <span id="lastupdated">
              <Translate contentKey="dboApp.uSDAUpdateMst.lastupdated">Lastupdated</Translate>
            </span>
          </dt>
          <dd>
            {uSDAUpdateMstEntity.lastupdated ? (
              <TextFormat value={uSDAUpdateMstEntity.lastupdated} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="longdescription">
              <Translate contentKey="dboApp.uSDAUpdateMst.longdescription">Longdescription</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.longdescription}</dd>
          <dt>
            <span id="milk">
              <Translate contentKey="dboApp.uSDAUpdateMst.milk">Milk</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.milk}</dd>
          <dt>
            <span id="nutrientFormatTypeCodeReferenceCode">
              <Translate contentKey="dboApp.uSDAUpdateMst.nutrientFormatTypeCodeReferenceCode">
                Nutrient Format Type Code Reference Code
              </Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.nutrientFormatTypeCodeReferenceCode}</dd>
          <dt>
            <span id="nutrientQuantityBasisTypeCode">
              <Translate contentKey="dboApp.uSDAUpdateMst.nutrientQuantityBasisTypeCode">Nutrient Quantity Basis Type Code</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.nutrientQuantityBasisTypeCode}</dd>
          <dt>
            <span id="nutrientQuantityBasisUnitofMeasure">
              <Translate contentKey="dboApp.uSDAUpdateMst.nutrientQuantityBasisUnitofMeasure">
                Nutrient Quantity Basis Unitof Measure
              </Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.nutrientQuantityBasisUnitofMeasure}</dd>
          <dt>
            <span id="nutrientQuantityBasisValue">
              <Translate contentKey="dboApp.uSDAUpdateMst.nutrientQuantityBasisValue">Nutrient Quantity Basis Value</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.nutrientQuantityBasisValue}</dd>
          <dt>
            <span id="nutrientsperservingcalculatedfrombymeasurereportedamount">
              <Translate contentKey="dboApp.uSDAUpdateMst.nutrientsperservingcalculatedfrombymeasurereportedamount">
                Nutrientsperservingcalculatedfrombymeasurereportedamount
              </Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.nutrientsperservingcalculatedfrombymeasurereportedamount ? 'true' : 'false'}</dd>
          <dt>
            <span id="peanuts">
              <Translate contentKey="dboApp.uSDAUpdateMst.peanuts">Peanuts</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.peanuts}</dd>
          <dt>
            <span id="pFSCreditableIngredientTypeCode">
              <Translate contentKey="dboApp.uSDAUpdateMst.pFSCreditableIngredientTypeCode">P FS Creditable Ingredient Type Code</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.pFSCreditableIngredientTypeCode}</dd>
          <dt>
            <span id="pFSDocument">
              <Translate contentKey="dboApp.uSDAUpdateMst.pFSDocument">P FS Document</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.pFSDocument}</dd>
          <dt>
            <span id="pFSTotalCreditableIngredientAmount">
              <Translate contentKey="dboApp.uSDAUpdateMst.pFSTotalCreditableIngredientAmount">
                P FS Total Creditable Ingredient Amount
              </Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.pFSTotalCreditableIngredientAmount}</dd>
          <dt>
            <span id="pFSTotalPortionWeight">
              <Translate contentKey="dboApp.uSDAUpdateMst.pFSTotalPortionWeight">P FS Total Portion Weight</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.pFSTotalPortionWeight}</dd>
          <dt>
            <span id="potassiumKmgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.potassiumKmgperServing">Potassium Kmgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.potassiumKmgperServing}</dd>
          <dt>
            <span id="preparationStateCode">
              <Translate contentKey="dboApp.uSDAUpdateMst.preparationStateCode">Preparation State Code</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.preparationStateCode}</dd>
          <dt>
            <span id="productFormulationStatement">
              <Translate contentKey="dboApp.uSDAUpdateMst.productFormulationStatement">Product Formulation Statement</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.productFormulationStatement}</dd>
          <dt>
            <span id="proteingperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.proteingperServing">Proteingper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.proteingperServing}</dd>
          <dt>
            <span id="saturatedFatgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.saturatedFatgperServing">Saturated Fatgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.saturatedFatgperServing}</dd>
          <dt>
            <span id="servingDescription">
              <Translate contentKey="dboApp.uSDAUpdateMst.servingDescription">Serving Description</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.servingDescription}</dd>
          <dt>
            <span id="servingSize">
              <Translate contentKey="dboApp.uSDAUpdateMst.servingSize">Serving Size</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.servingSize}</dd>
          <dt>
            <span id="servingUnitofMeasure">
              <Translate contentKey="dboApp.uSDAUpdateMst.servingUnitofMeasure">Serving Unitof Measure</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.servingUnitofMeasure}</dd>
          <dt>
            <span id="servingsPerCase">
              <Translate contentKey="dboApp.uSDAUpdateMst.servingsPerCase">Servings Per Case</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.servingsPerCase}</dd>
          <dt>
            <span id="sesame">
              <Translate contentKey="dboApp.uSDAUpdateMst.sesame">Sesame</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.sesame}</dd>
          <dt>
            <span id="shellfish">
              <Translate contentKey="dboApp.uSDAUpdateMst.shellfish">Shellfish</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.shellfish}</dd>
          <dt>
            <span id="shortdescription">
              <Translate contentKey="dboApp.uSDAUpdateMst.shortdescription">Shortdescription</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.shortdescription}</dd>
          <dt>
            <span id="sodiummgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.sodiummgperServing">Sodiummgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.sodiummgperServing}</dd>
          <dt>
            <span id="soybeans">
              <Translate contentKey="dboApp.uSDAUpdateMst.soybeans">Soybeans</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.soybeans}</dd>
          <dt>
            <span id="sugarsgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.sugarsgperServing">Sugarsgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.sugarsgperServing}</dd>
          <dt>
            <span id="totalCarbohydrategperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.totalCarbohydrategperServing">Total Carbohydrategper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.totalCarbohydrategperServing}</dd>
          <dt>
            <span id="totalFatgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.totalFatgperServing">Total Fatgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.totalFatgperServing}</dd>
          <dt>
            <span id="tradeChannels">
              <Translate contentKey="dboApp.uSDAUpdateMst.tradeChannels">Trade Channels</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.tradeChannels}</dd>
          <dt>
            <span id="transFatgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.transFatgperServing">Trans Fatgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.transFatgperServing}</dd>
          <dt>
            <span id="treeNuts">
              <Translate contentKey="dboApp.uSDAUpdateMst.treeNuts">Tree Nuts</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.treeNuts}</dd>
          <dt>
            <span id="uSDAFoodsMaterialCode">
              <Translate contentKey="dboApp.uSDAUpdateMst.uSDAFoodsMaterialCode">U SDA Foods Material Code</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.uSDAFoodsMaterialCode}</dd>
          <dt>
            <span id="uSDAFoodsProductsDescription">
              <Translate contentKey="dboApp.uSDAUpdateMst.uSDAFoodsProductsDescription">U SDA Foods Products Description</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.uSDAFoodsProductsDescription}</dd>
          <dt>
            <span id="vendorName">
              <Translate contentKey="dboApp.uSDAUpdateMst.vendorName">Vendor Name</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.vendorName}</dd>
          <dt>
            <span id="vendorID">
              <Translate contentKey="dboApp.uSDAUpdateMst.vendorID">Vendor ID</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.vendorID}</dd>
          <dt>
            <span id="vitaminDmcgperServing">
              <Translate contentKey="dboApp.uSDAUpdateMst.vitaminDmcgperServing">Vitamin Dmcgper Serving</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.vitaminDmcgperServing}</dd>
          <dt>
            <span id="wheat">
              <Translate contentKey="dboApp.uSDAUpdateMst.wheat">Wheat</Translate>
            </span>
          </dt>
          <dd>{uSDAUpdateMstEntity.wheat}</dd>
        </dl>
        <Button tag={Link} to="/usda-update-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/usda-update-mst/${uSDAUpdateMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default USDAUpdateMstDetail;
