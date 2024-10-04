import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './error-log.reducer';

export const ErrorLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const errorLogEntity = useAppSelector(state => state.errorLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="errorLogDetailsHeading">
          <Translate contentKey="dboApp.errorLog.detail.title">ErrorLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{errorLogEntity.id}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.errorLog.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {errorLogEntity.createdOn ? <TextFormat value={errorLogEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="errorId">
              <Translate contentKey="dboApp.errorLog.errorId">Error Id</Translate>
            </span>
          </dt>
          <dd>{errorLogEntity.errorId}</dd>
          <dt>
            <span id="errorMessage">
              <Translate contentKey="dboApp.errorLog.errorMessage">Error Message</Translate>
            </span>
          </dt>
          <dd>{errorLogEntity.errorMessage}</dd>
          <dt>
            <span id="errorPath">
              <Translate contentKey="dboApp.errorLog.errorPath">Error Path</Translate>
            </span>
          </dt>
          <dd>{errorLogEntity.errorPath}</dd>
        </dl>
        <Button tag={Link} to="/error-log" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/error-log/${errorLogEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ErrorLogDetail;
