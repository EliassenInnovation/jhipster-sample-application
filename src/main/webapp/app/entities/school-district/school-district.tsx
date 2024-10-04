import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Col, Form, FormGroup, Input, InputGroup, Row, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities, searchEntities } from './school-district.reducer';

export const SchoolDistrict = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const schoolDistrictList = useAppSelector(state => state.schoolDistrict.entities);
  const loading = useAppSelector(state => state.schoolDistrict.loading);

  const getAllEntities = () => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    } else {
      dispatch(
        getEntities({
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
  };

  const startSearching = e => {
    if (search) {
      dispatch(
        searchEntities({
          query: search,
          sort: `${sortState.sort},${sortState.order}`,
        }),
      );
    }
    e.preventDefault();
  };

  const clear = () => {
    setSearch('');
    dispatch(getEntities({}));
  };

  const handleSearch = event => setSearch(event.target.value);

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort, search]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="school-district-heading" data-cy="SchoolDistrictHeading">
        <Translate contentKey="dboApp.schoolDistrict.home.title">School Districts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.schoolDistrict.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/school-district/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.schoolDistrict.home.createLabel">Create new School District</Translate>
          </Link>
        </div>
      </h2>
      <Row>
        <Col sm="12">
          <Form onSubmit={startSearching}>
            <FormGroup>
              <InputGroup>
                <Input
                  type="text"
                  name="search"
                  defaultValue={search}
                  onChange={handleSearch}
                  placeholder={translate('dboApp.schoolDistrict.home.search')}
                />
                <Button className="input-group-addon">
                  <FontAwesomeIcon icon="search" />
                </Button>
                <Button type="reset" className="input-group-addon" onClick={clear}>
                  <FontAwesomeIcon icon="trash" />
                </Button>
              </InputGroup>
            </FormGroup>
          </Form>
        </Col>
      </Row>
      <div className="table-responsive">
        {schoolDistrictList && schoolDistrictList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.schoolDistrict.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="dboApp.schoolDistrict.city">City</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('contractCompany')}>
                  <Translate contentKey="dboApp.schoolDistrict.contractCompany">Contract Company</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contractCompany')} />
                </th>
                <th className="hand" onClick={sort('country')}>
                  <Translate contentKey="dboApp.schoolDistrict.country">Country</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('country')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.schoolDistrict.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.schoolDistrict.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('districtLogo')}>
                  <Translate contentKey="dboApp.schoolDistrict.districtLogo">District Logo</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('districtLogo')} />
                </th>
                <th className="hand" onClick={sort('districtName')}>
                  <Translate contentKey="dboApp.schoolDistrict.districtName">District Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('districtName')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="dboApp.schoolDistrict.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('foodServiceOptions')}>
                  <Translate contentKey="dboApp.schoolDistrict.foodServiceOptions">Food Service Options</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('foodServiceOptions')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.schoolDistrict.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedBy')}>
                  <Translate contentKey="dboApp.schoolDistrict.lastUpdatedBy">Last Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedBy')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedOn')}>
                  <Translate contentKey="dboApp.schoolDistrict.lastUpdatedOn">Last Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedOn')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="dboApp.schoolDistrict.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('schoolDistrictId')}>
                  <Translate contentKey="dboApp.schoolDistrict.schoolDistrictId">School District Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('schoolDistrictId')} />
                </th>
                <th className="hand" onClick={sort('siteCode')}>
                  <Translate contentKey="dboApp.schoolDistrict.siteCode">Site Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('siteCode')} />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="dboApp.schoolDistrict.state">State</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('state')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {schoolDistrictList.map((schoolDistrict, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/school-district/${schoolDistrict.id}`} color="link" size="sm">
                      {schoolDistrict.id}
                    </Button>
                  </td>
                  <td>{schoolDistrict.city}</td>
                  <td>{schoolDistrict.contractCompany}</td>
                  <td>{schoolDistrict.country}</td>
                  <td>{schoolDistrict.createdBy}</td>
                  <td>
                    {schoolDistrict.createdOn ? (
                      <TextFormat type="date" value={schoolDistrict.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{schoolDistrict.districtLogo}</td>
                  <td>{schoolDistrict.districtName}</td>
                  <td>{schoolDistrict.email}</td>
                  <td>{schoolDistrict.foodServiceOptions}</td>
                  <td>{schoolDistrict.isActive ? 'true' : 'false'}</td>
                  <td>{schoolDistrict.lastUpdatedBy}</td>
                  <td>
                    {schoolDistrict.lastUpdatedOn ? (
                      <TextFormat type="date" value={schoolDistrict.lastUpdatedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{schoolDistrict.phoneNumber}</td>
                  <td>{schoolDistrict.schoolDistrictId}</td>
                  <td>{schoolDistrict.siteCode}</td>
                  <td>{schoolDistrict.state}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/school-district/${schoolDistrict.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/school-district/${schoolDistrict.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/school-district/${schoolDistrict.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="dboApp.schoolDistrict.home.notFound">No School Districts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SchoolDistrict;
