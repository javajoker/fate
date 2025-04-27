import React from "react";
import "./LifeChart.css";

const FiveRadar = ({ elements }) => {
  return (
    <div className="radar-chart">
      <svg viewBox="-40 -10 280 220">
        {/* Background polygons */}
        <polygon
          points={generateRadarPoints({
            fire: 100,
            metal: 100,
            water: 100,
            earth: 100,
            wood: 100,
          })}
          fill="rgba(103, 58, 183, 0.1)"
        />
        <polygon
          points={generateRadarPoints({
            fire: 70,
            metal: 70,
            water: 70,
            earth: 70,
            wood: 70,
          })}
          fill="rgba(103, 58, 183, 0.05)"
        />
        <polygon
          points={generateRadarPoints({
            fire: 40,
            metal: 40,
            water: 40,
            earth: 40,
            wood: 40,
          })}
          fill="rgba(152, 18, 18, 0.5)"
        />

        {/* Element data polygon */}
        <polygon
          points={generateRadarPoints(elements.yang)}
          fill="rgba(103, 58, 183, 0.7)"
          stroke="var(--primary-color)"
          strokeWidth="2"
        />

        <polygon
          points={generateRadarPoints(elements.yin)}
          fill="rgba(58, 183, 177, 0.7)"
          stroke="var(--info-color)"
          strokeWidth="2"
        />

        {/* Axes */}
        <line
          x1="100"
          y1="10"
          x2="100"
          y2="100"
          stroke="#ccc"
          strokeWidth="1"
        />
        <line
          x1="14.4"
          y1="72.2"
          x2="100"
          y2="100"
          stroke="#ccc"
          strokeWidth="1"
        />
        <line
          x1="47.1"
          y1="172.8"
          x2="100"
          y2="100"
          stroke="#ccc"
          strokeWidth="1"
        />
        <line
          x1="100"
          y1="100"
          x2="152.9"
          y2="172.8"
          stroke="#ccc"
          strokeWidth="1"
        />
        <line
          x1="100"
          y1="100"
          x2="185.6"
          y2="72.2"
          stroke="#ccc"
          strokeWidth="1"
        />

        {/* Element labels */}
        <text
          x="100"
          y="5"
          textAnchor="middle"
          fill="var(--primary-color)"
          fontWeight="bold"
        >
          Fire
        </text>
        <text
          x="195"
          y="73"
          textAnchor="start"
          fill="var(--accent-color)"
          fontWeight="bold"
        >
          Metal
        </text>
        <text
          x="170"
          y="195"
          textAnchor="middle"
          fill="var(--info-color)"
          fontWeight="bold"
        >
          Water
        </text>
        <text
          x="30"
          y="195"
          textAnchor="middle"
          fill="var(--success-color)"
          fontWeight="bold"
        >
          Earth
        </text>
        <text
          x="5"
          y="73"
          textAnchor="end"
          fill="var(--warning-color)"
          fontWeight="bold"
        >
          Wood
        </text>

        {/* Element values */}
        <text x="100" y="25" textAnchor="middle" fill="#333">
          {elements.yang.fire}({elements.yin.fire})%
        </text>
        <text x="165" y="82" textAnchor="middle" fill="#333">
          {elements.yang.metal}({elements.yin.metal})%
        </text>
        <text x="140" y="170" textAnchor="middle" fill="#333">
          {elements.yang.water}({elements.yin.water})%
        </text>
        <text x="60" y="170" textAnchor="middle" fill="#333">
          {elements.yang.earth}({elements.yin.earth})%
        </text>
        <text x="35" y="82" textAnchor="middle" fill="#333">
          {elements.yang.wood}({elements.yin.wood})%
        </text>
      </svg>
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
  const fireY = centerY - (radius * elements.fire) / 100;
  const fireX = centerX;

  const metalAngle = (Math.PI * 2) / 5;
  const metalX =
    centerX + ((radius * elements.metal) / 100) * Math.sin(metalAngle);
  const metalY =
    centerY - ((radius * elements.metal) / 100) * Math.cos(metalAngle);

  const waterAngle = (Math.PI * 1) / 5;
  const waterX =
    centerX + ((radius * elements.water) / 100) * Math.sin(waterAngle);
  const waterY =
    centerY + ((radius * elements.water) / 100) * Math.cos(waterAngle);

  const earthAngle = (Math.PI * -1) / 5;
  const earthX =
    centerX + ((radius * elements.earth) / 100) * Math.sin(earthAngle);
  const earthY =
    centerY + ((radius * elements.earth) / 100) * Math.cos(earthAngle);

  const woodAngle = (Math.PI * -2) / 5;
  const woodX =
    centerX + ((radius * elements.wood) / 100) * Math.sin(woodAngle);
  const woodY =
    centerY - ((radius * elements.wood) / 100) * Math.cos(woodAngle);

  return `${fireX},${fireY} ${metalX},${metalY} ${waterX},${waterY} ${earthX},${earthY} ${woodX},${woodY}`;
};

export default FiveRadar;
