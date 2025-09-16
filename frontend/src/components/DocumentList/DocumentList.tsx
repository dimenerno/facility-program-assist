import React, { useState } from "react";
import Card from "../Card";
import Button from "../Button";
import { useDocuments, useDocumentDownload } from "../../hooks/useDocuments";
import "./DocumentList.css";

const DocumentList: React.FC = () => {
  const [currentPage, setCurrentPage] = useState(0); // 0-based for API
  const itemsPerPage = 5;
  
  const { 
    documents, 
    loading, 
    error, 
    totalCount, 
    totalPages, 
    hasNext, 
    hasPrevious,
    refetch 
  } = useDocuments(currentPage, itemsPerPage);

  const { download, downloading } = useDocumentDownload();

  const handlePageChange = (page: number) => {
    setCurrentPage(page - 1); // Convert to 0-based
  };

  const handleDocumentClick = (document: any) => {
    console.log("Document clicked:", document);
    download(document.id, document.fileName);
  };

  const handleCreateDocument = () => {
    console.log("Create document clicked");
    // TODO: Navigate to document upload page
  };

  const getFileIcon = (fileType: string) => {
    if (fileType.includes('pdf')) return "📄";
    if (fileType.includes('excel') || fileType.includes('spreadsheet') || fileType.includes('xlsx')) return "📊";
    if (fileType.includes('word') || fileType.includes('document') || fileType.includes('docx')) return "📝";
    if (fileType.includes('presentation') || fileType.includes('powerpoint') || fileType.includes('pptx')) return "📋";
    if (fileType.includes('image') || fileType.includes('jpeg') || fileType.includes('jpg') || fileType.includes('png')) return "🖼️";
    return "📁";
  };

  return (
    <div className="document-list">
      <Card className="document-card" padding="medium">
        <div className="document-header">
          <div className="document-header-left">
            <h2 className="document-title">자료실</h2>
            <span className="document-count">
              총 {loading ? '...' : error ? '0' : totalCount}건
            </span>
          </div>
          <Button
            type="strong"
            onClick={handleCreateDocument}
            className="create-document-btn"
          >
            작성
          </Button>
        </div>

        <div className="document-items">
          {loading ? (
            <div className="document-loading">로딩 중...</div>
          ) : error ? (
            <div className="document-error">
              {error}
              <div style={{ marginTop: '10px' }}>
                <Button type="normal" onClick={refetch}>
                  다시 시도
                </Button>
              </div>
            </div>
          ) : documents.length === 0 ? (
            <div className="document-empty">문서가 없습니다.</div>
          ) : (
            documents.map((document) => (
              <div
                key={document.id}
                className="document-item"
                onClick={() => handleDocumentClick(document)}
              >
                <div className="document-icon">{getFileIcon(document.fileType)}</div>
                <div className="document-content">
                  <h4 className="document-title">{document.title}</h4>
                  <div className="document-meta">
                    <span className="document-type">{document.fileType.split('/')[1]?.toUpperCase() || 'FILE'}</span>
                    <span className="document-size">{document.formattedFileSize}</span>
                    <span className="document-date">{document.formattedDate}</span>
                    <span className="document-author">{document.uploaderName}</span>
                  </div>
                </div>
                {downloading && (
                  <div className="document-downloading">다운로드 중...</div>
                )}
              </div>
            ))
          )}
        </div>

        {!loading && !error && totalPages > 1 && (
          <div className="document-pagination">
            <Button
              type="normal"
              className="pagination-btn"
              disabled={!hasPrevious}
              onClick={() => handlePageChange(currentPage)}
            >
              이전
            </Button>

            <div className="pagination-numbers">
              {Array.from({ length: totalPages }, (_, i) => i + 1).map(
                (page) => (
                  <Button
                    key={page}
                    type={currentPage + 1 === page ? "strong" : "normal"}
                    className="pagination-number"
                    onClick={() => handlePageChange(page)}
                  >
                    {page}
                  </Button>
                )
              )}
            </div>

            <Button
              type="normal"
              className="pagination-btn"
              disabled={!hasNext}
              onClick={() => handlePageChange(currentPage + 2)}
            >
              다음
            </Button>
          </div>
        )}
      </Card>
    </div>
  );
};

export default DocumentList;
