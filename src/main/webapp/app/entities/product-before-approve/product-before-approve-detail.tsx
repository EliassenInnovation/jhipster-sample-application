import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-before-approve.reducer';

export const ProductBeforeApproveDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productBeforeApproveEntity = useAppSelector(state => state.productBeforeApprove.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productBeforeApproveDetailsHeading">
          <Translate contentKey="dboApp.productBeforeApprove.detail.title">ProductBeforeApprove</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.id}</dd>
          <dt>
            <span id="addedSugar">
              <Translate contentKey="dboApp.productBeforeApprove.addedSugar">Added Sugar</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.addedSugar}</dd>
          <dt>
            <span id="addedSugarUom">
              <Translate contentKey="dboApp.productBeforeApprove.addedSugarUom">Added Sugar Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.addedSugarUom}</dd>
          <dt>
            <span id="allergenKeywords">
              <Translate contentKey="dboApp.productBeforeApprove.allergenKeywords">Allergen Keywords</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.allergenKeywords}</dd>
          <dt>
            <span id="brandName">
              <Translate contentKey="dboApp.productBeforeApprove.brandName">Brand Name</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.brandName}</dd>
          <dt>
            <span id="calories">
              <Translate contentKey="dboApp.productBeforeApprove.calories">Calories</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.calories}</dd>
          <dt>
            <span id="caloriesUom">
              <Translate contentKey="dboApp.productBeforeApprove.caloriesUom">Calories Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.caloriesUom}</dd>
          <dt>
            <span id="carbohydrates">
              <Translate contentKey="dboApp.productBeforeApprove.carbohydrates">Carbohydrates</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.carbohydrates}</dd>
          <dt>
            <span id="carbohydratesUom">
              <Translate contentKey="dboApp.productBeforeApprove.carbohydratesUom">Carbohydrates Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.carbohydratesUom}</dd>
          <dt>
            <span id="categoryId">
              <Translate contentKey="dboApp.productBeforeApprove.categoryId">Category Id</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.categoryId}</dd>
          <dt>
            <span id="cholesterol">
              <Translate contentKey="dboApp.productBeforeApprove.cholesterol">Cholesterol</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.cholesterol}</dd>
          <dt>
            <span id="cholesterolUOM">
              <Translate contentKey="dboApp.productBeforeApprove.cholesterolUOM">Cholesterol UOM</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.cholesterolUOM}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productBeforeApprove.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productBeforeApprove.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productBeforeApproveEntity.createdOn ? (
              <TextFormat value={productBeforeApproveEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="dboApp.productBeforeApprove.description">Description</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.description}</dd>
          <dt>
            <span id="dietaryFiber">
              <Translate contentKey="dboApp.productBeforeApprove.dietaryFiber">Dietary Fiber</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.dietaryFiber}</dd>
          <dt>
            <span id="dietaryFiberUom">
              <Translate contentKey="dboApp.productBeforeApprove.dietaryFiberUom">Dietary Fiber Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.dietaryFiberUom}</dd>
          <dt>
            <span id="distributorId">
              <Translate contentKey="dboApp.productBeforeApprove.distributorId">Distributor Id</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.distributorId}</dd>
          <dt>
            <span id="gTIN">
              <Translate contentKey="dboApp.productBeforeApprove.gTIN">G TIN</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.gTIN}</dd>
          <dt>
            <span id="ingredients">
              <Translate contentKey="dboApp.productBeforeApprove.ingredients">Ingredients</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.ingredients}</dd>
          <dt>
            <span id="iocCategoryId">
              <Translate contentKey="dboApp.productBeforeApprove.iocCategoryId">Ioc Category Id</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.iocCategoryId}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productBeforeApprove.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isMerge">
              <Translate contentKey="dboApp.productBeforeApprove.isMerge">Is Merge</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.isMerge ? 'true' : 'false'}</dd>
          <dt>
            <span id="manufacturerId">
              <Translate contentKey="dboApp.productBeforeApprove.manufacturerId">Manufacturer Id</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.manufacturerId}</dd>
          <dt>
            <span id="manufacturerProductCode">
              <Translate contentKey="dboApp.productBeforeApprove.manufacturerProductCode">Manufacturer Product Code</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.manufacturerProductCode}</dd>
          <dt>
            <span id="mergeDate">
              <Translate contentKey="dboApp.productBeforeApprove.mergeDate">Merge Date</Translate>
            </span>
          </dt>
          <dd>
            {productBeforeApproveEntity.mergeDate ? (
              <TextFormat value={productBeforeApproveEntity.mergeDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productBeforeApprove.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.productId}</dd>
          <dt>
            <span id="productLabelPdfUrl">
              <Translate contentKey="dboApp.productBeforeApprove.productLabelPdfUrl">Product Label Pdf Url</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.productLabelPdfUrl}</dd>
          <dt>
            <span id="productName">
              <Translate contentKey="dboApp.productBeforeApprove.productName">Product Name</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.productName}</dd>
          <dt>
            <span id="protein">
              <Translate contentKey="dboApp.productBeforeApprove.protein">Protein</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.protein}</dd>
          <dt>
            <span id="proteinUom">
              <Translate contentKey="dboApp.productBeforeApprove.proteinUom">Protein Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.proteinUom}</dd>
          <dt>
            <span id="saturatedFat">
              <Translate contentKey="dboApp.productBeforeApprove.saturatedFat">Saturated Fat</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.saturatedFat}</dd>
          <dt>
            <span id="serving">
              <Translate contentKey="dboApp.productBeforeApprove.serving">Serving</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.serving}</dd>
          <dt>
            <span id="servingUom">
              <Translate contentKey="dboApp.productBeforeApprove.servingUom">Serving Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.servingUom}</dd>
          <dt>
            <span id="sodium">
              <Translate contentKey="dboApp.productBeforeApprove.sodium">Sodium</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.sodium}</dd>
          <dt>
            <span id="sodiumUom">
              <Translate contentKey="dboApp.productBeforeApprove.sodiumUom">Sodium Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.sodiumUom}</dd>
          <dt>
            <span id="storageTypeId">
              <Translate contentKey="dboApp.productBeforeApprove.storageTypeId">Storage Type Id</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.storageTypeId}</dd>
          <dt>
            <span id="subCategoryId">
              <Translate contentKey="dboApp.productBeforeApprove.subCategoryId">Sub Category Id</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.subCategoryId}</dd>
          <dt>
            <span id="sugar">
              <Translate contentKey="dboApp.productBeforeApprove.sugar">Sugar</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.sugar}</dd>
          <dt>
            <span id="sugarUom">
              <Translate contentKey="dboApp.productBeforeApprove.sugarUom">Sugar Uom</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.sugarUom}</dd>
          <dt>
            <span id="totalFat">
              <Translate contentKey="dboApp.productBeforeApprove.totalFat">Total Fat</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.totalFat}</dd>
          <dt>
            <span id="transFat">
              <Translate contentKey="dboApp.productBeforeApprove.transFat">Trans Fat</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.transFat}</dd>
          <dt>
            <span id="uPC">
              <Translate contentKey="dboApp.productBeforeApprove.uPC">U PC</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.uPC}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productBeforeApprove.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productBeforeApprove.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productBeforeApproveEntity.updatedOn ? (
              <TextFormat value={productBeforeApproveEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="vendor">
              <Translate contentKey="dboApp.productBeforeApprove.vendor">Vendor</Translate>
            </span>
          </dt>
          <dd>{productBeforeApproveEntity.vendor}</dd>
        </dl>
        <Button tag={Link} to="/product-before-approve" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-before-approve/${productBeforeApproveEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductBeforeApproveDetail;
