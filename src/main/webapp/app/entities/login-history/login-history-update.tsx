import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './login-history.reducer';

export const LoginHistoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const loginHistoryEntity = useAppSelector(state => state.loginHistory.entity);
  const loading = useAppSelector(state => state.loginHistory.loading);
  const updating = useAppSelector(state => state.loginHistory.updating);
  const updateSuccess = useAppSelector(state => state.loginHistory.updateSuccess);

  const handleClose = () => {
    navigate('/login-history');
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
    if (values.forgotPin !== undefined && typeof values.forgotPin !== 'number') {
      values.forgotPin = Number(values.forgotPin);
    }
    if (values.loginLogId !== undefined && typeof values.loginLogId !== 'number') {
      values.loginLogId = Number(values.loginLogId);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...loginHistoryEntity,
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
          ...loginHistoryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.loginHistory.home.createOrEditLabel" data-cy="LoginHistoryCreateUpdateHeading">
            <Translate contentKey="dboApp.loginHistory.home.createOrEditLabel">Create or edit a LoginHistory</Translate>
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
                  id="login-history-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.loginHistory.forgotPin')}
                id="login-history-forgotPin"
                name="forgotPin"
                data-cy="forgotPin"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.loginHistory.isActive')}
                id="login-history-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.loginHistory.loginDate')}
                id="login-history-loginDate"
                name="loginDate"
                data-cy="loginDate"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.loginHistory.loginLogId')}
                id="login-history-loginLogId"
                name="loginLogId"
                data-cy="loginLogId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.loginHistory.loginType')}
                id="login-history-loginType"
                name="loginType"
                data-cy="loginType"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.loginHistory.logOutDate')}
                id="login-history-logOutDate"
                name="logOutDate"
                data-cy="logOutDate"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.loginHistory.userId')}
                id="login-history-userId"
                name="userId"
                data-cy="userId"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/login-history" replace color="info">
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

export default LoginHistoryUpdate;
