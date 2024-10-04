import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './community-post.reducer';

export const CommunityPostDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const communityPostEntity = useAppSelector(state => state.communityPost.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="communityPostDetailsHeading">
          <Translate contentKey="dboApp.communityPost.detail.title">CommunityPost</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.id}</dd>
          <dt>
            <span id="communityPostId">
              <Translate contentKey="dboApp.communityPost.communityPostId">Community Post Id</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.communityPostId}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.communityPost.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.communityPost.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {communityPostEntity.createdOn ? (
              <TextFormat value={communityPostEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="date">
              <Translate contentKey="dboApp.communityPost.date">Date</Translate>
            </span>
          </dt>
          <dd>
            {communityPostEntity.date ? <TextFormat value={communityPostEntity.date} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="dboApp.communityPost.description">Description</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.description}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.communityPost.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdatedBy">
              <Translate contentKey="dboApp.communityPost.lastUpdatedBy">Last Updated By</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.lastUpdatedBy}</dd>
          <dt>
            <span id="lastUpdatedOn">
              <Translate contentKey="dboApp.communityPost.lastUpdatedOn">Last Updated On</Translate>
            </span>
          </dt>
          <dd>
            {communityPostEntity.lastUpdatedOn ? (
              <TextFormat value={communityPostEntity.lastUpdatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="postTypeId">
              <Translate contentKey="dboApp.communityPost.postTypeId">Post Type Id</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.postTypeId}</dd>
          <dt>
            <span id="privacyTypeId">
              <Translate contentKey="dboApp.communityPost.privacyTypeId">Privacy Type Id</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.privacyTypeId}</dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.communityPost.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.schoolDistrictId}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dboApp.communityPost.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{communityPostEntity.userId}</dd>
        </dl>
        <Button tag={Link} to="/community-post" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/community-post/${communityPostEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommunityPostDetail;
