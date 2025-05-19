import React, { useState } from "react";
import "./UserForm.css";
import { useTranslation } from "react-i18next";
import { geoSuggestion } from "../api/api";

const UserForm = ({ onSubmit, profiles, switchProfile }) => {
  const [formData, setFormData] = useState({
    name: "",
    birthDate: "",
    birthTime: "",
    birthPlace: "",
    latitude: "",
    longitude: "",
    timezone: "",
    relationship: "self",
  });
  const [locationSuggestions, setLocationSuggestions] = useState([]);
  const [isSearching, setIsSearching] = useState(false);

  const { t } = useTranslation();

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
    if (!formData.name || !formData.birthDate || !formData.birthPlace) {
      alert(t("form-hint"));
      return;
    }
    onSubmit(formData);
  };

  return (
    <div className="user-form-container fade-in">
      <div className="user-form-card">
        <div className="form-header">
          <h1>{t("form-header")}</h1>
          <p>{t("form-desc")}</p>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">{t("info-name")}</label>
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
              placeholder={t("info-place-placeholder")}
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

          <button type="submit" className="submit-btn pulse">
            {t("form-submit")}
          </button>
        </form>

        {profiles.length > 0 && (
          <div className="saved-profiles">
            <h3>{t("info-profiles")}</h3>
            <div className="profile-list">
              {profiles.map((profile, index) => (
                <div
                  key={index}
                  className="profile-item"
                  onClick={() => switchProfile(profile)}
                >
                  <div className="profile-name">
                    [{profile.gender == 1 ? t("gender-f") : t("gender-m")}]{" "}
                    {profile.name}
                  </div>
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
