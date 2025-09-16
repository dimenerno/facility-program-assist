import { useState } from 'react';
import { createNotice, type CreateNoticeRequest } from '../api/notice/noticeApi';

export const useCreateNotice = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const createNoticeHandler = async (request: CreateNoticeRequest) => {
    setLoading(true);
    setError(null);

    try {
      const result = await createNotice(request);
      if (result) {
        return result;
      } else {
        throw new Error('공지사항 생성에 실패했습니다.');
      }
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : '공지사항 생성 중 오류가 발생했습니다.';
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    createNotice: createNoticeHandler,
    loading,
    error,
  };
};
