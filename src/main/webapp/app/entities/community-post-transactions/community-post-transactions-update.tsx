import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './community-post-transactions.reducer';

export const CommunityPostTransactionsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const communityPostTransactionsEntity = useAppSelector(state => state.communityPostTransactions.entity);
  const loading = useAppSelector(state => state.communityPostTransactions.loading);
  const updating = useAppSelector(state => state.communityPostTransactions.updating);
  const updateSuccess = useAppSelector(state => state.communityPostTransactions.updateSuccess);

  const handleClose = () => {
    navigate('/community-post-transactions');
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

    const entity = {
      ...communityPostTransactionsEntity,
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
          ...communityPostTransactionsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.communityPostTransactions.home.createOrEditLabel" data-cy="CommunityPostTransactionsCreateUpdateHeading">
            <Translate contentKey="dboApp.communityPostTransactions.home.createOrEditLabel">
              Create or edit a CommunityPostTransactions
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
                  id="community-post-transactions-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.attachmentUrl')}
                id="community-post-transactions-attachmentUrl"
                name="attachmentUrl"
                data-cy="attachmentUrl"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.communityPostId')}
                id="community-post-transactions-communityPostId"
                name="communityPostId"
                data-cy="communityPostId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.communityPostTransactionId')}
                id="community-post-transactions-communityPostTransactionId"
                name="communityPostTransactionId"
                data-cy="communityPostTransactionId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.createdBy')}
                id="community-post-transactions-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.createdOn')}
                id="community-post-transactions-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.isActive')}
                id="community-post-transactions-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.lastUpdatedBy')}
                id="community-post-transactions-lastUpdatedBy"
                name="lastUpdatedBy"
                data-cy="lastUpdatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.communityPostTransactions.lastUpdatedOn')}
                id="community-post-transactions-lastUpdatedOn"
                name="lastUpdatedOn"
                data-cy="lastUpdatedOn"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/community-post-transactions" replace color="info">
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

export default CommunityPostTransactionsUpdate;
