import dayjs from 'dayjs';

export interface IProductActivityHistory {
  id?: number;
  activityId?: number | null;
  activityType?: string | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  date?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  productId?: number | null;
  suggestedProductId?: number | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IProductActivityHistory> = {
  isActive: false,
};
