import axios from "axios";

axios.defaults.timeout = 50000;

axios.interceptors.request.use(
  (config) => {
    // ...
    return config;
  },
  (error) => {
    return Promise.error(error);
  }
);

function getName(params) {
  return axios.get(`xx/xx/${params}`);
}

export { getName };
