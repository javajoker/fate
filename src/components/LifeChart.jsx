import React, { useState, useEffect } from "react";
import "./LifeChart.css";
import { useTranslation } from "react-i18next";
import { fate, getPillarInfo } from "../api/api";
import FiveRadar from "./FiveRadar";
import LifePhases from "./LifePhases";


const LifeChart = ({ userData }) => {
  const [lifeData, setLifeData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const { t } = useTranslation();

  // Calculate life phases and element strengths based on user's birth data
  useEffect(() => {
    setIsLoading(true);

    const pillarInfo = getPillarInfo(userData);
    fate(pillarInfo).then((res) => {
      console.log(res);
      // Generate deterministic "random" data based on user input
      const userHash = hashCode(
        `${userData.name}-${userData.birthDate}-${userData.birthPlace}`
      );

      // Calculate life phases (ages 0-80 in 10-year segments)
      const lifePhases = generateLifePhases(userHash);

      // Calculate element strengths
      const elements = res;

      setLifeData({
        phases: lifePhases,
        elements: elements,
      });
      setIsLoading(false);
    });
  }, [userData]);

  // Helper function to generate a hash code from a string
  const hashCode = (str) => {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      const char = str.charCodeAt(i);
      hash = (hash << 5) - hash + char;
      hash = hash & hash; // Convert to 32bit integer
    }
    return Math.abs(hash);
  };

  // Generate life phases based on the hash
  const generateLifePhases = (hash) => {
    const phases = [];
    const baseValue = (hash % 30) + 50; // Base value between 50-80

    for (let i = 0; i < 8; i++) {
      const ageStart = i * 10;
      const ageEnd = ageStart + 9;

      // Fluctuate around the base value to create a natural curve
      const modifier = ((hash >> (i * 3)) % 50) - 25;
      const intensity = Math.min(100, Math.max(0, baseValue + modifier));

      phases.push({
        range: `${ageStart}-${ageEnd}`,
        intensity: intensity,
        description: getPhaseDescription(intensity, i),
      });
    }

    return phases;
  };

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
      `life-personality-10-${((max.e >> 1) - (self.e >> 1) + 5) % 5}-${self.e % 2 === max.e % 2 ? 0 : 1
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

  // Get description for a life phase based on its intensity
  const getPhaseDescription = (intensity, phaseIndex) => {
    const phaseDescriptions = [
      // Childhood (0-9)
      [
        "A challenging start to life requiring resilience and adaptation.",
        "A balanced childhood with both nurturing support and growth challenges.",
        "A fortunate childhood filled with opportunities and strong foundations.",
      ],
      // Youth (10-19)
      [
        "A period of significant challenges, identity formation, and necessary growth.",
        "A time of self-discovery with balanced social and personal development.",
        "A flourishing youth marked by achievements and strong relationships.",
      ],
      // Early Adulthood (20-29)
      [
        "A demanding phase requiring persistence through career and relationship tests.",
        "A steady development period balancing career growth and personal life.",
        "A prosperous time of significant opportunities and meaningful connections.",
      ],
      // Thirties (30-39)
      [
        "A transformative decade requiring difficult choices and realignment.",
        "A period of consolidation and establishing deeper foundations.",
        "A rewarding phase of abundance and expanded influence.",
      ],
      // Forties (40-49)
      [
        "A challenging midlife period calling for reevaluation and courage.",
        "A balanced time of maturity and measured progress.",
        "A peak phase of success and reaping rewards from earlier efforts.",
      ],
      // Fifties (50-59)
      [
        "A time of necessary transitions and overcoming unexpected obstacles.",
        "A period of stability with gradual shifts toward new priorities.",
        "A fulfilling decade of wisdom, influence, and enjoying achievements.",
      ],
      // Sixties (60-69)
      [
        "A phase requiring adaptation to changing health and life circumstances.",
        "A balanced period of selective engagement and meaningful activities.",
        "A gratifying time of sharing wisdom and enjoying life's pleasures.",
      ],
      // Seventies & Beyond (70-79)
      [
        "A time of facing limitations while finding new forms of meaning.",
        "A period of graceful adaptation and cherishing relationships.",
        "A blessed phase of continued vitality and leaving a lasting legacy.",
      ],
    ];

    // Select low, medium, or high description based on intensity
    let descriptionIndex;
    if (intensity < 40) descriptionIndex = 0;
    else if (intensity < 70) descriptionIndex = 1;
    else descriptionIndex = 2;

    return phaseDescriptions[phaseIndex][descriptionIndex];
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
          {getElementInterpretation(lifeData.elements).map((v) => {
            return <p>{v}</p>;
          })}
        </div>
      </div>

      <div className="phase-details">
        <h3>{t("life-phase")}</h3>
        {getCurrentPhaseDetails(lifeData.phases)}
      </div>

      <div className="life-chart">
        <h3>{t("life-phases")}</h3>
        <LifePhases elements={lifeData.elements} />
        <div className="phases-container">
          {lifeData.phases.map((phase, index) => (
            <div className="phase-item" key={index}>
              <div className="phase-bar-container">
                <div
                  className="phase-bar"
                  style={{ height: `${phase.intensity}%` }}
                ></div>
              </div>
              <div className="phase-label">{phase.range}</div>
            </div>
          ))}
        </div>

        <div className="scale">
          <div>Prosperity</div>
          <div>Balance</div>
          <div>Challenge</div>
        </div>
      </div>
    </div>
  );
};

// Helper function to get current or next phase details based on user's age
const getCurrentPhaseDetails = (phases) => {
  // Calculate user's age
  // In a real app, this would use the userData birthDate
  const currentAge = 35; // Example age

  let currentPhase = null;
  let nextPhase = null;

  for (let i = 0; i < phases.length; i++) {
    const range = phases[i].range;
    const [start, end] = range.split("-").map((num) => parseInt(num));

    if (currentAge >= start && currentAge <= end) {
      currentPhase = phases[i];
      nextPhase = i < phases.length - 1 ? phases[i + 1] : null;
      break;
    }
  }

  if (!currentPhase) {
    // If age is beyond our phases or something went wrong
    return <p>Unable to determine your current life phase.</p>;
  }

  return (
    <div className="current-phase">
      <div className="phase-card">
        <h4>Ages {currentPhase.range} (Current)</h4>
        <div className="phase-intensity">
          <div
            className="intensity-bar"
            style={{ width: `${currentPhase.intensity}%` }}
          ></div>
          <span>{currentPhase.intensity}%</span>
        </div>
        <p>{currentPhase.description}</p>
      </div>

      {nextPhase && (
        <div className="phase-card next">
          <h4>Ages {nextPhase.range} (Upcoming)</h4>
          <div className="phase-intensity">
            <div
              className="intensity-bar"
              style={{ width: `${nextPhase.intensity}%` }}
            ></div>
            <span>{nextPhase.intensity}%</span>
          </div>
          <p>{nextPhase.description}</p>
        </div>
      )}
    </div>
  );
};

export default LifeChart;
