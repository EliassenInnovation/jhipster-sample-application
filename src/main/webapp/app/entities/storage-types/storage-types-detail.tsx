import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './storage-types.reducer';

export const StorageTypesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const storageTypesEntity = useAppSelector(state => state.storageTypes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="storageTypesDetailsHeading">
          <Translate contentKey="dboApp.storageTypes.detail.title">StorageTypes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{storageTypesEntity.id}</dd>
          <dt>
            <span id="storageTypeId">
              <Translate contentKey="dboApp.storageTypes.storageTypeId">Storage Type Id</Translate>
            </span>
          </dt>
          <dd>{storageTypesEntity.storageTypeId}</dd>
          <dt>
            <span id="storageTypeName">
              <Translate contentKey="dboApp.storageTypes.storageTypeName">Storage Type Name</Translate>
            </span>
          </dt>
          <dd>{storageTypesEntity.storageTypeName}</dd>
        </dl>
        <Button tag={Link} to="/storage-types" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/storage-types/${storageTypesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default StorageTypesDetail;
