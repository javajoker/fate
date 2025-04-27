import React, { useState, useEffect } from 'react';
import './LifeChart.css';
import { fate } from '../api/api';
import { lookupViaCity } from 'city-timezones';
import moment from 'moment-timezone';

const LifeChart = ({ userData }) => {
  const [lifeData, setLifeData] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // Calculate life phases and element strengths based on user's birth data
  useEffect(() => {
    setIsLoading(true);

    let birthTime = `${userData.birthDate}T${userData.birthTime || '00:00:00'}Z-3`;
    const lookup = lookupViaCity('San Francisco')//userData.birthPlace);
    console.log(lookup);
    if (lookup) {
      const a = moment(birthTime).tz(lookup[0].timezone)
      console.log(a.format('YYYY-MM-DD HH:mm:ss'))
      const b = a.tz('Asia/Shanghai').format('YYYY-MM-DD HH:mm:ss')
      console.log(b)
      birthTime = moment(birthTime).tz(lookup[0].timezone).tz('Asia/Shanghai').format('YYYY-MM-DD HH:mm:ss')
    }
    const date = new Date(birthTime);
    fate({
      gender: userData.gender || 0,
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate(),
      hour: date.getHours(),
      zone: 'CCT'
    }).then((res) => {
      console.log(res)

      // Generate deterministic "random" data based on user input
      const userHash = hashCode(`${userData.name}-${userData.birthDate}-${userData.birthPlace}`);

      // Calculate life phases (ages 0-80 in 10-year segments)
      const lifePhases = generateLifePhases(userHash);

      // Calculate element strengths
      const elements = generateElementStrengths(userHash);

      setLifeData({
        phases: lifePhases,
        elements: elements
      });

      setIsLoading(false);
    });

  }, [userData]);

  // Helper function to generate a hash code from a string
  const hashCode = (str) => {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      const char = str.charCodeAt(i);
      hash = ((hash << 5) - hash) + char;
      hash = hash & hash; // Convert to 32bit integer
    }
    return Math.abs(hash);
  };

  // Generate life phases based on the hash
  const generateLifePhases = (hash) => {
    const phases = [];
    const baseValue = hash % 30 + 50; // Base value between 50-80

    for (let i = 0; i < 8; i++) {
      const ageStart = i * 10;
      const ageEnd = ageStart + 9;

      // Fluctuate around the base value to create a natural curve
      const modifier = ((hash >> (i * 3)) % 50) - 25;
      const intensity = Math.min(100, Math.max(0, baseValue + modifier));

      phases.push({
        range: `${ageStart}-${ageEnd}`,
        intensity: intensity,
        description: getPhaseDescription(intensity, i)
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
        metal: Math.min(100, Math.max(20, ((hash >> 16) % 80) + 20))
      }, yang: {
        fire: Math.min(100, Math.max(20, ((hash >> 4) % 80) + 20)),
        water: Math.min(100, Math.max(20, (hash % 80) + 20)),
        earth: Math.min(100, Math.max(20, ((hash >> 12) % 80) + 20)),
        wood: Math.min(100, Math.max(20, ((hash >> 8) % 80) + 20)),
        metal: Math.min(100, Math.max(20, ((hash >> 16) % 80) + 20))
      }
    };
  };

  // Get description for a life phase based on its intensity
  const getPhaseDescription = (intensity, phaseIndex) => {
    const phaseDescriptions = [
      // Childhood (0-9)
      [
        "A challenging start to life requiring resilience and adaptation.",
        "A balanced childhood with both nurturing support and growth challenges.",
        "A fortunate childhood filled with opportunities and strong foundations."
      ],
      // Youth (10-19)
      [
        "A period of significant challenges, identity formation, and necessary growth.",
        "A time of self-discovery with balanced social and personal development.",
        "A flourishing youth marked by achievements and strong relationships."
      ],
      // Early Adulthood (20-29)
      [
        "A demanding phase requiring persistence through career and relationship tests.",
        "A steady development period balancing career growth and personal life.",
        "A prosperous time of significant opportunities and meaningful connections."
      ],
      // Thirties (30-39)
      [
        "A transformative decade requiring difficult choices and realignment.",
        "A period of consolidation and establishing deeper foundations.",
        "A rewarding phase of abundance and expanded influence."
      ],
      // Forties (40-49)
      [
        "A challenging midlife period calling for reevaluation and courage.",
        "A balanced time of maturity and measured progress.",
        "A peak phase of success and reaping rewards from earlier efforts."
      ],
      // Fifties (50-59)
      [
        "A time of necessary transitions and overcoming unexpected obstacles.",
        "A period of stability with gradual shifts toward new priorities.",
        "A fulfilling decade of wisdom, influence, and enjoying achievements."
      ],
      // Sixties (60-69)
      [
        "A phase requiring adaptation to changing health and life circumstances.",
        "A balanced period of selective engagement and meaningful activities.",
        "A gratifying time of sharing wisdom and enjoying life's pleasures."
      ],
      // Seventies & Beyond (70-79)
      [
        "A time of facing limitations while finding new forms of meaning.",
        "A period of graceful adaptation and cherishing relationships.",
        "A blessed phase of continued vitality and leaving a lasting legacy."
      ]
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
        <p>Calculating your life patterns...</p>
      </div>
    );
  }

  return (
    <div className="life-container slide-in-right">
      <div className="life-header">
        <h2>Your Life Journey</h2>
        <p>Based on cosmic influences at your birth</p>
      </div>

      <div className="phase-details">
        <h3>Current/Next Life Phase</h3>
        {getCurrentPhaseDetails(lifeData.phases)}
      </div>

      <div className="elements-chart">
        <h3>Your Five Elements</h3>
        <div className="radar-chart">
          <svg viewBox="-40 -10 280 220">
            {/* Background polygons */}
            <polygon points={generateRadarPoints({ fire: 100, metal: 100, water: 100, earth: 100, wood: 100 })} fill="rgba(103, 58, 183, 0.1)" />
            <polygon points={generateRadarPoints({ fire: 70, metal: 70, water: 70, earth: 70, wood: 70 })} fill="rgba(103, 58, 183, 0.05)" />
            <polygon points={generateRadarPoints({ fire: 40, metal: 40, water: 40, earth: 40, wood: 40 })} fill="rgba(152, 18, 18, 0.5)" />

            {/* Element data polygon */}
            <polygon
              points={generateRadarPoints(lifeData.elements.yang)}
              fill="rgba(103, 58, 183, 0.7)"
              stroke="var(--primary-color)"
              strokeWidth="2"
            />

            <polygon
              points={generateRadarPoints(lifeData.elements.yin)}
              fill="rgba(58, 183, 177, 0.7)"
              stroke="var(--info-color)"
              strokeWidth="2"
            />

            {/* Axes */}
            <line x1="100" y1="10" x2="100" y2="100" stroke="#ccc" strokeWidth="1" />
            <line x1="14.4" y1="72.2" x2="100" y2="100" stroke="#ccc" strokeWidth="1" />
            <line x1="47.1" y1="172.8" x2="100" y2="100" stroke="#ccc" strokeWidth="1" />
            <line x1="100" y1="100" x2="152.9" y2="172.8" stroke="#ccc" strokeWidth="1" />
            <line x1="100" y1="100" x2="185.6" y2="72.2" stroke="#ccc" strokeWidth="1" />

            {/* Element labels */}
            <text x="100" y="5" textAnchor="middle" fill="var(--primary-color)" fontWeight="bold">Fire</text>
            <text x="195" y="73" textAnchor="start" fill="var(--accent-color)" fontWeight="bold">Metal</text>
            <text x="170" y="195" textAnchor="middle" fill="var(--info-color)" fontWeight="bold">Water</text>
            <text x="30" y="195" textAnchor="middle" fill="var(--success-color)" fontWeight="bold">Earth</text>
            <text x="5" y="73" textAnchor="end" fill="var(--warning-color)" fontWeight="bold">Wood</text>

            {/* Element values */}
            <text x="100" y="25" textAnchor="middle" fill="#333">{lifeData.elements.yang.fire}({lifeData.elements.yin.fire})%</text>
            <text x="165" y="82" textAnchor="middle" fill="#333">{lifeData.elements.yang.metal}({lifeData.elements.yin.metal})%</text>
            <text x="140" y="170" textAnchor="middle" fill="#333">{lifeData.elements.yang.water}({lifeData.elements.yin.water})%</text>
            <text x="60" y="170" textAnchor="middle" fill="#333">{lifeData.elements.yang.earth}({lifeData.elements.yin.earth})%</text>
            <text x="35" y="82" textAnchor="middle" fill="#333">{lifeData.elements.yang.wood}({lifeData.elements.yin.wood})%</text>
          </svg>
        </div>

        <div className="elements-interpretation">
          <h4>Element Balance Interpretation</h4>
          <p>{getElementInterpretation(lifeData.elements)}</p>
        </div>

        <div className="life-chart">
          <h3>Life Phases</h3>
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
    const [start, end] = range.split('-').map(num => parseInt(num));

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
          <div className="intensity-bar" style={{ width: `${currentPhase.intensity}%` }}></div>
          <span>{currentPhase.intensity}%</span>
        </div>
        <p>{currentPhase.description}</p>
      </div>

      {nextPhase && (
        <div className="phase-card next">
          <h4>Ages {nextPhase.range} (Upcoming)</h4>
          <div className="phase-intensity">
            <div className="intensity-bar" style={{ width: `${nextPhase.intensity}%` }}></div>
            <span>{nextPhase.intensity}%</span>
          </div>
          <p>{nextPhase.description}</p>
        </div>
      )}
    </div>
  );
};

