import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './suggested-products.reducer';

export const SuggestedProductsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const suggestedProductsEntity = useAppSelector(state => state.suggestedProducts.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="suggestedProductsDetailsHeading">
          <Translate contentKey="dboApp.suggestedProducts.detail.title">SuggestedProducts</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.id}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.suggestedProducts.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isApprove">
              <Translate contentKey="dboApp.suggestedProducts.isApprove">Is Approve</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.isApprove ? 'true' : 'false'}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.suggestedProducts.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.productId}</dd>
          <dt>
            <span id="suggestedByDistrict">
              <Translate contentKey="dboApp.suggestedProducts.suggestedByDistrict">Suggested By District</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.suggestedByDistrict}</dd>
          <dt>
            <span id="suggestedByUserId">
              <Translate contentKey="dboApp.suggestedProducts.suggestedByUserId">Suggested By User Id</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.suggestedByUserId}</dd>
          <dt>
            <span id="suggestedProductId">
              <Translate contentKey="dboApp.suggestedProducts.suggestedProductId">Suggested Product Id</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.suggestedProductId}</dd>
          <dt>
            <span id="suggestionDate">
              <Translate contentKey="dboApp.suggestedProducts.suggestionDate">Suggestion Date</Translate>
            </span>
          </dt>
          <dd>
            {suggestedProductsEntity.suggestionDate ? (
              <TextFormat value={suggestedProductsEntity.suggestionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="suggestionId">
              <Translate contentKey="dboApp.suggestedProducts.suggestionId">Suggestion Id</Translate>
            </span>
          </dt>
          <dd>{suggestedProductsEntity.suggestionId}</dd>
        </dl>
        <Button tag={Link} to="/suggested-products" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/suggested-products/${suggestedProductsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SuggestedProductsDetail;
