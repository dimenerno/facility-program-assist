import React, { useState } from "react";
import Card from "../Card";
import Button from "../Button";
import "./DocumentList.css";

interface Document {
  id: number;
  title: string;
  type: string;
  size: string;
  date: string;
  author: string;
}

const DocumentList: React.FC = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  // Mock data - in real app, this would come from API
  const documents: Document[] = [
    {
      id: 1,
      title: "시설물 관리 매뉴얼 v2.1",
      type: "PDF",
      size: "2.3MB",
      date: "2024-01-15",
      author: "시스템관리자",
    },
    {
      id: 2,
      title: "시설물 점검 체크리스트",
      type: "XLSX",
      size: "156KB",
      date: "2024-01-12",
      author: "시스템관리자",
    },
    {
      id: 3,
      title: "시설물 안전관리 규정",
      type: "PDF",
      size: "1.8MB",
      date: "2024-01-10",
      author: "시스템관리자",
    },
    {
      id: 4,
      title: "시설물 수리비 예산 계획서",
      type: "XLSX",
      size: "89KB",
      date: "2024-01-08",
      author: "시스템관리자",
    },
    {
      id: 5,
      title: "시설물 관리 교육 자료",
      type: "PPTX",
      size: "5.2MB",
      date: "2024-01-05",
      author: "시스템관리자",
    },
    {
      id: 6,
      title: "시설물 등록 양식",
      type: "DOCX",
      size: "45KB",
      date: "2024-01-03",
      author: "시스템관리자",
    },
    {
      id: 7,
      title: "시설물 점검 보고서 템플릿",
      type: "DOCX",
      size: "67KB",
      date: "2024-01-01",
      author: "시스템관리자",
    },
    {
      id: 8,
      title: "시설물 유지보수 가이드",
      type: "PDF",
      size: "3.1MB",
      date: "2023-12-28",
      author: "시스템관리자",
    },
    {
      id: 9,
      title: "시설물 관리 시스템 사용법",
      type: "PDF",
      size: "1.5MB",
      date: "2023-12-25",
      author: "시스템관리자",
    },
    {
      id: 10,
      title: "시설물 분류 기준표",
      type: "XLSX",
      size: "234KB",
      date: "2023-12-20",
      author: "시스템관리자",
    },
  ];

  const totalPages = Math.ceil(documents.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const currentDocuments = documents.slice(startIndex, endIndex);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleDocumentClick = (document: Document) => {
    console.log("Document clicked:", document);
    // TODO: Download or open document
  };

  const getFileIcon = (type: string) => {
    switch (type) {
      case "PDF":
        return "📄";
      case "XLSX":
        return "📊";
      case "DOCX":
        return "📝";
      case "PPTX":
        return "📋";
      default:
        return "📁";
    }
  };

  return (
    <div className="document-list">
      <Card className="document-card" padding="medium">
        <div className="document-header">
          <h2 className="document-title">자료실</h2>
          <span className="document-count">총 {documents.length}건</span>
        </div>

        <div className="document-items">
          {currentDocuments.map((document) => (
            <div
              key={document.id}
              className="document-item"
              onClick={() => handleDocumentClick(document)}
            >
              <div className="document-icon">{getFileIcon(document.type)}</div>
              <div className="document-content">
                <h4 className="document-title">{document.title}</h4>
                <div className="document-meta">
                  <span className="document-type">{document.type}</span>
                  <span className="document-size">{document.size}</span>
                  <span className="document-date">{document.date}</span>
                  <span className="document-author">{document.author}</span>
                </div>
              </div>
            </div>
          ))}
        </div>

        {totalPages > 1 && (
          <div className="document-pagination">
            <Button
              type="normal"
              className="pagination-btn"
              disabled={currentPage === 1}
              onClick={() => handlePageChange(currentPage - 1)}
            >
              이전
            </Button>

            <div className="pagination-numbers">
              {Array.from({ length: totalPages }, (_, i) => i + 1).map(
                (page) => (
                  <Button
                    key={page}
                    type={currentPage === page ? "strong" : "normal"}
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
              disabled={currentPage === totalPages}
              onClick={() => handlePageChange(currentPage + 1)}
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
