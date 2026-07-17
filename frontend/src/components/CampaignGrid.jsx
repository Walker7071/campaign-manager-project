import React, { useState, useEffect } from 'react';
import {
  Grid, Card, CardContent, CardActions, Button, Typography,
  Box, Chip
} from '@mui/material';
import api from '../services/api';
import CampaignDrawerForm from './CampaignDrawerForm';

const CampaignGrid = ({ onDataChanged }) => {
  const [campaigns, setCampaigns] = useState([]);
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [selectedCampaign, setSelectedCampaign] = useState(null);

  const fetchCampaigns = async () => {
    try {
      const response = await api.get('/campaigns');
      setCampaigns(response.data.content);
    } catch (error) {
      console.error('Błąd pobierania danych:', error);
    }
  };

  useEffect(() => {
    fetchCampaigns();
  }, []);

  const refreshAll = () => {
    fetchCampaigns();
    if (onDataChanged) onDataChanged();
  };

  const handleDelete = async (id) => {
    if (window.confirm('Czy na pewno chcesz usunąć tę kampanię? Budżet wróci do konta Emerald.')) {
      try {
        await api.delete(`/campaigns/${id}`);
        refreshAll();
      } catch (error) {
        console.error('Błąd usuwania:', error);
      }
    }
  };

  const openNewForm = () => {
    setSelectedCampaign(null);
    setIsDrawerOpen(true);
  };

  const openEditForm = (item) => {
    setSelectedCampaign(item);
    setIsDrawerOpen(true);
  };

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h6" fontWeight="bold" color="primary.main">
          Przegląd Kampanii
        </Typography>
        <Button variant="contained" color="primary" onClick={openNewForm} disableElevation sx={{ fontWeight: 'bold', px: 3, py: 1 }}>
          + Nowa Kampania
        </Button>
      </Box>

      {campaigns.length === 0 ? (
        <Box p={4} textAlign="center" bgcolor="white" borderRadius={3} border="1px dashed #cbd5e1">
          <Typography color="textSecondary">Brak kampanii. Kliknij "Nowa Kampania", aby zacząć.</Typography>
        </Box>
      ) : (
        <Grid container spacing={3}>
          {campaigns.map((item) => (
            <Grid item xs={12} sm={6} md={4} key={item.id}>
              <Card sx={{
                borderRadius: 4,
                boxShadow: '0 4px 12px rgba(0,0,0,0.03)',
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                transition: 'transform 0.2s, box-shadow 0.2s',
                '&:hover': { transform: 'translateY(-4px)', boxShadow: '0 8px 24px rgba(0,0,0,0.08)' }
              }}>
                <CardContent sx={{ flexGrow: 1, p: 3 }}>
                  <Box display="flex" justifyContent="space-between" alignItems="flex-start" mb={2}>
                    <Typography variant="h6" fontWeight="bold" noWrap title={item.campaignName}>
                      {item.campaignName}
                    </Typography>
                    <Chip
                      label={item.status === 'ON' ? 'Aktywna' : 'Wstrzymana'}
                      color={item.status === 'ON' ? 'success' : 'default'}
                      size="small"
                      sx={{ fontWeight: 'bold', fontSize: '0.7rem' }}
                    />
                  </Box>

                  <Typography variant="body2" color="textSecondary" mb={1}>
                    📍 {item.town} (+{item.radius}km)
                  </Typography>

                  <Box mt={2} p={2} bgcolor="#f8fafc" borderRadius={3}>
                    <Grid container spacing={1}>
                      <Grid item xs={6}>
                        <Typography variant="caption" color="textSecondary" display="block">Budżet</Typography>
                        <Typography variant="body2" fontWeight="bold" color="primary.main">{item.campaignFund} PLN</Typography>
                      </Grid>
                      <Grid item xs={6}>
                        <Typography variant="caption" color="textSecondary" display="block">Stawka</Typography>
                        <Typography variant="body2" fontWeight="bold" color="primary.main">{item.bidAmount} PLN</Typography>
                      </Grid>
                    </Grid>
                  </Box>
                </CardContent>

                <CardActions sx={{ justifyContent: 'flex-end', p: 2, pt: 0, gap: 1 }}>
                  <Button size="small" color="primary" sx={{ fontWeight: 'bold' }} onClick={() => openEditForm(item)}>
                    Edytuj
                  </Button>
                  <Button size="small" color="error" sx={{ fontWeight: 'bold' }} onClick={() => handleDelete(item.id)}>
                    Usuń
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      <CampaignDrawerForm
        open={isDrawerOpen}
        onClose={() => setIsDrawerOpen(false)}
        onSuccess={refreshAll}
        campaignData={selectedCampaign}
      />
    </Box>
  );
};

export default CampaignGrid;