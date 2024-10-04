import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './post-types.reducer';

export const PostTypesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const postTypesEntity = useAppSelector(state => state.postTypes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="postTypesDetailsHeading">
          <Translate contentKey="dboApp.postTypes.detail.title">PostTypes</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{postTypesEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.postTypes.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{postTypesEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.postTypes.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {postTypesEntity.createdOn ? <TextFormat value={postTypesEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.postTypes.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{postTypesEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdatedBy">
              <Translate contentKey="dboApp.postTypes.lastUpdatedBy">Last Updated By</Translate>
            </span>
          </dt>
          <dd>{postTypesEntity.lastUpdatedBy}</dd>
          <dt>
            <span id="lastUpdatedOn">
              <Translate contentKey="dboApp.postTypes.lastUpdatedOn">Last Updated On</Translate>
            </span>
          </dt>
          <dd>
            {postTypesEntity.lastUpdatedOn ? (
              <TextFormat value={postTypesEntity.lastUpdatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="postType">
              <Translate contentKey="dboApp.postTypes.postType">Post Type</Translate>
            </span>
          </dt>
          <dd>{postTypesEntity.postType}</dd>
          <dt>
            <span id="postTypeId">
              <Translate contentKey="dboApp.postTypes.postTypeId">Post Type Id</Translate>
            </span>
          </dt>
          <dd>{postTypesEntity.postTypeId}</dd>
        </dl>
        <Button tag={Link} to="/post-types" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/post-types/${postTypesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PostTypesDetail;
