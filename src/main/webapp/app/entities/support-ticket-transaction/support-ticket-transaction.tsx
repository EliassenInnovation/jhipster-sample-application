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

import { getEntities, searchEntities } from './support-ticket-transaction.reducer';

export const SupportTicketTransaction = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const supportTicketTransactionList = useAppSelector(state => state.supportTicketTransaction.entities);
  const loading = useAppSelector(state => state.supportTicketTransaction.loading);

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
      <h2 id="support-ticket-transaction-heading" data-cy="SupportTicketTransactionHeading">
        <Translate contentKey="dboApp.supportTicketTransaction.home.title">Support Ticket Transactions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.supportTicketTransaction.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/support-ticket-transaction/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.supportTicketTransaction.home.createLabel">Create new Support Ticket Transaction</Translate>
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
                  placeholder={translate('dboApp.supportTicketTransaction.home.search')}
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
        {supportTicketTransactionList && supportTicketTransactionList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('fileExtension')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.fileExtension">File Extension</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileExtension')} />
                </th>
                <th className="hand" onClick={sort('fileName')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.fileName">File Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileName')} />
                </th>
                <th className="hand" onClick={sort('filePath')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.filePath">File Path</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('filePath')} />
                </th>
                <th className="hand" onClick={sort('fileSize')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.fileSize">File Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileSize')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('isSentByFigSupport')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.isSentByFigSupport">Is Sent By Fig Support</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isSentByFigSupport')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedBy')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.lastUpdatedBy">Last Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedBy')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedOn')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.lastUpdatedOn">Last Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedOn')} />
                </th>
                <th className="hand" onClick={sort('ticketId')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.ticketId">Ticket Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ticketId')} />
                </th>
                <th className="hand" onClick={sort('ticketTransactionId')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.ticketTransactionId">Ticket Transaction Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ticketTransactionId')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="dboApp.supportTicketTransaction.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {supportTicketTransactionList.map((supportTicketTransaction, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/support-ticket-transaction/${supportTicketTransaction.id}`} color="link" size="sm">
                      {supportTicketTransaction.id}
                    </Button>
                  </td>
                  <td>{supportTicketTransaction.createdBy}</td>
                  <td>
                    {supportTicketTransaction.createdOn ? (
                      <TextFormat type="date" value={supportTicketTransaction.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{supportTicketTransaction.description}</td>
                  <td>{supportTicketTransaction.fileExtension}</td>
                  <td>{supportTicketTransaction.fileName}</td>
                  <td>{supportTicketTransaction.filePath}</td>
                  <td>{supportTicketTransaction.fileSize}</td>
                  <td>{supportTicketTransaction.isActive ? 'true' : 'false'}</td>
                  <td>{supportTicketTransaction.isSentByFigSupport ? 'true' : 'false'}</td>
                  <td>{supportTicketTransaction.lastUpdatedBy}</td>
                  <td>
                    {supportTicketTransaction.lastUpdatedOn ? (
                      <TextFormat type="date" value={supportTicketTransaction.lastUpdatedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{supportTicketTransaction.ticketId}</td>
                  <td>{supportTicketTransaction.ticketTransactionId}</td>
                  <td>{supportTicketTransaction.userId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/support-ticket-transaction/${supportTicketTransaction.id}`}
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
                        to={`/support-ticket-transaction/${supportTicketTransaction.id}/edit`}
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
                        onClick={() => (window.location.href = `/support-ticket-transaction/${supportTicketTransaction.id}/delete`)}
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
              <Translate contentKey="dboApp.supportTicketTransaction.home.notFound">No Support Ticket Transactions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SupportTicketTransaction;
