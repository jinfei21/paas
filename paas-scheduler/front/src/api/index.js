import axios from 'axios'
import services from './services'

axios.defaults.timeout = 5000;
axios.defaults.baseURL = 'http://172.17.2.161:8089';



export const api = services;


