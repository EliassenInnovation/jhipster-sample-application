import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './privacy-type.reducer';

export const PrivacyTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const privacyTypeEntity = useAppSelector(state => state.privacyType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="privacyTypeDetailsHeading">
          <Translate contentKey="dboApp.privacyType.detail.title">PrivacyType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{privacyTypeEntity.id}</dd>
          <dt>
            <span id="privacyTypeId">
              <Translate contentKey="dboApp.privacyType.privacyTypeId">Privacy Type Id</Translate>
            </span>
          </dt>
          <dd>{privacyTypeEntity.privacyTypeId}</dd>
          <dt>
            <span id="privacyTypeName">
              <Translate contentKey="dboApp.privacyType.privacyTypeName">Privacy Type Name</Translate>
            </span>
          </dt>
          <dd>{privacyTypeEntity.privacyTypeName}</dd>
        </dl>
        <Button tag={Link} to="/privacy-type" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/privacy-type/${privacyTypeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PrivacyTypeDetail;
