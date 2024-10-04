import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './support-ticket-transaction.reducer';

export const SupportTicketTransactionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const supportTicketTransactionEntity = useAppSelector(state => state.supportTicketTransaction.entity);
  const loading = useAppSelector(state => state.supportTicketTransaction.loading);
  const updating = useAppSelector(state => state.supportTicketTransaction.updating);
  const updateSuccess = useAppSelector(state => state.supportTicketTransaction.updateSuccess);

  const handleClose = () => {
    navigate('/support-ticket-transaction');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.createdBy !== undefined && typeof values.createdBy !== 'number') {
      values.createdBy = Number(values.createdBy);
    }
    if (values.fileSize !== undefined && typeof values.fileSize !== 'number') {
      values.fileSize = Number(values.fileSize);
    }
    if (values.lastUpdatedBy !== undefined && typeof values.lastUpdatedBy !== 'number') {
      values.lastUpdatedBy = Number(values.lastUpdatedBy);
    }
    if (values.ticketId !== undefined && typeof values.ticketId !== 'number') {
      values.ticketId = Number(values.ticketId);
    }
    if (values.ticketTransactionId !== undefined && typeof values.ticketTransactionId !== 'number') {
      values.ticketTransactionId = Number(values.ticketTransactionId);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...supportTicketTransactionEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...supportTicketTransactionEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.supportTicketTransaction.home.createOrEditLabel" data-cy="SupportTicketTransactionCreateUpdateHeading">
            <Translate contentKey="dboApp.supportTicketTransaction.home.createOrEditLabel">
              Create or edit a SupportTicketTransaction
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="support-ticket-transaction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.createdBy')}
                id="support-ticket-transaction-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.createdOn')}
                id="support-ticket-transaction-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.description')}
                id="support-ticket-transaction-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.fileExtension')}
                id="support-ticket-transaction-fileExtension"
                name="fileExtension"
                data-cy="fileExtension"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.fileName')}
                id="support-ticket-transaction-fileName"
                name="fileName"
                data-cy="fileName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.filePath')}
                id="support-ticket-transaction-filePath"
                name="filePath"
                data-cy="filePath"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.fileSize')}
                id="support-ticket-transaction-fileSize"
                name="fileSize"
                data-cy="fileSize"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.isActive')}
                id="support-ticket-transaction-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.isSentByFigSupport')}
                id="support-ticket-transaction-isSentByFigSupport"
                name="isSentByFigSupport"
                data-cy="isSentByFigSupport"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.lastUpdatedBy')}
                id="support-ticket-transaction-lastUpdatedBy"
                name="lastUpdatedBy"
                data-cy="lastUpdatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.lastUpdatedOn')}
                id="support-ticket-transaction-lastUpdatedOn"
                name="lastUpdatedOn"
                data-cy="lastUpdatedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.ticketId')}
                id="support-ticket-transaction-ticketId"
                name="ticketId"
                data-cy="ticketId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.ticketTransactionId')}
                id="support-ticket-transaction-ticketTransactionId"
                name="ticketTransactionId"
                data-cy="ticketTransactionId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketTransaction.userId')}
                id="support-ticket-transaction-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/support-ticket-transaction" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default SupportTicketTransactionUpdate;
