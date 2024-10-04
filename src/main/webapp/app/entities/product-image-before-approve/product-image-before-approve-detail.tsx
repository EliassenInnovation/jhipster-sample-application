import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-image-before-approve.reducer';

export const ProductImageBeforeApproveDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productImageBeforeApproveEntity = useAppSelector(state => state.productImageBeforeApprove.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productImageBeforeApproveDetailsHeading">
          <Translate contentKey="dboApp.productImageBeforeApprove.detail.title">ProductImageBeforeApprove</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productImageBeforeApproveEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.productImageBeforeApprove.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{productImageBeforeApproveEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.productImageBeforeApprove.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {productImageBeforeApproveEntity.createdOn ? (
              <TextFormat value={productImageBeforeApproveEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="imageURL">
              <Translate contentKey="dboApp.productImageBeforeApprove.imageURL">Image URL</Translate>
            </span>
          </dt>
          <dd>{productImageBeforeApproveEntity.imageURL}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.productImageBeforeApprove.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{productImageBeforeApproveEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productImageBeforeApprove.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productImageBeforeApproveEntity.productId}</dd>
          <dt>
            <span id="productImageId">
              <Translate contentKey="dboApp.productImageBeforeApprove.productImageId">Product Image Id</Translate>
            </span>
          </dt>
          <dd>{productImageBeforeApproveEntity.productImageId}</dd>
        </dl>
        <Button tag={Link} to="/product-image-before-approve" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-image-before-approve/${productImageBeforeApproveEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductImageBeforeApproveDetail;
