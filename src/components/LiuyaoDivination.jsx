import React, { useState } from "react";
import { Coins, RotateCcw, Book, Star, Globe } from "lucide-react";
import { useTranslation } from "react-i18next";
import "./LiuyaoDivination.css";
import { iching_content, iching_list, iching_place } from "../api/iching";

const LiuyaoDivination = () => {
  const [hexagram, setHexagram] = useState([]);
  const [isThrowingCoins, setIsThrowingCoins] = useState(false);
  const [currentLine, setCurrentLine] = useState(0);
  const [showResult, setShowResult] = useState(false);
  const [language, setLanguage] = useState("en");

  const { t } = useTranslation();

  // Throw coins to get lines
  const throwCoins = async () => {
    if (isThrowingCoins) return;

    setIsThrowingCoins(true);
    setShowResult(false);
    setHexagram([]);
    setCurrentLine(0);

    for (let i = 0; i < 6; i++) {
      await new Promise((resolve) => setTimeout(resolve, 800));

      // Simulate throwing three coins
      const coin1 = Math.random() > 0.5 ? 3 : 2; // Heads 3 points, tails 2 points
      const coin2 = Math.random() > 0.5 ? 3 : 2;
      const coin3 = Math.random() > 0.5 ? 3 : 2;
      const total = coin1 + coin2 + coin3;

      let line;
      if (total === 6)
        line = { type: "yin", changing: false, value: 0 }; // Old yin
      else if (total === 7)
        line = { type: "yang", changing: false, value: 1 }; // Young yang
      else if (total === 8)
        line = { type: "yin", changing: false, value: 0 }; // Young yin
      else line = { type: "yang", changing: true, value: 1 }; // Old yang

      setHexagram((prev) => [...prev, line]);
      setCurrentLine(i + 1);
    }

    setIsThrowingCoins(false);
    setShowResult(true);
  };

  // Reset
  const reset = () => {
    setHexagram([]);
    setShowResult(false);
    setCurrentLine(0);
  };

  // Get hexagram interpretation
  const getHexagramInterpretation = () => {
    if (hexagram.length !== 6) return null;
    const gram1 = hexagram.map((line) => line.value).join(""),
      gram2 = hexagram
        .map((line) => (line.changing ? (line.value ? 0 : 1) : line.value))
        .join(""),
      changings = [];
    hexagram.forEach((v, i) => {
      if (v.changing) {
        changings.push(i);
      }
    });
    const meaning = [],
      name = [];
    changings.forEach((i) => {
      meaning.push(iching_content[iching_list.indexOf(gram1) * 7 + i]);
      meaning.push(iching_content[iching_list.indexOf(gram2) * 7 + i]);
      name.push(iching_place[i]);
    });

    return { name, meaning };
  };

  return (
    <div className="main-card">
      {/* Hexagram Display Area */}
      <div className="hexagram-container">
        <div className="hexagram-display">
          {hexagram.length === 0 ? (
            <div className="empty-state">
              <Book size={32} />
              <p>{t("startMessage")}</p>
            </div>
          ) : (
            <div className="hexagram-lines">
              {Array.from({ length: 6 }, (_, i) => 5 - i).map((index) => {
                const line = hexagram[index];
                const isActive = index < currentLine;

                return (
                  <div key={index} className="line-row">
                    <div className="line-container">
                      <span className="line-label">
                        {isActive ? `${t("line")} ${index + 1}` : ""}
                      </span>
                      <div className="line-visual">
                        {isActive ? (
                          line.type === "yang" ? (
                            <div
                              className={`yang-line ${
                                line.changing ? "changing" : ""
                              }`}
                            />
                          ) : (
                            <>
                              <div
                                className={`yin-line-part ${
                                  line.changing ? "changing" : ""
                                }`}
                              />
                              <div className="yin-line-gap" />
                              <div
                                className={`yin-line-part ${
                                  line.changing ? "changing" : ""
                                }`}
                              />
                            </>
                          )
                        ) : (
                          <div className="line-inactive" />
                        )}
                      </div>
                      <span className="changing-indicator">
                        {isActive && line.changing ? t("changing") : ""}
                      </span>
                    </div>
                  </div>
                );
              })}
            </div>
          )}

          {/* Coin throwing animation indicator */}
          {isThrowingCoins && (
            <div className="coin-animation">
              <Coins className="spinning-coin" size={24} />
              <p className="coin-text">
                {language === "en"
                  ? `${t("throwingCoins")} ${currentLine + 1}...`
                  : `${t("throwingCoins")} ${currentLine + 1} ${t(
                      "line"
                    )}投币...`}
              </p>
            </div>
          )}
        </div>
      </div>

      {/* Divination Result */}
      {showResult && (
        <div className="result-section">
          <h3 className="result-title">{t("hexagramReading")}</h3>
          {(() => {
            const interpretation = getHexagramInterpretation();
            return interpretation ? (
              <div className="result-content">
                <div className="hexagram-name">{interpretation.name}</div>
                {interpretation.meaning.map((v) => (
                  <p className="hexagram-meaning">{v}</p>
                ))}
              </div>
            ) : (
              <p className="hexagram-meaning">Loading interpretation...</p>
            );
          })()}
        </div>
      )}

      {/* Control Buttons */}
      <div className="controls">
        <button
          onClick={throwCoins}
          disabled={isThrowingCoins}
          className="primary-button"
        >
          <Coins size={20} />
          <span>{isThrowingCoins ? t("divining") : t("startDivination")}</span>
        </button>

        {hexagram.length > 0 && (
          <button onClick={reset} className="secondary-button">
            <RotateCcw size={18} />
            <span>{t("newDivination")}</span>
          </button>
        )}
      </div>

      {/* Description */}
      <div className="description">
        <p className="description-text">{t("description")}</p>
      </div>
    </div>
  );
};

export default LiuyaoDivination;
