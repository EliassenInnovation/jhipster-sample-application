import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './set-mappings.reducer';

export const SetMappingsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const setMappingsEntity = useAppSelector(state => state.setMappings.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="setMappingsDetailsHeading">
          <Translate contentKey="dboApp.setMappings.detail.title">SetMappings</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{setMappingsEntity.id}</dd>
          <dt>
            <span id="iD">
              <Translate contentKey="dboApp.setMappings.iD">I D</Translate>
            </span>
          </dt>
          <dd>{setMappingsEntity.iD}</dd>
          <dt>
            <span id="oneWorldValue">
              <Translate contentKey="dboApp.setMappings.oneWorldValue">One World Value</Translate>
            </span>
          </dt>
          <dd>{setMappingsEntity.oneWorldValue}</dd>
          <dt>
            <span id="productValue">
              <Translate contentKey="dboApp.setMappings.productValue">Product Value</Translate>
            </span>
          </dt>
          <dd>{setMappingsEntity.productValue}</dd>
          <dt>
            <span id="setName">
              <Translate contentKey="dboApp.setMappings.setName">Set Name</Translate>
            </span>
          </dt>
          <dd>{setMappingsEntity.setName}</dd>
        </dl>
        <Button tag={Link} to="/set-mappings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/set-mappings/${setMappingsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SetMappingsDetail;
