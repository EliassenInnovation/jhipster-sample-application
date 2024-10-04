import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './state-info.reducer';

export const StateInfoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const stateInfoEntity = useAppSelector(state => state.stateInfo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="stateInfoDetailsHeading">
          <Translate contentKey="dboApp.stateInfo.detail.title">StateInfo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{stateInfoEntity.id}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.stateInfo.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {stateInfoEntity.createdOn ? <TextFormat value={stateInfoEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.stateInfo.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{stateInfoEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="stateId">
              <Translate contentKey="dboApp.stateInfo.stateId">State Id</Translate>
            </span>
          </dt>
          <dd>{stateInfoEntity.stateId}</dd>
          <dt>
            <span id="stateName">
              <Translate contentKey="dboApp.stateInfo.stateName">State Name</Translate>
            </span>
          </dt>
          <dd>{stateInfoEntity.stateName}</dd>
        </dl>
        <Button tag={Link} to="/state-info" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/state-info/${stateInfoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StateInfoDetail;
