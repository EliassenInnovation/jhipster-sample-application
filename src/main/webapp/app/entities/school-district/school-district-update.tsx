import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './school-district.reducer';

export const SchoolDistrictUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const schoolDistrictEntity = useAppSelector(state => state.schoolDistrict.entity);
  const loading = useAppSelector(state => state.schoolDistrict.loading);
  const updating = useAppSelector(state => state.schoolDistrict.updating);
  const updateSuccess = useAppSelector(state => state.schoolDistrict.updateSuccess);

  const handleClose = () => {
    navigate('/school-district');
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
    if (values.state !== undefined && typeof values.state !== 'number') {
      values.state = Number(values.state);
    }

    const entity = {
      ...schoolDistrictEntity,
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
          ...schoolDistrictEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.schoolDistrict.home.createOrEditLabel" data-cy="SchoolDistrictCreateUpdateHeading">
            <Translate contentKey="dboApp.schoolDistrict.home.createOrEditLabel">Create or edit a SchoolDistrict</Translate>
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
                  id="school-district-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.schoolDistrict.city')}
                id="school-district-city"
                name="city"
                data-cy="city"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.contractCompany')}
                id="school-district-contractCompany"
                name="contractCompany"
                data-cy="contractCompany"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.country')}
                id="school-district-country"
                name="country"
                data-cy="country"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.createdBy')}
                id="school-district-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.createdOn')}
                id="school-district-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.districtLogo')}
                id="school-district-districtLogo"
                name="districtLogo"
                data-cy="districtLogo"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.districtName')}
                id="school-district-districtName"
                name="districtName"
                data-cy="districtName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.email')}
                id="school-district-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.foodServiceOptions')}
                id="school-district-foodServiceOptions"
                name="foodServiceOptions"
                data-cy="foodServiceOptions"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.isActive')}
                id="school-district-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.lastUpdatedBy')}
                id="school-district-lastUpdatedBy"
                name="lastUpdatedBy"
                data-cy="lastUpdatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.lastUpdatedOn')}
                id="school-district-lastUpdatedOn"
                name="lastUpdatedOn"
                data-cy="lastUpdatedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.phoneNumber')}
                id="school-district-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.schoolDistrictId')}
                id="school-district-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.siteCode')}
                id="school-district-siteCode"
                name="siteCode"
                data-cy="siteCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.schoolDistrict.state')}
                id="school-district-state"
                name="state"
                data-cy="state"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/school-district" replace color="info">
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

export default SchoolDistrictUpdate;
