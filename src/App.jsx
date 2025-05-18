import React, { useState, useEffect } from 'react';
import './App.css';
import Dashboard from './components/Dashboard';
import UserForm from './components/UserForm';

const App = () => {
  const [userData, setUserData] = useState(null);
  const [profileList, setProfileList] = useState([]);
  const [isFormVisible, setIsFormVisible] = useState(true);

  useEffect(() => {
    // Load saved profiles from localStorage
    const savedProfiles = localStorage.getItem('fortuneProfiles');
    if (savedProfiles) {
      setProfileList(JSON.parse(savedProfiles));
    }

    // Load last active profile
    const lastProfile = localStorage.getItem('lastActiveProfile');
    if (lastProfile) {
      setUserData(JSON.parse(lastProfile));
      setIsFormVisible(false);
    }
  }, []);

  const handleSubmitData = (data) => {
    setUserData(data);
    setIsFormVisible(false);

    // Save to localStorage
    localStorage.setItem('lastActiveProfile', JSON.stringify(data));

    // Add to profiles if not exists
    const exists = profileList.some(profile =>
      profile.name === data.name &&
      profile.birthDate === data.birthDate &&
      profile.birthPlace === data.birthPlace
    );

    if (!exists) {
      const newProfileList = [...profileList, data];
      setProfileList(newProfileList);
      localStorage.setItem('fortuneProfiles', JSON.stringify(newProfileList));
    }
  };

  const resetUserData = () => {
    setUserData(null);
    setIsFormVisible(true);
    localStorage.removeItem('lastActiveProfile');
  };

  const switchProfile = (profile) => {
    setUserData(profile);
    localStorage.setItem('lastActiveProfile', JSON.stringify(profile));
    setIsFormVisible(false);
  };

  return (
    <div className="app">
      {isFormVisible || !userData ? (
        <UserForm
          onSubmit={handleSubmitData}
          profiles={profileList}
          switchProfile={switchProfile}
        />
      ) : (
        <Dashboard
          userData={userData}
          resetUser={resetUserData}
          profileList={profileList}
          updateProfiles={setProfileList}
          switchProfile={switchProfile}
        />
      )}
    </div>
  );
};

export default App;