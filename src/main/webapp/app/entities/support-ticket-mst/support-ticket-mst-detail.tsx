import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './support-ticket-mst.reducer';

export const SupportTicketMstDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const supportTicketMstEntity = useAppSelector(state => state.supportTicketMst.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="supportTicketMstDetailsHeading">
          <Translate contentKey="dboApp.supportTicketMst.detail.title">SupportTicketMst</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.id}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.supportTicketMst.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.supportTicketMst.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {supportTicketMstEntity.createdOn ? (
              <TextFormat value={supportTicketMstEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="email">
              <Translate contentKey="dboApp.supportTicketMst.email">Email</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.email}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.supportTicketMst.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isWithOutLogin">
              <Translate contentKey="dboApp.supportTicketMst.isWithOutLogin">Is With Out Login</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.isWithOutLogin ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdatedBy">
              <Translate contentKey="dboApp.supportTicketMst.lastUpdatedBy">Last Updated By</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.lastUpdatedBy}</dd>
          <dt>
            <span id="lastUpdatedOn">
              <Translate contentKey="dboApp.supportTicketMst.lastUpdatedOn">Last Updated On</Translate>
            </span>
          </dt>
          <dd>
            {supportTicketMstEntity.lastUpdatedOn ? (
              <TextFormat value={supportTicketMstEntity.lastUpdatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="priority">
              <Translate contentKey="dboApp.supportTicketMst.priority">Priority</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.priority}</dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.supportTicketMst.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.schoolDistrictId}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="dboApp.supportTicketMst.status">Status</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.status}</dd>
          <dt>
            <span id="subject">
              <Translate contentKey="dboApp.supportTicketMst.subject">Subject</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.subject}</dd>
          <dt>
            <span id="supportCategoryId">
              <Translate contentKey="dboApp.supportTicketMst.supportCategoryId">Support Category Id</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.supportCategoryId}</dd>
          <dt>
            <span id="ticketId">
              <Translate contentKey="dboApp.supportTicketMst.ticketId">Ticket Id</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.ticketId}</dd>
          <dt>
            <span id="ticketReferenceNumber">
              <Translate contentKey="dboApp.supportTicketMst.ticketReferenceNumber">Ticket Reference Number</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.ticketReferenceNumber}</dd>
          <dt>
            <span id="userId">
              <Translate contentKey="dboApp.supportTicketMst.userId">User Id</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.userId}</dd>
          <dt>
            <span id="userName">
              <Translate contentKey="dboApp.supportTicketMst.userName">User Name</Translate>
            </span>
          </dt>
          <dd>{supportTicketMstEntity.userName}</dd>
        </dl>
        <Button tag={Link} to="/support-ticket-mst" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/support-ticket-mst/${supportTicketMstEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SupportTicketMstDetail;
