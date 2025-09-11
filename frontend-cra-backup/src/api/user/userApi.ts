import { ApiResponse, UserInfo } from '../types';

const API_BASE_URL = 'http://localhost:8080/api';

/**
 * Get current logged-in user information
 * @returns Promise<UserInfo | null>
 */
export const getCurrentUserInfo = async (): Promise<UserInfo | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/users/me`, {
      method: 'GET',
      credentials: 'include', // Include cookies for session authentication
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error('Failed to get user info:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<UserInfo> = await response.json();
    
    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error fetching user info:', error);
    return null;
  }
};

/**
 * Get user information by username
 * @param username - The username to search for
 * @returns Promise<UserInfo | null>
 */
export const getUserInfoByUsername = async (username: string): Promise<UserInfo | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/users/${username}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error('Failed to get user info:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<UserInfo> = await response.json();
    
    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error fetching user info:', error);
    return null;
  }
};
