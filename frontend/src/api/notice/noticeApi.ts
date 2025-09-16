import type { ApiResponse, NoticeDetail, NoticeListResponse } from '../types';

// Create Notice Request Type
export interface CreateNoticeRequest {
  title: string;
  content: string;
}

const API_BASE_URL = '/api';

/**
 * Get recent notices with pagination
 * @param page page number (0-based, defaults to 0)
 * @param size page size (defaults to 5)
 * @returns Promise<NoticeListResponse | null>
 */
export const getRecentNotices = async (page: number = 0, size: number = 5): Promise<NoticeListResponse | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/notices?page=${page}&size=${size}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error('Failed to get recent notices:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<NoticeListResponse> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error fetching recent notices:', error);
    return null;
  }
};

/**
 * Get notice details by ID
 * @param id notice ID
 * @returns Promise<NoticeDetail | null>
 */
export const getNoticeById = async (id: number): Promise<NoticeDetail | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/notices/${id}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error(`Failed to get notice ${id}:`, response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<NoticeDetail> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error(`Error fetching notice ${id}:`, error);
    return null;
  }
};

/**
 * Get all notices (for admin purposes)
 * @returns Promise<NoticeListResponse | null>
 */
export const getAllNotices = async (): Promise<NoticeListResponse | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/notices/all`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error('Failed to get all notices:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<NoticeListResponse> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error fetching all notices:', error);
    return null;
  }
};

/**
 * Create a new notice
 * @param request CreateNoticeRequest containing title and content
 * @returns Promise<NoticeDetail | null>
 */
export const createNotice = async (request: CreateNoticeRequest): Promise<NoticeDetail | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/notices`, {
      method: 'POST',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(request),
    });

    if (!response.ok) {
      console.error('Failed to create notice:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<NoticeDetail> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error creating notice:', error);
    return null;
  }
};
