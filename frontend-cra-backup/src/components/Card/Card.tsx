import React from 'react';
import './Card.css';

export interface CardProps {
  children: React.ReactNode;
  className?: string;
  padding?: 'small' | 'medium' | 'large';
  shadow?: 'none' | 'small' | 'medium' | 'large';
}

const Card: React.FC<CardProps> = ({
  children,
  className = '',
  padding = 'medium',
  shadow = 'medium',
}) => {
  const cardClasses = [
    'card',
    `card--padding-${padding}`,
    `card--shadow-${shadow}`,
    className,
  ].filter(Boolean).join(' ');

  return (
    <div className={cardClasses}>
      {children}
    </div>
  );
};

export default Card;
