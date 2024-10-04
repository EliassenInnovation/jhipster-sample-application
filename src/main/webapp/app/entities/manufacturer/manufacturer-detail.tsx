import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './manufacturer.reducer';

export const ManufacturerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const manufacturerEntity = useAppSelector(state => state.manufacturer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="manufacturerDetailsHeading">
          <Translate contentKey="dboApp.manufacturer.detail.title">Manufacturer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.manufacturer.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.manufacturer.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {manufacturerEntity.createdOn ? (
              <TextFormat value={manufacturerEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="email">
              <Translate contentKey="dboApp.manufacturer.email">Email</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.email}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="dboApp.manufacturer.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.firstName}</dd>
          <dt>
            <span id="glnNumber">
              <Translate contentKey="dboApp.manufacturer.glnNumber">Gln Number</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.glnNumber}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.manufacturer.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="dboApp.manufacturer.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.lastName}</dd>
          <dt>
            <span id="manufacturer">
              <Translate contentKey="dboApp.manufacturer.manufacturer">Manufacturer</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.manufacturer}</dd>
          <dt>
            <span id="manufacturerId">
              <Translate contentKey="dboApp.manufacturer.manufacturerId">Manufacturer Id</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.manufacturerId}</dd>
          <dt>
            <span id="password">
              <Translate contentKey="dboApp.manufacturer.password">Password</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.password}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="dboApp.manufacturer.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{manufacturerEntity.phoneNumber}</dd>
        </dl>
        <Button tag={Link} to="/manufacturer" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/manufacturer/${manufacturerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ManufacturerDetail;
