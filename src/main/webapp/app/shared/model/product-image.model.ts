import dayjs from 'dayjs';

export interface IProductImage {
  id?: number;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  imageURL?: string | null;
  isActive?: boolean | null;
  productId?: number | null;
  productImageId?: number | null;
}

export const defaultValue: Readonly<IProductImage> = {
  isActive: false,
};
