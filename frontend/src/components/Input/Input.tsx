import React, { useState } from 'react';
import './Input.css';

export interface InputProps {
  type?: 'text' | 'password' | 'email';
  label?: string;
  placeholder?: string;
  value?: string;
  onChange?: (value: string) => void;
  disabled?: boolean;
  error?: string;
  className?: string;
  autoFocus?: boolean;
}

const Input: React.FC<InputProps> = ({
  type = 'text',
  label,
  placeholder,
  value = '',
  onChange,
  disabled = false,
  error,
  className = '',
  autoFocus = false,
}) => {
  const [isFocused, setIsFocused] = useState(false);
  const [internalValue, setInternalValue] = useState(value);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    setInternalValue(newValue);
    onChange?.(newValue);
  };

  const handleFocus = () => setIsFocused(true);
  const handleBlur = () => setIsFocused(false);

  const inputClasses = [
    'input',
    isFocused ? 'input--focused' : '',
    error ? 'input--error' : '',
    disabled ? 'input--disabled' : '',
    className,
  ].filter(Boolean).join(' ');

  return (
    <div className="input-container">
      {label && (
        <label className="input-label">
          {label}
        </label>
      )}
      <div className="input-wrapper">
        <input
          type={type}
          className={inputClasses}
          placeholder={placeholder}
          value={value || internalValue}
          onChange={handleChange}
          onFocus={handleFocus}
          onBlur={handleBlur}
          disabled={disabled}
          autoFocus={autoFocus}
        />
        <div className={`input-underline ${isFocused ? 'input-underline--focused' : ''}`} />
      </div>
      {error && (
        <div className="input-error">
          {error}
        </div>
      )}
    </div>
  );
};

export default Input;
