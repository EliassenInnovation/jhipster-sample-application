import dayjs from 'dayjs';

export interface IProductImageBeforeApprove {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  imageURL?: string | null;
  isActive?: boolean | null;
  productId?: number | null;
  productImageId?: number | null;
}

export const defaultValue: Readonly<IProductImageBeforeApprove> = {
  isActive: false,
};
