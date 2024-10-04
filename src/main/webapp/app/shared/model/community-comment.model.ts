import dayjs from 'dayjs';

export interface ICommunityComment {
  id?: number;
  comment?: string | null;
  commentByUserId?: number | null;
  communityCommentId?: string | null;
  communityPostId?: string | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  lastUpdatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ICommunityComment> = {
  isActive: false,
};
