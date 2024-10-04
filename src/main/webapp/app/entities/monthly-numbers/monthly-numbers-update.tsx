import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './monthly-numbers.reducer';

export const MonthlyNumbersUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const monthlyNumbersEntity = useAppSelector(state => state.monthlyNumbers.entity);
  const loading = useAppSelector(state => state.monthlyNumbers.loading);
  const updating = useAppSelector(state => state.monthlyNumbers.updating);
  const updateSuccess = useAppSelector(state => state.monthlyNumbers.updateSuccess);

  const handleClose = () => {
    navigate('/monthly-numbers');
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
    if (values.actualMonthId !== undefined && typeof values.actualMonthId !== 'number') {
      values.actualMonthId = Number(values.actualMonthId);
    }
    if (values.enrollment !== undefined && typeof values.enrollment !== 'number') {
      values.enrollment = Number(values.enrollment);
    }
    if (values.freeAndReducedPercent !== undefined && typeof values.freeAndReducedPercent !== 'number') {
      values.freeAndReducedPercent = Number(values.freeAndReducedPercent);
    }
    if (values.iD !== undefined && typeof values.iD !== 'number') {
      values.iD = Number(values.iD);
    }
    if (values.mealsServed !== undefined && typeof values.mealsServed !== 'number') {
      values.mealsServed = Number(values.mealsServed);
    }
    if (values.monthId !== undefined && typeof values.monthId !== 'number') {
      values.monthId = Number(values.monthId);
    }
    if (values.numberOfDistricts !== undefined && typeof values.numberOfDistricts !== 'number') {
      values.numberOfDistricts = Number(values.numberOfDistricts);
    }
    if (values.numberOfSites !== undefined && typeof values.numberOfSites !== 'number') {
      values.numberOfSites = Number(values.numberOfSites);
    }
    if (values.schoolDistrictId !== undefined && typeof values.schoolDistrictId !== 'number') {
      values.schoolDistrictId = Number(values.schoolDistrictId);
    }

    const entity = {
      ...monthlyNumbersEntity,
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
          ...monthlyNumbersEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.monthlyNumbers.home.createOrEditLabel" data-cy="MonthlyNumbersCreateUpdateHeading">
            <Translate contentKey="dboApp.monthlyNumbers.home.createOrEditLabel">Create or edit a MonthlyNumbers</Translate>
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
                  id="monthly-numbers-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.actualMonthId')}
                id="monthly-numbers-actualMonthId"
                name="actualMonthId"
                data-cy="actualMonthId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.createdOn')}
                id="monthly-numbers-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.enrollment')}
                id="monthly-numbers-enrollment"
                name="enrollment"
                data-cy="enrollment"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.freeAndReducedPercent')}
                id="monthly-numbers-freeAndReducedPercent"
                name="freeAndReducedPercent"
                data-cy="freeAndReducedPercent"
                type="text"
              />
              <ValidatedField label={translate('dboApp.monthlyNumbers.iD')} id="monthly-numbers-iD" name="iD" data-cy="iD" type="text" />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.isActive')}
                id="monthly-numbers-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.mealsServed')}
                id="monthly-numbers-mealsServed"
                name="mealsServed"
                data-cy="mealsServed"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.modifiedOn')}
                id="monthly-numbers-modifiedOn"
                name="modifiedOn"
                data-cy="modifiedOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.monthId')}
                id="monthly-numbers-monthId"
                name="monthId"
                data-cy="monthId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.numberOfDistricts')}
                id="monthly-numbers-numberOfDistricts"
                name="numberOfDistricts"
                data-cy="numberOfDistricts"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.numberOfSites')}
                id="monthly-numbers-numberOfSites"
                name="numberOfSites"
                data-cy="numberOfSites"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.regDate')}
                id="monthly-numbers-regDate"
                name="regDate"
                data-cy="regDate"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.schoolDistrictId')}
                id="monthly-numbers-schoolDistrictId"
                name="schoolDistrictId"
                data-cy="schoolDistrictId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.monthlyNumbers.year')}
                id="monthly-numbers-year"
                name="year"
                data-cy="year"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/monthly-numbers" replace color="info">
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

export default MonthlyNumbersUpdate;
