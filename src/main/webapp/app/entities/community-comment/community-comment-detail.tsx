import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './community-comment.reducer';

export const CommunityCommentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const communityCommentEntity = useAppSelector(state => state.communityComment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="communityCommentDetailsHeading">
          <Translate contentKey="dboApp.communityComment.detail.title">CommunityComment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{communityCommentEntity.id}</dd>
          <dt>
            <span id="comment">
              <Translate contentKey="dboApp.communityComment.comment">Comment</Translate>
            </span>
          </dt>
          <dd>{communityCommentEntity.comment}</dd>
          <dt>
            <span id="commentByUserId">
              <Translate contentKey="dboApp.communityComment.commentByUserId">Comment By User Id</Translate>
            </span>
          </dt>
          <dd>{communityCommentEntity.commentByUserId}</dd>
          <dt>
            <span id="communityCommentId">
              <Translate contentKey="dboApp.communityComment.communityCommentId">Community Comment Id</Translate>
            </span>
          </dt>
          <dd>{communityCommentEntity.communityCommentId}</dd>
          <dt>
            <span id="communityPostId">
              <Translate contentKey="dboApp.communityComment.communityPostId">Community Post Id</Translate>
            </span>
          </dt>
          <dd>{communityCommentEntity.communityPostId}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.communityComment.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {communityCommentEntity.createdOn ? (
              <TextFormat value={communityCommentEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.communityComment.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{communityCommentEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdatedOn">
              <Translate contentKey="dboApp.communityComment.lastUpdatedOn">Last Updated On</Translate>
            </span>
          </dt>
          <dd>
            {communityCommentEntity.lastUpdatedOn ? (
              <TextFormat value={communityCommentEntity.lastUpdatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/community-comment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/community-comment/${communityCommentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommunityCommentDetail;
