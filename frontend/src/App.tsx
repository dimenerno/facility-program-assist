import React from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import FacilityProjects from "./pages/FacilityProjects";
import "./App.css";

const App: React.FC = () => {
  return (
    <div className="App">
      <Router>
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/facility-projects" element={<FacilityProjects />} />
          <Route path="/facility-maintenance" element={<FacilityProjects />} />
          <Route path="/water" element={<FacilityProjects />} />
          <Route path="/oil" element={<FacilityProjects />} />
          <Route path="/mechanics" element={<FacilityProjects />} />
          <Route path="/electricity" element={<FacilityProjects />} />
          {/* <Route path="/task-management" element={<FacilityProjects />} />
          <Route path="/work-order" element={<FacilityProjects />} />
          <Route path="/progress-report" element={<FacilityProjects />} />
          <Route path="/completion-report" element={<FacilityProjects />} />
          <Route path="/user-management" element={<FacilityProjects />} />
          <Route path="/unit-management" element={<FacilityProjects />} />
          <Route path="/system-settings" element={<FacilityProjects />} /> */}
        </Routes>
      </Router>
    </div>
  );
};

export default App;
