import axios from 'axios';

const api = axios.create({
  baseURL: 'https://campaign-hub-api.onrender.com/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

export default api;