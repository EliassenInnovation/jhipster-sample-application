import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-mst.reducer';

export const UserMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userMstEntity = useAppSelector(state => state.userMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userMstDetailsHeading">
          <Translate contentKey="dboApp.userMst.detail.title">UserMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.id}</dd>
          <dt>
            <span id="addressLine1">
              <Translate contentKey="dboApp.userMst.addressLine1">Address Line 1</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.addressLine1}</dd>
          <dt>
            <span id="addressLine2">
              <Translate contentKey="dboApp.userMst.addressLine2">Address Line 2</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.addressLine2}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="dboApp.userMst.city">City</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.city}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="dboApp.userMst.country">Country</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.country}</dd>
          <dt>
            <span id="coverImage">
              <Translate contentKey="dboApp.userMst.coverImage">Cover Image</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.coverImage}</dd>
          <dt>
            <span id="createBy">
              <Translate contentKey="dboApp.userMst.createBy">Create By</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.createBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.userMst.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {userMstEntity.createdOn ? <TextFormat value={userMstEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="email">
              <Translate contentKey="dboApp.userMst.email">Email</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.email}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="dboApp.userMst.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.firstName}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.userMst.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="dboApp.userMst.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.lastName}</dd>
          <dt>
            <span id="manufacturerId">
              <Translate contentKey="dboApp.userMst.manufacturerId">Manufacturer Id</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.manufacturerId}</dd>
          <dt>
            <span id="mobile">
              <Translate contentKey="dboApp.userMst.mobile">Mobile</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.mobile}</dd>
          <dt>
            <span id="objectId">
              <Translate contentKey="dboApp.userMst.objectId">Object Id</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.objectId}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="dboApp.userMst.password">Password</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.password}</dd>
          <dt>
            <span id="profileImage">
              <Translate contentKey="dboApp.userMst.profileImage">Profile Image</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.profileImage}</dd>
          <dt>
            <span id="roleId">
              <Translate contentKey="dboApp.userMst.roleId">Role Id</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.roleId}</dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.userMst.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.schoolDistrictId}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="dboApp.userMst.state">State</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.state}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.userMst.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.userMst.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {userMstEntity.updatedOn ? <TextFormat value={userMstEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dboApp.userMst.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.userId}</dd>
          <dt>
            <span id="zipCode">
              <Translate contentKey="dboApp.userMst.zipCode">Zip Code</Translate>
            </span>
          </dt>
          <dd>{userMstEntity.zipCode}</dd>
        </dl>
        <Button tag={Link} to="/user-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-mst/${userMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserMstDetail;
