import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './application-value.reducer';

export const ApplicationValueUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const applicationValueEntity = useAppSelector(state => state.applicationValue.entity);
  const loading = useAppSelector(state => state.applicationValue.loading);
  const updating = useAppSelector(state => state.applicationValue.updating);
  const updateSuccess = useAppSelector(state => state.applicationValue.updateSuccess);

  const handleClose = () => {
    navigate('/application-value');
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
    if (values.applicationValueId !== undefined && typeof values.applicationValueId !== 'number') {
      values.applicationValueId = Number(values.applicationValueId);
    }
    values.valueDate = convertDateTimeToServer(values.valueDate);
    if (values.valueInt !== undefined && typeof values.valueInt !== 'number') {
      values.valueInt = Number(values.valueInt);
    }
    if (values.valueLong !== undefined && typeof values.valueLong !== 'number') {
      values.valueLong = Number(values.valueLong);
    }

    const entity = {
      ...applicationValueEntity,
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
      ? {
          valueDate: displayDefaultDateTime(),
        }
      : {
          ...applicationValueEntity,
          valueDate: convertDateTimeFromServer(applicationValueEntity.valueDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.applicationValue.home.createOrEditLabel" data-cy="ApplicationValueCreateUpdateHeading">
            <Translate contentKey="dboApp.applicationValue.home.createOrEditLabel">Create or edit a ApplicationValue</Translate>
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
                  id="application-value-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.applicationValue.applicationValueId')}
                id="application-value-applicationValueId"
                name="applicationValueId"
                data-cy="applicationValueId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.applicationValue.key')}
                id="application-value-key"
                name="key"
                data-cy="key"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.applicationValue.valueDate')}
                id="application-value-valueDate"
                name="valueDate"
                data-cy="valueDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('dboApp.applicationValue.valueInt')}
                id="application-value-valueInt"
                name="valueInt"
                data-cy="valueInt"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.applicationValue.valueLong')}
                id="application-value-valueLong"
                name="valueLong"
                data-cy="valueLong"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.applicationValue.valueString')}
                id="application-value-valueString"
                name="valueString"
                data-cy="valueString"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/application-value" replace color="info">
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

export default ApplicationValueUpdate;
