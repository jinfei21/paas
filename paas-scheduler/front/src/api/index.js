import axios from 'axios'
import services from './services'

axios.defaults.timeout = 5000;
axios.defaults.baseURL = 'http://localhost:8089';



export const api = services;


