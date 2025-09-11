import React from 'react';
import { useNavigate } from 'react-router-dom';
import Card from '../../components/Card';
import Button from '../../components/Button';
import './FacilityProjects.css';

const FacilityProjects: React.FC = () => {
  const navigate = useNavigate();

  return (
    <div className="facility-projects">
      <div className="page-header">
        <Button type="normal" onClick={() => navigate('/dashboard')}>
          ← 대시보드로 돌아가기
        </Button>
        <h1>시설 사업</h1>
      </div>

      <div className="page-content">
        <Card className="content-card" padding="large">
          <h2>시설 사업 관리</h2>
          <p>시설 사업 관련 기능이 여기에 구현됩니다.</p>
          <div className="placeholder-content">
            <p>• 시설 사업 계획 수립</p>
            <p>• 사업 진행 상황 관리</p>
            <p>• 사업 완료 보고</p>
            <p>• 사업 비용 관리</p>
          </div>
        </Card>
      </div>
    </div>
  );
};

export default FacilityProjects;
