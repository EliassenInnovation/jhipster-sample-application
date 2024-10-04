export interface IProductAllergen {
  id?: number;
  allergenId?: number | null;
  allergenGroup?: string | null;
  allergenName?: string | null;
  description?: string | null;
  gTIN?: string | null;
  gTINUPC?: string | null;
  productAllergenId?: number | null;
  productId?: number | null;
  uPC?: string | null;
}

export const defaultValue: Readonly<IProductAllergen> = {};
