import { useState, useEffect } from 'react';
import { UserInfo, getCurrentUserInfo } from '../api';

/**
 * Custom hook for managing user information
 */
export const useUser = () => {
  const [user, setUser] = useState<UserInfo | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const fetchUserInfo = async () => {
    try {
      setLoading(true);
      setError(null);
      
      const userInfo = await getCurrentUserInfo();
      
      if (userInfo) {
        setUser(userInfo);
      } else {
        setError('사용자 정보를 가져올 수 없습니다.');
      }
    } catch (err) {
      setError('사용자 정보를 가져오는 중 오류가 발생했습니다.');
      console.error('Error fetching user info:', err);
    } finally {
      setLoading(false);
    }
  };

  const refreshUser = () => {
    fetchUserInfo();
  };

  const clearUser = () => {
    setUser(null);
    setError(null);
  };

  useEffect(() => {
    fetchUserInfo();
  }, []);

  return {
    user,
    loading,
    error,
    refreshUser,
    clearUser,
  };
};
