import React, { useState } from 'react';
import './UserInfo.css';

const UserInfo = ({ userData, resetUser, profileList, updateProfiles, switchProfile }) => {
  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({ ...userData });
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [deleteIndex, setDeleteIndex] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Update profile in profileList
    const updatedProfiles = [...profileList];
    const index = updatedProfiles.findIndex(profile =>
      profile.name === userData.name &&
      profile.birthDate === userData.birthDate &&
      profile.birthPlace === userData.birthPlace
    );

    if (index !== -1) {
      updatedProfiles[index] = formData;
      updateProfiles(updatedProfiles);
      localStorage.setItem('fortuneProfiles', JSON.stringify(updatedProfiles));
    }

    // Update current user data
    switchProfile(formData);
    setEditMode(false);
  };

  const handleDelete = (index) => {
    setDeleteIndex(index);
    setShowConfirmDelete(true);
  };

  const confirmDelete = () => {
    const updatedProfiles = [...profileList];
    updatedProfiles.splice(deleteIndex, 1);
    updateProfiles(updatedProfiles);
    localStorage.setItem('fortuneProfiles', JSON.stringify(updatedProfiles));

    // If current profile is deleted, reset
    if (deleteIndex === profileList.findIndex(profile =>
      profile.name === userData.name &&
      profile.birthDate === userData.birthDate &&
      profile.birthPlace === userData.birthPlace
    )) {
      resetUser();
    }

    setShowConfirmDelete(false);
  };

  const cancelDelete = () => {
    setShowConfirmDelete(false);
    setDeleteIndex(null);
  };

  return (
    <div className="user-info-container slide-in-right">
      {editMode ? (
        <div className="edit-form-container">
          <h3>Edit Profile</h3>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="name">Name</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
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

            <div className="form-actions">
              <button type="submit" className="save-btn">Save Changes</button>
              <button
                type="button"
                className="cancel-btn"
                onClick={() => setEditMode(false)}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      ) : (
        <>
          <div className="current-profile">
            <h3>Current Profile</h3>
            <div className="profile-details-card">
              <div className="profile-header">
                <h4>[{userData.gender ? 'F' : 'M'}] {userData.name}</h4>
                <span className="relationship-tag">{userData.relationship}</span>
              </div>

              <div className="profile-detail">
                <span className="detail-label">Birth Date:</span>
                <span className="detail-value">{new Date(userData.birthDate).toLocaleDateString()}</span>
              </div>

              {userData.birthTime && (
                <div className="profile-detail">
                  <span className="detail-label">Birth Time:</span>
                  <span className="detail-value">{userData.birthTime}</span>
                </div>
              )}

              <div className="profile-detail">
                <span className="detail-label">Birth Place:</span>
                <span className="detail-value">{userData.birthPlace}</span>
              </div>

              <div className="profile-actions">
                <button
                  className="edit-btn"
                  onClick={() => setEditMode(true)}
                >
                  Edit
                </button>
                <button
                  className="new-reading-btn"
                  onClick={resetUser}
                >
                  New Reading
                </button>
              </div>
            </div>
          </div>

          {profileList.length > 1 && (
            <div className="saved-profiles">
              <h3>Saved Profiles</h3>
              <div className="profiles-grid">
                {profileList.map((profile, index) => {
                  // Skip current profile
                  if (profile.name === userData.name &&
                    profile.birthDate === userData.birthDate &&
                    profile.birthPlace === userData.birthPlace) {
                    return null;
                  }

                  return (
                    <div className="profile-card" key={index}>
                      <div className="profile-header">
                        <h4>[{profile.gender ? 'F' : 'M'}] {profile.name}</h4>
                        <span className="relationship-tag">{profile.relationship}</span>
                      </div>

                      <div className="profile-brief">
                        <div>{new Date(profile.birthDate).toLocaleDateString()}</div>
                        <div>{profile.birthPlace}</div>
                      </div>

                      <div className="profile-card-actions">
                        <button
                          className="switch-btn"
                          onClick={() => switchProfile(profile)}
                        >
                          Switch
                        </button>
                        <button
                          className="delete-btn"
                          onClick={() => handleDelete(index)}
                        >
                          Delete
                        </button>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          )}
        </>
      )}

      {showConfirmDelete && (
        <div className="modal-overlay">
          <div className="confirm-modal">
            <h4>Confirm Delete</h4>
            <p>Are you sure you want to delete this profile?</p>
            <div className="modal-actions">
              <button
                className="confirm-btn"
                onClick={confirmDelete}
              >
                Yes, Delete
              </button>
              <button
                className="cancel-btn"
                onClick={cancelDelete}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserInfo;
