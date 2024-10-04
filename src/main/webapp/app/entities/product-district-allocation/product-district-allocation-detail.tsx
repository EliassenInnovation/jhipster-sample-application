import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-district-allocation.reducer';

export const ProductDistrictAllocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productDistrictAllocationEntity = useAppSelector(state => state.productDistrictAllocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productDistrictAllocationDetailsHeading">
          <Translate contentKey="dboApp.productDistrictAllocation.detail.title">ProductDistrictAllocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productDistrictAllocationEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productDistrictAllocation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productDistrictAllocationEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productDistrictAllocation.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productDistrictAllocationEntity.createdOn ? (
              <TextFormat value={productDistrictAllocationEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isAllocated">
              <Translate contentKey="dboApp.productDistrictAllocation.isAllocated">Is Allocated</Translate>
            </span>
          </dt>
          <dd>{productDistrictAllocationEntity.isAllocated ? 'true' : 'false'}</dd>
          <dt>
            <span id="productDistrictAllocationId">
              <Translate contentKey="dboApp.productDistrictAllocation.productDistrictAllocationId">
                Product District Allocation Id
              </Translate>
            </span>
          </dt>
          <dd>{productDistrictAllocationEntity.productDistrictAllocationId}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productDistrictAllocation.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productDistrictAllocationEntity.productId}</dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.productDistrictAllocation.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{productDistrictAllocationEntity.schoolDistrictId}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productDistrictAllocation.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productDistrictAllocationEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productDistrictAllocation.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productDistrictAllocationEntity.updatedOn ? (
              <TextFormat value={productDistrictAllocationEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-district-allocation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-district-allocation/${productDistrictAllocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductDistrictAllocationDetail;
