import React, { useState, useEffect } from "react";
import "./TodayFortune.css";
import { useTranslation } from "react-i18next";
import LiuyaoDivination from "./LiuyaoDivination";

const TodayFortune = ({ userData }) => {
  const [fortune, setFortune] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [status, setStatus] = useState([]);
  const [now, setNow] = useState([]);
  const [future, setFuture] = useState([]);

  const { t } = useTranslation();

  useEffect(() => {
    setIsLoading(true);

    setTimeout(() => {
      const today = new Date();
      const dateString = `${today.getFullYear()}-${today.getMonth()}-${today.getDate()}`;
      const userString = `${userData.name}-${userData.birthDate}`;
      setFortune({
        date: today.toLocaleDateString(),
      });

      setIsLoading(false);
    }, 1000);
  }, [userData]);

  if (isLoading) {
    return (
      <div className="fortune-loading">
        <div className="spinner"></div>
        <p>{t("today-loading")}</p>
      </div>
    );
  }

  const liuyaoUpdate = ({ status, now, future }) => {
    setStatus(status);
    setNow(now);
    setFuture(future);
  };

  return (
    <div className="fortune-container slide-in-right">
      <div className="fortune-date">
        <h2>{t("today-header")}</h2>
        <p>{t("today-for", { date: fortune?.date ?? "" })}</p>
      </div>

      {status?.length > 0 && (
        <div className="fortune-card mood">
          <h3>{t("today-mood")}</h3>
          {status.map((v) => (
            <p>{v}</p>
          ))}
        </div>
      )}

      {future?.length > 0 && (
        <div className="fortune-card luck">
          <h3>{t("today-luck")}</h3>
          {future.map((v) => (
            <p>{v}</p>
          ))}
        </div>
      )}

      {now?.length > 0 && (
        <div className="fortune-card advice">
          <h3>{t("today-advice")}</h3>
          {now.map((v) => (
            <p>{v}</p>
          ))}
        </div>
      )}
      <LiuyaoDivination onUpdate={liuyaoUpdate} />
    </div>
  );
};

export default TodayFortune;
