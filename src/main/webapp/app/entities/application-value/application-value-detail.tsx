import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './application-value.reducer';

export const ApplicationValueDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const applicationValueEntity = useAppSelector(state => state.applicationValue.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="applicationValueDetailsHeading">
          <Translate contentKey="dboApp.applicationValue.detail.title">ApplicationValue</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{applicationValueEntity.id}</dd>
          <dt>
            <span id="applicationValueId">
              <Translate contentKey="dboApp.applicationValue.applicationValueId">Application Value Id</Translate>
            </span>
          </dt>
          <dd>{applicationValueEntity.applicationValueId}</dd>
          <dt>
            <span id="key">
              <Translate contentKey="dboApp.applicationValue.key">Key</Translate>
            </span>
          </dt>
          <dd>{applicationValueEntity.key}</dd>
          <dt>
            <span id="valueDate">
              <Translate contentKey="dboApp.applicationValue.valueDate">Value Date</Translate>
            </span>
          </dt>
          <dd>
            {applicationValueEntity.valueDate ? (
              <TextFormat value={applicationValueEntity.valueDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="valueInt">
              <Translate contentKey="dboApp.applicationValue.valueInt">Value Int</Translate>
            </span>
          </dt>
          <dd>{applicationValueEntity.valueInt}</dd>
          <dt>
            <span id="valueLong">
              <Translate contentKey="dboApp.applicationValue.valueLong">Value Long</Translate>
            </span>
          </dt>
          <dd>{applicationValueEntity.valueLong}</dd>
          <dt>
            <span id="valueString">
              <Translate contentKey="dboApp.applicationValue.valueString">Value String</Translate>
            </span>
          </dt>
          <dd>{applicationValueEntity.valueString}</dd>
        </dl>
        <Button tag={Link} to="/application-value" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/application-value/${applicationValueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ApplicationValueDetail;
