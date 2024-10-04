import dayjs from 'dayjs';

export interface IUserDistrictAllocation {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isAllocated?: boolean | null;
  schoolDistrictId?: number | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  userDistrictAllocationId?: string | null;
  userId?: number | null;
}

export const defaultValue: Readonly<IUserDistrictAllocation> = {
  isAllocated: false,
};
