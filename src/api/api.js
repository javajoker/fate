import axios from "axios";

export const PORT = 4000;
export const BASE_URL = `http://localhost:${PORT}`;
export const API_URL = `${BASE_URL}/api`;

export const fate = async (info) => {
  try {
    const response = await axios.get(
      `${API_URL}/fate/${info.gender}/${info.year}/${info.month}/${info.day}/${info.hour}/${info.zone}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fate:", error);
    throw error;
  }
};

export const fortune = async (info) => {
  try {
    const response = await axios.post(`${API_URL}/fortune`, info);
    return response.data;
  } catch (error) {
    console.error("Error fortune:", error);
    throw error;
  }
};

export const getEnvironmentScript = (fortuneEnviroment) => {
  return `${BASE_URL}/res/env/${fortuneEnviroment.toLowerCase()}.js`;
};
