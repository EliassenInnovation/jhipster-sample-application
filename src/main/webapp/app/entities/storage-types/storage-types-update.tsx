import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './storage-types.reducer';

export const StorageTypesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const storageTypesEntity = useAppSelector(state => state.storageTypes.entity);
  const loading = useAppSelector(state => state.storageTypes.loading);
  const updating = useAppSelector(state => state.storageTypes.updating);
  const updateSuccess = useAppSelector(state => state.storageTypes.updateSuccess);

  const handleClose = () => {
    navigate('/storage-types');
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
    if (values.storageTypeId !== undefined && typeof values.storageTypeId !== 'number') {
      values.storageTypeId = Number(values.storageTypeId);
    }

    const entity = {
      ...storageTypesEntity,
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
          ...storageTypesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.storageTypes.home.createOrEditLabel" data-cy="StorageTypesCreateUpdateHeading">
            <Translate contentKey="dboApp.storageTypes.home.createOrEditLabel">Create or edit a StorageTypes</Translate>
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
                  id="storage-types-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.storageTypes.storageTypeId')}
                id="storage-types-storageTypeId"
                name="storageTypeId"
                data-cy="storageTypeId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.storageTypes.storageTypeName')}
                id="storage-types-storageTypeName"
                name="storageTypeName"
                data-cy="storageTypeName"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/storage-types" replace color="info">
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

export default StorageTypesUpdate;
