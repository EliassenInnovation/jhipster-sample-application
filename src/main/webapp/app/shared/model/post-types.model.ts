import dayjs from 'dayjs';

export interface IPostTypes {
  id?: number;
  createdBy?: string | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  lastUpdatedBy?: string | null;
  lastUpdatedOn?: dayjs.Dayjs | null;
  postType?: string | null;
  postTypeId?: number | null;
}

export const defaultValue: Readonly<IPostTypes> = {
  isActive: false,
};
