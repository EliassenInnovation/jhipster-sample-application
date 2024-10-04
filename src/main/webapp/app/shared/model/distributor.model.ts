import dayjs from 'dayjs';

export interface IDistributor {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  distributorCode?: string | null;
  distributorID?: number | null;
  distributorName?: string | null;
  isActive?: boolean | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IDistributor> = {
  isActive: false,
};
