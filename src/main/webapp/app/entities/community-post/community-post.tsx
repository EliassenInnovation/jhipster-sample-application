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

import { getEntities, searchEntities } from './community-post.reducer';

export const CommunityPost = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const communityPostList = useAppSelector(state => state.communityPost.entities);
  const loading = useAppSelector(state => state.communityPost.loading);

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
      <h2 id="community-post-heading" data-cy="CommunityPostHeading">
        <Translate contentKey="dboApp.communityPost.home.title">Community Posts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.communityPost.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/community-post/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.communityPost.home.createLabel">Create new Community Post</Translate>
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
                  placeholder={translate('dboApp.communityPost.home.search')}
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
        {communityPostList && communityPostList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.communityPost.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('communityPostId')}>
                  <Translate contentKey="dboApp.communityPost.communityPostId">Community Post Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('communityPostId')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.communityPost.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.communityPost.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('date')}>
                  <Translate contentKey="dboApp.communityPost.date">Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('date')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="dboApp.communityPost.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.communityPost.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedBy')}>
                  <Translate contentKey="dboApp.communityPost.lastUpdatedBy">Last Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedBy')} />
                </th>
                <th className="hand" onClick={sort('lastUpdatedOn')}>
                  <Translate contentKey="dboApp.communityPost.lastUpdatedOn">Last Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastUpdatedOn')} />
                </th>
                <th className="hand" onClick={sort('postTypeId')}>
                  <Translate contentKey="dboApp.communityPost.postTypeId">Post Type Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('postTypeId')} />
                </th>
                <th className="hand" onClick={sort('privacyTypeId')}>
                  <Translate contentKey="dboApp.communityPost.privacyTypeId">Privacy Type Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('privacyTypeId')} />
                </th>
                <th className="hand" onClick={sort('schoolDistrictId')}>
                  <Translate contentKey="dboApp.communityPost.schoolDistrictId">School District Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('schoolDistrictId')} />
                </th>
                <th className="hand" onClick={sort('userId')}>
                  <Translate contentKey="dboApp.communityPost.userId">User Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userId')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {communityPostList.map((communityPost, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/community-post/${communityPost.id}`} color="link" size="sm">
                      {communityPost.id}
                    </Button>
                  </td>
                  <td>{communityPost.communityPostId}</td>
                  <td>{communityPost.createdBy}</td>
                  <td>
                    {communityPost.createdOn ? (
                      <TextFormat type="date" value={communityPost.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {communityPost.date ? <TextFormat type="date" value={communityPost.date} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{communityPost.description}</td>
                  <td>{communityPost.isActive ? 'true' : 'false'}</td>
                  <td>{communityPost.lastUpdatedBy}</td>
                  <td>
                    {communityPost.lastUpdatedOn ? (
                      <TextFormat type="date" value={communityPost.lastUpdatedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{communityPost.postTypeId}</td>
                  <td>{communityPost.privacyTypeId}</td>
                  <td>{communityPost.schoolDistrictId}</td>
                  <td>{communityPost.userId}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/community-post/${communityPost.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/community-post/${communityPost.id}/edit`}
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
                        onClick={() => (window.location.href = `/community-post/${communityPost.id}/delete`)}
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
              <Translate contentKey="dboApp.communityPost.home.notFound">No Community Posts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CommunityPost;
