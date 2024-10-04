import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-activity-history.reducer';

export const ProductActivityHistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productActivityHistoryEntity = useAppSelector(state => state.productActivityHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productActivityHistoryDetailsHeading">
          <Translate contentKey="dboApp.productActivityHistory.detail.title">ProductActivityHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.id}</dd>
          <dt>
            <span id="activityId">
              <Translate contentKey="dboApp.productActivityHistory.activityId">Activity Id</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.activityId}</dd>
          <dt>
            <span id="activityType">
              <Translate contentKey="dboApp.productActivityHistory.activityType">Activity Type</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.activityType}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productActivityHistory.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productActivityHistory.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productActivityHistoryEntity.createdOn ? (
              <TextFormat value={productActivityHistoryEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="date">
              <Translate contentKey="dboApp.productActivityHistory.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {productActivityHistoryEntity.date ? (
              <TextFormat value={productActivityHistoryEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productActivityHistory.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productActivityHistory.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.productId}</dd>
          <dt>
            <span id="suggestedProductId">
              <Translate contentKey="dboApp.productActivityHistory.suggestedProductId">Suggested Product Id</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.suggestedProductId}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.productActivityHistory.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{productActivityHistoryEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.productActivityHistory.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {productActivityHistoryEntity.updatedOn ? (
              <TextFormat value={productActivityHistoryEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/product-activity-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-activity-history/${productActivityHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductActivityHistoryDetail;
