import dayjs from 'dayjs';

export interface IUserMst {
  id?: number;
  addressLine1?: string | null;
  addressLine2?: string | null;
  city?: string | null;
  country?: string | null;
  coverImage?: string | null;
  createBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  email?: string | null;
  firstName?: string | null;
  isActive?: boolean | null;
  lastName?: string | null;
  manufacturerId?: number | null;
  mobile?: string | null;
  objectId?: string | null;
  password?: string | null;
  profileImage?: string | null;
  roleId?: number | null;
  schoolDistrictId?: number | null;
  state?: number | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
  userId?: number | null;
  zipCode?: string | null;
}

export const defaultValue: Readonly<IUserMst> = {
  isActive: false,
};
