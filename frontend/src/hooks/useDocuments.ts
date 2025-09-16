import { useState, useEffect } from 'react';
import type { DocumentSummary, DocumentDetail } from '../api';
import { getRecentDocuments, getDocumentById, downloadDocument } from '../api';

/**
 * Custom hook for managing documents
 */
export const useDocuments = (page: number = 0, size: number = 5) => {
  const [documents, setDocuments] = useState<DocumentSummary[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [totalCount, setTotalCount] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  useEffect(() => {
    const fetchDocuments = async () => {
      try {
        setLoading(true);
        setError(null);
        
        const result = await getRecentDocuments(page, size);
        
        if (result) {
          setDocuments(result.documents);
          setTotalCount(result.totalCount);
          setCurrentPage(result.currentPage - 1); // Convert to 0-based
          setTotalPages(result.totalPages);
          setHasNext(result.hasNext);
          setHasPrevious(result.hasPrevious);
        } else {
          setError('문서를 가져오지 못했습니다.');
        }
      } catch (err) {
        console.error('Failed to fetch documents:', err);
        setError('문서를 가져오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchDocuments();
  }, [page, size]);

  return {
    documents,
    loading,
    error,
    totalCount,
    currentPage,
    totalPages,
    hasNext,
    hasPrevious,
    refetch: () => {
      const fetchDocuments = async () => {
        try {
          setLoading(true);
          setError(null);
          
          const result = await getRecentDocuments(page, size);
          
          if (result) {
            setDocuments(result.documents);
            setTotalCount(result.totalCount);
            setCurrentPage(result.currentPage - 1);
            setTotalPages(result.totalPages);
            setHasNext(result.hasNext);
            setHasPrevious(result.hasPrevious);
          } else {
            setError('문서를 가져오지 못했습니다.');
          }
        } catch (err) {
          console.error('Failed to fetch documents:', err);
          setError('문서를 가져오는 중 오류가 발생했습니다.');
        } finally {
          setLoading(false);
        }
      };
      fetchDocuments();
    }
  };
};

/**
 * Custom hook for getting a single document by ID
 */
export const useDocument = (id: number | null) => {
  const [document, setDocument] = useState<DocumentDetail | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      setDocument(null);
      return;
    }

    const fetchDocument = async () => {
      try {
        setLoading(true);
        setError(null);
        
        const result = await getDocumentById(id);
        
        if (result) {
          setDocument(result);
        } else {
          setError('문서를 가져오지 못했습니다.');
        }
      } catch (err) {
        console.error('Failed to fetch document:', err);
        setError('문서를 가져오는 중 오류가 발생했습니다.');
      } finally {
        setLoading(false);
      }
    };

    fetchDocument();
  }, [id]);

  return { document, loading, error };
};

/**
 * Custom hook for downloading documents
 */
export const useDocumentDownload = () => {
  const [downloading, setDownloading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const download = async (id: number, fileName: string) => {
    try {
      setDownloading(true);
      setError(null);
      
      const blob = await downloadDocument(id);
      
      if (blob) {
        // Create download link
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = fileName;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      } else {
        setError('문서 다운로드에 실패했습니다.');
      }
    } catch (err) {
      console.error('Failed to download document:', err);
      setError('문서 다운로드 중 오류가 발생했습니다.');
    } finally {
      setDownloading(false);
    }
  };

  return { download, downloading, error };
};

