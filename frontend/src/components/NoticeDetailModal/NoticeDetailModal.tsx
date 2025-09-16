import React from 'react';
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
  Chip,
} from '@mui/material';
import { Close as CloseIcon, Person as PersonIcon, CalendarToday as CalendarIcon } from '@mui/icons-material';
import type { NoticeDetail } from '../../api';

interface NoticeDetailModalProps {
  open: boolean;
  onClose: () => void;
  notice: NoticeDetail | null;
  loading?: boolean;
}

const NoticeDetailModal: React.FC<NoticeDetailModalProps> = ({
  open,
  onClose,
  notice,
  loading = false,
}) => {
  if (!notice && !loading) {
    return null;
  }

  return (
    <Dialog
      open={open}
      onClose={onClose}
      maxWidth="md"
      fullWidth
      PaperProps={{
        sx: {
          borderRadius: 2,
          minHeight: '400px',
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
          공지사항 상세
        </Typography>
        <IconButton
          onClick={onClose}
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
        {loading ? (
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '200px',
            }}
          >
            <Typography color="text.secondary">로딩 중...</Typography>
          </Box>
        ) : notice ? (
          <Box>
            {/* Title */}
            <Typography
              variant="h5"
              component="h1"
              sx={{
                fontWeight: 600,
                mb: 3,
                color: 'text.primary',
                lineHeight: 1.3,
              }}
            >
              {notice.title}
            </Typography>

            {/* Meta Information */}
            <Box
              sx={{
                display: 'flex',
                gap: 2,
                mb: 3,
                flexWrap: 'wrap',
                alignItems: 'center',
              }}
            >
              <Chip
                icon={<PersonIcon />}
                label={notice.authorName}
                variant="outlined"
                size="small"
                sx={{
                  backgroundColor: 'primary.50',
                  borderColor: 'primary.200',
                  color: 'primary.700',
                }}
              />
              <Chip
                icon={<CalendarIcon />}
                label={notice.formattedDate}
                variant="outlined"
                size="small"
                sx={{
                  backgroundColor: 'grey.50',
                  borderColor: 'grey.300',
                  color: 'grey.700',
                }}
              />
            </Box>

            <Divider sx={{ mb: 3 }} />

            {/* Content */}
            <Box
              sx={{
                backgroundColor: 'grey.50',
                borderRadius: 1,
                p: 3,
                border: '1px solid',
                borderColor: 'grey.200',
              }}
            >
              <Typography
                variant="body1"
                sx={{
                  lineHeight: 1.7,
                  color: 'text.primary',
                  whiteSpace: 'pre-wrap',
                  wordBreak: 'break-word',
                }}
              >
                {notice.content}
              </Typography>
            </Box>
          </Box>
        ) : (
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '200px',
            }}
          >
            <Typography color="text.secondary">
              공지사항을 불러올 수 없습니다.
            </Typography>
          </Box>
        )}
      </DialogContent>

      <Divider />

      <DialogActions sx={{ p: 2 }}>
        <Button
          onClick={onClose}
          variant="contained"
          color="primary"
          sx={{
            minWidth: '80px',
            borderRadius: 1,
            backgroundColor: '#3B82F6',
            boxShadow: 'none',
            '&:hover': {
              backgroundColor: '#2563EB',
              boxShadow: 'none',
            },
          }}
        >
          닫기
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default NoticeDetailModal;
