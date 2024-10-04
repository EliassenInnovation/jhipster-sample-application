export interface IStorageTypes {
  id?: number;
  storageTypeId?: number | null;
  storageTypeName?: string | null;
}

export const defaultValue: Readonly<IStorageTypes> = {};
