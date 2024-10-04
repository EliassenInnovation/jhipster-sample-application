import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './sub-category.reducer';

export const SubCategoryUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const subCategoryEntity = useAppSelector(state => state.subCategory.entity);
  const loading = useAppSelector(state => state.subCategory.loading);
  const updating = useAppSelector(state => state.subCategory.updating);
  const updateSuccess = useAppSelector(state => state.subCategory.updateSuccess);

  const handleClose = () => {
    navigate('/sub-category');
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
    if (values.categoryId !== undefined && typeof values.categoryId !== 'number') {
      values.categoryId = Number(values.categoryId);
    }
    if (values.createdBy !== undefined && typeof values.createdBy !== 'number') {
      values.createdBy = Number(values.createdBy);
    }
    if (values.subCategoryId !== undefined && typeof values.subCategoryId !== 'number') {
      values.subCategoryId = Number(values.subCategoryId);
    }
    if (values.updatedBy !== undefined && typeof values.updatedBy !== 'number') {
      values.updatedBy = Number(values.updatedBy);
    }

    const entity = {
      ...subCategoryEntity,
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
          ...subCategoryEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.subCategory.home.createOrEditLabel" data-cy="SubCategoryCreateUpdateHeading">
            <Translate contentKey="dboApp.subCategory.home.createOrEditLabel">Create or edit a SubCategory</Translate>
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
                  id="sub-category-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.subCategory.categoryId')}
                id="sub-category-categoryId"
                name="categoryId"
                data-cy="categoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.createdBy')}
                id="sub-category-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.createdOn')}
                id="sub-category-createdOn"
                name="createdOn"
                data-cy="createdOn"
                type="date"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.isActive')}
                id="sub-category-isActive"
                name="isActive"
                data-cy="isActive"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.subCategoryCode')}
                id="sub-category-subCategoryCode"
                name="subCategoryCode"
                data-cy="subCategoryCode"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.subCategoryId')}
                id="sub-category-subCategoryId"
                name="subCategoryId"
                data-cy="subCategoryId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.subCategoryName')}
                id="sub-category-subCategoryName"
                name="subCategoryName"
                data-cy="subCategoryName"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.updatedBy')}
                id="sub-category-updatedBy"
                name="updatedBy"
                data-cy="updatedBy"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.subCategory.updatedOn')}
                id="sub-category-updatedOn"
                name="updatedOn"
                data-cy="updatedOn"
                type="date"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/sub-category" replace color="info">
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

export default SubCategoryUpdate;
