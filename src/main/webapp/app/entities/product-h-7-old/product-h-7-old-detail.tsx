import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-h-7-old.reducer';

export const ProductH7OldDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productH7OldEntity = useAppSelector(state => state.productH7Old.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productH7OldDetailsHeading">
          <Translate contentKey="dboApp.productH7Old.detail.title">ProductH7Old</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productH7OldEntity.id}</dd>
          <dt>
            <span id="gtinUpc">
              <Translate contentKey="dboApp.productH7Old.gtinUpc">Gtin Upc</Translate>
            </span>
          </dt>
          <dd>{productH7OldEntity.gtinUpc}</dd>
          <dt>
            <span id="h7KeywordId">
              <Translate contentKey="dboApp.productH7Old.h7KeywordId">H 7 Keyword Id</Translate>
            </span>
          </dt>
          <dd>{productH7OldEntity.h7KeywordId}</dd>
          <dt>
            <span id="iOCGroup">
              <Translate contentKey="dboApp.productH7Old.iOCGroup">I OC Group</Translate>
            </span>
          </dt>
          <dd>{productH7OldEntity.iOCGroup}</dd>
          <dt>
            <span id="productH7Id">
              <Translate contentKey="dboApp.productH7Old.productH7Id">Product H 7 Id</Translate>
            </span>
          </dt>
          <dd>{productH7OldEntity.productH7Id}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productH7Old.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productH7OldEntity.productId}</dd>
          <dt>
            <span id="productName">
              <Translate contentKey="dboApp.productH7Old.productName">Product Name</Translate>
            </span>
          </dt>
          <dd>{productH7OldEntity.productName}</dd>
        </dl>
        <Button tag={Link} to="/product-h-7-old" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-h-7-old/${productH7OldEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductH7OldDetail;
