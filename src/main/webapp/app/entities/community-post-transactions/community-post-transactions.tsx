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

import { getEntities, searchEntities } from './community-post-transactions.reducer';

export const CommunityPostTransactions = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const communityPostTransactionsList = useAppSelector(state => state.communityPostTransactions.entities);
  const loading = useAppSelector(state => state.communityPostTransactions.loading);

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
      <h2 id="community-post-transactions-heading" data-cy="CommunityPostTransactionsHeading">
        <Translate contentKey="dboApp.communityPostTransactions.home.title">Community Post Transactions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.communityPostTransactions.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/community-post-transactions/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.communityPostTransactions.home.createLabel">Create new Community Post Transactions</Translate>
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
                  placeholder={translate('dboApp.communityPostTransactions.home.search')}
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
        {communityPostTransactionsList && communityPostTransactionsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.communityPostTransactions.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('attachmentUrl')}>
                  <Translate contentKey="dboApp.communityPostTransactions.attachmentUrl">Attachment Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('attachmentUrl')} />
                </th>
                <th className="hand" onClick={sort('communityPostId')}>
                  <Translate contentKey="dboApp.communityPostTransactions.communityPostId">Community Post Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('communityPostId')} />
                </th>
                <th className="hand" onClick={sort('communityPostTransactionId')}>
                  <Translate contentKey="dboApp.communityPostTransactions.communityPostTransactionId">
                    Community Post Transaction Id
                  </Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('communityPostTransactionId')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.communityPostTransactions.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.communityPostTransactions.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.communityPostTransactions.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedBy')}>
                  <Translate contentKey="dboApp.communityPostTransactions.lastUpdatedBy">Last Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedBy')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedOn')}>
                  <Translate contentKey="dboApp.communityPostTransactions.lastUpdatedOn">Last Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedOn')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {communityPostTransactionsList.map((communityPostTransactions, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/community-post-transactions/${communityPostTransactions.id}`} color="link" size="sm">
                      {communityPostTransactions.id}
                    </Button>
                  </td>
                  <td>{communityPostTransactions.attachmentUrl}</td>
                  <td>{communityPostTransactions.communityPostId}</td>
                  <td>{communityPostTransactions.communityPostTransactionId}</td>
                  <td>{communityPostTransactions.createdBy}</td>
                  <td>
                    {communityPostTransactions.createdOn ? (
                      <TextFormat type="date" value={communityPostTransactions.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{communityPostTransactions.isActive ? 'true' : 'false'}</td>
                  <td>{communityPostTransactions.lastUpdatedBy}</td>
                  <td>
                    {communityPostTransactions.lastUpdatedOn ? (
                      <TextFormat type="date" value={communityPostTransactions.lastUpdatedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/community-post-transactions/${communityPostTransactions.id}`}
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
                        to={`/community-post-transactions/${communityPostTransactions.id}/edit`}
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
                        onClick={() => (window.location.href = `/community-post-transactions/${communityPostTransactions.id}/delete`)}
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
              <Translate contentKey="dboApp.communityPostTransactions.home.notFound">No Community Post Transactions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CommunityPostTransactions;
