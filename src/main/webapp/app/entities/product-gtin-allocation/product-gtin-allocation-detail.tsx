import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-gtin-allocation.reducer';

export const ProductGtinAllocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productGtinAllocationEntity = useAppSelector(state => state.productGtinAllocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productGtinAllocationDetailsHeading">
          <Translate contentKey="dboApp.productGtinAllocation.detail.title">ProductGtinAllocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productGtinAllocationEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productGtinAllocation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productGtinAllocationEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productGtinAllocation.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productGtinAllocationEntity.createdOn ? (
              <TextFormat value={productGtinAllocationEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="gTIN">
              <Translate contentKey="dboApp.productGtinAllocation.gTIN">G TIN</Translate>
            </span>
          </dt>
          <dd>{productGtinAllocationEntity.gTIN}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productGtinAllocation.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productGtinAllocationEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="productGtinId">
              <Translate contentKey="dboApp.productGtinAllocation.productGtinId">Product Gtin Id</Translate>
            </span>
          </dt>
          <dd>{productGtinAllocationEntity.productGtinId}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productGtinAllocation.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productGtinAllocationEntity.productId}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productGtinAllocation.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productGtinAllocationEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productGtinAllocation.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productGtinAllocationEntity.updatedOn ? (
              <TextFormat value={productGtinAllocationEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-gtin-allocation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-gtin-allocation/${productGtinAllocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductGtinAllocationDetail;
