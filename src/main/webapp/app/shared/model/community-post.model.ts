import dayjs from 'dayjs';

export interface ICommunityPost {
  id?: number;
  communityPostId?: string | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  date?: dayjs.Dayjs | null;
  description?: string | null;
  isActive?: boolean | null;
  lastUpdatedBy?: number | null;
  lastUpdatedOn?: dayjs.Dayjs | null;
  postTypeId?: number | null;
  privacyTypeId?: number | null;
  schoolDistrictId?: number | null;
  userId?: number | null;
}

export const defaultValue: Readonly<ICommunityPost> = {
  isActive: false,
};
