import dayjs from 'dayjs';

export interface IProductDistrictAllocation {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isAllocated?: boolean | null;
  productDistrictAllocationId?: string | null;
  productId?: number | null;
  schoolDistrictId?: number | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IProductDistrictAllocation> = {
  isAllocated: false,
};
