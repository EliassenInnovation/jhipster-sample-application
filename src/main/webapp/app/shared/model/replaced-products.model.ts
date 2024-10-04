import dayjs from 'dayjs';

export interface IReplacedProducts {
  id?: number;
  isActive?: boolean | null;
  productId?: number | null;
  replacedByUserId?: number | null;
  replacedId?: number | null;
  replacedProductId?: number | null;
  replacementDate?: dayjs.Dayjs | null;
  schoolDistrictId?: number | null;
}

export const defaultValue: Readonly<IReplacedProducts> = {
  isActive: false,
};
