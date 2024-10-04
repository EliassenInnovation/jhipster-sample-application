import dayjs from 'dayjs';

export interface ICommunityPostTransactions {
  id?: number;
  attachmentUrl?: string | null;
  communityPostId?: string | null;
  communityPostTransactionId?: string | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  lastUpdatedBy?: number | null;
  lastUpdatedOn?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ICommunityPostTransactions> = {
  isActive: false,
};
