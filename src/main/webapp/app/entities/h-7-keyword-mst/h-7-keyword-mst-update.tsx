import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './h-7-keyword-mst.reducer';

export const H7KeywordMstUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const h7KeywordMstEntity = useAppSelector(state => state.h7KeywordMst.entity);
  const loading = useAppSelector(state => state.h7KeywordMst.loading);
  const updating = useAppSelector(state => state.h7KeywordMst.updating);
  const updateSuccess = useAppSelector(state => state.h7KeywordMst.updateSuccess);

  const handleClose = () => {
    navigate('/h-7-keyword-mst');
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
    if (values.h7keywordId !== undefined && typeof values.h7keywordId !== 'number') {
      values.h7keywordId = Number(values.h7keywordId);
    }

    const entity = {
      ...h7KeywordMstEntity,
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
          ...h7KeywordMstEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.h7KeywordMst.home.createOrEditLabel" data-cy="H7KeywordMstCreateUpdateHeading">
            <Translate contentKey="dboApp.h7KeywordMst.home.createOrEditLabel">Create or edit a H7KeywordMst</Translate>
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
                  id="h-7-keyword-mst-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.h7KeywordMst.h7Group')}
                id="h-7-keyword-mst-h7Group"
                name="h7Group"
                data-cy="h7Group"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.h7KeywordMst.h7Keyword')}
                id="h-7-keyword-mst-h7Keyword"
                name="h7Keyword"
                data-cy="h7Keyword"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.h7KeywordMst.h7keywordId')}
                id="h-7-keyword-mst-h7keywordId"
                name="h7keywordId"
                data-cy="h7keywordId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.h7KeywordMst.iocGroup')}
                id="h-7-keyword-mst-iocGroup"
                name="iocGroup"
                data-cy="iocGroup"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/h-7-keyword-mst" replace color="info">
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

export default H7KeywordMstUpdate;
