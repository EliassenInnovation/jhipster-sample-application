import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-change-history.reducer';

export const ProductChangeHistoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productChangeHistoryEntity = useAppSelector(state => state.productChangeHistory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productChangeHistoryDetailsHeading">
          <Translate contentKey="dboApp.productChangeHistory.detail.title">ProductChangeHistory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productChangeHistory.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.createdBy}</dd>
          <dt>
            <span id="dateCreated">
              <Translate contentKey="dboApp.productChangeHistory.dateCreated">Date Created</Translate>
            </span>
          </dt>
          <dd>
            {productChangeHistoryEntity.dateCreated ? (
              <TextFormat value={productChangeHistoryEntity.dateCreated} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="historyId">
              <Translate contentKey="dboApp.productChangeHistory.historyId">History Id</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.historyId}</dd>
          <dt>
            <span id="iocCategoryId">
              <Translate contentKey="dboApp.productChangeHistory.iocCategoryId">Ioc Category Id</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.iocCategoryId}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productChangeHistory.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productChangeHistory.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.productId}</dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.productChangeHistory.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.schoolDistrictId}</dd>
          <dt>
            <span id="selectionType">
              <Translate contentKey="dboApp.productChangeHistory.selectionType">Selection Type</Translate>
            </span>
          </dt>
          <dd>{productChangeHistoryEntity.selectionType}</dd>
        </dl>
        <Button tag={Link} to="/product-change-history" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-change-history/${productChangeHistoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductChangeHistoryDetail;
