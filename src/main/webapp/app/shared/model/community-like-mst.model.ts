import dayjs from 'dayjs';

export interface ICommunityLikeMst {
  id?: number;
  communityLikeId?: string | null;
  communityPostId?: string | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  isLiked?: boolean | null;
  likedByUserId?: number | null;
}

export const defaultValue: Readonly<ICommunityLikeMst> = {
  isActive: false,
  isLiked: false,
};
