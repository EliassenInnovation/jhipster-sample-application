import dayjs from 'dayjs';

export interface IMonthlyNumbers {
  id?: number;
  actualMonthId?: number | null;
  createdOn?: dayjs.Dayjs | null;
  enrollment?: number | null;
  freeAndReducedPercent?: number | null;
  iD?: number | null;
  isActive?: boolean | null;
  mealsServed?: number | null;
  modifiedOn?: dayjs.Dayjs | null;
  monthId?: number | null;
  numberOfDistricts?: number | null;
  numberOfSites?: number | null;
  regDate?: dayjs.Dayjs | null;
  schoolDistrictId?: number | null;
  year?: string | null;
}

export const defaultValue: Readonly<IMonthlyNumbers> = {
  isActive: false,
};
