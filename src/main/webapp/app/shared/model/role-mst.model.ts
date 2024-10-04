import dayjs from 'dayjs';

export interface IRoleMst {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  parentRoleId?: number | null;
  roleId?: number | null;
  roleName?: string | null;
  updatedBy?: number | null;
  updatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IRoleMst> = {
  isActive: false,
};
