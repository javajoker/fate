import React, { useState } from 'react';
import './UserForm.css';

const UserForm = ({ onSubmit, profiles, switchProfile }) => {
  const [formData, setFormData] = useState({
    name: '',
    birthDate: '',
    birthTime: '',
    birthPlace: '',
    relationship: 'self'
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!formData.name || !formData.birthDate || !formData.birthPlace) {
      alert('Please fill in at least name, birth date and birth place!');
      return;
    }
    onSubmit(formData);
  };

  return (
    <div className="user-form-container fade-in">
      <div className="user-form-card">
        <div className="form-header">
          <h1>Your Cosmic Journey</h1>
          <p>Enter your birth details to discover your fortune</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">Name</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              placeholder="Enter your name"
              required
            />
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="birthDate">Birth Date</label>
              <input
                type="date"
                id="birthDate"
                name="birthDate"
                value={formData.birthDate}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="birthTime">Birth Time (optional)</label>
              <input
                type="time"
                id="birthTime"
                name="birthTime"
                value={formData.birthTime}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="gender">Gender</label>
              <select
                id="gender"
                name="gender"
                value={formData.gender}
                onChange={handleChange}
              >
                <option value="0">Male</option>
                <option value="1">Female</option>
              </select>
            </div>

            <div className="form-group">
              <label htmlFor="birthPlace">Birth Place</label>
              <input
                type="text"
                id="birthPlace"
                name="birthPlace"
                value={formData.birthPlace}
                onChange={handleChange}
                placeholder="City, Country"
                required
              />
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="relationship">Relationship</label>
            <select
              id="relationship"
              name="relationship"
              value={formData.relationship}
              onChange={handleChange}
            >
              <option value="self">Self</option>
              <option value="partner">Partner</option>
              <option value="child">Child</option>
              <option value="parent">Parent</option>
              <option value="friend">Friend</option>
              <option value="other">Other</option>
            </select>
          </div>

          <button type="submit" className="submit-btn pulse">
            Reveal My Fortune
          </button>
        </form>

        {profiles.length > 0 && (
          <div className="saved-profiles">
            <h3>Saved Profiles</h3>
            <div className="profile-list">
              {profiles.map((profile, index) => (
                <div
                  key={index}
                  className="profile-item"
                  onClick={() => switchProfile(profile)}
                >
                  <div className="profile-name">[{profile.gender ? 'F' : 'M'}] {profile.name}</div>
                  <div className="profile-details">
                    {new Date(profile.birthDate).toLocaleDateString()}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default UserForm;
