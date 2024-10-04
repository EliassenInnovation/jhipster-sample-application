import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './community-like-mst.reducer';

export const CommunityLikeMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const communityLikeMstEntity = useAppSelector(state => state.communityLikeMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="communityLikeMstDetailsHeading">
          <Translate contentKey="dboApp.communityLikeMst.detail.title">CommunityLikeMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{communityLikeMstEntity.id}</dd>
          <dt>
            <span id="communityLikeId">
              <Translate contentKey="dboApp.communityLikeMst.communityLikeId">Community Like Id</Translate>
            </span>
          </dt>
          <dd>{communityLikeMstEntity.communityLikeId}</dd>
          <dt>
            <span id="communityPostId">
              <Translate contentKey="dboApp.communityLikeMst.communityPostId">Community Post Id</Translate>
            </span>
          </dt>
          <dd>{communityLikeMstEntity.communityPostId}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.communityLikeMst.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {communityLikeMstEntity.createdOn ? (
              <TextFormat value={communityLikeMstEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.communityLikeMst.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{communityLikeMstEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isLiked">
              <Translate contentKey="dboApp.communityLikeMst.isLiked">Is Liked</Translate>
            </span>
          </dt>
          <dd>{communityLikeMstEntity.isLiked ? 'true' : 'false'}</dd>
          <dt>
            <span id="likedByUserId">
              <Translate contentKey="dboApp.communityLikeMst.likedByUserId">Liked By User Id</Translate>
            </span>
          </dt>
          <dd>{communityLikeMstEntity.likedByUserId}</dd>
        </dl>
        <Button tag={Link} to="/community-like-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/community-like-mst/${communityLikeMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommunityLikeMstDetail;
