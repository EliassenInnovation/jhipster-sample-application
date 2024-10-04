import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-mst.reducer';

export const ProductMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productMstEntity = useAppSelector(state => state.productMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productMstDetailsHeading">
          <Translate contentKey="dboApp.productMst.detail.title">ProductMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.id}</dd>
          <dt>
            <span id="addedSugar">
              <Translate contentKey="dboApp.productMst.addedSugar">Added Sugar</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.addedSugar}</dd>
          <dt>
            <span id="addedSugarUom">
              <Translate contentKey="dboApp.productMst.addedSugarUom">Added Sugar Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.addedSugarUom}</dd>
          <dt>
            <span id="allergenKeywords">
              <Translate contentKey="dboApp.productMst.allergenKeywords">Allergen Keywords</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.allergenKeywords}</dd>
          <dt>
            <span id="brandName">
              <Translate contentKey="dboApp.productMst.brandName">Brand Name</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.brandName}</dd>
          <dt>
            <span id="calories">
              <Translate contentKey="dboApp.productMst.calories">Calories</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.calories}</dd>
          <dt>
            <span id="caloriesUom">
              <Translate contentKey="dboApp.productMst.caloriesUom">Calories Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.caloriesUom}</dd>
          <dt>
            <span id="carbohydrates">
              <Translate contentKey="dboApp.productMst.carbohydrates">Carbohydrates</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.carbohydrates}</dd>
          <dt>
            <span id="carbohydratesUom">
              <Translate contentKey="dboApp.productMst.carbohydratesUom">Carbohydrates Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.carbohydratesUom}</dd>
          <dt>
            <span id="categoryId">
              <Translate contentKey="dboApp.productMst.categoryId">Category Id</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.categoryId}</dd>
          <dt>
            <span id="cholesterol">
              <Translate contentKey="dboApp.productMst.cholesterol">Cholesterol</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.cholesterol}</dd>
          <dt>
            <span id="cholesterolUOM">
              <Translate contentKey="dboApp.productMst.cholesterolUOM">Cholesterol UOM</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.cholesterolUOM}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productMst.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productMst.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productMstEntity.createdOn ? (
              <TextFormat value={productMstEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="dboApp.productMst.description">Description</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.description}</dd>
          <dt>
            <span id="dietaryFiber">
              <Translate contentKey="dboApp.productMst.dietaryFiber">Dietary Fiber</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.dietaryFiber}</dd>
          <dt>
            <span id="dietaryFiberUom">
              <Translate contentKey="dboApp.productMst.dietaryFiberUom">Dietary Fiber Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.dietaryFiberUom}</dd>
          <dt>
            <span id="gTIN">
              <Translate contentKey="dboApp.productMst.gTIN">G TIN</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.gTIN}</dd>
          <dt>
            <span id="ingredients">
              <Translate contentKey="dboApp.productMst.ingredients">Ingredients</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.ingredients}</dd>
          <dt>
            <span id="iOCCategoryId">
              <Translate contentKey="dboApp.productMst.iOCCategoryId">I OC Category Id</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.iOCCategoryId}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productMst.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isMerge">
              <Translate contentKey="dboApp.productMst.isMerge">Is Merge</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.isMerge ? 'true' : 'false'}</dd>
          <dt>
            <span id="isOneWorldSyncProduct">
              <Translate contentKey="dboApp.productMst.isOneWorldSyncProduct">Is One World Sync Product</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.isOneWorldSyncProduct ? 'true' : 'false'}</dd>
          <dt>
            <span id="manufacturerId">
              <Translate contentKey="dboApp.productMst.manufacturerId">Manufacturer Id</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.manufacturerId}</dd>
          <dt>
            <span id="manufacturerProductCode">
              <Translate contentKey="dboApp.productMst.manufacturerProductCode">Manufacturer Product Code</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.manufacturerProductCode}</dd>
          <dt>
            <span id="manufacturerText1Ws">
              <Translate contentKey="dboApp.productMst.manufacturerText1Ws">Manufacturer Text 1 Ws</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.manufacturerText1Ws}</dd>
          <dt>
            <span id="mergeDate">
              <Translate contentKey="dboApp.productMst.mergeDate">Merge Date</Translate>
            </span>
          </dt>
          <dd>
            {productMstEntity.mergeDate ? (
              <TextFormat value={productMstEntity.mergeDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productMst.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.productId}</dd>
          <dt>
            <span id="productLabelPdfUrl">
              <Translate contentKey="dboApp.productMst.productLabelPdfUrl">Product Label Pdf Url</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.productLabelPdfUrl}</dd>
          <dt>
            <span id="productName">
              <Translate contentKey="dboApp.productMst.productName">Product Name</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.productName}</dd>
          <dt>
            <span id="protein">
              <Translate contentKey="dboApp.productMst.protein">Protein</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.protein}</dd>
          <dt>
            <span id="proteinUom">
              <Translate contentKey="dboApp.productMst.proteinUom">Protein Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.proteinUom}</dd>
          <dt>
            <span id="saturatedFat">
              <Translate contentKey="dboApp.productMst.saturatedFat">Saturated Fat</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.saturatedFat}</dd>
          <dt>
            <span id="serving">
              <Translate contentKey="dboApp.productMst.serving">Serving</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.serving}</dd>
          <dt>
            <span id="servingUom">
              <Translate contentKey="dboApp.productMst.servingUom">Serving Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.servingUom}</dd>
          <dt>
            <span id="sodium">
              <Translate contentKey="dboApp.productMst.sodium">Sodium</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.sodium}</dd>
          <dt>
            <span id="sodiumUom">
              <Translate contentKey="dboApp.productMst.sodiumUom">Sodium Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.sodiumUom}</dd>
          <dt>
            <span id="storageTypeId">
              <Translate contentKey="dboApp.productMst.storageTypeId">Storage Type Id</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.storageTypeId}</dd>
          <dt>
            <span id="subCategoryId">
              <Translate contentKey="dboApp.productMst.subCategoryId">Sub Category Id</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.subCategoryId}</dd>
          <dt>
            <span id="sugar">
              <Translate contentKey="dboApp.productMst.sugar">Sugar</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.sugar}</dd>
          <dt>
            <span id="sugarUom">
              <Translate contentKey="dboApp.productMst.sugarUom">Sugar Uom</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.sugarUom}</dd>
          <dt>
            <span id="totalFat">
              <Translate contentKey="dboApp.productMst.totalFat">Total Fat</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.totalFat}</dd>
          <dt>
            <span id="transFat">
              <Translate contentKey="dboApp.productMst.transFat">Trans Fat</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.transFat}</dd>
          <dt>
            <span id="uPC">
              <Translate contentKey="dboApp.productMst.uPC">U PC</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.uPC}</dd>
          <dt>
            <span id="uPCGTIN">
              <Translate contentKey="dboApp.productMst.uPCGTIN">U PCGTIN</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.uPCGTIN}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productMst.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productMst.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productMstEntity.updatedOn ? (
              <TextFormat value={productMstEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="vendor">
              <Translate contentKey="dboApp.productMst.vendor">Vendor</Translate>
            </span>
          </dt>
          <dd>{productMstEntity.vendor}</dd>
        </dl>
        <Button tag={Link} to="/product-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-mst/${productMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductMstDetail;
