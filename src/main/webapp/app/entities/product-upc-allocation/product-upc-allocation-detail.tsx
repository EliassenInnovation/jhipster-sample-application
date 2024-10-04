import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-upc-allocation.reducer';

export const ProductUpcAllocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productUpcAllocationEntity = useAppSelector(state => state.productUpcAllocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productUpcAllocationDetailsHeading">
          <Translate contentKey="dboApp.productUpcAllocation.detail.title">ProductUpcAllocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productUpcAllocationEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productUpcAllocation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productUpcAllocationEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productUpcAllocation.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productUpcAllocationEntity.createdOn ? (
              <TextFormat value={productUpcAllocationEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productUpcAllocation.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productUpcAllocationEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productUpcAllocation.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productUpcAllocationEntity.productId}</dd>
          <dt>
            <span id="productUpcId">
              <Translate contentKey="dboApp.productUpcAllocation.productUpcId">Product Upc Id</Translate>
            </span>
          </dt>
          <dd>{productUpcAllocationEntity.productUpcId}</dd>
          <dt>
            <span id="uPC">
              <Translate contentKey="dboApp.productUpcAllocation.uPC">U PC</Translate>
            </span>
          </dt>
          <dd>{productUpcAllocationEntity.uPC}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productUpcAllocation.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productUpcAllocationEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productUpcAllocation.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productUpcAllocationEntity.updatedOn ? (
              <TextFormat value={productUpcAllocationEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-upc-allocation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-upc-allocation/${productUpcAllocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductUpcAllocationDetail;
