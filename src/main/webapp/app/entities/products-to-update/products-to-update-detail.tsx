import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './products-to-update.reducer';

export const ProductsToUpdateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productsToUpdateEntity = useAppSelector(state => state.productsToUpdate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productsToUpdateDetailsHeading">
          <Translate contentKey="dboApp.productsToUpdate.detail.title">ProductsToUpdate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productsToUpdateEntity.id}</dd>
          <dt>
            <span id="maxGLNCode">
              <Translate contentKey="dboApp.productsToUpdate.maxGLNCode">Max GLN Code</Translate>
            </span>
          </dt>
          <dd>{productsToUpdateEntity.maxGLNCode}</dd>
          <dt>
            <span id="maxManufacturerID">
              <Translate contentKey="dboApp.productsToUpdate.maxManufacturerID">Max Manufacturer ID</Translate>
            </span>
          </dt>
          <dd>{productsToUpdateEntity.maxManufacturerID}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productsToUpdate.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productsToUpdateEntity.productId}</dd>
        </dl>
        <Button tag={Link} to="/products-to-update" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/products-to-update/${productsToUpdateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductsToUpdateDetail;
