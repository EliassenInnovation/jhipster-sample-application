import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './manufacturer.reducer';

export const ManufacturerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const manufacturerEntity = useAppSelector(state => state.manufacturer.entity);
  const loading = useAppSelector(state => state.manufacturer.loading);
  const updating = useAppSelector(state => state.manufacturer.updating);
  const updateSuccess = useAppSelector(state => state.manufacturer.updateSuccess);

  const handleClose = () => {
    navigate('/manufacturer');
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
    if (values.manufacturerId !== undefined && typeof values.manufacturerId !== 'number') {
      values.manufacturerId = Number(values.manufacturerId);
    }

    const entity = {
      ...manufacturerEntity,
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
          ...manufacturerEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.manufacturer.home.createOrEditLabel" data-cy="ManufacturerCreateUpdateHeading">
            <Translate contentKey="dboApp.manufacturer.home.createOrEditLabel">Create or edit a Manufacturer</Translate>
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
                  id="manufacturer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.manufacturer.createdBy')}
                id="manufacturer-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.createdOn')}
                id="manufacturer-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.email')}
                id="manufacturer-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.firstName')}
                id="manufacturer-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.glnNumber')}
                id="manufacturer-glnNumber"
                name="glnNumber"
                data-cy="glnNumber"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.isActive')}
                id="manufacturer-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.lastName')}
                id="manufacturer-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.manufacturer')}
                id="manufacturer-manufacturer"
                name="manufacturer"
                data-cy="manufacturer"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.manufacturerId')}
                id="manufacturer-manufacturerId"
                name="manufacturerId"
                data-cy="manufacturerId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.password')}
                id="manufacturer-password"
                name="password"
                data-cy="password"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.manufacturer.phoneNumber')}
                id="manufacturer-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/manufacturer" replace color="info">
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

export default ManufacturerUpdate;
