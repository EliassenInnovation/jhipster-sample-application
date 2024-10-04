import dayjs from 'dayjs';

export interface IStateInfo {
  id?: number;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  stateId?: number | null;
  stateName?: string | null;
}

export const defaultValue: Readonly<IStateInfo> = {
  isActive: false,
};
