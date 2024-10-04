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

import { getEntities, searchEntities } from './support-ticket-mst.reducer';

export const SupportTicketMst = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const supportTicketMstList = useAppSelector(state => state.supportTicketMst.entities);
  const loading = useAppSelector(state => state.supportTicketMst.loading);

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
      <h2 id="support-ticket-mst-heading" data-cy="SupportTicketMstHeading">
        <Translate contentKey="dboApp.supportTicketMst.home.title">Support Ticket Msts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.supportTicketMst.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/support-ticket-mst/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.supportTicketMst.home.createLabel">Create new Support Ticket Mst</Translate>
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
                  placeholder={translate('dboApp.supportTicketMst.home.search')}
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
        {supportTicketMstList && supportTicketMstList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.supportTicketMst.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.supportTicketMst.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.supportTicketMst.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="dboApp.supportTicketMst.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.supportTicketMst.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('isWithOutLogin')}>
                  <Translate contentKey="dboApp.supportTicketMst.isWithOutLogin">Is With Out Login</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isWithOutLogin')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedBy')}>
                  <Translate contentKey="dboApp.supportTicketMst.lastUpdatedBy">Last Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedBy')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedOn')}>
                  <Translate contentKey="dboApp.supportTicketMst.lastUpdatedOn">Last Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedOn')} />
                </th>
                <th className="hand" onClick={sort('priority')}>
                  <Translate contentKey="dboApp.supportTicketMst.priority">Priority</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('priority')} />
                </th>
                <th className="hand" onClick={sort('schoolDistrictId')}>
                  <Translate contentKey="dboApp.supportTicketMst.schoolDistrictId">School District Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('schoolDistrictId')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="dboApp.supportTicketMst.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('subject')}>
                  <Translate contentKey="dboApp.supportTicketMst.subject">Subject</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subject')} />
                </th>
                <th className="hand" onClick={sort('supportCategoryId')}>
                  <Translate contentKey="dboApp.supportTicketMst.supportCategoryId">Support Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('supportCategoryId')} />
                </th>
                <th className="hand" onClick={sort('ticketId')}>
                  <Translate contentKey="dboApp.supportTicketMst.ticketId">Ticket Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ticketId')} />
                </th>
                <th className="hand" onClick={sort('ticketReferenceNumber')}>
                  <Translate contentKey="dboApp.supportTicketMst.ticketReferenceNumber">Ticket Reference Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ticketReferenceNumber')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="dboApp.supportTicketMst.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th className="hand" onClick={sort('userName')}>
                  <Translate contentKey="dboApp.supportTicketMst.userName">User Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userName')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {supportTicketMstList.map((supportTicketMst, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/support-ticket-mst/${supportTicketMst.id}`} color="link" size="sm">
                      {supportTicketMst.id}
                    </Button>
                  </td>
                  <td>{supportTicketMst.createdBy}</td>
                  <td>
                    {supportTicketMst.createdOn ? (
                      <TextFormat type="date" value={supportTicketMst.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{supportTicketMst.email}</td>
                  <td>{supportTicketMst.isActive ? 'true' : 'false'}</td>
                  <td>{supportTicketMst.isWithOutLogin ? 'true' : 'false'}</td>
                  <td>{supportTicketMst.lastUpdatedBy}</td>
                  <td>
                    {supportTicketMst.lastUpdatedOn ? (
                      <TextFormat type="date" value={supportTicketMst.lastUpdatedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{supportTicketMst.priority}</td>
                  <td>{supportTicketMst.schoolDistrictId}</td>
                  <td>{supportTicketMst.status}</td>
                  <td>{supportTicketMst.subject}</td>
                  <td>{supportTicketMst.supportCategoryId}</td>
                  <td>{supportTicketMst.ticketId}</td>
                  <td>{supportTicketMst.ticketReferenceNumber}</td>
                  <td>{supportTicketMst.userId}</td>
                  <td>{supportTicketMst.userName}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/support-ticket-mst/${supportTicketMst.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/support-ticket-mst/${supportTicketMst.id}/edit`}
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
                        onClick={() => (window.location.href = `/support-ticket-mst/${supportTicketMst.id}/delete`)}
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
              <Translate contentKey="dboApp.supportTicketMst.home.notFound">No Support Ticket Msts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SupportTicketMst;
