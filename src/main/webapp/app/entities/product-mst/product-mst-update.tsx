import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-mst.reducer';

export const ProductMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productMstEntity = useAppSelector(state => state.productMst.entity);
  const loading = useAppSelector(state => state.productMst.loading);
  const updating = useAppSelector(state => state.productMst.updating);
  const updateSuccess = useAppSelector(state => state.productMst.updateSuccess);

  const handleClose = () => {
    navigate('/product-mst');
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
    if (values.addedSugar !== undefined && typeof values.addedSugar !== 'number') {
      values.addedSugar = Number(values.addedSugar);
    }
    if (values.calories !== undefined && typeof values.calories !== 'number') {
      values.calories = Number(values.calories);
    }
    if (values.carbohydrates !== undefined && typeof values.carbohydrates !== 'number') {
      values.carbohydrates = Number(values.carbohydrates);
    }
    if (values.categoryId !== undefined && typeof values.categoryId !== 'number') {
      values.categoryId = Number(values.categoryId);
    }
    if (values.cholesterol !== undefined && typeof values.cholesterol !== 'number') {
      values.cholesterol = Number(values.cholesterol);
    }
    if (values.createdBy !== undefined && typeof values.createdBy !== 'number') {
      values.createdBy = Number(values.createdBy);
    }
    if (values.dietaryFiber !== undefined && typeof values.dietaryFiber !== 'number') {
      values.dietaryFiber = Number(values.dietaryFiber);
    }
    if (values.iOCCategoryId !== undefined && typeof values.iOCCategoryId !== 'number') {
      values.iOCCategoryId = Number(values.iOCCategoryId);
    }
    if (values.manufacturerId !== undefined && typeof values.manufacturerId !== 'number') {
      values.manufacturerId = Number(values.manufacturerId);
    }
    if (values.productId !== undefined && typeof values.productId !== 'number') {
      values.productId = Number(values.productId);
    }
    if (values.protein !== undefined && typeof values.protein !== 'number') {
      values.protein = Number(values.protein);
    }
    if (values.saturatedFat !== undefined && typeof values.saturatedFat !== 'number') {
      values.saturatedFat = Number(values.saturatedFat);
    }
    if (values.serving !== undefined && typeof values.serving !== 'number') {
      values.serving = Number(values.serving);
    }
    if (values.sodium !== undefined && typeof values.sodium !== 'number') {
      values.sodium = Number(values.sodium);
    }
    if (values.storageTypeId !== undefined && typeof values.storageTypeId !== 'number') {
      values.storageTypeId = Number(values.storageTypeId);
    }
    if (values.subCategoryId !== undefined && typeof values.subCategoryId !== 'number') {
      values.subCategoryId = Number(values.subCategoryId);
    }
    if (values.sugar !== undefined && typeof values.sugar !== 'number') {
      values.sugar = Number(values.sugar);
    }
    if (values.totalFat !== undefined && typeof values.totalFat !== 'number') {
      values.totalFat = Number(values.totalFat);
    }
    if (values.transFat !== undefined && typeof values.transFat !== 'number') {
      values.transFat = Number(values.transFat);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...productMstEntity,
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
          ...productMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productMst.home.createOrEditLabel" data-cy="ProductMstCreateUpdateHeading">
            <Translate contentKey="dboApp.productMst.home.createOrEditLabel">Create or edit a ProductMst</Translate>
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
                  id="product-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productMst.addedSugar')}
                id="product-mst-addedSugar"
                name="addedSugar"
                data-cy="addedSugar"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.addedSugarUom')}
                id="product-mst-addedSugarUom"
                name="addedSugarUom"
                data-cy="addedSugarUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.allergenKeywords')}
                id="product-mst-allergenKeywords"
                name="allergenKeywords"
                data-cy="allergenKeywords"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productMst.brandName')}
                id="product-mst-brandName"
                name="brandName"
                data-cy="brandName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.calories')}
                id="product-mst-calories"
                name="calories"
                data-cy="calories"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.caloriesUom')}
                id="product-mst-caloriesUom"
                name="caloriesUom"
                data-cy="caloriesUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.carbohydrates')}
                id="product-mst-carbohydrates"
                name="carbohydrates"
                data-cy="carbohydrates"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.carbohydratesUom')}
                id="product-mst-carbohydratesUom"
                name="carbohydratesUom"
                data-cy="carbohydratesUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.categoryId')}
                id="product-mst-categoryId"
                name="categoryId"
                data-cy="categoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.cholesterol')}
                id="product-mst-cholesterol"
                name="cholesterol"
                data-cy="cholesterol"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.cholesterolUOM')}
                id="product-mst-cholesterolUOM"
                name="cholesterolUOM"
                data-cy="cholesterolUOM"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.createdBy')}
                id="product-mst-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.createdOn')}
                id="product-mst-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productMst.description')}
                id="product-mst-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productMst.dietaryFiber')}
                id="product-mst-dietaryFiber"
                name="dietaryFiber"
                data-cy="dietaryFiber"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.dietaryFiberUom')}
                id="product-mst-dietaryFiberUom"
                name="dietaryFiberUom"
                data-cy="dietaryFiberUom"
                type="text"
              />
              <ValidatedField label={translate('dboApp.productMst.gTIN')} id="product-mst-gTIN" name="gTIN" data-cy="gTIN" type="text" />
              <ValidatedField
                label={translate('dboApp.productMst.ingredients')}
                id="product-mst-ingredients"
                name="ingredients"
                data-cy="ingredients"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productMst.iOCCategoryId')}
                id="product-mst-iOCCategoryId"
                name="iOCCategoryId"
                data-cy="iOCCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.isActive')}
                id="product-mst-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productMst.isMerge')}
                id="product-mst-isMerge"
                name="isMerge"
                data-cy="isMerge"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productMst.isOneWorldSyncProduct')}
                id="product-mst-isOneWorldSyncProduct"
                name="isOneWorldSyncProduct"
                data-cy="isOneWorldSyncProduct"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productMst.manufacturerId')}
                id="product-mst-manufacturerId"
                name="manufacturerId"
                data-cy="manufacturerId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.manufacturerProductCode')}
                id="product-mst-manufacturerProductCode"
                name="manufacturerProductCode"
                data-cy="manufacturerProductCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.manufacturerText1Ws')}
                id="product-mst-manufacturerText1Ws"
                name="manufacturerText1Ws"
                data-cy="manufacturerText1Ws"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.mergeDate')}
                id="product-mst-mergeDate"
                name="mergeDate"
                data-cy="mergeDate"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productMst.productId')}
                id="product-mst-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.productLabelPdfUrl')}
                id="product-mst-productLabelPdfUrl"
                name="productLabelPdfUrl"
                data-cy="productLabelPdfUrl"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productMst.productName')}
                id="product-mst-productName"
                name="productName"
                data-cy="productName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.protein')}
                id="product-mst-protein"
                name="protein"
                data-cy="protein"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.proteinUom')}
                id="product-mst-proteinUom"
                name="proteinUom"
                data-cy="proteinUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.saturatedFat')}
                id="product-mst-saturatedFat"
                name="saturatedFat"
                data-cy="saturatedFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.serving')}
                id="product-mst-serving"
                name="serving"
                data-cy="serving"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.servingUom')}
                id="product-mst-servingUom"
                name="servingUom"
                data-cy="servingUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.sodium')}
                id="product-mst-sodium"
                name="sodium"
                data-cy="sodium"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.sodiumUom')}
                id="product-mst-sodiumUom"
                name="sodiumUom"
                data-cy="sodiumUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.storageTypeId')}
                id="product-mst-storageTypeId"
                name="storageTypeId"
                data-cy="storageTypeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.subCategoryId')}
                id="product-mst-subCategoryId"
                name="subCategoryId"
                data-cy="subCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.sugar')}
                id="product-mst-sugar"
                name="sugar"
                data-cy="sugar"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.sugarUom')}
                id="product-mst-sugarUom"
                name="sugarUom"
                data-cy="sugarUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.totalFat')}
                id="product-mst-totalFat"
                name="totalFat"
                data-cy="totalFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.transFat')}
                id="product-mst-transFat"
                name="transFat"
                data-cy="transFat"
                type="text"
              />
              <ValidatedField label={translate('dboApp.productMst.uPC')} id="product-mst-uPC" name="uPC" data-cy="uPC" type="text" />
              <ValidatedField
                label={translate('dboApp.productMst.uPCGTIN')}
                id="product-mst-uPCGTIN"
                name="uPCGTIN"
                data-cy="uPCGTIN"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.updatedBy')}
                id="product-mst-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productMst.updatedOn')}
                id="product-mst-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productMst.vendor')}
                id="product-mst-vendor"
                name="vendor"
                data-cy="vendor"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-mst" replace color="info">
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

export default ProductMstUpdate;
