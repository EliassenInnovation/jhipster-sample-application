import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './block-report-post.reducer';

export const BlockReportPostDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const blockReportPostEntity = useAppSelector(state => state.blockReportPost.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="blockReportPostDetailsHeading">
          <Translate contentKey="dboApp.blockReportPost.detail.title">BlockReportPost</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{blockReportPostEntity.id}</dd>
          <dt>
            <span id="blockCategories">
              <Translate contentKey="dboApp.blockReportPost.blockCategories">Block Categories</Translate>
            </span>
          </dt>
          <dd>{blockReportPostEntity.blockCategories}</dd>
          <dt>
            <span id="blockingReason">
              <Translate contentKey="dboApp.blockReportPost.blockingReason">Blocking Reason</Translate>
            </span>
          </dt>
          <dd>{blockReportPostEntity.blockingReason}</dd>
          <dt>
            <span id="postBlockReportId">
              <Translate contentKey="dboApp.blockReportPost.postBlockReportId">Post Block Report Id</Translate>
            </span>
          </dt>
          <dd>{blockReportPostEntity.postBlockReportId}</dd>
          <dt>
            <span id="postId">
              <Translate contentKey="dboApp.blockReportPost.postId">Post Id</Translate>
            </span>
          </dt>
          <dd>{blockReportPostEntity.postId}</dd>
          <dt>
            <span id="postType">
              <Translate contentKey="dboApp.blockReportPost.postType">Post Type</Translate>
            </span>
          </dt>
          <dd>{blockReportPostEntity.postType}</dd>
          <dt>
            <span id="requestedBy">
              <Translate contentKey="dboApp.blockReportPost.requestedBy">Requested By</Translate>
            </span>
          </dt>
          <dd>{blockReportPostEntity.requestedBy}</dd>
        </dl>
        <Button tag={Link} to="/block-report-post" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/block-report-post/${blockReportPostEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BlockReportPostDetail;
