import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './replaced-products.reducer';

export const ReplacedProductsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const replacedProductsEntity = useAppSelector(state => state.replacedProducts.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="replacedProductsDetailsHeading">
          <Translate contentKey="dboApp.replacedProducts.detail.title">ReplacedProducts</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{replacedProductsEntity.id}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.replacedProducts.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{replacedProductsEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="productId">
              <Translate contentKey="dboApp.replacedProducts.productId">Product Id</Translate>
            </span>
          </dt>
          <dd>{replacedProductsEntity.productId}</dd>
          <dt>
            <span id="replacedByUserId">
              <Translate contentKey="dboApp.replacedProducts.replacedByUserId">Replaced By User Id</Translate>
            </span>
          </dt>
          <dd>{replacedProductsEntity.replacedByUserId}</dd>
          <dt>
            <span id="replacedId">
              <Translate contentKey="dboApp.replacedProducts.replacedId">Replaced Id</Translate>
            </span>
          </dt>
          <dd>{replacedProductsEntity.replacedId}</dd>
          <dt>
            <span id="replacedProductId">
              <Translate contentKey="dboApp.replacedProducts.replacedProductId">Replaced Product Id</Translate>
            </span>
          </dt>
          <dd>{replacedProductsEntity.replacedProductId}</dd>
          <dt>
            <span id="replacementDate">
              <Translate contentKey="dboApp.replacedProducts.replacementDate">Replacement Date</Translate>
            </span>
          </dt>
          <dd>
            {replacedProductsEntity.replacementDate ? (
              <TextFormat value={replacedProductsEntity.replacementDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.replacedProducts.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{replacedProductsEntity.schoolDistrictId}</dd>
        </dl>
        <Button tag={Link} to="/replaced-products" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/replaced-products/${replacedProductsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReplacedProductsDetail;
