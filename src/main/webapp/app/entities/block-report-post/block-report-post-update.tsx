import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './block-report-post.reducer';

export const BlockReportPostUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const blockReportPostEntity = useAppSelector(state => state.blockReportPost.entity);
  const loading = useAppSelector(state => state.blockReportPost.loading);
  const updating = useAppSelector(state => state.blockReportPost.updating);
  const updateSuccess = useAppSelector(state => state.blockReportPost.updateSuccess);

  const handleClose = () => {
    navigate('/block-report-post');
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
    if (values.requestedBy !== undefined && typeof values.requestedBy !== 'number') {
      values.requestedBy = Number(values.requestedBy);
    }

    const entity = {
      ...blockReportPostEntity,
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
          ...blockReportPostEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="dboApp.blockReportPost.home.createOrEditLabel" data-cy="BlockReportPostCreateUpdateHeading">
            <Translate contentKey="dboApp.blockReportPost.home.createOrEditLabel">Create or edit a BlockReportPost</Translate>
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
                  id="block-report-post-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('dboApp.blockReportPost.blockCategories')}
                id="block-report-post-blockCategories"
                name="blockCategories"
                data-cy="blockCategories"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.blockReportPost.blockingReason')}
                id="block-report-post-blockingReason"
                name="blockingReason"
                data-cy="blockingReason"
                type="textarea"
              />
              <ValidatedField
                label={translate('dboApp.blockReportPost.postBlockReportId')}
                id="block-report-post-postBlockReportId"
                name="postBlockReportId"
                data-cy="postBlockReportId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.blockReportPost.postId')}
                id="block-report-post-postId"
                name="postId"
                data-cy="postId"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.blockReportPost.postType')}
                id="block-report-post-postType"
                name="postType"
                data-cy="postType"
                type="text"
              />
              <ValidatedField
                label={translate('dboApp.blockReportPost.requestedBy')}
                id="block-report-post-requestedBy"
                name="requestedBy"
                data-cy="requestedBy"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/block-report-post" replace color="info">
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

export default BlockReportPostUpdate;
