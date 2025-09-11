import React from "react";
import { useNavigate } from "react-router-dom";
import Card from "../../components/Card";
import Button from "../../components/Button";
import NoticeList from "../../components/NoticeList";
import DocumentList from "../../components/DocumentList";
import { useUser } from "../../hooks/useUser";
import "./Dashboard.css";

const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const { user, loading, error } = useUser();

  const handleNavigation = (path: string) => {
    navigate(path);
  };

  const handleLogout = async () => {
    try {
      await fetch("http://localhost:8080/api/auth/logout", {
        method: "POST",
        credentials: "include",
      });
      navigate("/login");
    } catch (error) {
      console.error("Logout error:", error);
      navigate("/login");
    }
  };

  return (
    <div className="dashboard">
      <div className="dashboard-header">
        <div className="header-left">
          <h1 className="dashboard-title">공병관리체계</h1>
          <div className="user-info">
            <span className="user-name">
              {loading ? '로딩 중...' : error ? '사용자 정보 없음' : user?.name || '사용자'}
            </span>
            <span className="user-role">
              {loading ? '' : error ? '' : user?.role === 'MANAGER' ? '관리자' : user?.role === 'ADMIN' ? '시스템 관리자' : '사용자'}
            </span>
          </div>
        </div>
        <div className="header-right">
          <Button type="normal" onClick={handleLogout}>
            로그아웃
          </Button>
        </div>
      </div>

      <div className="dashboard-content">
        <div className="navigation-section">
          <h2 className="section-title">메뉴</h2>
          <div className="nav-buttons">
            <Button
              type="normal"
              className="nav-button"
              onClick={() => handleNavigation("/facility-projects")}
            >
              시설 사업
            </Button>
            <Button
              type="normal"
              className="nav-button"
              onClick={() => handleNavigation("/facility-maintenance")}
            >
              시설물 유지
            </Button>
            <Button
              type="normal"
              className="nav-button"
              onClick={() => handleNavigation("/facility-inspection")}
            >
              시설물 안전
            </Button>
            <Button
              type="normal"
              className="nav-button"
              onClick={() => handleNavigation("/water")}
            >
              급수 시설
            </Button>
            <Button
              type="normal"
              className="nav-button"
              onClick={() => handleNavigation("/oil")}
            >
              유류 시설
            </Button>
            <Button
              type="normal"
              className="nav-button"
              onClick={() => handleNavigation("/machinery")}
            >
              기계 설비
            </Button>
            <Button
              type="normal"
              className="nav-button"
              onClick={() => handleNavigation("/electricity")}
            >
              전기 시설
            </Button>
          </div>
        </div>
        <div className="dashboard-main">
          <NoticeList />
          <DocumentList />
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
