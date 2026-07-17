import React, { useState, useEffect } from 'react';
import {
  Drawer, Box, Typography, TextField, Button, MenuItem,
  Autocomplete, Alert, Divider, IconButton
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import api from '../services/api';

const KEYWORD_SUGGESTIONS = ['marketing', 'seo', 'ads', 'social-media', 'b2b', 'b2c', 'promocja', 'sprzedaż'];

const TOWN_SUGGESTIONS = [
  'Warszawa', 'Kraków', 'Łódź', 'Wrocław', 'Poznań',
  'Gdańsk', 'Szczecin', 'Bydgoszcz', 'Lublin', 'Katowice'
];

const initialFormState = {
  campaignName: '', keywords: [], bidAmount: '', campaignFund: '',
  status: 'ON', town: '', radius: ''
};

const CampaignDrawerForm = ({ open, onClose, onSuccess, campaignData }) => {
  const [formData, setFormData] = useState(initialFormState);
  const [error, setError] = useState('');

  useEffect(() => {
    if (campaignData && open) {
      setFormData(campaignData);
    } else if (!campaignData && open) {
      setFormData(initialFormState);
    }
  }, [campaignData, open]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (campaignData && campaignData.id) {
        await api.patch(`/campaigns/${campaignData.id}`, formData);
      } else {
        await api.post('/campaigns', formData);
      }
      setFormData(initialFormState);
      onSuccess();
      onClose();
    } catch (err) {
      if (err.response && err.response.data) {
        const errorMsg = typeof err.response.data === 'object'
            ? Object.values(err.response.data).join(' | ')
            : err.response.data;
        setError(errorMsg || 'Wystąpił błąd zapisu.');
      } else {
        setError('Brak połączenia z serwerem.');
      }
    }
  };

  return (
    <Drawer anchor="right" open={open} onClose={onClose}>
      <Box sx={{ width: { xs: '100vw', sm: 450 }, p: 3, display: 'flex', flexDirection: 'column', height: '100%' }}>

        <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
          <Typography variant="h6" fontWeight="bold">
            {campaignData ? 'Edytuj kampanię' : 'Dodaj nową kampanię'}
          </Typography>
          <IconButton onClick={onClose}><CloseIcon /></IconButton>
        </Box>
        <Divider sx={{ mb: 3 }} />

        <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', flexGrow: 1, gap: '16px' }}>
          {error && <Alert severity="error">{error}</Alert>}

          <TextField label="Nazwa kampanii" name="campaignName" value={formData.campaignName} onChange={handleChange} required fullWidth />

          <Autocomplete
            multiple freeSolo options={KEYWORD_SUGGESTIONS} value={formData.keywords || []}
            onChange={(e, newValue) => setFormData(prev => ({ ...prev, keywords: newValue }))}
            renderInput={(params) => <TextField {...params} label="Słowa kluczowe (Typeahead)" placeholder="Wpisz lub wybierz..." />}
          />

          <Box sx={{ display: 'flex', gap: 2 }}>
            <TextField label="Budżet (PLN)" name="campaignFund" type="number" value={formData.campaignFund} onChange={handleChange} required fullWidth />
            <TextField label="Stawka (PLN)" name="bidAmount" type="number" value={formData.bidAmount} onChange={handleChange} required fullWidth />
          </Box>

          <TextField select label="Status" name="status" value={formData.status} onChange={handleChange} fullWidth>
            <MenuItem value="ON">Aktywna (ON)</MenuItem>
            <MenuItem value="OFF">Nieaktywna (OFF)</MenuItem>
          </TextField>

          <Box sx={{ display: 'flex', gap: 2 }}>
                      <Autocomplete
                        freeSolo
                        options={TOWN_SUGGESTIONS}
                        value={formData.town || ''}
                        onInputChange={(event, newInputValue) => {
                          setFormData(prev => ({ ...prev, town: newInputValue }));
                        }}
                        sx={{ flex: 2 }}
                        renderInput={(params) => <TextField {...params} label="Miasto" required fullWidth />}
                      />

                      <TextField
                        label="Promień (km)"
                        name="radius"
                        type="number"
                        value={formData.radius}
                        onChange={handleChange}
                        required
                        sx={{ flex: 1 }}
                      />
                    </Box>

          <Box mt="auto" pt={3} display="flex" gap={2} justifyContent="flex-end">
            <Button onClick={onClose} color="inherit">Anuluj</Button>
            <Button type="submit" variant="contained" color="primary" disableElevation>
              {campaignData ? 'Zapisz zmiany' : 'Dodaj kampanię'}
            </Button>
          </Box>
        </form>
      </Box>
    </Drawer>
  );
};

export default CampaignDrawerForm;