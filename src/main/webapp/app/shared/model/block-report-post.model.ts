export interface IBlockReportPost {
  id?: number;
  blockCategories?: string | null;
  blockingReason?: string | null;
  postBlockReportId?: string | null;
  postId?: string | null;
  postType?: string | null;
  requestedBy?: number | null;
}

export const defaultValue: Readonly<IBlockReportPost> = {};
