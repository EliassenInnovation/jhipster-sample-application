import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './support-category.reducer';

export const SupportCategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const supportCategoryEntity = useAppSelector(state => state.supportCategory.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="supportCategoryDetailsHeading">
          <Translate contentKey="dboApp.supportCategory.detail.title">SupportCategory</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{supportCategoryEntity.id}</dd>
          <dt>
            <span id="supportCategoryId">
              <Translate contentKey="dboApp.supportCategory.supportCategoryId">Support Category Id</Translate>
            </span>
          </dt>
          <dd>{supportCategoryEntity.supportCategoryId}</dd>
          <dt>
            <span id="supportCategoryName">
              <Translate contentKey="dboApp.supportCategory.supportCategoryName">Support Category Name</Translate>
            </span>
          </dt>
          <dd>{supportCategoryEntity.supportCategoryName}</dd>
        </dl>
        <Button tag={Link} to="/support-category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/support-category/${supportCategoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SupportCategoryDetail;
