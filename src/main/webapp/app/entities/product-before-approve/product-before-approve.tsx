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

import { getEntities, searchEntities } from './product-before-approve.reducer';

export const ProductBeforeApprove = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const productBeforeApproveList = useAppSelector(state => state.productBeforeApprove.entities);
  const loading = useAppSelector(state => state.productBeforeApprove.loading);

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
      <h2 id="product-before-approve-heading" data-cy="ProductBeforeApproveHeading">
        <Translate contentKey="dboApp.productBeforeApprove.home.title">Product Before Approves</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.productBeforeApprove.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/product-before-approve/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.productBeforeApprove.home.createLabel">Create new Product Before Approve</Translate>
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
                  placeholder={translate('dboApp.productBeforeApprove.home.search')}
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
        {productBeforeApproveList && productBeforeApproveList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.productBeforeApprove.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('addedSugar')}>
                  <Translate contentKey="dboApp.productBeforeApprove.addedSugar">Added Sugar</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addedSugar')} />
                </th>
                <th className="hand" onClick={sort('addedSugarUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.addedSugarUom">Added Sugar Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addedSugarUom')} />
                </th>
                <th className="hand" onClick={sort('allergenKeywords')}>
                  <Translate contentKey="dboApp.productBeforeApprove.allergenKeywords">Allergen Keywords</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allergenKeywords')} />
                </th>
                <th className="hand" onClick={sort('brandName')}>
                  <Translate contentKey="dboApp.productBeforeApprove.brandName">Brand Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('brandName')} />
                </th>
                <th className="hand" onClick={sort('calories')}>
                  <Translate contentKey="dboApp.productBeforeApprove.calories">Calories</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('calories')} />
                </th>
                <th className="hand" onClick={sort('caloriesUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.caloriesUom">Calories Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('caloriesUom')} />
                </th>
                <th className="hand" onClick={sort('carbohydrates')}>
                  <Translate contentKey="dboApp.productBeforeApprove.carbohydrates">Carbohydrates</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carbohydrates')} />
                </th>
                <th className="hand" onClick={sort('carbohydratesUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.carbohydratesUom">Carbohydrates Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carbohydratesUom')} />
                </th>
                <th className="hand" onClick={sort('categoryId')}>
                  <Translate contentKey="dboApp.productBeforeApprove.categoryId">Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('categoryId')} />
                </th>
                <th className="hand" onClick={sort('cholesterol')}>
                  <Translate contentKey="dboApp.productBeforeApprove.cholesterol">Cholesterol</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cholesterol')} />
                </th>
                <th className="hand" onClick={sort('cholesterolUOM')}>
                  <Translate contentKey="dboApp.productBeforeApprove.cholesterolUOM">Cholesterol UOM</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cholesterolUOM')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.productBeforeApprove.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.productBeforeApprove.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="dboApp.productBeforeApprove.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('dietaryFiber')}>
                  <Translate contentKey="dboApp.productBeforeApprove.dietaryFiber">Dietary Fiber</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dietaryFiber')} />
                </th>
                <th className="hand" onClick={sort('dietaryFiberUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.dietaryFiberUom">Dietary Fiber Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dietaryFiberUom')} />
                </th>
                <th className="hand" onClick={sort('distributorId')}>
                  <Translate contentKey="dboApp.productBeforeApprove.distributorId">Distributor Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('distributorId')} />
                </th>
                <th className="hand" onClick={sort('gTIN')}>
                  <Translate contentKey="dboApp.productBeforeApprove.gTIN">G TIN</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gTIN')} />
                </th>
                <th className="hand" onClick={sort('ingredients')}>
                  <Translate contentKey="dboApp.productBeforeApprove.ingredients">Ingredients</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ingredients')} />
                </th>
                <th className="hand" onClick={sort('iocCategoryId')}>
                  <Translate contentKey="dboApp.productBeforeApprove.iocCategoryId">Ioc Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('iocCategoryId')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.productBeforeApprove.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('isMerge')}>
                  <Translate contentKey="dboApp.productBeforeApprove.isMerge">Is Merge</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isMerge')} />
                </th>
                <th className="hand" onClick={sort('manufacturerId')}>
                  <Translate contentKey="dboApp.productBeforeApprove.manufacturerId">Manufacturer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerId')} />
                </th>
                <th className="hand" onClick={sort('manufacturerProductCode')}>
                  <Translate contentKey="dboApp.productBeforeApprove.manufacturerProductCode">Manufacturer Product Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerProductCode')} />
                </th>
                <th className="hand" onClick={sort('mergeDate')}>
                  <Translate contentKey="dboApp.productBeforeApprove.mergeDate">Merge Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mergeDate')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.productBeforeApprove.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('productLabelPdfUrl')}>
                  <Translate contentKey="dboApp.productBeforeApprove.productLabelPdfUrl">Product Label Pdf Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productLabelPdfUrl')} />
                </th>
                <th className="hand" onClick={sort('productName')}>
                  <Translate contentKey="dboApp.productBeforeApprove.productName">Product Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productName')} />
                </th>
                <th className="hand" onClick={sort('protein')}>
                  <Translate contentKey="dboApp.productBeforeApprove.protein">Protein</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('protein')} />
                </th>
                <th className="hand" onClick={sort('proteinUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.proteinUom">Protein Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('proteinUom')} />
                </th>
                <th className="hand" onClick={sort('saturatedFat')}>
                  <Translate contentKey="dboApp.productBeforeApprove.saturatedFat">Saturated Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('saturatedFat')} />
                </th>
                <th className="hand" onClick={sort('serving')}>
                  <Translate contentKey="dboApp.productBeforeApprove.serving">Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('serving')} />
                </th>
                <th className="hand" onClick={sort('servingUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.servingUom">Serving Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servingUom')} />
                </th>
                <th className="hand" onClick={sort('sodium')}>
                  <Translate contentKey="dboApp.productBeforeApprove.sodium">Sodium</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sodium')} />
                </th>
                <th className="hand" onClick={sort('sodiumUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.sodiumUom">Sodium Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sodiumUom')} />
                </th>
                <th className="hand" onClick={sort('storageTypeId')}>
                  <Translate contentKey="dboApp.productBeforeApprove.storageTypeId">Storage Type Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storageTypeId')} />
                </th>
                <th className="hand" onClick={sort('subCategoryId')}>
                  <Translate contentKey="dboApp.productBeforeApprove.subCategoryId">Sub Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subCategoryId')} />
                </th>
                <th className="hand" onClick={sort('sugar')}>
                  <Translate contentKey="dboApp.productBeforeApprove.sugar">Sugar</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sugar')} />
                </th>
                <th className="hand" onClick={sort('sugarUom')}>
                  <Translate contentKey="dboApp.productBeforeApprove.sugarUom">Sugar Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sugarUom')} />
                </th>
                <th className="hand" onClick={sort('totalFat')}>
                  <Translate contentKey="dboApp.productBeforeApprove.totalFat">Total Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalFat')} />
                </th>
                <th className="hand" onClick={sort('transFat')}>
                  <Translate contentKey="dboApp.productBeforeApprove.transFat">Trans Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transFat')} />
                </th>
                <th className="hand" onClick={sort('uPC')}>
                  <Translate contentKey="dboApp.productBeforeApprove.uPC">U PC</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uPC')} />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  <Translate contentKey="dboApp.productBeforeApprove.updatedBy">Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')} />
                </th>
                <th className="hand" onClick={sort('updatedOn')}>
                  <Translate contentKey="dboApp.productBeforeApprove.updatedOn">Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedOn')} />
                </th>
                <th className="hand" onClick={sort('vendor')}>
                  <Translate contentKey="dboApp.productBeforeApprove.vendor">Vendor</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vendor')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productBeforeApproveList.map((productBeforeApprove, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-before-approve/${productBeforeApprove.id}`} color="link" size="sm">
                      {productBeforeApprove.id}
                    </Button>
                  </td>
                  <td>{productBeforeApprove.addedSugar}</td>
                  <td>{productBeforeApprove.addedSugarUom}</td>
                  <td>{productBeforeApprove.allergenKeywords}</td>
                  <td>{productBeforeApprove.brandName}</td>
                  <td>{productBeforeApprove.calories}</td>
                  <td>{productBeforeApprove.caloriesUom}</td>
                  <td>{productBeforeApprove.carbohydrates}</td>
                  <td>{productBeforeApprove.carbohydratesUom}</td>
                  <td>{productBeforeApprove.categoryId}</td>
                  <td>{productBeforeApprove.cholesterol}</td>
                  <td>{productBeforeApprove.cholesterolUOM}</td>
                  <td>{productBeforeApprove.createdBy}</td>
                  <td>
                    {productBeforeApprove.createdOn ? (
                      <TextFormat type="date" value={productBeforeApprove.createdOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{productBeforeApprove.description}</td>
                  <td>{productBeforeApprove.dietaryFiber}</td>
                  <td>{productBeforeApprove.dietaryFiberUom}</td>
                  <td>{productBeforeApprove.distributorId}</td>
                  <td>{productBeforeApprove.gTIN}</td>
                  <td>{productBeforeApprove.ingredients}</td>
                  <td>{productBeforeApprove.iocCategoryId}</td>
                  <td>{productBeforeApprove.isActive ? 'true' : 'false'}</td>
                  <td>{productBeforeApprove.isMerge ? 'true' : 'false'}</td>
                  <td>{productBeforeApprove.manufacturerId}</td>
                  <td>{productBeforeApprove.manufacturerProductCode}</td>
                  <td>
                    {productBeforeApprove.mergeDate ? (
                      <TextFormat type="date" value={productBeforeApprove.mergeDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{productBeforeApprove.productId}</td>
                  <td>{productBeforeApprove.productLabelPdfUrl}</td>
                  <td>{productBeforeApprove.productName}</td>
                  <td>{productBeforeApprove.protein}</td>
                  <td>{productBeforeApprove.proteinUom}</td>
                  <td>{productBeforeApprove.saturatedFat}</td>
                  <td>{productBeforeApprove.serving}</td>
                  <td>{productBeforeApprove.servingUom}</td>
                  <td>{productBeforeApprove.sodium}</td>
                  <td>{productBeforeApprove.sodiumUom}</td>
                  <td>{productBeforeApprove.storageTypeId}</td>
                  <td>{productBeforeApprove.subCategoryId}</td>
                  <td>{productBeforeApprove.sugar}</td>
                  <td>{productBeforeApprove.sugarUom}</td>
                  <td>{productBeforeApprove.totalFat}</td>
                  <td>{productBeforeApprove.transFat}</td>
                  <td>{productBeforeApprove.uPC}</td>
                  <td>{productBeforeApprove.updatedBy}</td>
                  <td>
                    {productBeforeApprove.updatedOn ? (
                      <TextFormat type="date" value={productBeforeApprove.updatedOn} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{productBeforeApprove.vendor}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/product-before-approve/${productBeforeApprove.id}`}
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
                        to={`/product-before-approve/${productBeforeApprove.id}/edit`}
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
                        onClick={() => (window.location.href = `/product-before-approve/${productBeforeApprove.id}/delete`)}
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
              <Translate contentKey="dboApp.productBeforeApprove.home.notFound">No Product Before Approves found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductBeforeApprove;
