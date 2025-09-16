// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data?: T;
}

// User Types
export interface UserInfo {
  id: number;
  username: string;
  name: string;
  role: 'ADMIN' | 'MANAGER' | 'USER';
  unit?: UnitInfo;
}

export interface UnitInfo {
  id: number;
  name: string;
  code: string;
}

// Auth Types
export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  data?: {
    username: string;
    name: string;
    role: string;
  };
}

// Notice Types
export interface NoticeSummary {
  id: number;
  title: string;
  authorName: string;
  createdAt: string;
  formattedDate: string;
}

export interface NoticeDetail {
  id: number;
  title: string;
  content: string;
  authorName: string;
  authorUsername: string;
  createdAt: string;
  formattedDate: string;
}

export interface NoticeListResponse {
  notices: NoticeSummary[];
  totalCount: number;
  currentPage: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

// Document Types
export interface DocumentSummary {
  id: number;
  title: string;
  description: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  uploaderName: string;
  uploadedAt: string;
  formattedDate: string;
  formattedFileSize: string;
}

export interface DocumentDetail {
  id: number;
  title: string;
  description: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  uploaderName: string;
  uploaderUsername: string;
  uploadedAt: string;
  formattedDate: string;
  formattedFileSize: string;
}

export interface DocumentListResponse {
  documents: DocumentSummary[];
  totalCount: number;
  currentPage: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}
