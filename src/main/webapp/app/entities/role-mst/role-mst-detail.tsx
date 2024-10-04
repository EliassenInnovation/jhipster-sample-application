import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './role-mst.reducer';

export const RoleMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const roleMstEntity = useAppSelector(state => state.roleMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="roleMstDetailsHeading">
          <Translate contentKey="dboApp.roleMst.detail.title">RoleMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{roleMstEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.roleMst.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{roleMstEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.roleMst.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {roleMstEntity.createdOn ? <TextFormat value={roleMstEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.roleMst.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{roleMstEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="parentRoleId">
              <Translate contentKey="dboApp.roleMst.parentRoleId">Parent Role Id</Translate>
            </span>
          </dt>
          <dd>{roleMstEntity.parentRoleId}</dd>
          <dt>
            <span id="roleId">
              <Translate contentKey="dboApp.roleMst.roleId">Role Id</Translate>
            </span>
          </dt>
          <dd>{roleMstEntity.roleId}</dd>
          <dt>
            <span id="roleName">
              <Translate contentKey="dboApp.roleMst.roleName">Role Name</Translate>
            </span>
          </dt>
          <dd>{roleMstEntity.roleName}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.roleMst.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{roleMstEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.roleMst.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {roleMstEntity.updatedOn ? <TextFormat value={roleMstEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/role-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/role-mst/${roleMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default RoleMstDetail;
