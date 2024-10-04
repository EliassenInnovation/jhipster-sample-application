import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-h-7-new.reducer';

export const ProductH7NewDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productH7NewEntity = useAppSelector(state => state.productH7New.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productH7NewDetailsHeading">
          <Translate contentKey="dboApp.productH7New.detail.title">ProductH7New</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productH7NewEntity.id}</dd>
          <dt>
            <span id="gtinUpc">
              <Translate contentKey="dboApp.productH7New.gtinUpc">Gtin Upc</Translate>
            </span>
          </dt>
          <dd>{productH7NewEntity.gtinUpc}</dd>
          <dt>
            <span id="h7KeywordId">
              <Translate contentKey="dboApp.productH7New.h7KeywordId">H 7 Keyword Id</Translate>
            </span>
          </dt>
          <dd>{productH7NewEntity.h7KeywordId}</dd>
          <dt>
            <span id="iOCGroup">
              <Translate contentKey="dboApp.productH7New.iOCGroup">I OC Group</Translate>
            </span>
          </dt>
          <dd>{productH7NewEntity.iOCGroup}</dd>
          <dt>
            <span id="productH7Id">
              <Translate contentKey="dboApp.productH7New.productH7Id">Product H 7 Id</Translate>
            </span>
          </dt>
          <dd>{productH7NewEntity.productH7Id}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productH7New.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productH7NewEntity.productId}</dd>
          <dt>
            <span id="productName">
              <Translate contentKey="dboApp.productH7New.productName">Product Name</Translate>
            </span>
          </dt>
          <dd>{productH7NewEntity.productName}</dd>
        </dl>
        <Button tag={Link} to="/product-h-7-new" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-h-7-new/${productH7NewEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductH7NewDetail;
