import React, { useState, useEffect } from "react";
import i18n from "../i18n";
import "./LanguageSwitcher.css";

function LanguageSwitcher() {
  const lang = localStorage.getItem("lang") ?? "en";
  const [language, setLanguage] = useState(lang);

  useEffect(() => {
    i18n.changeLanguage(language);
    localStorage.setItem('lang', language);
  }, [language]);

  return (
    <div>
      <button
        className="lang-tag"
        onClick={() => setLanguage(language === "en" ? "zh" : "en")}
      >
        {language === "en" ? "繁" : "EN"}
      </button>
    </div>
  );
}

export default LanguageSwitcher;
