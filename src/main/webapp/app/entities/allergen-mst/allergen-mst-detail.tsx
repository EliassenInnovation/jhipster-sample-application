import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './allergen-mst.reducer';

export const AllergenMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const allergenMstEntity = useAppSelector(state => state.allergenMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="allergenMstDetailsHeading">
          <Translate contentKey="dboApp.allergenMst.detail.title">AllergenMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{allergenMstEntity.id}</dd>
          <dt>
            <span id="allergenGroup">
              <Translate contentKey="dboApp.allergenMst.allergenGroup">Allergen Group</Translate>
            </span>
          </dt>
          <dd>{allergenMstEntity.allergenGroup}</dd>
          <dt>
            <span id="allergenId">
              <Translate contentKey="dboApp.allergenMst.allergenId">Allergen Id</Translate>
            </span>
          </dt>
          <dd>{allergenMstEntity.allergenId}</dd>
          <dt>
            <span id="allergenName">
              <Translate contentKey="dboApp.allergenMst.allergenName">Allergen Name</Translate>
            </span>
          </dt>
          <dd>{allergenMstEntity.allergenName}</dd>
        </dl>
        <Button tag={Link} to="/allergen-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/allergen-mst/${allergenMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AllergenMstDetail;
