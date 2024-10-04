import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './product-allergen.reducer';

export const ProductAllergenDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const productAllergenEntity = useAppSelector(state => state.productAllergen.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="productAllergenDetailsHeading">
          <Translate contentKey="dboApp.productAllergen.detail.title">ProductAllergen</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.id}</dd>
          <dt>
            <span id="allergenId">
              <Translate contentKey="dboApp.productAllergen.allergenId">Allergen Id</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.allergenId}</dd>
          <dt>
            <span id="allergenGroup">
              <Translate contentKey="dboApp.productAllergen.allergenGroup">Allergen Group</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.allergenGroup}</dd>
          <dt>
            <span id="allergenName">
              <Translate contentKey="dboApp.productAllergen.allergenName">Allergen Name</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.allergenName}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="dboApp.productAllergen.description">Description</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.description}</dd>
          <dt>
            <span id="gTIN">
              <Translate contentKey="dboApp.productAllergen.gTIN">G TIN</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.gTIN}</dd>
          <dt>
            <span id="gTINUPC">
              <Translate contentKey="dboApp.productAllergen.gTINUPC">G TINUPC</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.gTINUPC}</dd>
          <dt>
            <span id="productAllergenId">
              <Translate contentKey="dboApp.productAllergen.productAllergenId">Product Allergen Id</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.productAllergenId}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.productAllergen.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.productId}</dd>
          <dt>
            <span id="uPC">
              <Translate contentKey="dboApp.productAllergen.uPC">U PC</Translate>
            </span>
          </dt>
          <dd>{productAllergenEntity.uPC}</dd>
        </dl>
        <Button tag={Link} to="/product-allergen" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/product-allergen/${productAllergenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProductAllergenDetail;
