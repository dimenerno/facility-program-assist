import React, { useState } from "react";
import Card from "../Card";
import Button from "../Button";
import "./NoticeList.css";

interface Notice {
  id: number;
  title: string;
  date: string;
  author: string;
}

const NoticeList: React.FC = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 5;

  // Mock data - in real app, this would come from API
  const notices: Notice[] = [
    {
      id: 1,
      title: "2024년 1분기 시설물 점검 일정 안내",
      date: "2024-01-15",
      author: "시스템관리자",
    },
    {
      id: 2,
      title: "시설물 유지보수 매뉴얼 업데이트",
      date: "2024-01-12",
      author: "시스템관리자",
    },
    {
      id: 3,
      title: "공병관리체계 정기 점검 안내",
      date: "2024-01-10",
      author: "시스템관리자",
    },
    {
      id: 4,
      title: "시설물 안전관리 규정 개정",
      date: "2024-01-08",
      author: "시스템관리자",
    },
    {
      id: 5,
      title: "2024년 시설물 관리 계획",
      date: "2024-01-05",
      author: "시스템관리자",
    },
    {
      id: 6,
      title: "시설물 수리비 예산 배정 안내",
      date: "2024-01-03",
      author: "시스템관리자",
    },
    {
      id: 7,
      title: "겨울철 시설물 관리 주의사항",
      date: "2024-01-01",
      author: "시스템관리자",
    },
    {
      id: 8,
      title: "시설물 점검 결과 보고서",
      date: "2023-12-28",
      author: "시스템관리자",
    },
    {
      id: 9,
      title: "신규 시설물 등록 절차 안내",
      date: "2023-12-25",
      author: "시스템관리자",
    },
    {
      id: 10,
      title: "시설물 관리자 교육 일정",
      date: "2023-12-20",
      author: "시스템관리자",
    },
  ];

  const totalPages = Math.ceil(notices.length / itemsPerPage);
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = startIndex + itemsPerPage;
  const currentNotices = notices.slice(startIndex, endIndex);

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
  };

  const handleNoticeClick = (notice: Notice) => {
    console.log("Notice clicked:", notice);
    // TODO: Navigate to notice detail page
  };

  return (
    <div className="notice-list">
      <Card className="notice-card" padding="medium">
        <div className="notice-header">
          <h2 className="notice-title">공지사항</h2>
          <span className="notice-count">총 {notices.length}건</span>
        </div>

        <div className="notice-items">
          {currentNotices.map((notice) => (
            <div
              key={notice.id}
              className="notice-item"
              onClick={() => handleNoticeClick(notice)}
            >
              <div className="notice-content">
                <h4 className="notice-title">{notice.title}</h4>
                <div className="notice-meta">
                  <span className="notice-date">{notice.date}</span>
                  <span className="notice-author">{notice.author}</span>
                </div>
              </div>
            </div>
          ))}
        </div>

        {totalPages > 1 && (
          <div className="notice-pagination">
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

export default NoticeList;
