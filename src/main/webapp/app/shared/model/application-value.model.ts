import dayjs from 'dayjs';

export interface IApplicationValue {
  id?: number;
  applicationValueId?: number | null;
  key?: string | null;
  valueDate?: dayjs.Dayjs | null;
  valueInt?: number | null;
  valueLong?: number | null;
  valueString?: string | null;
}

export const defaultValue: Readonly<IApplicationValue> = {};
