import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './notification.reducer';

export const NotificationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const notificationEntity = useAppSelector(state => state.notification.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="notificationDetailsHeading">
          <Translate contentKey="dboApp.notification.detail.title">Notification</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="dboApp.notification.content">Content</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.content}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.notification.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.notification.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {notificationEntity.createdOn ? (
              <TextFormat value={notificationEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.notification.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="notificationId">
              <Translate contentKey="dboApp.notification.notificationId">Notification Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.notificationId}</dd>
          <dt>
            <span id="referenceId">
              <Translate contentKey="dboApp.notification.referenceId">Reference Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.referenceId}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="dboApp.notification.type">Type</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.type}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dboApp.notification.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{notificationEntity.userId}</dd>
        </dl>
        <Button tag={Link} to="/notification" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/notification/${notificationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NotificationDetail;
