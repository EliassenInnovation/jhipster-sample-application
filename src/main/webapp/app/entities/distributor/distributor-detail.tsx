import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './distributor.reducer';

export const DistributorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const distributorEntity = useAppSelector(state => state.distributor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="distributorDetailsHeading">
          <Translate contentKey="dboApp.distributor.detail.title">Distributor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{distributorEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.distributor.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{distributorEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.distributor.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {distributorEntity.createdOn ? (
              <TextFormat value={distributorEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="distributorCode">
              <Translate contentKey="dboApp.distributor.distributorCode">Distributor Code</Translate>
            </span>
          </dt>
          <dd>{distributorEntity.distributorCode}</dd>
          <dt>
            <span id="distributorID">
              <Translate contentKey="dboApp.distributor.distributorID">Distributor ID</Translate>
            </span>
          </dt>
          <dd>{distributorEntity.distributorID}</dd>
          <dt>
            <span id="distributorName">
              <Translate contentKey="dboApp.distributor.distributorName">Distributor Name</Translate>
            </span>
          </dt>
          <dd>{distributorEntity.distributorName}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.distributor.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{distributorEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.distributor.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{distributorEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.distributor.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {distributorEntity.updatedOn ? (
              <TextFormat value={distributorEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/distributor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/distributor/${distributorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default DistributorDetail;
