import React, { useState, useEffect } from 'react';
import './Dashboard.css';
import TodayFortune from './TodayFortune';
import LifeChart from './LifeChart';
import UserInfo from './UserInfo';

const Dashboard = ({ userData, resetUser, profileList, updateProfiles, switchProfile }) => {
  const [activeTab, setActiveTab] = useState('fortune');
  const [isAnimating, setIsAnimating] = useState(false);

  const handleTabChange = (tab) => {
    if (tab !== activeTab) {
      setIsAnimating(true);
      setTimeout(() => {
        setActiveTab(tab);
        setIsAnimating(false);
      }, 300);
    }
  };

  return (
    <div className="dashboard-container">
      <header className="dashboard-header">
        <h1>Cosmic Insights</h1>
        <div className="user-info-brief">
          <span className="user-name">[{userData.gender ? 'F' : 'M'}] {userData.name}</span>
          <span className="user-birthdate">{new Date(userData.birthDate).toLocaleDateString()}</span>
        </div>
      </header>

      <div className="tab-container">
        <div className="tabs">
          <button
            className={`tab ${activeTab === 'fortune' ? 'active' : ''}`}
            onClick={() => handleTabChange('fortune')}
          >
            Today's Fortune
          </button>
          <button
            className={`tab ${activeTab === 'life' ? 'active' : ''}`}
            onClick={() => handleTabChange('life')}
          >
            My Life
          </button>
          <button
            className={`tab ${activeTab === 'info' ? 'active' : ''}`}
            onClick={() => handleTabChange('info')}
          >
            My Info
          </button>
        </div>

        <div className={`tab-content ${isAnimating ? 'fade-out' : 'fade-in'}`}>
          {activeTab === 'fortune' && <TodayFortune userData={userData} />}
          {activeTab === 'life' && <LifeChart userData={userData} />}
          {activeTab === 'info' && (
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
