import React, { useState, useEffect } from "react";
import "./LifeChart.css";
import { useTranslation } from "react-i18next";
import { fate, getPillarInfo } from "../api/api";
import FiveRadar from "./FiveRadar";
import Life20y from "./Life20y";
import LifePhases from "./LifePhases";
import CurrentPhase from "./CurrentPhase";

const LifeChart = ({ userData }) => {
  const [lifeData, setLifeData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const { t } = useTranslation();

  // Calculate life phases and element strengths based on user's birth data
  useEffect(() => {
    setIsLoading(true);

    const pillarInfo = getPillarInfo(userData);
    fate(pillarInfo).then((res) => {
      setLifeData({
        elements: res,
      });
      setIsLoading(false);
    });
  }, [userData]);

  // Helper function to interpret element balance
  const getElementInterpretation = (elements) => {
    const self = elements.self;
    const es = [...elements.es];
    es[self.e] -= self.v;
    const max = { v: 0 };
    es.forEach((v, i) => {
      if ((v ?? 0) > max.v) {
        max.v = v;
        max.e = i;
      }
    });
    const personality10 = t(
      `life-personality-10-${((max.e >> 1) - (self.e >> 1) + 5) % 5}-${
        self.e % 2 === max.e % 2 ? 0 : 1
      }`
    );

    const values = [
      (es[0] ?? 0) + (es[1] ?? 0),
      (es[2] ?? 0) + (es[3] ?? 0),
      (es[4] ?? 0) + (es[5] ?? 0),
      (es[6] ?? 0) + (es[7] ?? 0),
      (es[8] ?? 0) + (es[9] ?? 0),
    ];

    // Calculate overall balance (standard deviation as a simple measure)
    const avg =
      values.reduce((sum, val) => sum + (val ?? 0), 0) / values.length;
    const variance =
      values.reduce((sum, val) => sum + Math.pow((val ?? 0) - avg, 2), 0) /
      values.length;
    const balance = Math.sqrt(variance);
    const s = Math.round((elements.self.v - avg) / balance);
    const personality5 = t(
      `life-personality-5-${self.e >> 1}-${s <= -1 ? -1 : s >= 1 ? 1 : 0}`
    );

    const sigma = [],
      assist = [],
      sigMax = { v: -1 };
    values.forEach((v, i) => {
      if (!v && i !== self.e >> 1) assist.push(i);
      const sig = Math.round((v - avg) / balance);
      sigma.push(sig);
      if (Math.abs(sig) > sigMax.v) {
        // only get one
        sigMax.v = Math.abs(sig);
        sigMax.e = i;
      }
    });
    if (sigma[sigMax.e] < 0) assist.push((sigMax.e - 1 + 5) % 5);
    else assist.push((sigMax.e + 1) % 5);
    if (s <= -1) assist.push(((self.e >> 1) - 1 + 5) % 5);
    else if (s >= 1) assist.push(((self.e >> 1) + 1) % 5);
    const suggestion = [];
    [...new Set(assist)].forEach((v) =>
      suggestion.push(t(`life-suggestion-${v}`))
    );

    return [
      personality10,
      personality5,
      t("life-suggestion", { suggestion: suggestion.join(" ") }),
    ];
  };

  if (isLoading) {
    return (
      <div className="life-loading">
        <div className="spinner"></div>
        <p>{t("life-loading")}</p>
      </div>
    );
  }

  return (
    <div className="life-container slide-in-right">
      <div className="life-header">
        <h2>{t("life-header")}</h2>
        <p>{t("life-desc")}</p>
        {userData.latitude && userData.longitude && (
          <div className="coordinates-info">
            <span>
              {t("life-place")}: {userData.latitude}, {userData.longitude}
            </span>
            <div className="coordinate-badge">{t("life-geo")}</div>
          </div>
        )}
      </div>

      <div className="elements-chart">
        <h3>{t("life-5")}</h3>
        <FiveRadar elements={lifeData.elements} />

        <div className="elements-interpretation">
          {/* <h4>{t("life-5explain")}</h4> */}
          {getElementInterpretation(lifeData.elements).map((v, index) => {
            return <p key={index}>{v}</p>;
          })}
        </div>
      </div>

      <div className="phase-details">
        <h3>{t("life-phase")}</h3>
        <Life20y elements={lifeData.elements} />
        <CurrentPhase
          elements={lifeData.elements}
          birth={new Date(userData.birthDate).getFullYear()}
        />
      </div>

      <div className="life-chart">
        <h3>{t("life-phases")}</h3>
        <LifePhases elements={lifeData.elements} />

        {/* <div className="scale">
          <div>Prosperity</div>
          <div>Balance</div>
          <div>Challenge</div>
        </div> */}
      </div>
    </div>
  );
};

export default LifeChart;
