import dayjs from 'dayjs';

export interface IProductBeforeApprove {
  id?: number;
  addedSugar?: number | null;
  addedSugarUom?: string | null;
  allergenKeywords?: string | null;
  brandName?: string | null;
  calories?: number | null;
  caloriesUom?: string | null;
  carbohydrates?: number | null;
  carbohydratesUom?: string | null;
  categoryId?: number | null;
  cholesterol?: number | null;
  cholesterolUOM?: string | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  description?: string | null;
  dietaryFiber?: number | null;
  dietaryFiberUom?: string | null;
  distributorId?: string | null;
  gTIN?: string | null;
  ingredients?: string | null;
  iocCategoryId?: number | null;
  isActive?: boolean | null;
  isMerge?: boolean | null;
  manufacturerId?: number | null;
  manufacturerProductCode?: string | null;
  mergeDate?: dayjs.Dayjs | null;
  productId?: number | null;
  productLabelPdfUrl?: string | null;
  productName?: string | null;
  protein?: number | null;
  proteinUom?: string | null;
  saturatedFat?: number | null;
  serving?: number | null;
  servingUom?: string | null;
  sodium?: number | null;
  sodiumUom?: string | null;
  storageTypeId?: number | null;
  subCategoryId?: number | null;
  sugar?: number | null;
  sugarUom?: string | null;
  totalFat?: number | null;
  transFat?: number | null;
  uPC?: string | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  vendor?: string | null;
}

export const defaultValue: Readonly<IProductBeforeApprove> = {
  isActive: false,
  isMerge: false,
};
