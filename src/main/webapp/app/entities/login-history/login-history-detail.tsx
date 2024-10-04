import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './login-history.reducer';

export const LoginHistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const loginHistoryEntity = useAppSelector(state => state.loginHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loginHistoryDetailsHeading">
          <Translate contentKey="dboApp.loginHistory.detail.title">LoginHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{loginHistoryEntity.id}</dd>
          <dt>
            <span id="forgotPin">
              <Translate contentKey="dboApp.loginHistory.forgotPin">Forgot Pin</Translate>
            </span>
          </dt>
          <dd>{loginHistoryEntity.forgotPin}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.loginHistory.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{loginHistoryEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="loginDate">
              <Translate contentKey="dboApp.loginHistory.loginDate">Login Date</Translate>
            </span>
          </dt>
          <dd>
            {loginHistoryEntity.loginDate ? (
              <TextFormat value={loginHistoryEntity.loginDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="loginLogId">
              <Translate contentKey="dboApp.loginHistory.loginLogId">Login Log Id</Translate>
            </span>
          </dt>
          <dd>{loginHistoryEntity.loginLogId}</dd>
          <dt>
            <span id="loginType">
              <Translate contentKey="dboApp.loginHistory.loginType">Login Type</Translate>
            </span>
          </dt>
          <dd>{loginHistoryEntity.loginType}</dd>
          <dt>
            <span id="logOutDate">
              <Translate contentKey="dboApp.loginHistory.logOutDate">Log Out Date</Translate>
            </span>
          </dt>
          <dd>
            {loginHistoryEntity.logOutDate ? (
              <TextFormat value={loginHistoryEntity.logOutDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dboApp.loginHistory.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{loginHistoryEntity.userId}</dd>
        </dl>
        <Button tag={Link} to="/login-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/login-history/${loginHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoginHistoryDetail;