// Helper function to generate radar chart points
const generateRadarPoints = (elements) => {
  // Calculate positions on pentagon
  const centerX = 100;
  const centerY = 100;
  const radius = 90;

  // Map element values to positions (0-100% maps to center-full radius)
  const fireY = centerY - (radius * elements.fire / 100);
  const fireX = centerX;

  const metalAngle = Math.PI * 2 / 5;
  const metalX = centerX + (radius * elements.metal / 100) * Math.sin(metalAngle);
  const metalY = centerY - (radius * elements.metal / 100) * Math.cos(metalAngle);

  const waterAngle = Math.PI * 1 / 5;
  const waterX = centerX + (radius * elements.water / 100) * Math.sin(waterAngle);
  const waterY = centerY + (radius * elements.water / 100) * Math.cos(waterAngle);

  const earthAngle = Math.PI * -1 / 5;
  const earthX = centerX + (radius * elements.earth / 100) * Math.sin(earthAngle);
  const earthY = centerY + (radius * elements.earth / 100) * Math.cos(earthAngle);

  const woodAngle = Math.PI * -2 / 5;
  const woodX = centerX + (radius * elements.wood / 100) * Math.sin(woodAngle);
  const woodY = centerY - (radius * elements.wood / 100) * Math.cos(woodAngle);

  return `${fireX},${fireY} ${metalX},${metalY} ${waterX},${waterY} ${earthX},${earthY} ${woodX},${woodY}`;
};

