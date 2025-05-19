import React, { useEffect, useState } from "react";
import "./CurrentPhase.css";
import { useTranslation } from "react-i18next";

const CurrentPhase = ({ elements, birth }) => {
  const [currentPhase, setCurrentPhase] = useState(null);
  const [nextPhase, setNextPhase] = useState(null);

  const { t } = useTranslation();

  useEffect(() => {
    if (!elements) return [];
    const { self, y10, y } = elements;
    const cid = Math.floor((new Date().getFullYear() - (+birth + y)) / 10),
      current = y10.slice(cid, cid + 1),
      next = y10.slice(cid + 1, cid + 2);
    if (current.length > 0)
      setCurrentPhase({
        intensity: Math.round((current[0].v * 100) / 10 / self.v),
        range: `${y + cid * 10}-${y + cid * 10 + 9}`,
        e: current[0].e,
      });
    if (next.length > 0)
      setNextPhase({
        intensity: Math.round((next[0].v * 100) / 10 / self.v),
        range: `${y + 10 + cid * 10}-${y + 10 + cid * 10 + 9}`,
        e: next[0].e,
      });
  }, [elements, birth]);

  const phaseDesc = (id) => {
    if (!elements) return "";
    return t(
      `life-godphase-10-${((id >> 1) - (elements.self.e >> 1) + 5) % 5}-${
        elements.self.e % 2 === id % 2 ? 0 : 1
      }`
    );
  };

  if (!currentPhase && !nextPhase) {
    // If age is beyond our phases or something went wrong
    return <p>Unable to determine your current life phase.</p>;
  }

  return (
    <div className="current-phase">
      {currentPhase && (
        <div className="phase-card">
          <h4>Ages {currentPhase.range} (Current)</h4>
          <div className="phase-intensity">
            <div
              className="intensity-bar"
              style={{ width: `${currentPhase.intensity}%` }}
            ></div>
            <span>{currentPhase.intensity}%</span>
          </div>
          <p>{phaseDesc(currentPhase.e)}</p>
        </div>
      )}

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
          <p>{phaseDesc(nextPhase.e)}</p>
        </div>
      )}
    </div>
  );
};

export default CurrentPhase;
