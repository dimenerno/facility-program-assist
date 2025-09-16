import React, { useState } from "react";
import Card from "../Card";
import Button from "../Button";
import NoticeDetailModal from "../NoticeDetailModal";
import CreateNoticeModal from "../CreateNoticeModal";
import { useNotices, useNotice } from "../../hooks/useNotices";
import { useCreateNotice } from "../../hooks/useCreateNotice";
import "./NoticeList.css";

const NoticeList: React.FC = () => {
  const [currentPage, setCurrentPage] = useState(0); // 0-based for API
  const [selectedNoticeId, setSelectedNoticeId] = useState<number | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const itemsPerPage = 5;
  
  const { 
    notices, 
    loading, 
    error, 
    totalCount, 
    totalPages, 
    hasNext, 
    hasPrevious,
    refetch 
  } = useNotices(currentPage, itemsPerPage);

  // Hook to fetch individual notice details
  const { notice: selectedNotice, loading: noticeLoading } = useNotice(selectedNoticeId);
  
  // Hook for creating notices
  const { createNotice, loading: createLoading } = useCreateNotice();

  const handlePageChange = (page: number) => {
    setCurrentPage(page - 1); // Convert to 0-based
  };

  const handleNoticeClick = (notice: any) => {
    console.log("Notice clicked:", notice);
    setSelectedNoticeId(notice.id);
    setModalOpen(true);
  };

  const handleCloseModal = () => {
    setModalOpen(false);
    setSelectedNoticeId(null);
  };

  const handleCreateNotice = () => {
    setCreateModalOpen(true);
  };

  const handleCloseCreateModal = () => {
    setCreateModalOpen(false);
  };

  const handleSubmitCreateNotice = async (request: { title: string; content: string }) => {
    try {
      await createNotice(request);
      // Refresh the notices list after successful creation
      refetch();
    } catch (error) {
      // Error handling is done in the modal
      throw error;
    }
  };

  return (
    <div className="notice-list">
      <Card className="notice-card" padding="medium">
        <div className="notice-header">
          <div className="notice-header-left">
            <h2 className="notice-title">공지사항</h2>
            <span className="notice-count">
              총 {loading ? '...' : error ? '0' : totalCount}건
            </span>
          </div>
          <Button
            type="strong"
            onClick={handleCreateNotice}
            className="create-notice-btn"
          >
            작성
          </Button>
        </div>

        <div className="notice-items">
          {loading ? (
            <div className="notice-loading">로딩 중...</div>
          ) : error ? (
            <div className="notice-error">
              {error}
              <div style={{ marginTop: '10px' }}>
                <Button type="normal" onClick={refetch}>
                  다시 시도
                </Button>
              </div>
            </div>
          ) : notices.length === 0 ? (
            <div className="notice-empty">공지사항이 없습니다.</div>
          ) : (
            notices.map((notice) => (
              <div
                key={notice.id}
                className="notice-item"
                onClick={() => handleNoticeClick(notice)}
              >
                <div className="notice-content">
                  <h4 className="notice-title">{notice.title}</h4>
                  <div className="notice-meta">
                    <span className="notice-date">{notice.formattedDate}</span>
                    <span className="notice-author">{notice.authorName}</span>
                  </div>
                </div>
              </div>
            ))
          )}
        </div>

        {!loading && !error && totalPages > 1 && (
          <div className="notice-pagination">
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

      {/* Notice Detail Modal */}
      <NoticeDetailModal
        open={modalOpen}
        onClose={handleCloseModal}
        notice={selectedNotice}
        loading={noticeLoading}
      />

      {/* Create Notice Modal */}
      <CreateNoticeModal
        open={createModalOpen}
        onClose={handleCloseCreateModal}
        onSubmit={handleSubmitCreateNotice}
        loading={createLoading}
      />
    </div>
  );
};

export default NoticeList;
