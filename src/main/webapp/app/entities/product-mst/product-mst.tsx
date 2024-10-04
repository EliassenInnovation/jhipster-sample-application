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

import { getEntities, searchEntities } from './product-mst.reducer';

export const ProductMst = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [search, setSearch] = useState('');
  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const productMstList = useAppSelector(state => state.productMst.entities);
  const loading = useAppSelector(state => state.productMst.loading);

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
      <h2 id="product-mst-heading" data-cy="ProductMstHeading">
        <Translate contentKey="dboApp.productMst.home.title">Product Msts</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="dboApp.productMst.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/product-mst/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="dboApp.productMst.home.createLabel">Create new Product Mst</Translate>
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
                  placeholder={translate('dboApp.productMst.home.search')}
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
        {productMstList && productMstList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="dboApp.productMst.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('addedSugar')}>
                  <Translate contentKey="dboApp.productMst.addedSugar">Added Sugar</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addedSugar')} />
                </th>
                <th className="hand" onClick={sort('addedSugarUom')}>
                  <Translate contentKey="dboApp.productMst.addedSugarUom">Added Sugar Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('addedSugarUom')} />
                </th>
                <th className="hand" onClick={sort('allergenKeywords')}>
                  <Translate contentKey="dboApp.productMst.allergenKeywords">Allergen Keywords</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('allergenKeywords')} />
                </th>
                <th className="hand" onClick={sort('brandName')}>
                  <Translate contentKey="dboApp.productMst.brandName">Brand Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('brandName')} />
                </th>
                <th className="hand" onClick={sort('calories')}>
                  <Translate contentKey="dboApp.productMst.calories">Calories</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('calories')} />
                </th>
                <th className="hand" onClick={sort('caloriesUom')}>
                  <Translate contentKey="dboApp.productMst.caloriesUom">Calories Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('caloriesUom')} />
                </th>
                <th className="hand" onClick={sort('carbohydrates')}>
                  <Translate contentKey="dboApp.productMst.carbohydrates">Carbohydrates</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carbohydrates')} />
                </th>
                <th className="hand" onClick={sort('carbohydratesUom')}>
                  <Translate contentKey="dboApp.productMst.carbohydratesUom">Carbohydrates Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('carbohydratesUom')} />
                </th>
                <th className="hand" onClick={sort('categoryId')}>
                  <Translate contentKey="dboApp.productMst.categoryId">Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('categoryId')} />
                </th>
                <th className="hand" onClick={sort('cholesterol')}>
                  <Translate contentKey="dboApp.productMst.cholesterol">Cholesterol</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cholesterol')} />
                </th>
                <th className="hand" onClick={sort('cholesterolUOM')}>
                  <Translate contentKey="dboApp.productMst.cholesterolUOM">Cholesterol UOM</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cholesterolUOM')} />
                </th>
                <th className="hand" onClick={sort('createdBy')}>
                  <Translate contentKey="dboApp.productMst.createdBy">Created By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdBy')} />
                </th>
                <th className="hand" onClick={sort('createdOn')}>
                  <Translate contentKey="dboApp.productMst.createdOn">Created On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdOn')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="dboApp.productMst.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('dietaryFiber')}>
                  <Translate contentKey="dboApp.productMst.dietaryFiber">Dietary Fiber</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dietaryFiber')} />
                </th>
                <th className="hand" onClick={sort('dietaryFiberUom')}>
                  <Translate contentKey="dboApp.productMst.dietaryFiberUom">Dietary Fiber Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dietaryFiberUom')} />
                </th>
                <th className="hand" onClick={sort('gTIN')}>
                  <Translate contentKey="dboApp.productMst.gTIN">G TIN</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('gTIN')} />
                </th>
                <th className="hand" onClick={sort('ingredients')}>
                  <Translate contentKey="dboApp.productMst.ingredients">Ingredients</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ingredients')} />
                </th>
                <th className="hand" onClick={sort('iOCCategoryId')}>
                  <Translate contentKey="dboApp.productMst.iOCCategoryId">I OC Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('iOCCategoryId')} />
                </th>
                <th className="hand" onClick={sort('isActive')}>
                  <Translate contentKey="dboApp.productMst.isActive">Is Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isActive')} />
                </th>
                <th className="hand" onClick={sort('isMerge')}>
                  <Translate contentKey="dboApp.productMst.isMerge">Is Merge</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isMerge')} />
                </th>
                <th className="hand" onClick={sort('isOneWorldSyncProduct')}>
                  <Translate contentKey="dboApp.productMst.isOneWorldSyncProduct">Is One World Sync Product</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('isOneWorldSyncProduct')} />
                </th>
                <th className="hand" onClick={sort('manufacturerId')}>
                  <Translate contentKey="dboApp.productMst.manufacturerId">Manufacturer Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerId')} />
                </th>
                <th className="hand" onClick={sort('manufacturerProductCode')}>
                  <Translate contentKey="dboApp.productMst.manufacturerProductCode">Manufacturer Product Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerProductCode')} />
                </th>
                <th className="hand" onClick={sort('manufacturerText1Ws')}>
                  <Translate contentKey="dboApp.productMst.manufacturerText1Ws">Manufacturer Text 1 Ws</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('manufacturerText1Ws')} />
                </th>
                <th className="hand" onClick={sort('mergeDate')}>
                  <Translate contentKey="dboApp.productMst.mergeDate">Merge Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mergeDate')} />
                </th>
                <th className="hand" onClick={sort('productId')}>
                  <Translate contentKey="dboApp.productMst.productId">Product Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productId')} />
                </th>
                <th className="hand" onClick={sort('productLabelPdfUrl')}>
                  <Translate contentKey="dboApp.productMst.productLabelPdfUrl">Product Label Pdf Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productLabelPdfUrl')} />
                </th>
                <th className="hand" onClick={sort('productName')}>
                  <Translate contentKey="dboApp.productMst.productName">Product Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productName')} />
                </th>
                <th className="hand" onClick={sort('protein')}>
                  <Translate contentKey="dboApp.productMst.protein">Protein</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('protein')} />
                </th>
                <th className="hand" onClick={sort('proteinUom')}>
                  <Translate contentKey="dboApp.productMst.proteinUom">Protein Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('proteinUom')} />
                </th>
                <th className="hand" onClick={sort('saturatedFat')}>
                  <Translate contentKey="dboApp.productMst.saturatedFat">Saturated Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('saturatedFat')} />
                </th>
                <th className="hand" onClick={sort('serving')}>
                  <Translate contentKey="dboApp.productMst.serving">Serving</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('serving')} />
                </th>
                <th className="hand" onClick={sort('servingUom')}>
                  <Translate contentKey="dboApp.productMst.servingUom">Serving Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('servingUom')} />
                </th>
                <th className="hand" onClick={sort('sodium')}>
                  <Translate contentKey="dboApp.productMst.sodium">Sodium</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sodium')} />
                </th>
                <th className="hand" onClick={sort('sodiumUom')}>
                  <Translate contentKey="dboApp.productMst.sodiumUom">Sodium Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sodiumUom')} />
                </th>
                <th className="hand" onClick={sort('storageTypeId')}>
                  <Translate contentKey="dboApp.productMst.storageTypeId">Storage Type Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storageTypeId')} />
                </th>
                <th className="hand" onClick={sort('subCategoryId')}>
                  <Translate contentKey="dboApp.productMst.subCategoryId">Sub Category Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('subCategoryId')} />
                </th>
                <th className="hand" onClick={sort('sugar')}>
                  <Translate contentKey="dboApp.productMst.sugar">Sugar</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sugar')} />
                </th>
                <th className="hand" onClick={sort('sugarUom')}>
                  <Translate contentKey="dboApp.productMst.sugarUom">Sugar Uom</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sugarUom')} />
                </th>
                <th className="hand" onClick={sort('totalFat')}>
                  <Translate contentKey="dboApp.productMst.totalFat">Total Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalFat')} />
                </th>
                <th className="hand" onClick={sort('transFat')}>
                  <Translate contentKey="dboApp.productMst.transFat">Trans Fat</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('transFat')} />
                </th>
                <th className="hand" onClick={sort('uPC')}>
                  <Translate contentKey="dboApp.productMst.uPC">U PC</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('uPC')} />
                </th>
                <th className="hand" onClick={sort('uPCGTIN')}>
                  <Translate contentKey="dboApp.productMst.uPCGTIN">U PCGTIN</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uPCGTIN')} />
                </th>
                <th className="hand" onClick={sort('updatedBy')}>
                  <Translate contentKey="dboApp.productMst.updatedBy">Updated By</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedBy')} />
                </th>
                <th className="hand" onClick={sort('updatedOn')}>
                  <Translate contentKey="dboApp.productMst.updatedOn">Updated On</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedOn')} />
                </th>
                <th className="hand" onClick={sort('vendor')}>
                  <Translate contentKey="dboApp.productMst.vendor">Vendor</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('vendor')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {productMstList.map((productMst, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/product-mst/${productMst.id}`} color="link" size="sm">
                      {productMst.id}
                    </Button>
                  </td>
                  <td>{productMst.addedSugar}</td>
                  <td>{productMst.addedSugarUom}</td>
                  <td>{productMst.allergenKeywords}</td>
                  <td>{productMst.brandName}</td>
                  <td>{productMst.calories}</td>
                  <td>{productMst.caloriesUom}</td>
                  <td>{productMst.carbohydrates}</td>
                  <td>{productMst.carbohydratesUom}</td>
                  <td>{productMst.categoryId}</td>
                  <td>{productMst.cholesterol}</td>
                  <td>{productMst.cholesterolUOM}</td>
                  <td>{productMst.createdBy}</td>
                  <td>
                    {productMst.createdOn ? <TextFormat type="date" value={productMst.createdOn} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{productMst.description}</td>
                  <td>{productMst.dietaryFiber}</td>
                  <td>{productMst.dietaryFiberUom}</td>
                  <td>{productMst.gTIN}</td>
                  <td>{productMst.ingredients}</td>
                  <td>{productMst.iOCCategoryId}</td>
                  <td>{productMst.isActive ? 'true' : 'false'}</td>
                  <td>{productMst.isMerge ? 'true' : 'false'}</td>
                  <td>{productMst.isOneWorldSyncProduct ? 'true' : 'false'}</td>
                  <td>{productMst.manufacturerId}</td>
                  <td>{productMst.manufacturerProductCode}</td>
                  <td>{productMst.manufacturerText1Ws}</td>
                  <td>
                    {productMst.mergeDate ? <TextFormat type="date" value={productMst.mergeDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{productMst.productId}</td>
                  <td>{productMst.productLabelPdfUrl}</td>
                  <td>{productMst.productName}</td>
                  <td>{productMst.protein}</td>
                  <td>{productMst.proteinUom}</td>
                  <td>{productMst.saturatedFat}</td>
                  <td>{productMst.serving}</td>
                  <td>{productMst.servingUom}</td>
                  <td>{productMst.sodium}</td>
                  <td>{productMst.sodiumUom}</td>
                  <td>{productMst.storageTypeId}</td>
                  <td>{productMst.subCategoryId}</td>
                  <td>{productMst.sugar}</td>
                  <td>{productMst.sugarUom}</td>
                  <td>{productMst.totalFat}</td>
                  <td>{productMst.transFat}</td>
                  <td>{productMst.uPC}</td>
                  <td>{productMst.uPCGTIN}</td>
                  <td>{productMst.updatedBy}</td>
                  <td>
                    {productMst.updatedOn ? <TextFormat type="date" value={productMst.updatedOn} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{productMst.vendor}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/product-mst/${productMst.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/product-mst/${productMst.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/product-mst/${productMst.id}/delete`)}
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
              <Translate contentKey="dboApp.productMst.home.notFound">No Product Msts found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProductMst;
