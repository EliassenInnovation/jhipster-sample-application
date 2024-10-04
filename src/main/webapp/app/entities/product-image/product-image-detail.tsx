import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-image.reducer';

export const ProductImageDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productImageEntity = useAppSelector(state => state.productImage.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productImageDetailsHeading">
          <Translate contentKey="dboApp.productImage.detail.title">ProductImage</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productImage.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productImage.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productImageEntity.createdOn ? (
              <TextFormat value={productImageEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="imageURL">
              <Translate contentKey="dboApp.productImage.imageURL">Image URL</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.imageURL}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productImage.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productImage.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.productId}</dd>
          <dt>
            <span id="productImageId">
              <Translate contentKey="dboApp.productImage.productImageId">Product Image Id</Translate>
            </span>
          </dt>
          <dd>{productImageEntity.productImageId}</dd>
        </dl>
        <Button tag={Link} to="/product-image" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-image/${productImageEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductImageDetail;
