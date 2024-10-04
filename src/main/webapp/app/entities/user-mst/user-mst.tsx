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

import { getEntities, searchEntities } from './user-mst.reducer';

export const UserMst = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const userMstList = useAppSelector(state => state.userMst.entities);
  const loading = useAppSelector(state => state.userMst.loading);

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
      <h2 id="user-mst-heading" data-cy="UserMstHeading">
        <Translate contentKey="dboApp.userMst.home.title">User Msts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.userMst.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-mst/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.userMst.home.createLabel">Create new User Mst</Translate>
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
                  placeholder={translate('dboApp.userMst.home.search')}
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
        {userMstList && userMstList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.userMst.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('addressLine1')}>
                  <Translate contentKey="dboApp.userMst.addressLine1">Address Line 1</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressLine1')} />
                </th>
                <th className="hand" onClick={sort('addressLine2')}>
                  <Translate contentKey="dboApp.userMst.addressLine2">Address Line 2</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addressLine2')} />
                </th>
                <th className="hand" onClick={sort('city')}>
                  <Translate contentKey="dboApp.userMst.city">City</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('city')} />
                </th>
                <th className="hand" onClick={sort('country')}>
                  <Translate contentKey="dboApp.userMst.country">Country</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('country')} />
                </th>
                <th className="hand" onClick={sort('coverImage')}>
                  <Translate contentKey="dboApp.userMst.coverImage">Cover Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('coverImage')} />
                </th>
                <th className="hand" onClick={sort('createBy')}>
                  <Translate contentKey="dboApp.userMst.createBy">Create By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.userMst.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="dboApp.userMst.email">Email</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="dboApp.userMst.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.userMst.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="dboApp.userMst.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('manufacturerId')}>
                  <Translate contentKey="dboApp.userMst.manufacturerId">Manufacturer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerId')} />
                </th>
                <th className="hand" onClick={sort('mobile')}>
                  <Translate contentKey="dboApp.userMst.mobile">Mobile</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mobile')} />
                </th>
                <th className="hand" onClick={sort('objectId')}>
                  <Translate contentKey="dboApp.userMst.objectId">Object Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('objectId')} />
                </th>
                <th className="hand" onClick={sort('password')}>
                  <Translate contentKey="dboApp.userMst.password">Password</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('password')} />
                </th>
                <th className="hand" onClick={sort('profileImage')}>
                  <Translate contentKey="dboApp.userMst.profileImage">Profile Image</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profileImage')} />
                </th>
                <th className="hand" onClick={sort('roleId')}>
                  <Translate contentKey="dboApp.userMst.roleId">Role Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('roleId')} />
                </th>
                <th className="hand" onClick={sort('schoolDistrictId')}>
                  <Translate contentKey="dboApp.userMst.schoolDistrictId">School District Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('schoolDistrictId')} />
                </th>
                <th className="hand" onClick={sort('state')}>
                  <Translate contentKey="dboApp.userMst.state">State</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('state')} />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  <Translate contentKey="dboApp.userMst.updatedBy">Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')} />
                </th>
                <th className="hand" onClick={sort('updatedOn')}>
                  <Translate contentKey="dboApp.userMst.updatedOn">Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedOn')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="dboApp.userMst.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('zipCode')}>
                  <Translate contentKey="dboApp.userMst.zipCode">Zip Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('zipCode')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userMstList.map((userMst, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-mst/${userMst.id}`} color="link" size="sm">
                      {userMst.id}
                    </Button>
                  </td>
                  <td>{userMst.addressLine1}</td>
                  <td>{userMst.addressLine2}</td>
                  <td>{userMst.city}</td>
                  <td>{userMst.country}</td>
                  <td>{userMst.coverImage}</td>
                  <td>{userMst.createBy}</td>
                  <td>{userMst.createdOn ? <TextFormat type="date" value={userMst.createdOn} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{userMst.email}</td>
                  <td>{userMst.firstName}</td>
                  <td>{userMst.isActive ? 'true' : 'false'}</td>
                  <td>{userMst.lastName}</td>
                  <td>{userMst.manufacturerId}</td>
                  <td>{userMst.mobile}</td>
                  <td>{userMst.objectId}</td>
                  <td>{userMst.password}</td>
                  <td>{userMst.profileImage}</td>
                  <td>{userMst.roleId}</td>
                  <td>{userMst.schoolDistrictId}</td>
                  <td>{userMst.state}</td>
                  <td>{userMst.updatedBy}</td>
                  <td>{userMst.updatedOn ? <TextFormat type="date" value={userMst.updatedOn} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{userMst.userId}</td>
                  <td>{userMst.zipCode}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-mst/${userMst.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/user-mst/${userMst.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/user-mst/${userMst.id}/delete`)}
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
              <Translate contentKey="dboApp.userMst.home.notFound">No User Msts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default UserMst;
