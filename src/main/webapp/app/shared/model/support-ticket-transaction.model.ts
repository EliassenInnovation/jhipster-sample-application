import dayjs from 'dayjs';

export interface ISupportTicketTransaction {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  description?: string | null;
  fileExtension?: string | null;
  fileName?: string | null;
  filePath?: string | null;
  fileSize?: number | null;
  isActive?: boolean | null;
  isSentByFigSupport?: boolean | null;
  lastUpdatedBy?: number | null;
  lastUpdatedOn?: dayjs.Dayjs | null;
  ticketId?: number | null;
  ticketTransactionId?: number | null;
  userId?: number | null;
}

export const defaultValue: Readonly<ISupportTicketTransaction> = {
  isActive: false,
  isSentByFigSupport: false,
};
