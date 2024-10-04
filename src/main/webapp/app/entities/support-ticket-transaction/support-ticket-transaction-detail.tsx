import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './support-ticket-transaction.reducer';

export const SupportTicketTransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const supportTicketTransactionEntity = useAppSelector(state => state.supportTicketTransaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="supportTicketTransactionDetailsHeading">
          <Translate contentKey="dboApp.supportTicketTransaction.detail.title">SupportTicketTransaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.supportTicketTransaction.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.supportTicketTransaction.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {supportTicketTransactionEntity.createdOn ? (
              <TextFormat value={supportTicketTransactionEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="description">
              <Translate contentKey="dboApp.supportTicketTransaction.description">Description</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.description}</dd>
          <dt>
            <span id="fileExtension">
              <Translate contentKey="dboApp.supportTicketTransaction.fileExtension">File Extension</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.fileExtension}</dd>
          <dt>
            <span id="fileName">
              <Translate contentKey="dboApp.supportTicketTransaction.fileName">File Name</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.fileName}</dd>
          <dt>
            <span id="filePath">
              <Translate contentKey="dboApp.supportTicketTransaction.filePath">File Path</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.filePath}</dd>
          <dt>
            <span id="fileSize">
              <Translate contentKey="dboApp.supportTicketTransaction.fileSize">File Size</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.fileSize}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.supportTicketTransaction.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isSentByFigSupport">
              <Translate contentKey="dboApp.supportTicketTransaction.isSentByFigSupport">Is Sent By Fig Support</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.isSentByFigSupport ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdatedBy">
              <Translate contentKey="dboApp.supportTicketTransaction.lastUpdatedBy">Last Updated By</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.lastUpdatedBy}</dd>
          <dt>
            <span id="lastUpdatedOn">
              <Translate contentKey="dboApp.supportTicketTransaction.lastUpdatedOn">Last Updated On</Translate>
            </span>
          </dt>
          <dd>
            {supportTicketTransactionEntity.lastUpdatedOn ? (
              <TextFormat value={supportTicketTransactionEntity.lastUpdatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="ticketId">
              <Translate contentKey="dboApp.supportTicketTransaction.ticketId">Ticket Id</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.ticketId}</dd>
          <dt>
            <span id="ticketTransactionId">
              <Translate contentKey="dboApp.supportTicketTransaction.ticketTransactionId">Ticket Transaction Id</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.ticketTransactionId}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dboApp.supportTicketTransaction.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{supportTicketTransactionEntity.userId}</dd>
        </dl>
        <Button tag={Link} to="/support-ticket-transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/support-ticket-transaction/${supportTicketTransactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SupportTicketTransactionDetail;
