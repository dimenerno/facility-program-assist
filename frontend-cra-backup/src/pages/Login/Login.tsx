import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Card from "../../components/Card";
import Input from "../../components/Input";
import Button from "../../components/Button";
import "./Login.css";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const handleInputChange = (field: string) => (value: string) => {
    setFormData((prev) => ({
      ...prev,
      [field]: value,
    }));
    // Clear error when user starts typing
    if (error) {
      setError("");
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.username || !formData.password) {
      setError("아이디와 비밀번호를 입력해주세요.");
      return;
    }

    setIsLoading(true);
    setError("");

    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // Include cookies for session management
        body: JSON.stringify({
          username: formData.username,
          password: formData.password,
        }),
      });

      const result = await response.json();

      if (result.success) {
        console.log("Login successful:", result.data);
        // Redirect to dashboard
        navigate('/dashboard');
      } else {
        setError(result.message || "로그인에 실패했습니다.");
      }
    } catch (err) {
      console.error("Login error:", err);
      setError("서버 연결에 실패했습니다. 잠시 후 다시 시도해주세요.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-container">
        <Card className="login-card" padding="large" shadow="medium">
          <div className="login-header">
            <h1 className="login-title">공병관리체계 로그인</h1>
          </div>

          <form className="login-form" onSubmit={handleSubmit}>
            <Input
              label="아이디 (군번)"
              type="text"
              value={formData.username}
              onChange={handleInputChange("username")}
              placeholder="아이디를 입력하세요"
              autoFocus
            />

            <Input
              label="비밀번호"
              type="password"
              value={formData.password}
              onChange={handleInputChange("password")}
              placeholder="비밀번호를 입력하세요"
            />

            {error && <div className="login-error">{error}</div>}

            <div className="login-help">
              관리자 회원가입 및 비밀번호 변경은 체계관리자에게 문의하세요.
            </div>

            <Button
              type="strong"
              onClick={handleSubmit}
              disabled={isLoading}
              className="login-button"
            >
              {isLoading ? "로그인 중..." : "로그인"}
            </Button>
          </form>
        </Card>
      </div>
    </div>
  );
};

export default Login;
