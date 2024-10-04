export interface IMonthMst {
  id?: number;
  isActive?: boolean | null;
  monthID?: number | null;
  monthName?: string | null;
  year?: string | null;
}

export const defaultValue: Readonly<IMonthMst> = {
  isActive: false,
};
