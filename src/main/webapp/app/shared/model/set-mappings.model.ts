export interface ISetMappings {
  id?: number;
  iD?: number | null;
  oneWorldValue?: string | null;
  productValue?: string | null;
  setName?: string | null;
}

export const defaultValue: Readonly<ISetMappings> = {};
