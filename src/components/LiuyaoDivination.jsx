import React, { useEffect, useState } from "react";
import { Coins, RotateCcw, Book, Star, Globe } from "lucide-react";
import { useTranslation } from "react-i18next";
import "./LiuyaoDivination.css";
import {
  iching_list,
  iching_back,
  iching_pure,
  iching_travel,
} from "../api/iching-base";
import {
  iching_abbr as iching_abbr_zh,
  iching_content as iching_content_zh,
  iching_place as iching_place_zh,
} from "../api/iching.zh-TW";
import {
  iching_abbr as iching_abbr_en,
  iching_content as iching_content_en,
  iching_place as iching_place_en,
} from "../api/iching.en";

const LiuyaoDivination = ({ onUpdate }) => {
  const [hexagram, setHexagram] = useState([]);
  const [isThrowingCoins, setIsThrowingCoins] = useState(false);
  const [currentLine, setCurrentLine] = useState(0);
  const [showResult, setShowResult] = useState(false);
  const [interpretation, setInterpretataion] = useState({});

  const { t, i18n } = useTranslation();

  useEffect(() => {
    if (!showResult || !onUpdate) return;
    getHexagramInterpretation();
  }, [showResult]);

  useEffect(() => {
    onUpdate(interpretation);
  }, [interpretation]);

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
    setInterpretataion({});
  };

  // Get hexagram interpretation
  const getHexagramInterpretation = () => {
    if (hexagram.length !== 6) return null;

    let iching_abbr, iching_content, iching_place;
    switch (i18n.language) {
      case "zh":
        iching_abbr = iching_abbr_zh;
        iching_content = iching_content_zh;
        iching_place = iching_place_zh;
        break;
      case "en":
        iching_abbr = iching_abbr_en;
        iching_content = iching_content_en;
        iching_place = iching_place_en;
        break;
    }

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
    const status = [],
      now = [],
      future = [];

    if (changings.length > 0) {
      changings.forEach((i) => {
        status.push(iching_place[i]);
        const id1 = iching_list.indexOf(gram1) * 7 + i,
          id2 = iching_list.indexOf(gram2) * 7 + i;
        now.push(`(${iching_abbr[id1]}) ${iching_content[id1]}`);
        future.push(`(${iching_abbr[id2]}) ${iching_content[id2]}`);
      });
    } else {
      let i = 1;
      if (iching_pure.indexOf(gram1) >= 0) {
        i = 6;
      } else if (iching_travel.indexOf(gram1) >= 0) {
        i = 4;
      } else if (iching_back.indexOf(gram1) >= 0) {
        i = 3;
      } else {
        const s =
          ((hexagram[0].value == hexagram[3].value ? 0 : 1) << 2) &
          ((hexagram[1].value == hexagram[4].value ? 0 : 1) << 1) &
          ((hexagram[2].value == hexagram[5].value ? 0 : 1) << 0);
        switch (s) {
          case 4:
            i = 1;
            break;
          case 3:
            i = 4;
            break;
          case 2:
            i = 3;
            break;
          case 5:
            i = 4;
            break;
          case 1:
            i = 5;
            break;
          case 6:
            i = 2;
            break;
          case 7:
            i = 3;
            break;
        }
      }
      const id1 = iching_list.indexOf(gram1) * 7 + i,
        id2 = iching_list.indexOf(gram1) * 7 + 1 + ((i + 2) % 6);
      now.push(`(${iching_abbr[id1]}) ${iching_content[id1]}`);
      future.push(`(${iching_abbr[id2]}) ${iching_content[id2]}`);
    }

    setInterpretataion({ status, now, future });
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
                {`${t("throwingCoins")} ${currentLine + 1} ${t("line")}...`}
              </p>
            </div>
          )}
        </div>
      </div>

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
        {Object.keys(interpretation).length > 0 && (
          <button onClick={reset} className="secondary-button">
            <RotateCcw size={18} />
            <span>{t("newDivination")}</span>
          </button>
        )}
      </div>
    </div>
  );
};

export default LiuyaoDivination;
