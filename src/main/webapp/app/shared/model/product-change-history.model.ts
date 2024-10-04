import dayjs from 'dayjs';

export interface IProductChangeHistory {
  id?: number;
  createdBy?: number | null;
  dateCreated?: dayjs.Dayjs | null;
  historyId?: string | null;
  iocCategoryId?: number | null;
  isActive?: boolean | null;
  productId?: number | null;
  schoolDistrictId?: number | null;
  selectionType?: string | null;
}

export const defaultValue: Readonly<IProductChangeHistory> = {
  isActive: false,
};
