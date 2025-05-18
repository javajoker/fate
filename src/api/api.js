import axios from "axios";

export const PORT = 4000;
export const BASE_URL = `http://localhost:${PORT}`;
export const API_URL = `${BASE_URL}/api`;

export const fate = async (info) => {
  try {
    const response = await axios.get(
      `${API_URL}/fate/${info.gender}/${info.year}/${info.month}/${info.day}/${info.hour}`
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

import { lookupViaCity } from "city-timezones";

export const geoSuggestion = (query) => {
  const lookup = lookupViaCity(query);
  lookup.forEach((v) => {
    v.place = `${v.city}, ${v.province}, ${v.country}`;
  });
  return lookup;
};

import moment from "moment-timezone";

export const getPillarInfo = (userData) => {
  let birthTime = `${userData.birthDate}T${userData.birthTime || "00:00:00"}`;
  if (userData.birthTime && userData.timezone) {
    birthTime = moment
      .tz(birthTime, userData.timezone)
      .tz("Asia/Shanghai")
      .format("YYYY-MM-DD HH:mm:ss");
  }
  const date = new Date(birthTime);
  const standardLng = 114.35, // Longitude of Kaifeng, Henan, China
    timeOffset = ((3600 * 1000) / 15) * (standardLng - 120);
  const realDate = userData.birthTime
    ? new Date(date.getTime() - timeOffset)
    : date;
  return {
    gender: userData.gender || 0,
    year: realDate.getFullYear(),
    month: realDate.getMonth() + 1,
    day: realDate.getDate(),
    hour: userData.birthTime ? realDate.getHours() : -1,
  };
};
