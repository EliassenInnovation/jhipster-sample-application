import dayjs from 'dayjs';

export interface IErrorLog {
  id?: number;
  createdOn?: dayjs.Dayjs | null;
  errorId?: number | null;
  errorMessage?: string | null;
  errorPath?: string | null;
}

export const defaultValue: Readonly<IErrorLog> = {};
