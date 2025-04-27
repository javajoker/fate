import React, { useState, useEffect } from "react";
import "./TodayFortune.css";
import { useTranslation } from "react-i18next";

const TodayFortune = ({ userData }) => {
  const [fortune, setFortune] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  const { t } = useTranslation();

  // Generate a deterministic but seemingly random fortune based on user data and current date
  useEffect(() => {
    setIsLoading(true);

    setTimeout(() => {
      const today = new Date();
      const dateString = `${today.getFullYear()}-${today.getMonth()}-${today.getDate()}`;
      const userString = `${userData.name}-${userData.birthDate}`;

      // Simple hash function to generate a deterministic "random" number
      const hash = (str) => {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
          const char = str.charCodeAt(i);
          hash = (hash << 5) - hash + char;
          hash = hash & hash; // Convert to 32bit integer
        }
        return Math.abs(hash);
      };

      const combinedHash = hash(dateString + userString);

      // Select fortune elements based on hash
      const moodIndex = combinedHash % moodPhrases.length;
      const luckIndex = (combinedHash >> 4) % luckPhrases.length;
      const adviceIndex = (combinedHash >> 8) % advicePhrases.length;
      const colorIndex = (combinedHash >> 12) % luckyColors.length;
      const numberIndex = (combinedHash >> 16) % 100;

      setFortune({
        mood: moodPhrases[moodIndex],
        luck: luckPhrases[luckIndex],
        advice: advicePhrases[adviceIndex],
        color: luckyColors[colorIndex],
        number: numberIndex,
        date: today.toLocaleDateString(),
      });

      setIsLoading(false);
    }, 1000); // Simulating API call
  }, [userData]);

  if (isLoading) {
    return (
      <div className="fortune-loading">
        <div className="spinner"></div>
        <p>{t("today-loading")}</p>
      </div>
    );
  }

  return (
    <div className="fortune-container slide-in-right">
      <div className="fortune-date">
        <h2>{t("today-header")}</h2>
        <p>{t("today-for", { date: fortune.date ?? "" })}</p>
      </div>

      <div className="fortune-card mood">
        <h3>{t('today-mood')}</h3>
        <p>{fortune.mood}</p>
      </div>

      <div className="fortune-card luck">
        <h3>{t('today-luck')}</h3>
        <p>{fortune.luck}</p>
      </div>

      <div className="fortune-card advice">
        <h3>{t('today-advice')}</h3>
        <p>{fortune.advice}</p>
      </div>

      {/* <div className="fortune-extras">
        <div className="lucky-item">
          <h4>Lucky Color</h4>
          <div
            className="color-circle"
            style={{ backgroundColor: fortune.color }}
          ></div>
          <span>{fortune.color}</span>
        </div>

        <div className="lucky-item">
          <h4>Lucky Number</h4>
          <div className="number-circle">{fortune.number}</div>
        </div>
      </div> */}
    </div>
  );
};

// Fortune data arrays
const moodPhrases = [
  "Today your energy is vibrant and uplifting. You'll find yourself motivated to tackle challenges head-on.",
  "Your emotional balance may be tested today. Practice mindfulness to maintain your center.",
  "Creativity flows through you today. Express yourself through art, music, or heartfelt conversation.",
  "Your intuition is heightened today. Pay attention to subtle feelings and inner guidance.",
  "A harmonious energy surrounds you. Relationships will feel particularly rewarding today.",
  "Your intellectual curiosity is strong today. It's an excellent time for learning and discovery.",
  "You may feel more introspective than usual. Honor this energy by making time for self-reflection.",
  "Dynamic energy fuels your actions today. Channel this power into your most important goals.",
  "You radiate confidence today. Others will be drawn to your positive presence and leadership.",
  "A peaceful energy envelops you. Today brings opportunities for healing and restoration.",
];

const luckPhrases = [
  "Unexpected opportunities may arise in your professional sphere. Stay alert to new possibilities.",
  "A chance encounter could lead to meaningful connections. Be open to meeting new people.",
  "Financial matters receive favorable cosmic energy today. Trust your instincts regarding resources.",
  "The stars align for travel or educational pursuits. Consider expanding your horizons.",
  "Fortune favors bold communication today. Express your truth with confidence.",
  "A stroke of luck may appear in an unlikely place. Look beyond the obvious.",
  "Collaborative efforts are especially favored today. Team projects will yield positive results.",
  "Your patience with a long-term project may soon be rewarded. Stay the course.",
  "A past investment of time or energy begins to show promising returns.",
  "Doors that seemed closed may suddenly open. Approach them with confident curiosity.",
];

const advicePhrases = [
  "Balance action with reflection today. Your most powerful insights come when you pause between efforts.",
  "Trust the timing of your life. What seems like a delay may be the universe preparing something better.",
  "Nurture connections with those who truly see you. Authentic relationships deserve your energy today.",
  "Release expectations about how things 'should' unfold. Embrace the wisdom of uncertainty.",
  "Your words carry particular power today. Choose them with care and speak from the heart.",
  "Pay attention to recurring symbols or patterns. The universe is sending you a message.",
  "Honor your physical well-being today. Your body wisdom offers valuable guidance.",
  "Consider a situation from an entirely new perspective. The solution may lie in changing your viewpoint.",
  "Share your gifts generously. The energy you put into the world returns multiplied.",
  "Follow the path of joy today. Your authentic happiness is a compass pointing toward your purpose.",
];

const luckyColors = [
  "Emerald Green",
  "Royal Blue",
  "Golden Yellow",
  "Ruby Red",
  "Purple Amethyst",
  "Turquoise",
  "Silver Gray",
  "Amber Orange",
  "Deep Indigo",
  "Rose Pink",
];

export default TodayFortune;
