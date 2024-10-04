import dayjs from 'dayjs';

export interface ISchoolDistrict {
  id?: number;
  city?: string | null;
  contractCompany?: string | null;
  country?: string | null;
  createdBy?: number | null;
  createdOn?: dayjs.Dayjs | null;
  districtLogo?: string | null;
  districtName?: string | null;
  email?: string | null;
  foodServiceOptions?: string | null;
  isActive?: boolean | null;
  lastUpdatedBy?: number | null;
  lastUpdatedOn?: dayjs.Dayjs | null;
  phoneNumber?: string | null;
  schoolDistrictId?: number | null;
  siteCode?: string | null;
  state?: number | null;
}

export const defaultValue: Readonly<ISchoolDistrict> = {
  isActive: false,
};
