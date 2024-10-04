import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-allergen-bak.reducer';

export const ProductAllergenBakDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productAllergenBakEntity = useAppSelector(state => state.productAllergenBak.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productAllergenBakDetailsHeading">
          <Translate contentKey="dboApp.productAllergenBak.detail.title">ProductAllergenBak</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.id}</dd>
          <dt>
            <span id="allergenId">
              <Translate contentKey="dboApp.productAllergenBak.allergenId">Allergen Id</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.allergenId}</dd>
          <dt>
            <span id="allergenGroup">
              <Translate contentKey="dboApp.productAllergenBak.allergenGroup">Allergen Group</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.allergenGroup}</dd>
          <dt>
            <span id="allergenName">
              <Translate contentKey="dboApp.productAllergenBak.allergenName">Allergen Name</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.allergenName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="dboApp.productAllergenBak.description">Description</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.description}</dd>
          <dt>
            <span id="gTIN">
              <Translate contentKey="dboApp.productAllergenBak.gTIN">G TIN</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.gTIN}</dd>
          <dt>
            <span id="gTINUPC">
              <Translate contentKey="dboApp.productAllergenBak.gTINUPC">G TINUPC</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.gTINUPC}</dd>
          <dt>
            <span id="productAllergenId">
              <Translate contentKey="dboApp.productAllergenBak.productAllergenId">Product Allergen Id</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.productAllergenId}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productAllergenBak.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.productId}</dd>
          <dt>
            <span id="uPC">
              <Translate contentKey="dboApp.productAllergenBak.uPC">U PC</Translate>
            </span>
          </dt>
          <dd>{productAllergenBakEntity.uPC}</dd>
        </dl>
        <Button tag={Link} to="/product-allergen-bak" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-allergen-bak/${productAllergenBakEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductAllergenBakDetail;
