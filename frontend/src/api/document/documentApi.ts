import type { ApiResponse, DocumentDetail, DocumentListResponse } from '../types';

const API_BASE_URL = '/api';

/**
 * Get recent documents with pagination
 * @param page page number (0-based, defaults to 0)
 * @param size page size (defaults to 5)
 * @returns Promise<DocumentListResponse | null>
 */
export const getRecentDocuments = async (page: number = 0, size: number = 5): Promise<DocumentListResponse | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents?page=${page}&size=${size}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error('Failed to get recent documents:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<DocumentListResponse> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error fetching recent documents:', error);
    return null;
  }
};

/**
 * Get document details by ID
 * @param id document ID
 * @returns Promise<DocumentDetail | null>
 */
export const getDocumentById = async (id: number): Promise<DocumentDetail | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents/${id}`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error(`Failed to get document ${id}:`, response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<DocumentDetail> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error(`Error fetching document ${id}:`, error);
    return null;
  }
};

/**
 * Download document by ID
 * @param id document ID
 * @returns Promise<Blob | null>
 */
export const downloadDocument = async (id: number): Promise<Blob | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents/${id}/download`, {
      method: 'GET',
      credentials: 'include',
    });

    if (!response.ok) {
      console.error(`Failed to download document ${id}:`, response.status, response.statusText);
      return null;
    }

    return await response.blob();
  } catch (error) {
    console.error(`Error downloading document ${id}:`, error);
    return null;
  }
};

/**
 * Get all documents (for admin purposes)
 * @returns Promise<DocumentListResponse | null>
 */
export const getAllDocuments = async (): Promise<DocumentListResponse | null> => {
  try {
    const response = await fetch(`${API_BASE_URL}/documents/all`, {
      method: 'GET',
      credentials: 'include',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) {
      console.error('Failed to get all documents:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<DocumentListResponse> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error fetching all documents:', error);
    return null;
  }
};

// Upload Document Request Type
export interface UploadDocumentRequest {
  title: string;
  description?: string;
  file: File;
}

/**
 * Upload a new document
 * @param request UploadDocumentRequest containing title, description and file
 * @returns Promise<DocumentDetail | null>
 */
export const uploadDocument = async (request: UploadDocumentRequest): Promise<DocumentDetail | null> => {
  try {
    const formData = new FormData();
    formData.append('title', request.title);
    if (request.description) {
      formData.append('description', request.description);
    }
    formData.append('file', request.file);

    const response = await fetch(`${API_BASE_URL}/documents`, {
      method: 'POST',
      credentials: 'include',
      body: formData,
    });

    if (!response.ok) {
      console.error('Failed to upload document:', response.status, response.statusText);
      return null;
    }

    const result: ApiResponse<DocumentDetail> = await response.json();

    if (result.success && result.data) {
      return result.data;
    } else {
      console.error('API returned error:', result.message);
      return null;
    }
  } catch (error) {
    console.error('Error uploading document:', error);
    return null;
  }
};

