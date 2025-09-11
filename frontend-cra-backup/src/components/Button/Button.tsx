import React from "react";
import "./Button.css";

export type ButtonType = "normal" | "positive" | "negative" | "strong";

export interface ButtonProps {
  type?: ButtonType;
  children: React.ReactNode;
  onClick?: (e: any) => void;
  disabled?: boolean;
  className?: string;
}

const Button: React.FC<ButtonProps> = ({
  type = "normal",
  children,
  onClick,
  disabled = false,
  className = "",
}) => {
  const buttonClasses = [
    "button",
    `button--${type}`,
    disabled ? "button--disabled" : "",
    className,
  ]
    .filter(Boolean)
    .join(" ");

  return (
    <button
      className={buttonClasses}
      onClick={onClick}
      disabled={disabled}
      type="button"
    >
      {children}
    </button>
  );
};

export default Button;
