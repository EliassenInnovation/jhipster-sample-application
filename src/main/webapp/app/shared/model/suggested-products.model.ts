import dayjs from 'dayjs';

export interface ISuggestedProducts {
  id?: number;
  isActive?: boolean | null;
  isApprove?: boolean | null;
  productId?: number | null;
  suggestedByDistrict?: number | null;
  suggestedByUserId?: number | null;
  suggestedProductId?: number | null;
  suggestionDate?: dayjs.Dayjs | null;
  suggestionId?: number | null;
}

export const defaultValue: Readonly<ISuggestedProducts> = {
  isActive: false,
  isApprove: false,
};
