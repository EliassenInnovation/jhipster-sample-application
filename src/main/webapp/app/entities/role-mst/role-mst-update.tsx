import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './role-mst.reducer';

export const RoleMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const roleMstEntity = useAppSelector(state => state.roleMst.entity);
  const loading = useAppSelector(state => state.roleMst.loading);
  const updating = useAppSelector(state => state.roleMst.updating);
  const updateSuccess = useAppSelector(state => state.roleMst.updateSuccess);

  const handleClose = () => {
    navigate('/role-mst');
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
    if (values.parentRoleId !== undefined && typeof values.parentRoleId !== 'number') {
      values.parentRoleId = Number(values.parentRoleId);
    }
    if (values.roleId !== undefined && typeof values.roleId !== 'number') {
      values.roleId = Number(values.roleId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...roleMstEntity,
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
          ...roleMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.roleMst.home.createOrEditLabel" data-cy="RoleMstCreateUpdateHeading">
            <Translate contentKey="dboApp.roleMst.home.createOrEditLabel">Create or edit a RoleMst</Translate>
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
                  id="role-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.roleMst.createdBy')}
                id="role-mst-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.roleMst.createdOn')}
                id="role-mst-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.roleMst.isActive')}
                id="role-mst-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.roleMst.parentRoleId')}
                id="role-mst-parentRoleId"
                name="parentRoleId"
                data-cy="parentRoleId"
                type="text"
              />
              <ValidatedField label={translate('dboApp.roleMst.roleId')} id="role-mst-roleId" name="roleId" data-cy="roleId" type="text" />
              <ValidatedField
                label={translate('dboApp.roleMst.roleName')}
                id="role-mst-roleName"
                name="roleName"
                data-cy="roleName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.roleMst.updatedBy')}
                id="role-mst-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.roleMst.updatedOn')}
                id="role-mst-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/role-mst" replace color="info">
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

export default RoleMstUpdate;
