import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './month-mst.reducer';

export const MonthMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const monthMstEntity = useAppSelector(state => state.monthMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="monthMstDetailsHeading">
          <Translate contentKey="dboApp.monthMst.detail.title">MonthMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{monthMstEntity.id}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.monthMst.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{monthMstEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="monthID">
              <Translate contentKey="dboApp.monthMst.monthID">Month ID</Translate>
            </span>
          </dt>
          <dd>{monthMstEntity.monthID}</dd>
          <dt>
            <span id="monthName">
              <Translate contentKey="dboApp.monthMst.monthName">Month Name</Translate>
            </span>
          </dt>
          <dd>{monthMstEntity.monthName}</dd>
          <dt>
            <span id="year">
              <Translate contentKey="dboApp.monthMst.year">Year</Translate>
            </span>
          </dt>
          <dd>{monthMstEntity.year}</dd>
        </dl>
        <Button tag={Link} to="/month-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/month-mst/${monthMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MonthMstDetail;
