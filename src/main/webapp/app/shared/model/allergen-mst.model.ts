export interface IAllergenMst {
  id?: number;
  allergenGroup?: string | null;
  allergenId?: number | null;
  allergenName?: string | null;
}

export const defaultValue: Readonly<IAllergenMst> = {};
