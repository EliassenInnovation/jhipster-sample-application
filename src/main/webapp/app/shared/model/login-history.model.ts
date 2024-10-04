import dayjs from 'dayjs';

export interface ILoginHistory {
  id?: number;
  forgotPin?: number | null;
  isActive?: boolean | null;
  loginDate?: dayjs.Dayjs | null;
  loginLogId?: number | null;
  loginType?: string | null;
  logOutDate?: dayjs.Dayjs | null;
  userId?: number | null;
}

export const defaultValue: Readonly<ILoginHistory> = {
  isActive: false,
};
