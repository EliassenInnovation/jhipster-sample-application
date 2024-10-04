import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './school-district.reducer';

export const SchoolDistrictDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const schoolDistrictEntity = useAppSelector(state => state.schoolDistrict.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="schoolDistrictDetailsHeading">
          <Translate contentKey="dboApp.schoolDistrict.detail.title">SchoolDistrict</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.id}</dd>
          <dt>
            <span id="city">
              <Translate contentKey="dboApp.schoolDistrict.city">City</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.city}</dd>
          <dt>
            <span id="contractCompany">
              <Translate contentKey="dboApp.schoolDistrict.contractCompany">Contract Company</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.contractCompany}</dd>
          <dt>
            <span id="country">
              <Translate contentKey="dboApp.schoolDistrict.country">Country</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.country}</dd>
          <dt>
            <span id="createdBy">
              <Translate contentKey="dboApp.schoolDistrict.createdBy">Created By</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.createdBy}</dd>
          <dt>
            <span id="createdOn">
              <Translate contentKey="dboApp.schoolDistrict.createdOn">Created On</Translate>
            </span>
          </dt>
          <dd>
            {schoolDistrictEntity.createdOn ? (
              <TextFormat value={schoolDistrictEntity.createdOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="districtLogo">
              <Translate contentKey="dboApp.schoolDistrict.districtLogo">District Logo</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.districtLogo}</dd>
          <dt>
            <span id="districtName">
              <Translate contentKey="dboApp.schoolDistrict.districtName">District Name</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.districtName}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="dboApp.schoolDistrict.email">Email</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.email}</dd>
          <dt>
            <span id="foodServiceOptions">
              <Translate contentKey="dboApp.schoolDistrict.foodServiceOptions">Food Service Options</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.foodServiceOptions}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="dboApp.schoolDistrict.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="lastUpdatedBy">
              <Translate contentKey="dboApp.schoolDistrict.lastUpdatedBy">Last Updated By</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.lastUpdatedBy}</dd>
          <dt>
            <span id="lastUpdatedOn">
              <Translate contentKey="dboApp.schoolDistrict.lastUpdatedOn">Last Updated On</Translate>
            </span>
          </dt>
          <dd>
            {schoolDistrictEntity.lastUpdatedOn ? (
              <TextFormat value={schoolDistrictEntity.lastUpdatedOn} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="dboApp.schoolDistrict.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.phoneNumber}</dd>
          <dt>
            <span id="schoolDistrictId">
              <Translate contentKey="dboApp.schoolDistrict.schoolDistrictId">School District Id</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.schoolDistrictId}</dd>
          <dt>
            <span id="siteCode">
              <Translate contentKey="dboApp.schoolDistrict.siteCode">Site Code</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.siteCode}</dd>
          <dt>
            <span id="state">
              <Translate contentKey="dboApp.schoolDistrict.state">State</Translate>
            </span>
          </dt>
          <dd>{schoolDistrictEntity.state}</dd>
        </dl>
        <Button tag={Link} to="/school-district" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/school-district/${schoolDistrictEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SchoolDistrictDetail;
