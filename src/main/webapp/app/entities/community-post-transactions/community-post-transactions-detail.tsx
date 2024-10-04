import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './community-post-transactions.reducer';

export const CommunityPostTransactionsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const communityPostTransactionsEntity = useAppSelector(state => state.communityPostTransactions.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="communityPostTransactionsDetailsHeading">
          <Translate contentKey="dboApp.communityPostTransactions.detail.title">CommunityPostTransactions</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{communityPostTransactionsEntity.id}</dd>
          <dt>
            <span id="attachmentUrl">
              <Translate contentKey="dboApp.communityPostTransactions.attachmentUrl">Attachment Url</Translate>
            </span>
          </dt>
          <dd>{communityPostTransactionsEntity.attachmentUrl}</dd>
          <dt>
            <span id="communityPostId">
              <Translate contentKey="dboApp.communityPostTransactions.communityPostId">Community Post Id</Translate>
            </span>
          </dt>
          <dd>{communityPostTransactionsEntity.communityPostId}</dd>
          <dt>
            <span id="communityPostTransactionId">
              <Translate contentKey="dboApp.communityPostTransactions.communityPostTransactionId">Community Post Transaction Id</Translate>
            </span>
          </dt>
          <dd>{communityPostTransactionsEntity.communityPostTransactionId}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.communityPostTransactions.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{communityPostTransactionsEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.communityPostTransactions.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {communityPostTransactionsEntity.createdOn ? (
              <TextFormat value={communityPostTransactionsEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.communityPostTransactions.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{communityPostTransactionsEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdatedBy">
              <Translate contentKey="dboApp.communityPostTransactions.lastUpdatedBy">Last Updated By</Translate>
            </span>
          </dt>
          <dd>{communityPostTransactionsEntity.lastUpdatedBy}</dd>
          <dt>
            <span id="lastUpdatedOn">
              <Translate contentKey="dboApp.communityPostTransactions.lastUpdatedOn">Last Updated On</Translate>
            </span>
          </dt>
          <dd>
            {communityPostTransactionsEntity.lastUpdatedOn ? (
              <TextFormat value={communityPostTransactionsEntity.lastUpdatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/community-post-transactions" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/community-post-transactions/${communityPostTransactionsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommunityPostTransactionsDetail;
