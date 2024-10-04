import dayjs from 'dayjs';

export interface IProductManufacturerAllocation {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isAllocated?: boolean | null;
  manufactureId?: number | null;
  productId?: number | null;
  productManufacturerAllocationId?: string | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IProductManufacturerAllocation> = {
  isAllocated: false,
};
