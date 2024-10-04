import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './support-ticket-mst.reducer';

export const SupportTicketMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const supportTicketMstEntity = useAppSelector(state => state.supportTicketMst.entity);
  const loading = useAppSelector(state => state.supportTicketMst.loading);
  const updating = useAppSelector(state => state.supportTicketMst.updating);
  const updateSuccess = useAppSelector(state => state.supportTicketMst.updateSuccess);

  const handleClose = () => {
    navigate('/support-ticket-mst');
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
    if (values.lastUpdatedBy !== undefined && typeof values.lastUpdatedBy !== 'number') {
      values.lastUpdatedBy = Number(values.lastUpdatedBy);
    }
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }
    if (values.supportCategoryId !== undefined && typeof values.supportCategoryId !== 'number') {
      values.supportCategoryId = Number(values.supportCategoryId);
    }
    if (values.ticketId !== undefined && typeof values.ticketId !== 'number') {
      values.ticketId = Number(values.ticketId);
    }
    if (values.ticketReferenceNumber !== undefined && typeof values.ticketReferenceNumber !== 'number') {
      values.ticketReferenceNumber = Number(values.ticketReferenceNumber);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...supportTicketMstEntity,
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
          ...supportTicketMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.supportTicketMst.home.createOrEditLabel" data-cy="SupportTicketMstCreateUpdateHeading">
            <Translate contentKey="dboApp.supportTicketMst.home.createOrEditLabel">Create or edit a SupportTicketMst</Translate>
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
                  id="support-ticket-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.supportTicketMst.createdBy')}
                id="support-ticket-mst-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.createdOn')}
                id="support-ticket-mst-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.email')}
                id="support-ticket-mst-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.isActive')}
                id="support-ticket-mst-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.isWithOutLogin')}
                id="support-ticket-mst-isWithOutLogin"
                name="isWithOutLogin"
                data-cy="isWithOutLogin"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.lastUpdatedBy')}
                id="support-ticket-mst-lastUpdatedBy"
                name="lastUpdatedBy"
                data-cy="lastUpdatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.lastUpdatedOn')}
                id="support-ticket-mst-lastUpdatedOn"
                name="lastUpdatedOn"
                data-cy="lastUpdatedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.priority')}
                id="support-ticket-mst-priority"
                name="priority"
                data-cy="priority"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.schoolDistrictId')}
                id="support-ticket-mst-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.status')}
                id="support-ticket-mst-status"
                name="status"
                data-cy="status"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.subject')}
                id="support-ticket-mst-subject"
                name="subject"
                data-cy="subject"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.supportCategoryId')}
                id="support-ticket-mst-supportCategoryId"
                name="supportCategoryId"
                data-cy="supportCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.ticketId')}
                id="support-ticket-mst-ticketId"
                name="ticketId"
                data-cy="ticketId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.ticketReferenceNumber')}
                id="support-ticket-mst-ticketReferenceNumber"
                name="ticketReferenceNumber"
                data-cy="ticketReferenceNumber"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.userId')}
                id="support-ticket-mst-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.supportTicketMst.userName')}
                id="support-ticket-mst-userName"
                name="userName"
                data-cy="userName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/support-ticket-mst" replace color="info">
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

export default SupportTicketMstUpdate;
