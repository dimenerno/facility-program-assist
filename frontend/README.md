# Facility Assist Frontend

React TypeScript frontend for the Facility Assist military unit task management system.

## Features

- **Button Component**: Four different button types (normal, positive, negative, strong)
- **TypeScript**: Full type safety and better development experience
- **Responsive Design**: Mobile-friendly layout
- **Modern CSS**: Clean, accessible styling with hover effects and focus states

## Button Types

1. **Normal**: Neutral gray button for general actions
2. **Positive**: Light blue button for positive actions (submit, save)
3. **Negative**: Light red button for destructive actions (delete, cancel)
4. **Strong**: Solid blue button for primary actions (confirm, proceed)

## Getting Started

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start the development server:
   ```bash
   npm start
   ```

3. Open [http://localhost:3000](http://localhost:3000) to view it in the browser.

## Available Scripts

- `npm start` - Runs the app in development mode
- `npm build` - Builds the app for production
- `npm test` - Launches the test runner
- `npm eject` - Ejects from Create React App (one-way operation)

## Button Usage

```tsx
import Button from './components/Button';

// Basic usage
<Button type="normal" onClick={() => console.log('clicked')}>
  Click me
</Button>

// All button types
<Button type="normal">Normal</Button>
<Button type="positive">Positive</Button>
<Button type="negative">Negative</Button>
<Button type="strong">Strong</Button>

// Disabled state
<Button type="strong" disabled>
  Disabled
</Button>
```
