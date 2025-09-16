import React, { useState, useRef } from 'react';
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
  FormControl,
  InputLabel,
  OutlinedInput,
  InputAdornment,
} from '@mui/material';
import { Close as CloseIcon, AttachFile as AttachFileIcon } from '@mui/icons-material';
import type { UploadDocumentRequest } from '../../api/document/documentApi';

interface UploadDocumentModalProps {
  open: boolean;
  onClose: () => void;
  onSubmit: (request: UploadDocumentRequest) => Promise<void>;
  loading?: boolean;
}

const UploadDocumentModal: React.FC<UploadDocumentModalProps> = ({
  open,
  onClose,
  onSubmit,
  loading = false,
}) => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [error, setError] = useState<string | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleClose = () => {
    setTitle('');
    setDescription('');
    setSelectedFile(null);
    setError(null);
    onClose();
  };

  const handleFileSelect = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (file) {
      // Check file size (max 10MB)
      if (file.size > 10 * 1024 * 1024) {
        setError('파일 크기는 10MB를 초과할 수 없습니다.');
        return;
      }
      setSelectedFile(file);
      setError(null);
    }
  };

  const handleSubmit = async () => {
    // Validation
    if (!title.trim()) {
      setError('제목을 입력해주세요.');
      return;
    }
    if (!selectedFile) {
      setError('파일을 선택해주세요.');
      return;
    }
    if (title.length > 200) {
      setError('제목은 200자를 초과할 수 없습니다.');
      return;
    }
    if (description && description.length > 1000) {
      setError('설명은 1000자를 초과할 수 없습니다.');
      return;
    }

    setError(null);
    
    try {
      await onSubmit({ 
        title: title.trim(), 
        description: description.trim() || undefined,
        file: selectedFile 
      });
      handleClose();
    } catch (err) {
      setError('문서 업로드 중 오류가 발생했습니다.');
    }
  };

  const formatFileSize = (bytes: number) => {
    if (bytes === 0) return '0 B';
    const k = 1024;
    const sizes = ['B', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
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
          문서 업로드
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
            placeholder="문서 제목을 입력하세요"
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

          {/* Description Input */}
          <TextField
            label="설명 (선택사항)"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            fullWidth
            multiline
            rows={3}
            variant="outlined"
            placeholder="문서 설명을 입력하세요"
            disabled={loading}
            inputProps={{
              maxLength: 1000,
            }}
            helperText={`${description.length}/1000`}
            sx={{
              '& .MuiOutlinedInput-root': {
                borderRadius: 1,
              },
            }}
          />

          {/* File Upload */}
          <FormControl fullWidth>
            <InputLabel htmlFor="file-input">파일 선택</InputLabel>
            <OutlinedInput
              id="file-input"
              type="text"
              value={selectedFile ? selectedFile.name : ''}
              readOnly
              endAdornment={
                <InputAdornment position="end">
                  <Button
                    variant="outlined"
                    component="label"
                    startIcon={<AttachFileIcon />}
                    disabled={loading}
                    sx={{ mr: -1 }}
                  >
                    파일 선택
                    <input
                      ref={fileInputRef}
                      type="file"
                      hidden
                      onChange={handleFileSelect}
                      accept=".pdf,.doc,.docx,.xls,.xlsx,.ppt,.pptx,.txt,.jpg,.jpeg,.png,.gif"
                    />
                  </Button>
                </InputAdornment>
              }
              sx={{
                '& .MuiOutlinedInput-root': {
                  borderRadius: 1,
                },
              }}
            />
            {selectedFile && (
              <Typography variant="caption" color="text.secondary" sx={{ mt: 1 }}>
                파일 크기: {formatFileSize(selectedFile.size)} | 
                파일 타입: {selectedFile.type || '알 수 없음'}
              </Typography>
            )}
          </FormControl>
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
          {loading ? '업로드 중...' : '업로드'}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default UploadDocumentModal;
