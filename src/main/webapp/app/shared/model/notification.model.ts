import dayjs from 'dayjs';

export interface INotification {
  id?: number;
  content?: string | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  notificationId?: number | null;
  referenceId?: number | null;
  type?: string | null;
  userId?: number | null;
}

export const defaultValue: Readonly<INotification> = {
  isActive: false,
};
