import dayjs from 'dayjs';

export interface ISupportTicketMst {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  email?: string | null;
  isActive?: boolean | null;
  isWithOutLogin?: boolean | null;
  lastUpdatedBy?: number | null;
  lastUpdatedOn?: dayjs.Dayjs | null;
  priority?: string | null;
  schoolDistrictId?: number | null;
  status?: string | null;
  subject?: string | null;
  supportCategoryId?: number | null;
  ticketId?: number | null;
  ticketReferenceNumber?: number | null;
  userId?: number | null;
  userName?: string | null;
}

export const defaultValue: Readonly<ISupportTicketMst> = {
  isActive: false,
  isWithOutLogin: false,
};
