export interface ISupportCategory {
  id?: number;
  supportCategoryId?: number | null;
  supportCategoryName?: string | null;
}

export const defaultValue: Readonly<ISupportCategory> = {};
