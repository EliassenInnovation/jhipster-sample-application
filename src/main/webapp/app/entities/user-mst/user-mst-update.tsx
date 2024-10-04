import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './user-mst.reducer';

export const UserMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userMstEntity = useAppSelector(state => state.userMst.entity);
  const loading = useAppSelector(state => state.userMst.loading);
  const updating = useAppSelector(state => state.userMst.updating);
  const updateSuccess = useAppSelector(state => state.userMst.updateSuccess);

  const handleClose = () => {
    navigate('/user-mst');
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
    if (values.createBy !== undefined && typeof values.createBy !== 'number') {
      values.createBy = Number(values.createBy);
    }
    if (values.manufacturerId !== undefined && typeof values.manufacturerId !== 'number') {
      values.manufacturerId = Number(values.manufacturerId);
    }
    if (values.roleId !== undefined && typeof values.roleId !== 'number') {
      values.roleId = Number(values.roleId);
    }
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }
    if (values.state !== undefined && typeof values.state !== 'number') {
      values.state = Number(values.state);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }
    if (values.userId !== undefined && typeof values.userId !== 'number') {
      values.userId = Number(values.userId);
    }

    const entity = {
      ...userMstEntity,
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
          ...userMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.userMst.home.createOrEditLabel" data-cy="UserMstCreateUpdateHeading">
            <Translate contentKey="dboApp.userMst.home.createOrEditLabel">Create or edit a UserMst</Translate>
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
                  id="user-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.userMst.addressLine1')}
                id="user-mst-addressLine1"
                name="addressLine1"
                data-cy="addressLine1"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.addressLine2')}
                id="user-mst-addressLine2"
                name="addressLine2"
                data-cy="addressLine2"
                type="text"
              />
              <ValidatedField label={translate('dboApp.userMst.city')} id="user-mst-city" name="city" data-cy="city" type="text" />
              <ValidatedField
                label={translate('dboApp.userMst.country')}
                id="user-mst-country"
                name="country"
                data-cy="country"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.coverImage')}
                id="user-mst-coverImage"
                name="coverImage"
                data-cy="coverImage"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.userMst.createBy')}
                id="user-mst-createBy"
                name="createBy"
                data-cy="createBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.createdOn')}
                id="user-mst-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField label={translate('dboApp.userMst.email')} id="user-mst-email" name="email" data-cy="email" type="text" />
              <ValidatedField
                label={translate('dboApp.userMst.firstName')}
                id="user-mst-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.isActive')}
                id="user-mst-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.userMst.lastName')}
                id="user-mst-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.manufacturerId')}
                id="user-mst-manufacturerId"
                name="manufacturerId"
                data-cy="manufacturerId"
                type="text"
              />
              <ValidatedField label={translate('dboApp.userMst.mobile')} id="user-mst-mobile" name="mobile" data-cy="mobile" type="text" />
              <ValidatedField
                label={translate('dboApp.userMst.objectId')}
                id="user-mst-objectId"
                name="objectId"
                data-cy="objectId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.password')}
                id="user-mst-password"
                name="password"
                data-cy="password"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.profileImage')}
                id="user-mst-profileImage"
                name="profileImage"
                data-cy="profileImage"
                type="textarea"
              />
              <ValidatedField label={translate('dboApp.userMst.roleId')} id="user-mst-roleId" name="roleId" data-cy="roleId" type="text" />
              <ValidatedField
                label={translate('dboApp.userMst.schoolDistrictId')}
                id="user-mst-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField label={translate('dboApp.userMst.state')} id="user-mst-state" name="state" data-cy="state" type="text" />
              <ValidatedField
                label={translate('dboApp.userMst.updatedBy')}
                id="user-mst-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.userMst.updatedOn')}
                id="user-mst-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <ValidatedField label={translate('dboApp.userMst.userId')} id="user-mst-userId" name="userId" data-cy="userId" type="text" />
              <ValidatedField
                label={translate('dboApp.userMst.zipCode')}
                id="user-mst-zipCode"
                name="zipCode"
                data-cy="zipCode"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-mst" replace color="info">
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

export default UserMstUpdate;
