import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './one-world-sync-product.reducer';

export const OneWorldSyncProductDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const oneWorldSyncProductEntity = useAppSelector(state => state.oneWorldSyncProduct.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="oneWorldSyncProductDetailsHeading">
          <Translate contentKey="dboApp.oneWorldSyncProduct.detail.title">OneWorldSyncProduct</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.id}</dd>
          <dt>
            <span id="addedSugars">
              <Translate contentKey="dboApp.oneWorldSyncProduct.addedSugars">Added Sugars</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.addedSugars}</dd>
          <dt>
            <span id="addedSugarUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.addedSugarUom">Added Sugar Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.addedSugarUom}</dd>
          <dt>
            <span id="allergenKeyword">
              <Translate contentKey="dboApp.oneWorldSyncProduct.allergenKeyword">Allergen Keyword</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.allergenKeyword}</dd>
          <dt>
            <span id="allergens">
              <Translate contentKey="dboApp.oneWorldSyncProduct.allergens">Allergens</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.allergens}</dd>
          <dt>
            <span id="brandName">
              <Translate contentKey="dboApp.oneWorldSyncProduct.brandName">Brand Name</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.brandName}</dd>
          <dt>
            <span id="calories">
              <Translate contentKey="dboApp.oneWorldSyncProduct.calories">Calories</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.calories}</dd>
          <dt>
            <span id="caloriesUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.caloriesUom">Calories Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.caloriesUom}</dd>
          <dt>
            <span id="carbohydrates">
              <Translate contentKey="dboApp.oneWorldSyncProduct.carbohydrates">Carbohydrates</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.carbohydrates}</dd>
          <dt>
            <span id="carbohydratesUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.carbohydratesUom">Carbohydrates Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.carbohydratesUom}</dd>
          <dt>
            <span id="categoryName">
              <Translate contentKey="dboApp.oneWorldSyncProduct.categoryName">Category Name</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.categoryName}</dd>
          <dt>
            <span id="cholesterol">
              <Translate contentKey="dboApp.oneWorldSyncProduct.cholesterol">Cholesterol</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.cholesterol}</dd>
          <dt>
            <span id="cholesterolUOM">
              <Translate contentKey="dboApp.oneWorldSyncProduct.cholesterolUOM">Cholesterol UOM</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.cholesterolUOM}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.oneWorldSyncProduct.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {oneWorldSyncProductEntity.createdOn ? (
              <TextFormat value={oneWorldSyncProductEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="dataForm">
              <Translate contentKey="dboApp.oneWorldSyncProduct.dataForm">Data Form</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.dataForm}</dd>
          <dt>
            <span id="dietaryFiber">
              <Translate contentKey="dboApp.oneWorldSyncProduct.dietaryFiber">Dietary Fiber</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.dietaryFiber}</dd>
          <dt>
            <span id="dietaryFiberUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.dietaryFiberUom">Dietary Fiber Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.dietaryFiberUom}</dd>
          <dt>
            <span id="distributor">
              <Translate contentKey="dboApp.oneWorldSyncProduct.distributor">Distributor</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.distributor}</dd>
          <dt>
            <span id="doNotConsiderProduct">
              <Translate contentKey="dboApp.oneWorldSyncProduct.doNotConsiderProduct">Do Not Consider Product</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.doNotConsiderProduct ? 'true' : 'false'}</dd>
          <dt>
            <span id="extendedModel">
              <Translate contentKey="dboApp.oneWorldSyncProduct.extendedModel">Extended Model</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.extendedModel}</dd>
          <dt>
            <span id="gLNNumber">
              <Translate contentKey="dboApp.oneWorldSyncProduct.gLNNumber">G LN Number</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.gLNNumber}</dd>
          <dt>
            <span id="gTIN">
              <Translate contentKey="dboApp.oneWorldSyncProduct.gTIN">G TIN</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.gTIN}</dd>
          <dt>
            <span id="h7">
              <Translate contentKey="dboApp.oneWorldSyncProduct.h7">H 7</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.h7}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="dboApp.oneWorldSyncProduct.image">Image</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.image}</dd>
          <dt>
            <span id="ingredients">
              <Translate contentKey="dboApp.oneWorldSyncProduct.ingredients">Ingredients</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.ingredients}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.oneWorldSyncProduct.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isApprove">
              <Translate contentKey="dboApp.oneWorldSyncProduct.isApprove">Is Approve</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.isApprove ? 'true' : 'false'}</dd>
          <dt>
            <span id="isMerge">
              <Translate contentKey="dboApp.oneWorldSyncProduct.isMerge">Is Merge</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.isMerge ? 'true' : 'false'}</dd>
          <dt>
            <span id="isProductSync">
              <Translate contentKey="dboApp.oneWorldSyncProduct.isProductSync">Is Product Sync</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.isProductSync ? 'true' : 'false'}</dd>
          <dt>
            <span id="manufacturer">
              <Translate contentKey="dboApp.oneWorldSyncProduct.manufacturer">Manufacturer</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.manufacturer}</dd>
          <dt>
            <span id="manufacturerId">
              <Translate contentKey="dboApp.oneWorldSyncProduct.manufacturerId">Manufacturer Id</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.manufacturerId}</dd>
          <dt>
            <span id="manufacturerText1Ws">
              <Translate contentKey="dboApp.oneWorldSyncProduct.manufacturerText1Ws">Manufacturer Text 1 Ws</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.manufacturerText1Ws}</dd>
          <dt>
            <span id="modifiedOn">
              <Translate contentKey="dboApp.oneWorldSyncProduct.modifiedOn">Modified On</Translate>
            </span>
          </dt>
          <dd>
            {oneWorldSyncProductEntity.modifiedOn ? (
              <TextFormat value={oneWorldSyncProductEntity.modifiedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="productDescription">
              <Translate contentKey="dboApp.oneWorldSyncProduct.productDescription">Product Description</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.productDescription}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.oneWorldSyncProduct.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.productId}</dd>
          <dt>
            <span id="productName">
              <Translate contentKey="dboApp.oneWorldSyncProduct.productName">Product Name</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.productName}</dd>
          <dt>
            <span id="protein">
              <Translate contentKey="dboApp.oneWorldSyncProduct.protein">Protein</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.protein}</dd>
          <dt>
            <span id="proteinUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.proteinUom">Protein Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.proteinUom}</dd>
          <dt>
            <span id="saturatedFat">
              <Translate contentKey="dboApp.oneWorldSyncProduct.saturatedFat">Saturated Fat</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.saturatedFat}</dd>
          <dt>
            <span id="serving">
              <Translate contentKey="dboApp.oneWorldSyncProduct.serving">Serving</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.serving}</dd>
          <dt>
            <span id="servingUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.servingUom">Serving Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.servingUom}</dd>
          <dt>
            <span id="sodium">
              <Translate contentKey="dboApp.oneWorldSyncProduct.sodium">Sodium</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.sodium}</dd>
          <dt>
            <span id="sodiumUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.sodiumUom">Sodium Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.sodiumUom}</dd>
          <dt>
            <span id="storageTypeId">
              <Translate contentKey="dboApp.oneWorldSyncProduct.storageTypeId">Storage Type Id</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.storageTypeId}</dd>
          <dt>
            <span id="storageTypeName">
              <Translate contentKey="dboApp.oneWorldSyncProduct.storageTypeName">Storage Type Name</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.storageTypeName}</dd>
          <dt>
            <span id="subCategory1Name">
              <Translate contentKey="dboApp.oneWorldSyncProduct.subCategory1Name">Sub Category 1 Name</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.subCategory1Name}</dd>
          <dt>
            <span id="subCategory2Name">
              <Translate contentKey="dboApp.oneWorldSyncProduct.subCategory2Name">Sub Category 2 Name</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.subCategory2Name}</dd>
          <dt>
            <span id="sugar">
              <Translate contentKey="dboApp.oneWorldSyncProduct.sugar">Sugar</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.sugar}</dd>
          <dt>
            <span id="sugarUom">
              <Translate contentKey="dboApp.oneWorldSyncProduct.sugarUom">Sugar Uom</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.sugarUom}</dd>
          <dt>
            <span id="syncEffective">
              <Translate contentKey="dboApp.oneWorldSyncProduct.syncEffective">Sync Effective</Translate>
            </span>
          </dt>
          <dd>
            {oneWorldSyncProductEntity.syncEffective ? (
              <TextFormat value={oneWorldSyncProductEntity.syncEffective} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="syncHeaderLastChange">
              <Translate contentKey="dboApp.oneWorldSyncProduct.syncHeaderLastChange">Sync Header Last Change</Translate>
            </span>
          </dt>
          <dd>
            {oneWorldSyncProductEntity.syncHeaderLastChange ? (
              <TextFormat value={oneWorldSyncProductEntity.syncHeaderLastChange} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="syncItemReferenceId">
              <Translate contentKey="dboApp.oneWorldSyncProduct.syncItemReferenceId">Sync Item Reference Id</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.syncItemReferenceId}</dd>
          <dt>
            <span id="syncLastChange">
              <Translate contentKey="dboApp.oneWorldSyncProduct.syncLastChange">Sync Last Change</Translate>
            </span>
          </dt>
          <dd>
            {oneWorldSyncProductEntity.syncLastChange ? (
              <TextFormat value={oneWorldSyncProductEntity.syncLastChange} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="syncPublication">
              <Translate contentKey="dboApp.oneWorldSyncProduct.syncPublication">Sync Publication</Translate>
            </span>
          </dt>
          <dd>
            {oneWorldSyncProductEntity.syncPublication ? (
              <TextFormat value={oneWorldSyncProductEntity.syncPublication} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="totalFat">
              <Translate contentKey="dboApp.oneWorldSyncProduct.totalFat">Total Fat</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.totalFat}</dd>
          <dt>
            <span id="transFat">
              <Translate contentKey="dboApp.oneWorldSyncProduct.transFat">Trans Fat</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.transFat}</dd>
          <dt>
            <span id="uPC">
              <Translate contentKey="dboApp.oneWorldSyncProduct.uPC">U PC</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.uPC}</dd>
          <dt>
            <span id="vendor">
              <Translate contentKey="dboApp.oneWorldSyncProduct.vendor">Vendor</Translate>
            </span>
          </dt>
          <dd>{oneWorldSyncProductEntity.vendor}</dd>
        </dl>
        <Button tag={Link} to="/one-world-sync-product" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/one-world-sync-product/${oneWorldSyncProductEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OneWorldSyncProductDetail;
