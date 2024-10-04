import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-distributor-allocation.reducer';

export const ProductDistributorAllocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productDistributorAllocationEntity = useAppSelector(state => state.productDistributorAllocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDistributorAllocationDetailsHeading">
          <Translate contentKey="dboApp.productDistributorAllocation.detail.title">ProductDistributorAllocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productDistributorAllocationEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productDistributorAllocation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productDistributorAllocationEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productDistributorAllocation.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productDistributorAllocationEntity.createdOn ? (
              <TextFormat value={productDistributorAllocationEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="distributorId">
              <Translate contentKey="dboApp.productDistributorAllocation.distributorId">Distributor Id</Translate>
            </span>
          </dt>
          <dd>{productDistributorAllocationEntity.distributorId}</dd>
          <dt>
            <span id="isAllocated">
              <Translate contentKey="dboApp.productDistributorAllocation.isAllocated">Is Allocated</Translate>
            </span>
          </dt>
          <dd>{productDistributorAllocationEntity.isAllocated ? 'true' : 'false'}</dd>
          <dt>
            <span id="productDistributorAllocationId">
              <Translate contentKey="dboApp.productDistributorAllocation.productDistributorAllocationId">
                Product Distributor Allocation Id
              </Translate>
            </span>
          </dt>
          <dd>{productDistributorAllocationEntity.productDistributorAllocationId}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productDistributorAllocation.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productDistributorAllocationEntity.productId}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productDistributorAllocation.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productDistributorAllocationEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productDistributorAllocation.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productDistributorAllocationEntity.updatedOn ? (
              <TextFormat value={productDistributorAllocationEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-distributor-allocation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-distributor-allocation/${productDistributorAllocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDistributorAllocationDetail;
