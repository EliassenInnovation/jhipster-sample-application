import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './one-world-sync-product.reducer';

export const OneWorldSyncProductUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const oneWorldSyncProductEntity = useAppSelector(state => state.oneWorldSyncProduct.entity);
  const loading = useAppSelector(state => state.oneWorldSyncProduct.loading);
  const updating = useAppSelector(state => state.oneWorldSyncProduct.updating);
  const updateSuccess = useAppSelector(state => state.oneWorldSyncProduct.updateSuccess);

  const handleClose = () => {
    navigate('/one-world-sync-product');
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
    if (values.allergens !== undefined && typeof values.allergens !== 'number') {
      values.allergens = Number(values.allergens);
    }
    if (values.h7 !== undefined && typeof values.h7 !== 'number') {
      values.h7 = Number(values.h7);
    }
    if (values.manufacturerId !== undefined && typeof values.manufacturerId !== 'number') {
      values.manufacturerId = Number(values.manufacturerId);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.storageTypeId !== undefined && typeof values.storageTypeId !== 'number') {
      values.storageTypeId = Number(values.storageTypeId);
    }

    const entity = {
      ...oneWorldSyncProductEntity,
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
          ...oneWorldSyncProductEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.oneWorldSyncProduct.home.createOrEditLabel" data-cy="OneWorldSyncProductCreateUpdateHeading">
            <Translate contentKey="dboApp.oneWorldSyncProduct.home.createOrEditLabel">Create or edit a OneWorldSyncProduct</Translate>
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
                  id="one-world-sync-product-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.addedSugars')}
                id="one-world-sync-product-addedSugars"
                name="addedSugars"
                data-cy="addedSugars"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.addedSugarUom')}
                id="one-world-sync-product-addedSugarUom"
                name="addedSugarUom"
                data-cy="addedSugarUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.allergenKeyword')}
                id="one-world-sync-product-allergenKeyword"
                name="allergenKeyword"
                data-cy="allergenKeyword"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.allergens')}
                id="one-world-sync-product-allergens"
                name="allergens"
                data-cy="allergens"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.brandName')}
                id="one-world-sync-product-brandName"
                name="brandName"
                data-cy="brandName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.calories')}
                id="one-world-sync-product-calories"
                name="calories"
                data-cy="calories"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.caloriesUom')}
                id="one-world-sync-product-caloriesUom"
                name="caloriesUom"
                data-cy="caloriesUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.carbohydrates')}
                id="one-world-sync-product-carbohydrates"
                name="carbohydrates"
                data-cy="carbohydrates"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.carbohydratesUom')}
                id="one-world-sync-product-carbohydratesUom"
                name="carbohydratesUom"
                data-cy="carbohydratesUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.categoryName')}
                id="one-world-sync-product-categoryName"
                name="categoryName"
                data-cy="categoryName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.cholesterol')}
                id="one-world-sync-product-cholesterol"
                name="cholesterol"
                data-cy="cholesterol"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.cholesterolUOM')}
                id="one-world-sync-product-cholesterolUOM"
                name="cholesterolUOM"
                data-cy="cholesterolUOM"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.createdOn')}
                id="one-world-sync-product-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.dataForm')}
                id="one-world-sync-product-dataForm"
                name="dataForm"
                data-cy="dataForm"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.dietaryFiber')}
                id="one-world-sync-product-dietaryFiber"
                name="dietaryFiber"
                data-cy="dietaryFiber"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.dietaryFiberUom')}
                id="one-world-sync-product-dietaryFiberUom"
                name="dietaryFiberUom"
                data-cy="dietaryFiberUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.distributor')}
                id="one-world-sync-product-distributor"
                name="distributor"
                data-cy="distributor"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.doNotConsiderProduct')}
                id="one-world-sync-product-doNotConsiderProduct"
                name="doNotConsiderProduct"
                data-cy="doNotConsiderProduct"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.extendedModel')}
                id="one-world-sync-product-extendedModel"
                name="extendedModel"
                data-cy="extendedModel"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.gLNNumber')}
                id="one-world-sync-product-gLNNumber"
                name="gLNNumber"
                data-cy="gLNNumber"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.gTIN')}
                id="one-world-sync-product-gTIN"
                name="gTIN"
                data-cy="gTIN"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.h7')}
                id="one-world-sync-product-h7"
                name="h7"
                data-cy="h7"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.image')}
                id="one-world-sync-product-image"
                name="image"
                data-cy="image"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.ingredients')}
                id="one-world-sync-product-ingredients"
                name="ingredients"
                data-cy="ingredients"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.isActive')}
                id="one-world-sync-product-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.isApprove')}
                id="one-world-sync-product-isApprove"
                name="isApprove"
                data-cy="isApprove"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.isMerge')}
                id="one-world-sync-product-isMerge"
                name="isMerge"
                data-cy="isMerge"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.isProductSync')}
                id="one-world-sync-product-isProductSync"
                name="isProductSync"
                data-cy="isProductSync"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.manufacturer')}
                id="one-world-sync-product-manufacturer"
                name="manufacturer"
                data-cy="manufacturer"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.manufacturerId')}
                id="one-world-sync-product-manufacturerId"
                name="manufacturerId"
                data-cy="manufacturerId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.manufacturerText1Ws')}
                id="one-world-sync-product-manufacturerText1Ws"
                name="manufacturerText1Ws"
                data-cy="manufacturerText1Ws"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.modifiedOn')}
                id="one-world-sync-product-modifiedOn"
                name="modifiedOn"
                data-cy="modifiedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.productDescription')}
                id="one-world-sync-product-productDescription"
                name="productDescription"
                data-cy="productDescription"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.productId')}
                id="one-world-sync-product-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.productName')}
                id="one-world-sync-product-productName"
                name="productName"
                data-cy="productName"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.protein')}
                id="one-world-sync-product-protein"
                name="protein"
                data-cy="protein"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.proteinUom')}
                id="one-world-sync-product-proteinUom"
                name="proteinUom"
                data-cy="proteinUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.saturatedFat')}
                id="one-world-sync-product-saturatedFat"
                name="saturatedFat"
                data-cy="saturatedFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.serving')}
                id="one-world-sync-product-serving"
                name="serving"
                data-cy="serving"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.servingUom')}
                id="one-world-sync-product-servingUom"
                name="servingUom"
                data-cy="servingUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.sodium')}
                id="one-world-sync-product-sodium"
                name="sodium"
                data-cy="sodium"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.sodiumUom')}
                id="one-world-sync-product-sodiumUom"
                name="sodiumUom"
                data-cy="sodiumUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.storageTypeId')}
                id="one-world-sync-product-storageTypeId"
                name="storageTypeId"
                data-cy="storageTypeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.storageTypeName')}
                id="one-world-sync-product-storageTypeName"
                name="storageTypeName"
                data-cy="storageTypeName"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.subCategory1Name')}
                id="one-world-sync-product-subCategory1Name"
                name="subCategory1Name"
                data-cy="subCategory1Name"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.subCategory2Name')}
                id="one-world-sync-product-subCategory2Name"
                name="subCategory2Name"
                data-cy="subCategory2Name"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.sugar')}
                id="one-world-sync-product-sugar"
                name="sugar"
                data-cy="sugar"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.sugarUom')}
                id="one-world-sync-product-sugarUom"
                name="sugarUom"
                data-cy="sugarUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.syncEffective')}
                id="one-world-sync-product-syncEffective"
                name="syncEffective"
                data-cy="syncEffective"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.syncHeaderLastChange')}
                id="one-world-sync-product-syncHeaderLastChange"
                name="syncHeaderLastChange"
                data-cy="syncHeaderLastChange"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.syncItemReferenceId')}
                id="one-world-sync-product-syncItemReferenceId"
                name="syncItemReferenceId"
                data-cy="syncItemReferenceId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.syncLastChange')}
                id="one-world-sync-product-syncLastChange"
                name="syncLastChange"
                data-cy="syncLastChange"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.syncPublication')}
                id="one-world-sync-product-syncPublication"
                name="syncPublication"
                data-cy="syncPublication"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.totalFat')}
                id="one-world-sync-product-totalFat"
                name="totalFat"
                data-cy="totalFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.transFat')}
                id="one-world-sync-product-transFat"
                name="transFat"
                data-cy="transFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.uPC')}
                id="one-world-sync-product-uPC"
                name="uPC"
                data-cy="uPC"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.oneWorldSyncProduct.vendor')}
                id="one-world-sync-product-vendor"
                name="vendor"
                data-cy="vendor"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/one-world-sync-product" replace color="info">
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

export default OneWorldSyncProductUpdate;
