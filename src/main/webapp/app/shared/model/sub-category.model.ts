import dayjs from 'dayjs';

export interface ISubCategory {
  id?: number;
  categoryId?: number | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  subCategoryCode?: string | null;
  subCategoryId?: number | null;
  subCategoryName?: string | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ISubCategory> = {
  isActive: false,
};
