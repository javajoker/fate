import React from "react";
import "./LifePhases.css";

const LifePhases = ({ elements }) => {
  const toPhaseData = () => {
    if (!elements) return [];
    const { self, y10 } = elements;
    let y = elements.y;
    const res = [];
    y10.forEach((v) => {
      res.push({
        intensity: Math.round((v.v * 100) / 10 / self.v),
        range: `${y}-${y + 9}`,
      });
      y += 10;
    });
    return res;
  };

  return (
    <div className="phases-container">
      {toPhaseData().map((phase, index) => (
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
  );
};

export default LifePhases;
