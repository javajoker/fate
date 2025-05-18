import React, { useEffect, useState } from "react";
import "./UserInfo.css";
import { useTranslation } from "react-i18next";
import { geoSuggestion } from "../api/api";

const UserInfo = ({
  userData,
  resetUser,
  profileList,
  updateProfiles,
  switchProfile,
}) => {
  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({ ...userData });
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [deleteIndex, setDeleteIndex] = useState(null);
  const [locationSuggestions, setLocationSuggestions] = useState([]);
  const [isSearching, setIsSearching] = useState(false);

  const { t } = useTranslation();

  useEffect(() => {
    setFormData({ ...userData });
  }, [userData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });

    // Clear coordinates when birth place changes
    if (name === "birthPlace") {
      setFormData((prev) => ({
        ...prev,
        latitude: "",
        longitude: "",
        timezone: "",
      }));

      // Get location suggestions
      if (value.length > 2) {
        setIsSearching(true);
        fetchLocationSuggestions(value);
      } else {
        setLocationSuggestions([]);
      }
    }
  };

  const fetchLocationSuggestions = (query) => {
    setTimeout(() => {
      const suggestions = geoSuggestion(query);
      setLocationSuggestions(suggestions);
      setIsSearching(false);
    }, 500);
  };

  const selectLocation = (suggestion) => {
    console.log(suggestion);
    setFormData({
      ...formData,
      birthPlace: suggestion.place,
      latitude: suggestion.lat.toFixed(4),
      longitude: suggestion.lng.toFixed(4),
      timezone: suggestion.timezone,
    });
    setLocationSuggestions([]);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Update profile in profileList
    const updatedProfiles = [...profileList];
    const index = updatedProfiles.findIndex(
      (profile) =>
        profile.name === userData.name &&
        profile.birthDate === userData.birthDate &&
        profile.birthPlace === userData.birthPlace
    );

    if (index !== -1) {
      updatedProfiles[index] = formData;
      updateProfiles(updatedProfiles);
      localStorage.setItem("fortuneProfiles", JSON.stringify(updatedProfiles));
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
    localStorage.setItem("fortuneProfiles", JSON.stringify(updatedProfiles));

    // If current profile is deleted, reset
    if (
      deleteIndex ===
      profileList.findIndex(
        (profile) =>
          profile.name === userData.name &&
          profile.birthDate === userData.birthDate &&
          profile.birthPlace === userData.birthPlace
      )
    ) {
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
          <h3>{t("info-edit")}</h3>
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="name">{t("info-name")}</label>
              <input
                type="text"
                id="name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </div>

            <div className="form-group">
              <label htmlFor="gender">{t("info-gender")}</label>
              <select
                id="gender"
                name="gender"
                value={formData.gender}
                onChange={handleChange}
              >
                <option value="0">{t("info-m")}</option>
                <option value="1">{t("info-f")}</option>
              </select>
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="birthDate">{t("info-date")}</label>
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
                <label htmlFor="birthTime">{t("info-time")}</label>
                <input
                  type="time"
                  id="birthTime"
                  name="birthTime"
                  value={formData.birthTime}
                  onChange={handleChange}
                />
              </div>
            </div>

            <div className="form-group location-input">
              <label htmlFor="birthPlace">{t("info-place")}</label>
              <input
                type="text"
                id="birthPlace"
                name="birthPlace"
                value={formData.birthPlace}
                onChange={handleChange}
                required
              />
              {isSearching && (
                <div className="location-loading">
                  <div className="location-spinner"></div>
                  <span>{t("info-place-search")}</span>
                </div>
              )}
              {locationSuggestions.length > 0 && (
                <div className="location-suggestions">
                  {locationSuggestions.map((suggestion, index) => (
                    <div
                      key={index}
                      className="location-item"
                      onClick={() => selectLocation(suggestion)}
                    >
                      <div className="location-name">{suggestion.place}</div>
                      <div className="location-coords">
                        {suggestion.lat.toFixed(4)}, {suggestion.lng.toFixed(4)}
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>

            <div className="form-row">
              <div className="form-group">
                <label htmlFor="latitude">{t("info-lat")}</label>
                <input
                  type="text"
                  id="latitude"
                  name="latitude"
                  value={formData.latitude}
                  onChange={handleChange}
                  placeholder={t("auto-fill")}
                  readOnly
                />
              </div>

              <div className="form-group">
                <label htmlFor="longitude">{t("info-lng")}</label>
                <input
                  type="text"
                  id="longitude"
                  name="longitude"
                  value={formData.longitude}
                  onChange={handleChange}
                  placeholder={t("auto-fill")}
                  readOnly
                />
              </div>
            </div>

            <div className="form-group">
              <label htmlFor="relationship">{t("info-relationship")}</label>
              <select
                id="relationship"
                name="relationship"
                value={formData.relationship}
                onChange={handleChange}
              >
                <option value="self">{t("info-rself")}</option>
                <option value="partner">{t("info-rpartner")}</option>
                <option value="child">{t("info-rchild")}</option>
                <option value="parent">{t("info-rparent")}</option>
                <option value="friend">{t("info-rfriend")}</option>
                <option value="other">{t("info-rother")}</option>
              </select>
            </div>

            <div className="form-actions">
              <button type="submit" className="save-btn">
                {t("info-save")}
              </button>
              <button
                type="button"
                className="cancel-btn"
                onClick={() => setEditMode(false)}
              >
                {t("info-cancel")}
              </button>
            </div>
          </form>
        </div>
      ) : (
        <>
          <div className="current-profile">
            <h3>{t("info-current")}</h3>
            <div className="profile-details-card">
              <div className="profile-header">
                <h4>
                  [{userData.gender ? t("gender-f") : t("gender-m")}]{" "}
                  {userData.name}
                </h4>
                <span className="relationship-tag">
                  {userData.relationship}
                </span>
              </div>

              <div className="profile-detail">
                <span className="detail-label">{t("info-date")}:</span>
                <span className="detail-value">
                  {new Date(userData.birthDate).toLocaleDateString()}
                </span>
              </div>

              {userData.birthTime && (
                <div className="profile-detail">
                  <span className="detail-label">{t("info-time")}:</span>
                  <span className="detail-value">{userData.birthTime}</span>
                </div>
              )}

              <div className="profile-detail">
                <span className="detail-label">{t("info-place")}:</span>
                <span className="detail-value">{userData.birthPlace}</span>
              </div>

              <div className="profile-actions">
                <button className="edit-btn" onClick={() => setEditMode(true)}>
                  {t("info-edit")}
                </button>
                <button className="new-reading-btn" onClick={resetUser}>
                  {t("info-new")}
                </button>
              </div>
            </div>
          </div>

          {profileList.length > 1 && (
            <div className="saved-profiles">
              <h3>{t("info-profiles")}</h3>
              <div className="profiles-grid">
                {profileList.map((profile, index) => {
                  // Skip current profile
                  if (
                    profile.name === userData.name &&
                    profile.birthDate === userData.birthDate &&
                    profile.birthPlace === userData.birthPlace
                  ) {
                    return null;
                  }

                  return (
                    <div className="profile-card" key={index}>
                      <div className="profile-header">
                        <h4>
                          [{profile.gender ? t("gender-f") : t("gender-m")}]{" "}
                          {profile.name}
                        </h4>
                        <span className="relationship-tag">
                          {profile.relationship}
                        </span>
                      </div>

                      <div className="profile-brief">
                        <div>
                          {new Date(profile.birthDate).toLocaleDateString()}
                        </div>
                        <div>{profile.birthPlace}</div>
                      </div>

                      <div className="profile-card-actions">
                        <button
                          className="switch-btn"
                          onClick={() => switchProfile(profile)}
                        >
                          {t("info-switch")}
                        </button>
                        <button
                          className="delete-btn"
                          onClick={() => handleDelete(index)}
                        >
                          {t("info-delete")}
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
            <h4>{t("info-delete-confirm")}</h4>
            <p>{t("info-delete-hint")}</p>
            <div className="modal-actions">
              <button className="confirm-btn" onClick={confirmDelete}>
                {t("info-delete-yes")}
              </button>
              <button className="cancel-btn" onClick={cancelDelete}>
                {t("info-cancel")}
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default UserInfo;
