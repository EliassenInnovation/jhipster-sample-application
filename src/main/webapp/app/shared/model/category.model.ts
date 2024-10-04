import dayjs from 'dayjs';

export interface ICategory {
  id?: number;
  categoryCode?: string | null;
  categoryId?: number | null;
  categoryName?: string | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ICategory> = {
  isActive: false,
};
