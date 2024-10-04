import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './error-log.reducer';

export const ErrorLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const errorLogEntity = useAppSelector(state => state.errorLog.entity);
  const loading = useAppSelector(state => state.errorLog.loading);
  const updating = useAppSelector(state => state.errorLog.updating);
  const updateSuccess = useAppSelector(state => state.errorLog.updateSuccess);

  const handleClose = () => {
    navigate('/error-log');
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
    if (values.errorId !== undefined && typeof values.errorId !== 'number') {
      values.errorId = Number(values.errorId);
    }

    const entity = {
      ...errorLogEntity,
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
          ...errorLogEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.errorLog.home.createOrEditLabel" data-cy="ErrorLogCreateUpdateHeading">
            <Translate contentKey="dboApp.errorLog.home.createOrEditLabel">Create or edit a ErrorLog</Translate>
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
                  id="error-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.errorLog.createdOn')}
                id="error-log-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.errorLog.errorId')}
                id="error-log-errorId"
                name="errorId"
                data-cy="errorId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.errorLog.errorMessage')}
                id="error-log-errorMessage"
                name="errorMessage"
                data-cy="errorMessage"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.errorLog.errorPath')}
                id="error-log-errorPath"
                name="errorPath"
                data-cy="errorPath"
                type="textarea"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/error-log" replace color="info">
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

export default ErrorLogUpdate;
