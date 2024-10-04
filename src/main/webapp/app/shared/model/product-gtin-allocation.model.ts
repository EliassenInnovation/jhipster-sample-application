import dayjs from 'dayjs';

export interface IProductGtinAllocation {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  gTIN?: string | null;
  isActive?: boolean | null;
  productGtinId?: number | null;
  productId?: number | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IProductGtinAllocation> = {
  isActive: false,
};
