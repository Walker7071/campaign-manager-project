import React, { useState } from 'react';
import { Container, Typography, CssBaseline, Box } from '@mui/material';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import EmeraldWalletOverview from './components/EmeraldWalletOverview';
import CampaignGrid from './components/CampaignGrid';

const theme = createTheme({
  palette: {
    background: {
      default: '#f8fafc',
    },
    primary: {
      main: '#0f172a',
    },
    secondary: {
      main: '#f59e0b',
    },
    success: {
      main: '#10b981',
    },
  },
  shape: {
    borderRadius: 12,
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Segoe UI", sans-serif',
    h4: {
      fontWeight: 900,
      color: '#0f172a',
      letterSpacing: '-0.5px',
    }
  }
});

function App() {
  const [updateTrigger, setUpdateTrigger] = useState(0);

  const refreshData = () => {
    setUpdateTrigger(prev => prev + 1);
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ minHeight: '100vh', pb: 8, pt: 4 }}>
        <Container maxWidth="lg">

          <Typography variant="h4" component="h1" gutterBottom sx={{ mb: 4, display: 'flex', alignItems: 'center', gap: 2 }}>
            <span style={{ color: '#10b981' }}></span> Campaign Manager
          </Typography>

          <EmeraldWalletOverview triggerUpdate={updateTrigger} />

          <CampaignGrid onDataChanged={refreshData} />

        </Container>
      </Box>
    </ThemeProvider>
  );
}

export default App;