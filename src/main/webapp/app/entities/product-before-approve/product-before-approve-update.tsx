import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './product-before-approve.reducer';

export const ProductBeforeApproveUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const productBeforeApproveEntity = useAppSelector(state => state.productBeforeApprove.entity);
  const loading = useAppSelector(state => state.productBeforeApprove.loading);
  const updating = useAppSelector(state => state.productBeforeApprove.updating);
  const updateSuccess = useAppSelector(state => state.productBeforeApprove.updateSuccess);

  const handleClose = () => {
    navigate('/product-before-approve');
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
    if (values.iocCategoryId !== undefined && typeof values.iocCategoryId !== 'number') {
      values.iocCategoryId = Number(values.iocCategoryId);
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
      ...productBeforeApproveEntity,
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
          ...productBeforeApproveEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.productBeforeApprove.home.createOrEditLabel" data-cy="ProductBeforeApproveCreateUpdateHeading">
            <Translate contentKey="dboApp.productBeforeApprove.home.createOrEditLabel">Create or edit a ProductBeforeApprove</Translate>
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
                  id="product-before-approve-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.addedSugar')}
                id="product-before-approve-addedSugar"
                name="addedSugar"
                data-cy="addedSugar"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.addedSugarUom')}
                id="product-before-approve-addedSugarUom"
                name="addedSugarUom"
                data-cy="addedSugarUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.allergenKeywords')}
                id="product-before-approve-allergenKeywords"
                name="allergenKeywords"
                data-cy="allergenKeywords"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.brandName')}
                id="product-before-approve-brandName"
                name="brandName"
                data-cy="brandName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.calories')}
                id="product-before-approve-calories"
                name="calories"
                data-cy="calories"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.caloriesUom')}
                id="product-before-approve-caloriesUom"
                name="caloriesUom"
                data-cy="caloriesUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.carbohydrates')}
                id="product-before-approve-carbohydrates"
                name="carbohydrates"
                data-cy="carbohydrates"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.carbohydratesUom')}
                id="product-before-approve-carbohydratesUom"
                name="carbohydratesUom"
                data-cy="carbohydratesUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.categoryId')}
                id="product-before-approve-categoryId"
                name="categoryId"
                data-cy="categoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.cholesterol')}
                id="product-before-approve-cholesterol"
                name="cholesterol"
                data-cy="cholesterol"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.cholesterolUOM')}
                id="product-before-approve-cholesterolUOM"
                name="cholesterolUOM"
                data-cy="cholesterolUOM"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.createdBy')}
                id="product-before-approve-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.createdOn')}
                id="product-before-approve-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.description')}
                id="product-before-approve-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.dietaryFiber')}
                id="product-before-approve-dietaryFiber"
                name="dietaryFiber"
                data-cy="dietaryFiber"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.dietaryFiberUom')}
                id="product-before-approve-dietaryFiberUom"
                name="dietaryFiberUom"
                data-cy="dietaryFiberUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.distributorId')}
                id="product-before-approve-distributorId"
                name="distributorId"
                data-cy="distributorId"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.gTIN')}
                id="product-before-approve-gTIN"
                name="gTIN"
                data-cy="gTIN"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.ingredients')}
                id="product-before-approve-ingredients"
                name="ingredients"
                data-cy="ingredients"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.iocCategoryId')}
                id="product-before-approve-iocCategoryId"
                name="iocCategoryId"
                data-cy="iocCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.isActive')}
                id="product-before-approve-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.isMerge')}
                id="product-before-approve-isMerge"
                name="isMerge"
                data-cy="isMerge"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.manufacturerId')}
                id="product-before-approve-manufacturerId"
                name="manufacturerId"
                data-cy="manufacturerId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.manufacturerProductCode')}
                id="product-before-approve-manufacturerProductCode"
                name="manufacturerProductCode"
                data-cy="manufacturerProductCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.mergeDate')}
                id="product-before-approve-mergeDate"
                name="mergeDate"
                data-cy="mergeDate"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.productId')}
                id="product-before-approve-productId"
                name="productId"
                data-cy="productId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.productLabelPdfUrl')}
                id="product-before-approve-productLabelPdfUrl"
                name="productLabelPdfUrl"
                data-cy="productLabelPdfUrl"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.productName')}
                id="product-before-approve-productName"
                name="productName"
                data-cy="productName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.protein')}
                id="product-before-approve-protein"
                name="protein"
                data-cy="protein"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.proteinUom')}
                id="product-before-approve-proteinUom"
                name="proteinUom"
                data-cy="proteinUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.saturatedFat')}
                id="product-before-approve-saturatedFat"
                name="saturatedFat"
                data-cy="saturatedFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.serving')}
                id="product-before-approve-serving"
                name="serving"
                data-cy="serving"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.servingUom')}
                id="product-before-approve-servingUom"
                name="servingUom"
                data-cy="servingUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.sodium')}
                id="product-before-approve-sodium"
                name="sodium"
                data-cy="sodium"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.sodiumUom')}
                id="product-before-approve-sodiumUom"
                name="sodiumUom"
                data-cy="sodiumUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.storageTypeId')}
                id="product-before-approve-storageTypeId"
                name="storageTypeId"
                data-cy="storageTypeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.subCategoryId')}
                id="product-before-approve-subCategoryId"
                name="subCategoryId"
                data-cy="subCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.sugar')}
                id="product-before-approve-sugar"
                name="sugar"
                data-cy="sugar"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.sugarUom')}
                id="product-before-approve-sugarUom"
                name="sugarUom"
                data-cy="sugarUom"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.totalFat')}
                id="product-before-approve-totalFat"
                name="totalFat"
                data-cy="totalFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.transFat')}
                id="product-before-approve-transFat"
                name="transFat"
                data-cy="transFat"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.uPC')}
                id="product-before-approve-uPC"
                name="uPC"
                data-cy="uPC"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.updatedBy')}
                id="product-before-approve-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.updatedOn')}
                id="product-before-approve-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.productBeforeApprove.vendor')}
                id="product-before-approve-vendor"
                name="vendor"
                data-cy="vendor"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/product-before-approve" replace color="info">
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

export default ProductBeforeApproveUpdate;
