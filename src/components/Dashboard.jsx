import React, { useState, useEffect } from "react";
import "./Dashboard.css";
import { useTranslation } from "react-i18next";
import LanguageSwitcher from "./LanguageSwitcher";
import TodayFortune from "./TodayFortune";
import LifeChart from "./LifeChart";
import UserInfo from "./UserInfo";

const Dashboard = ({
  userData,
  resetUser,
  profileList,
  updateProfiles,
  switchProfile,
}) => {
  const [activeTab, setActiveTab] = useState("fortune");
  const [isAnimating, setIsAnimating] = useState(false);

  const { t } = useTranslation();

  const handleTabChange = (tab) => {
    if (tab !== activeTab) {
      setIsAnimating(true);
      setTimeout(() => {
        setActiveTab(tab);
        setIsAnimating(false);
      }, 300);
    }
  };

  // Show location information if available
  const getLocationDisplay = () => {
    if (userData.latitude && userData.longitude) {
      return (
        <span className="user-location">
          {userData.birthPlace}
          <span className="coord-badge">
            {userData.latitude}, {userData.longitude}
          </span>
        </span>
      );
    }
    return <span className="user-birthdate">{userData.birthPlace}</span>;
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <LanguageSwitcher />
        <h1>{t("dash-header")}</h1>
        <div className="user-info-brief">
          <div className="tab active">
            <span className="user-name">
              {/* [{userData.gender ? t("gender-f") : t("gender-m")}]  */}
              {userData.name}
            </span>
            {/* <span className="user-birthdate">
              {new Date(userData.birthDate).toLocaleDateString()}
            </span>
            {getLocationDisplay()} */}
          </div>
        </div>
      </header>

      <div className="tab-container">
        <div className="tabs">
          <button
            className={`tab ${activeTab === "fortune" ? "active" : ""}`}
            onClick={() => handleTabChange("fortune")}
          >
            {t("today-tab")}
          </button>
          <button
            className={`tab ${activeTab === "life" ? "active" : ""}`}
            onClick={() => handleTabChange("life")}
          >
            {t("life-tab")}
          </button>
          <button
            className={`tab ${activeTab === "info" ? "active" : ""}`}
            onClick={() => handleTabChange("info")}
          >
            {t("info-tab")}
          </button>
        </div>

        <div className={`tab-content ${isAnimating ? "fade-out" : "fade-in"}`}>
          {activeTab === "fortune" && <TodayFortune userData={userData} />}
          {activeTab === "life" && <LifeChart userData={userData} />}
          {activeTab === "info" && (
            <UserInfo
              userData={userData}
              resetUser={resetUser}
              profileList={profileList}
              updateProfiles={updateProfiles}
              switchProfile={switchProfile}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
