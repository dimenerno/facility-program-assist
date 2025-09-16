import { useState, useEffect } from 'react';
import type { NoticeSummary, NoticeDetail } from '../api';
import { getRecentNotices, getNoticeById } from '../api';

/**
 * Custom hook for managing notices
 */
export const useNotices = (page: number = 0, size: number = 5) => {
  const [notices, setNotices] = useState<NoticeSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalCount, setTotalCount] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  useEffect(() => {
    const fetchNotices = async () => {
      try {
        setLoading(true);
        setError(null);
        
        const result = await getRecentNotices(page, size);
        
        if (result) {
          setNotices(result.notices);
          setTotalCount(result.totalCount);
          setCurrentPage(result.currentPage - 1); // Convert to 0-based
          setTotalPages(result.totalPages);
          setHasNext(result.hasNext);
          setHasPrevious(result.hasPrevious);
        } else {
          setError('공지사항을 가져오지 못했습니다.');
        }
      } catch (err) {
        console.error('Failed to fetch notices:', err);
        setError('공지사항을 가져오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchNotices();
  }, [page, size]);

  return {
    notices,
    loading,
    error,
    totalCount,
    currentPage,
    totalPages,
    hasNext,
    hasPrevious,
    refetch: () => {
      const fetchNotices = async () => {
        try {
          setLoading(true);
          setError(null);
          
          const result = await getRecentNotices(page, size);
          
          if (result) {
            setNotices(result.notices);
            setTotalCount(result.totalCount);
            setCurrentPage(result.currentPage - 1);
            setTotalPages(result.totalPages);
            setHasNext(result.hasNext);
            setHasPrevious(result.hasPrevious);
          } else {
            setError('공지사항을 가져오지 못했습니다.');
          }
        } catch (err) {
          console.error('Failed to fetch notices:', err);
          setError('공지사항을 가져오는 중 오류가 발생했습니다.');
        } finally {
          setLoading(false);
        }
      };
      fetchNotices();
    }
  };
};

/**
 * Custom hook for getting a single notice by ID
 */
export const useNotice = (id: number | null) => {
  const [notice, setNotice] = useState<NoticeDetail | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      setNotice(null);
      return;
    }

    const fetchNotice = async () => {
      try {
        setLoading(true);
        setError(null);
        
        const result = await getNoticeById(id);
        
        if (result) {
          setNotice(result);
        } else {
          setError('공지사항을 가져오지 못했습니다.');
        }
      } catch (err) {
        console.error('Failed to fetch notice:', err);
        setError('공지사항을 가져오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchNotice();
  }, [id]);

  return { notice, loading, error };
};
