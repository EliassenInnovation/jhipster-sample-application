import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './category.reducer';

export const CategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const categoryEntity = useAppSelector(state => state.category.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="categoryDetailsHeading">
          <Translate contentKey="dboApp.category.detail.title">Category</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.id}</dd>
          <dt>
            <span id="categoryCode">
              <Translate contentKey="dboApp.category.categoryCode">Category Code</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.categoryCode}</dd>
          <dt>
            <span id="categoryId">
              <Translate contentKey="dboApp.category.categoryId">Category Id</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.categoryId}</dd>
          <dt>
            <span id="categoryName">
              <Translate contentKey="dboApp.category.categoryName">Category Name</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.categoryName}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.category.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.category.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {categoryEntity.createdOn ? <TextFormat value={categoryEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.category.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="updatedBy">
              <Translate contentKey="dboApp.category.updatedBy">Updated By</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.updatedBy}</dd>
          <dt>
            <span id="updatedOn">
              <Translate contentKey="dboApp.category.updatedOn">Updated On</Translate>
            </span>
          </dt>
          <dd>
            {categoryEntity.updatedOn ? <TextFormat value={categoryEntity.updatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
        </dl>
        <Button tag={Link} to="/category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/category/${categoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CategoryDetail;
