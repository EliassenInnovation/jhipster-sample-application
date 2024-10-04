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

import { getEntities, searchEntities } from './community-like-mst.reducer';

export const CommunityLikeMst = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const communityLikeMstList = useAppSelector(state => state.communityLikeMst.entities);
  const loading = useAppSelector(state => state.communityLikeMst.loading);

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
      <h2 id="community-like-mst-heading" data-cy="CommunityLikeMstHeading">
        <Translate contentKey="dboApp.communityLikeMst.home.title">Community Like Msts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.communityLikeMst.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/community-like-mst/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.communityLikeMst.home.createLabel">Create new Community Like Mst</Translate>
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
                  placeholder={translate('dboApp.communityLikeMst.home.search')}
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
        {communityLikeMstList && communityLikeMstList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.communityLikeMst.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('communityLikeId')}>
                  <Translate contentKey="dboApp.communityLikeMst.communityLikeId">Community Like Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('communityLikeId')} />
                </th>
                <th className="hand" onClick={sort('communityPostId')}>
                  <Translate contentKey="dboApp.communityLikeMst.communityPostId">Community Post Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('communityPostId')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.communityLikeMst.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.communityLikeMst.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('isLiked')}>
                  <Translate contentKey="dboApp.communityLikeMst.isLiked">Is Liked</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isLiked')} />
                </th>
                <th className="hand" onClick={sort('likedByUserId')}>
                  <Translate contentKey="dboApp.communityLikeMst.likedByUserId">Liked By User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('likedByUserId')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {communityLikeMstList.map((communityLikeMst, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/community-like-mst/${communityLikeMst.id}`} color="link" size="sm">
                      {communityLikeMst.id}
                    </Button>
                  </td>
                  <td>{communityLikeMst.communityLikeId}</td>
                  <td>{communityLikeMst.communityPostId}</td>
                  <td>
                    {communityLikeMst.createdOn ? (
                      <TextFormat type="date" value={communityLikeMst.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{communityLikeMst.isActive ? 'true' : 'false'}</td>
                  <td>{communityLikeMst.isLiked ? 'true' : 'false'}</td>
                  <td>{communityLikeMst.likedByUserId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/community-like-mst/${communityLikeMst.id}`}
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
                        to={`/community-like-mst/${communityLikeMst.id}/edit`}
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
                        onClick={() => (window.location.href = `/community-like-mst/${communityLikeMst.id}/delete`)}
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
              <Translate contentKey="dboApp.communityLikeMst.home.notFound">No Community Like Msts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CommunityLikeMst;
