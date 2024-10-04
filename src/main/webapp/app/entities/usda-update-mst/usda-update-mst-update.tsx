import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './usda-update-mst.reducer';

export const USDAUpdateMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const uSDAUpdateMstEntity = useAppSelector(state => state.uSDAUpdateMst.entity);
  const loading = useAppSelector(state => state.uSDAUpdateMst.loading);
  const updating = useAppSelector(state => state.uSDAUpdateMst.updating);
  const updateSuccess = useAppSelector(state => state.uSDAUpdateMst.updateSuccess);

  const handleClose = () => {
    navigate('/usda-update-mst');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.calciumCamgperServing !== undefined && typeof values.calciumCamgperServing !== 'number') {
      values.calciumCamgperServing = Number(values.calciumCamgperServing);
    }
    if (values.saturatedFatgperServing !== undefined && typeof values.saturatedFatgperServing !== 'number') {
      values.saturatedFatgperServing = Number(values.saturatedFatgperServing);
    }
    if (values.totalCarbohydrategperServing !== undefined && typeof values.totalCarbohydrategperServing !== 'number') {
      values.totalCarbohydrategperServing = Number(values.totalCarbohydrategperServing);
    }

    const entity = {
      ...uSDAUpdateMstEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...uSDAUpdateMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.uSDAUpdateMst.home.createOrEditLabel" data-cy="USDAUpdateMstCreateUpdateHeading">
            <Translate contentKey="dboApp.uSDAUpdateMst.home.createOrEditLabel">Create or edit a USDAUpdateMst</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="usda-update-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.addedSugarsgperServing')}
                id="usda-update-mst-addedSugarsgperServing"
                name="addedSugarsgperServing"
                data-cy="addedSugarsgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.allAllergens')}
                id="usda-update-mst-allAllergens"
                name="allAllergens"
                data-cy="allAllergens"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.brandName')}
                id="usda-update-mst-brandName"
                name="brandName"
                data-cy="brandName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.calciumCamgperServing')}
                id="usda-update-mst-calciumCamgperServing"
                name="calciumCamgperServing"
                data-cy="calciumCamgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.calorieskcalperServing')}
                id="usda-update-mst-calorieskcalperServing"
                name="calorieskcalperServing"
                data-cy="calorieskcalperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cholesterolmgperServing')}
                id="usda-update-mst-cholesterolmgperServing"
                name="cholesterolmgperServing"
                data-cy="cholesterolmgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNCredited')}
                id="usda-update-mst-cNCredited"
                name="cNCredited"
                data-cy="cNCredited"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNCrediting')}
                id="usda-update-mst-cNCrediting"
                name="cNCrediting"
                data-cy="cNCrediting"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNExpirationDate')}
                id="usda-update-mst-cNExpirationDate"
                name="cNExpirationDate"
                data-cy="cNExpirationDate"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNLabelDocument')}
                id="usda-update-mst-cNLabelDocument"
                name="cNLabelDocument"
                data-cy="cNLabelDocument"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNLabelStatement')}
                id="usda-update-mst-cNLabelStatement"
                name="cNLabelStatement"
                data-cy="cNLabelStatement"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNProductIdentification')}
                id="usda-update-mst-cNProductIdentification"
                name="cNProductIdentification"
                data-cy="cNProductIdentification"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNQualified')}
                id="usda-update-mst-cNQualified"
                name="cNQualified"
                data-cy="cNQualified"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.cNQualifierCode')}
                id="usda-update-mst-cNQualifierCode"
                name="cNQualifierCode"
                data-cy="cNQualifierCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.dietaryFibergperServing')}
                id="usda-update-mst-dietaryFibergperServing"
                name="dietaryFibergperServing"
                data-cy="dietaryFibergperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.downloaded')}
                id="usda-update-mst-downloaded"
                name="downloaded"
                data-cy="downloaded"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.eggs')}
                id="usda-update-mst-eggs"
                name="eggs"
                data-cy="eggs"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.fish')}
                id="usda-update-mst-fish"
                name="fish"
                data-cy="fish"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.foodCategory')}
                id="usda-update-mst-foodCategory"
                name="foodCategory"
                data-cy="foodCategory"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.functionalname')}
                id="usda-update-mst-functionalname"
                name="functionalname"
                data-cy="functionalname"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.gtin')}
                id="usda-update-mst-gtin"
                name="gtin"
                data-cy="gtin"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.halal')}
                id="usda-update-mst-halal"
                name="halal"
                data-cy="halal"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.hierarchicalPlacement')}
                id="usda-update-mst-hierarchicalPlacement"
                name="hierarchicalPlacement"
                data-cy="hierarchicalPlacement"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.informationProvider')}
                id="usda-update-mst-informationProvider"
                name="informationProvider"
                data-cy="informationProvider"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.ingredientsenglish')}
                id="usda-update-mst-ingredientsenglish"
                name="ingredientsenglish"
                data-cy="ingredientsenglish"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.ironFemgperServing')}
                id="usda-update-mst-ironFemgperServing"
                name="ironFemgperServing"
                data-cy="ironFemgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.kosher')}
                id="usda-update-mst-kosher"
                name="kosher"
                data-cy="kosher"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.lastupdated')}
                id="usda-update-mst-lastupdated"
                name="lastupdated"
                data-cy="lastupdated"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.longdescription')}
                id="usda-update-mst-longdescription"
                name="longdescription"
                data-cy="longdescription"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.milk')}
                id="usda-update-mst-milk"
                name="milk"
                data-cy="milk"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.nutrientFormatTypeCodeReferenceCode')}
                id="usda-update-mst-nutrientFormatTypeCodeReferenceCode"
                name="nutrientFormatTypeCodeReferenceCode"
                data-cy="nutrientFormatTypeCodeReferenceCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.nutrientQuantityBasisTypeCode')}
                id="usda-update-mst-nutrientQuantityBasisTypeCode"
                name="nutrientQuantityBasisTypeCode"
                data-cy="nutrientQuantityBasisTypeCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.nutrientQuantityBasisUnitofMeasure')}
                id="usda-update-mst-nutrientQuantityBasisUnitofMeasure"
                name="nutrientQuantityBasisUnitofMeasure"
                data-cy="nutrientQuantityBasisUnitofMeasure"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.nutrientQuantityBasisValue')}
                id="usda-update-mst-nutrientQuantityBasisValue"
                name="nutrientQuantityBasisValue"
                data-cy="nutrientQuantityBasisValue"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.nutrientsperservingcalculatedfrombymeasurereportedamount')}
                id="usda-update-mst-nutrientsperservingcalculatedfrombymeasurereportedamount"
                name="nutrientsperservingcalculatedfrombymeasurereportedamount"
                data-cy="nutrientsperservingcalculatedfrombymeasurereportedamount"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.peanuts')}
                id="usda-update-mst-peanuts"
                name="peanuts"
                data-cy="peanuts"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.pFSCreditableIngredientTypeCode')}
                id="usda-update-mst-pFSCreditableIngredientTypeCode"
                name="pFSCreditableIngredientTypeCode"
                data-cy="pFSCreditableIngredientTypeCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.pFSDocument')}
                id="usda-update-mst-pFSDocument"
                name="pFSDocument"
                data-cy="pFSDocument"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.pFSTotalCreditableIngredientAmount')}
                id="usda-update-mst-pFSTotalCreditableIngredientAmount"
                name="pFSTotalCreditableIngredientAmount"
                data-cy="pFSTotalCreditableIngredientAmount"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.pFSTotalPortionWeight')}
                id="usda-update-mst-pFSTotalPortionWeight"
                name="pFSTotalPortionWeight"
                data-cy="pFSTotalPortionWeight"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.potassiumKmgperServing')}
                id="usda-update-mst-potassiumKmgperServing"
                name="potassiumKmgperServing"
                data-cy="potassiumKmgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.preparationStateCode')}
                id="usda-update-mst-preparationStateCode"
                name="preparationStateCode"
                data-cy="preparationStateCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.productFormulationStatement')}
                id="usda-update-mst-productFormulationStatement"
                name="productFormulationStatement"
                data-cy="productFormulationStatement"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.proteingperServing')}
                id="usda-update-mst-proteingperServing"
                name="proteingperServing"
                data-cy="proteingperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.saturatedFatgperServing')}
                id="usda-update-mst-saturatedFatgperServing"
                name="saturatedFatgperServing"
                data-cy="saturatedFatgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.servingDescription')}
                id="usda-update-mst-servingDescription"
                name="servingDescription"
                data-cy="servingDescription"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.servingSize')}
                id="usda-update-mst-servingSize"
                name="servingSize"
                data-cy="servingSize"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.servingUnitofMeasure')}
                id="usda-update-mst-servingUnitofMeasure"
                name="servingUnitofMeasure"
                data-cy="servingUnitofMeasure"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.servingsPerCase')}
                id="usda-update-mst-servingsPerCase"
                name="servingsPerCase"
                data-cy="servingsPerCase"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.sesame')}
                id="usda-update-mst-sesame"
                name="sesame"
                data-cy="sesame"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.shellfish')}
                id="usda-update-mst-shellfish"
                name="shellfish"
                data-cy="shellfish"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.shortdescription')}
                id="usda-update-mst-shortdescription"
                name="shortdescription"
                data-cy="shortdescription"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.sodiummgperServing')}
                id="usda-update-mst-sodiummgperServing"
                name="sodiummgperServing"
                data-cy="sodiummgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.soybeans')}
                id="usda-update-mst-soybeans"
                name="soybeans"
                data-cy="soybeans"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.sugarsgperServing')}
                id="usda-update-mst-sugarsgperServing"
                name="sugarsgperServing"
                data-cy="sugarsgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.totalCarbohydrategperServing')}
                id="usda-update-mst-totalCarbohydrategperServing"
                name="totalCarbohydrategperServing"
                data-cy="totalCarbohydrategperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.totalFatgperServing')}
                id="usda-update-mst-totalFatgperServing"
                name="totalFatgperServing"
                data-cy="totalFatgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.tradeChannels')}
                id="usda-update-mst-tradeChannels"
                name="tradeChannels"
                data-cy="tradeChannels"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.transFatgperServing')}
                id="usda-update-mst-transFatgperServing"
                name="transFatgperServing"
                data-cy="transFatgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.treeNuts')}
                id="usda-update-mst-treeNuts"
                name="treeNuts"
                data-cy="treeNuts"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.uSDAFoodsMaterialCode')}
                id="usda-update-mst-uSDAFoodsMaterialCode"
                name="uSDAFoodsMaterialCode"
                data-cy="uSDAFoodsMaterialCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.uSDAFoodsProductsDescription')}
                id="usda-update-mst-uSDAFoodsProductsDescription"
                name="uSDAFoodsProductsDescription"
                data-cy="uSDAFoodsProductsDescription"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.vendorName')}
                id="usda-update-mst-vendorName"
                name="vendorName"
                data-cy="vendorName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.vendorID')}
                id="usda-update-mst-vendorID"
                name="vendorID"
                data-cy="vendorID"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.vitaminDmcgperServing')}
                id="usda-update-mst-vitaminDmcgperServing"
                name="vitaminDmcgperServing"
                data-cy="vitaminDmcgperServing"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.uSDAUpdateMst.wheat')}
                id="usda-update-mst-wheat"
                name="wheat"
                data-cy="wheat"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/usda-update-mst" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default USDAUpdateMstUpdate;
