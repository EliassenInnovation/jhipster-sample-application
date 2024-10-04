import dayjs from 'dayjs';

export interface IOneWorldSyncProduct {
  id?: number;
  addedSugars?: string | null;
  addedSugarUom?: string | null;
  allergenKeyword?: string | null;
  allergens?: number | null;
  brandName?: string | null;
  calories?: string | null;
  caloriesUom?: string | null;
  carbohydrates?: string | null;
  carbohydratesUom?: string | null;
  categoryName?: string | null;
  cholesterol?: string | null;
  cholesterolUOM?: string | null;
  createdOn?: dayjs.Dayjs | null;
  dataForm?: string | null;
  dietaryFiber?: string | null;
  dietaryFiberUom?: string | null;
  distributor?: string | null;
  doNotConsiderProduct?: boolean | null;
  extendedModel?: string | null;
  gLNNumber?: string | null;
  gTIN?: string | null;
  h7?: number | null;
  image?: string | null;
  ingredients?: string | null;
  isActive?: boolean | null;
  isApprove?: boolean | null;
  isMerge?: boolean | null;
  isProductSync?: boolean | null;
  manufacturer?: string | null;
  manufacturerId?: number | null;
  manufacturerText1Ws?: string | null;
  modifiedOn?: dayjs.Dayjs | null;
  productDescription?: string | null;
  productId?: number | null;
  productName?: string | null;
  protein?: string | null;
  proteinUom?: string | null;
  saturatedFat?: string | null;
  serving?: string | null;
  servingUom?: string | null;
  sodium?: string | null;
  sodiumUom?: string | null;
  storageTypeId?: number | null;
  storageTypeName?: string | null;
  subCategory1Name?: string | null;
  subCategory2Name?: string | null;
  sugar?: string | null;
  sugarUom?: string | null;
  syncEffective?: dayjs.Dayjs | null;
  syncHeaderLastChange?: dayjs.Dayjs | null;
  syncItemReferenceId?: string | null;
  syncLastChange?: dayjs.Dayjs | null;
  syncPublication?: dayjs.Dayjs | null;
  totalFat?: string | null;
  transFat?: string | null;
  uPC?: string | null;
  vendor?: string | null;
}

export const defaultValue: Readonly<IOneWorldSyncProduct> = {
  doNotConsiderProduct: false,
  isActive: false,
  isApprove: false,
  isMerge: false,
  isProductSync: false,
};
