import dayjs from 'dayjs';

export interface IManufacturer {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  email?: string | null;
  firstName?: string | null;
  glnNumber?: string | null;
  isActive?: boolean | null;
  lastName?: string | null;
  manufacturer?: string | null;
  manufacturerId?: number | null;
  password?: string | null;
  phoneNumber?: string | null;
}

export const defaultValue: Readonly<IManufacturer> = {
  isActive: false,
};
