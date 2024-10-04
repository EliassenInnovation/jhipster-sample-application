export interface IProductsToUpdate {
  id?: number;
  maxGLNCode?: string | null;
  maxManufacturerID?: number | null;
  productId?: number | null;
}

export const defaultValue: Readonly<IProductsToUpdate> = {};
