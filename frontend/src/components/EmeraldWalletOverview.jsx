import React, { useState, useEffect } from 'react';
import { Typography, Paper, Box, CircularProgress } from '@mui/material';
import AccountBalanceWalletIcon from '@mui/icons-material/AccountBalanceWallet';
import api from '../services/api';

const EmeraldWalletOverview = ({ triggerUpdate }) => {
  const [funds, setFunds] = useState(null);

  const fetchWalletData = async () => {
    try {
      const response = await api.get('/account/balance');
      setFunds(response.data.balance);
    } catch (error) {
      console.error('Błąd pobierania stanu portfela:', error);
    }
  };

  useEffect(() => {
    fetchWalletData();
  }, [triggerUpdate]);

  return (
    <Paper
      elevation={3}
      sx={{
        p: 3,
        mb: 4,

        background: 'linear-gradient(135deg, #10b981 0%, #047857 100%)',
        color: 'white',
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        position: 'relative',
        overflow: 'hidden'
      }}
    >
      <Box sx={{
        position: 'absolute', right: -20, top: -40,
        width: 150, height: 150, borderRadius: '50%',
        bgcolor: 'rgba(255,255,255,0.08)'
      }} />

      <Box display="flex" alignItems="center" gap={2} zIndex={1}>
        <Box sx={{ bgcolor: 'rgba(255,255,255,0.15)', p: 1.5, borderRadius: 2 }}>
          <AccountBalanceWalletIcon sx={{ fontSize: 32 }} />
        </Box>
        <Box>
          <Typography variant="overline" sx={{ opacity: 0.85, fontSize: '0.75rem', fontWeight: 'bold', letterSpacing: 1 }}>
            Dostępne Saldo (Emerald)
          </Typography>
          <Typography variant="h4" fontWeight="bold">
            {funds !== null ? `${Number(funds).toLocaleString('pl-PL')} PLN` : <CircularProgress size={24} color="inherit" />}
          </Typography>
        </Box>
      </Box>
    </Paper>
  );
};

export default EmeraldWalletOverview;