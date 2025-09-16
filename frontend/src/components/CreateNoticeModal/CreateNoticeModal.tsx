import React, { useState } from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Typography,
  Button,
  Box,
  Divider,
  IconButton,
  TextField,
  Alert,
} from '@mui/material';
import { Close as CloseIcon } from '@mui/icons-material';
import type { CreateNoticeRequest } from '../../api/notice/noticeApi';

interface CreateNoticeModalProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (request: CreateNoticeRequest) => Promise<void>;
  loading?: boolean;
}

const CreateNoticeModal: React.FC<CreateNoticeModalProps> = ({
  open,
  onClose,
  onSubmit,
  loading = false,
}) => {
  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [error, setError] = useState<string | null>(null);

  const handleClose = () => {
    setTitle('');
    setContent('');
    setError(null);
    onClose();
  };

  const handleSubmit = async () => {
    // Validation
    if (!title.trim()) {
      setError('제목을 입력해주세요.');
      return;
    }
    if (!content.trim()) {
      setError('내용을 입력해주세요.');
      return;
    }
    if (title.length > 200) {
      setError('제목은 200자를 초과할 수 없습니다.');
      return;
    }
    if (content.length > 5000) {
      setError('내용은 5000자를 초과할 수 없습니다.');
      return;
    }

    setError(null);
    
    try {
      await onSubmit({ title: title.trim(), content: content.trim() });
      handleClose();
    } catch (err) {
      setError('공지사항 작성 중 오류가 발생했습니다.');
    }
  };

  return (
    <Dialog
      open={open}
      onClose={handleClose}
      maxWidth="md"
      fullWidth
      PaperProps={{
        sx: {
          borderRadius: 2,
          minHeight: '500px',
        },
      }}
    >
      <DialogTitle
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          pb: 1,
        }}
      >
        <Typography variant="h6" component="div" sx={{ fontWeight: 600 }}>
          공지사항 작성
        </Typography>
        <IconButton
          onClick={handleClose}
          size="small"
          sx={{
            color: 'text.secondary',
            '&:hover': {
              backgroundColor: 'action.hover',
            },
          }}
        >
          <CloseIcon />
        </IconButton>
      </DialogTitle>

      <Divider />

      <DialogContent sx={{ pt: 3, pb: 2 }}>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
          {/* Title Input */}
          <TextField
            label="제목"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            fullWidth
            variant="outlined"
            placeholder="공지사항 제목을 입력하세요"
            disabled={loading}
            inputProps={{
              maxLength: 200,
            }}
            helperText={`${title.length}/200`}
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: 1,
              },
            }}
          />

          {/* Content Input */}
          <TextField
            label="내용"
            value={content}
            onChange={(e) => setContent(e.target.value)}
            fullWidth
            multiline
            rows={12}
            variant="outlined"
            placeholder="공지사항 내용을 입력하세요"
            disabled={loading}
            inputProps={{
              maxLength: 5000,
            }}
            helperText={`${content.length}/5000`}
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: 1,
              },
            }}
          />
        </Box>
      </DialogContent>

      <Divider />

      <DialogActions sx={{ p: 2, gap: 1 }}>
        <Button
          onClick={handleClose}
          variant="outlined"
          disabled={loading}
          sx={{
            minWidth: '80px',
            borderRadius: 1,
            borderColor: '#D1D5DB',
            color: '#6B7280',
            '&:hover': {
              borderColor: '#9CA3AF',
              backgroundColor: '#F9FAFB',
            },
          }}
        >
          취소
        </Button>
        <Button
          onClick={handleSubmit}
          variant="contained"
          disabled={loading}
          sx={{
            minWidth: '80px',
            borderRadius: 1,
            backgroundColor: '#3B82F6',
            boxShadow: 'none',
            '&:hover': {
              backgroundColor: '#2563EB',
              boxShadow: 'none',
            },
            '&:disabled': {
              backgroundColor: '#9CA3AF',
            },
          }}
        >
          {loading ? '작성 중...' : '완료'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default CreateNoticeModal;
