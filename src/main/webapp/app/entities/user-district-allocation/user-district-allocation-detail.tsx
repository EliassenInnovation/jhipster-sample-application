import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-district-allocation.reducer';

export const UserDistrictAllocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userDistrictAllocationEntity = useAppSelector(state => state.userDistrictAllocation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userDistrictAllocationDetailsHeading">
          <Translate contentKey="dboApp.userDistrictAllocation.detail.title">UserDistrictAllocation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userDistrictAllocationEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.userDistrictAllocation.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{userDistrictAllocationEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.userDistrictAllocation.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {userDistrictAllocationEntity.createdOn ? (
              <TextFormat value={userDistrictAllocationEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isAllocated">
              <Translate contentKey="dboApp.userDistrictAllocation.isAllocated">Is Allocated</Translate>
            </span>
          </dt>
          <dd>{userDistrictAllocationEntity.isAllocated ? 'true' : 'false'}</dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.userDistrictAllocation.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{userDistrictAllocationEntity.schoolDistrictId}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.userDistrictAllocation.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{userDistrictAllocationEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.userDistrictAllocation.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {userDistrictAllocationEntity.updatedOn ? (
              <TextFormat value={userDistrictAllocationEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="userDistrictAllocationId">
              <Translate contentKey="dboApp.userDistrictAllocation.userDistrictAllocationId">User District Allocation Id</Translate>
            </span>
          </dt>
          <dd>{userDistrictAllocationEntity.userDistrictAllocationId}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dboApp.userDistrictAllocation.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{userDistrictAllocationEntity.userId}</dd>
        </dl>
        <Button tag={Link} to="/user-district-allocation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-district-allocation/${userDistrictAllocationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserDistrictAllocationDetail;
