import dayjs from 'dayjs';

export interface IProductUpcAllocation {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  productId?: number | null;
  productUpcId?: number | null;
  uPC?: string | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IProductUpcAllocation> = {
  isActive: false,
};
