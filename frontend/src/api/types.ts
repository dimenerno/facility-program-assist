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
