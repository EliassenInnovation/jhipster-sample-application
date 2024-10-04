import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-manufacturer-allocation.reducer';

export const ProductManufacturerAllocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productManufacturerAllocationEntity = useAppSelector(state => state.productManufacturerAllocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productManufacturerAllocationDetailsHeading">
          <Translate contentKey="dboApp.productManufacturerAllocation.detail.title">ProductManufacturerAllocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productManufacturerAllocationEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productManufacturerAllocation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productManufacturerAllocationEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productManufacturerAllocation.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productManufacturerAllocationEntity.createdOn ? (
              <TextFormat value={productManufacturerAllocationEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isAllocated">
              <Translate contentKey="dboApp.productManufacturerAllocation.isAllocated">Is Allocated</Translate>
            </span>
          </dt>
          <dd>{productManufacturerAllocationEntity.isAllocated ? 'true' : 'false'}</dd>
          <dt>
            <span id="manufactureId">
              <Translate contentKey="dboApp.productManufacturerAllocation.manufactureId">Manufacture Id</Translate>
            </span>
          </dt>
          <dd>{productManufacturerAllocationEntity.manufactureId}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productManufacturerAllocation.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productManufacturerAllocationEntity.productId}</dd>
          <dt>
            <span id="productManufacturerAllocationId">
              <Translate contentKey="dboApp.productManufacturerAllocation.productManufacturerAllocationId">
                Product Manufacturer Allocation Id
              </Translate>
            </span>
          </dt>
          <dd>{productManufacturerAllocationEntity.productManufacturerAllocationId}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productManufacturerAllocation.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productManufacturerAllocationEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productManufacturerAllocation.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productManufacturerAllocationEntity.updatedOn ? (
              <TextFormat value={productManufacturerAllocationEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-manufacturer-allocation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-manufacturer-allocation/${productManufacturerAllocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductManufacturerAllocationDetail;
