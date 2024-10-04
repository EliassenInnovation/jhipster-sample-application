import dayjs from 'dayjs';

export interface IProductDistributorAllocation {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  distributorId?: number | null;
  isAllocated?: boolean | null;
  productDistributorAllocationId?: string | null;
  productId?: number | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IProductDistributorAllocation> = {
  isAllocated: false,
};