// Helper function to interpret element balance
const getElementInterpretation = (elements) => {
  // Find dominant and weakest elements
  let dominant = 'fire';
  let weakest = 'fire';

  Object.entries(elements).forEach(([element, value]) => {
    if (value > elements[dominant]) dominant = element;
    if (value < elements[weakest]) weakest = element;
  });

  // Calculate overall balance (standard deviation as a simple measure)
  const values = Object.values(elements);
  const avg = values.reduce((sum, val) => sum + val, 0) / values.length;
  const variance = values.reduce((sum, val) => sum + Math.pow(val - avg, 2), 0) / values.length;
  const balance = Math.sqrt(variance);

  // Interpretation based on dominant element and balance
  const interpretations = {
    fire: "Your chart shows strong Fire energy, indicating passion, creativity, and leadership potential. You likely approach life with enthusiasm and have a natural ability to inspire others.",
    water: "Water is your dominant element, suggesting emotional depth, intuition, and adaptability. You likely have a natural understanding of others' feelings and flow easily with life's changes.",
    earth: "Your Earth element predominance indicates practicality, reliability, and groundedness. You excel at creating stable foundations and bringing ideas into tangible reality.",
    wood: "With Wood as your dominant element, you possess strong intellectual qualities, communication skills, and adaptability. You naturally connect ideas and people, seeing patterns others miss.",
    metal: "Your chart shows Metal as your strongest element, suggesting precision, discipline, and refinement. You excel at discernment and have a natural ability to distill wisdom from experience."
  };

  const balanceComment = balance < 10
    ? "Your elements show remarkable harmony, suggesting versatility and balanced capabilities across life domains."
    : "The variation between your elements indicates specialized strengths and potential growth areas.";

  const weaknessComment = `Your ${weakest} element presents an opportunity for development. Consciously strengthening this aspect can bring greater wholeness to your life journey.`;

  return `${interpretations[dominant]} ${balanceComment} ${weaknessComment}`;
};

export default LifeChart;
