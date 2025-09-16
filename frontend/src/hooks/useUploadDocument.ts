import { useState } from 'react';
import { uploadDocument, type UploadDocumentRequest } from '../api/document/documentApi';

export const useUploadDocument = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const uploadDocumentHandler = async (request: UploadDocumentRequest) => {
    setLoading(true);
    setError(null);

    try {
      const result = await uploadDocument(request);
      if (result) {
        return result;
      } else {
        throw new Error('문서 업로드에 실패했습니다.');
      }
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : '문서 업로드 중 오류가 발생했습니다.';
      setError(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  return {
    uploadDocument: uploadDocumentHandler,
    loading,
    error,
  };
};
