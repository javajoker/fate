import React, { useState, useEffect } from "react";
import "./LifeChart.css";
import { useTranslation } from "react-i18next";
import { fate } from "../api/api";
import { lookupViaCity } from "city-timezones";
import moment from "moment-timezone";
import FiveRadar from "./FiveRadar";
import {
  Radar,
  PolarAngleAxis,
  PolarGrid,
  PolarRadiusAxis,
  RadarChart,
  ResponsiveContainer,
  Tooltip,
} from "recharts";

const LifeChart = ({ userData }) => {
  const [lifeData, setLifeData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const { t } = useTranslation();

  // Calculate life phases and element strengths based on user's birth data
  useEffect(() => {
    setIsLoading(true);

    console.log(userData);
    let birthTime = `${userData.birthDate}T${userData.birthTime || "00:00:00"}`;
    const lookup = lookupViaCity(userData.birthPlace);
    console.log(lookup);
    if (lookup?.length > 0) {
      birthTime = moment
        .tz(birthTime, lookup[0].timezone)
        .tz("Asia/Shanghai")
        .format("YYYY-MM-DD HH:mm:ss");
    }
    const date = new Date(birthTime);
    fate({
      gender: userData.gender || 0,
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate(),
      hour: date.getHours(),
      zone: "CCT",
    }).then((res) => {
      console.log(res);

      // Generate deterministic "random" data based on user input
      const userHash = hashCode(
        `${userData.name}-${userData.birthDate}-${userData.birthPlace}`
      );

      // Calculate life phases (ages 0-80 in 10-year segments)
      const lifePhases = generateLifePhases(userHash);

      // Calculate element strengths
      const elements = generateElementStrengths(userHash);

      setLifeData({
        phases: lifePhases,
        elements: elements,
        radar: toRadarData(elements),
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

  // Generate element strengths based on the hash
  const generateElementStrengths = (hash) => {
    return {
      yin: {
        fire: Math.min(100, Math.max(20, (hash % 80) + 20)),
        water: Math.min(100, Math.max(20, ((hash >> 4) % 80) + 20)),
        earth: Math.min(100, Math.max(20, ((hash >> 8) % 80) + 20)),
        wood: Math.min(100, Math.max(20, ((hash >> 12) % 80) + 20)),
        metal: Math.min(100, Math.max(20, ((hash >> 16) % 80) + 20)),
      },
      yang: {
        fire: Math.min(100, Math.max(20, ((hash >> 4) % 80) + 20)),
        water: Math.min(100, Math.max(20, (hash % 80) + 20)),
        earth: Math.min(100, Math.max(20, ((hash >> 12) % 80) + 20)),
        wood: Math.min(100, Math.max(20, ((hash >> 8) % 80) + 20)),
        metal: Math.min(100, Math.max(20, ((hash >> 16) % 80) + 20)),
      },
    };
  };

  const toRadarData = (elements) => {
    return [
      {
        subject: "🪵🌱",
        yin: elements?.yin?.wood ?? 0,
        yang: elements?.yang?.wood ?? 0,
        fullMark: 100,
      },
      {
        subject: "🌋🔥",
        yin: elements?.yin?.fire ?? 0,
        yang: elements?.yang?.fire ?? 0,
        fullMark: 100,
      },
      {
        subject: "⛰️🛤",
        yin: elements?.yin?.earth ?? 0,
        yang: elements?.yang?.earth ?? 0,
        fullMark: 100,
      },
      {
        subject: "🗡️🧈",
        yin: elements?.yin?.metal ?? 0,
        yang: elements?.yang?.metal ?? 0,
        fullMark: 100,
      },
      {
        subject: "🌊💦",
        yin: elements?.yin?.water ?? 0,
        yang: elements?.yang?.water ?? 0,
        fullMark: 100,
      },
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
      </div>

      <div className="elements-chart">
        <h3>{t("life-5")}</h3>
        {/* <FiveRadar elements={lifeData.elements} /> */}
        <div className="radar-chart" style={{ height: "min(400px,70vw)" }}>
          <ResponsiveContainer width="100%" height="100%">
            <RadarChart
              cx="50%"
              cy="50%"
              outerRadius="80%"
              data={lifeData.radar}
            >
              <PolarGrid />
              <PolarAngleAxis dataKey="subject" />
              <PolarRadiusAxis angle={54} domain={[0, "dataMax"]} />
              <Tooltip />
              <Radar
                name="🌞"
                dataKey="yang"
                stroke="#8884d8"
                fill="#8884d8"
                fillOpacity={0.6}
              />
              <Radar
                name="🌜"
                dataKey="yin"
                stroke="#82ca9d"
                fill="#82ca9d"
                fillOpacity={0.6}
              />
            </RadarChart>
          </ResponsiveContainer>
        </div>

        <div className="elements-interpretation">
          <h4>{t("life-5explain")}</h4>
          <p>{getElementInterpretation(lifeData.elements)}</p>
        </div>
      </div>

      <div className="phase-details">
        <h3>{t("life-phase")}</h3>
        {getCurrentPhaseDetails(lifeData.phases)}
      </div>

      <div className="life-chart">
        <h3>{t("life-phases")}</h3>
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

// Helper function to interpret element balance
const getElementInterpretation = (elements) => {
  // Find dominant and weakest elements
  let dominant = "fire";
  let weakest = "fire";

  Object.entries(elements).forEach(([element, value]) => {
    if (value > elements[dominant]) dominant = element;
    if (value < elements[weakest]) weakest = element;
  });

  // Calculate overall balance (standard deviation as a simple measure)
  const values = Object.values(elements);
  const avg = values.reduce((sum, val) => sum + val, 0) / values.length;
  const variance =
    values.reduce((sum, val) => sum + Math.pow(val - avg, 2), 0) /
    values.length;
  const balance = Math.sqrt(variance);

  // Interpretation based on dominant element and balance
  const interpretations = {
    fire: "Your chart shows strong Fire energy, indicating passion, creativity, and leadership potential. You likely approach life with enthusiasm and have a natural ability to inspire others.",
    water:
      "Water is your dominant element, suggesting emotional depth, intuition, and adaptability. You likely have a natural understanding of others' feelings and flow easily with life's changes.",
    earth:
      "Your Earth element predominance indicates practicality, reliability, and groundedness. You excel at creating stable foundations and bringing ideas into tangible reality.",
    wood: "With Wood as your dominant element, you possess strong intellectual qualities, communication skills, and adaptability. You naturally connect ideas and people, seeing patterns others miss.",
    metal:
      "Your chart shows Metal as your strongest element, suggesting precision, discipline, and refinement. You excel at discernment and have a natural ability to distill wisdom from experience.",
  };

  const balanceComment =
    balance < 10
      ? "Your elements show remarkable harmony, suggesting versatility and balanced capabilities across life domains."
      : "The variation between your elements indicates specialized strengths and potential growth areas.";

  const weaknessComment = `Your ${weakest} element presents an opportunity for development. Consciously strengthening this aspect can bring greater wholeness to your life journey.`;

  return `${interpretations[dominant]} ${balanceComment} ${weaknessComment}`;
};

export default LifeChart;
