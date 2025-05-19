import React, { useState } from "react";
import "./Life20y.css";
import { useTranslation } from "react-i18next";
import {
  LineChart,
  Line,
  XAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

const Life20y = ({ elements }) => {
  const [visibleSeries, setVisibleSeries] = useState({
    series0: false,
    series1: false,
    series2: false,
    series3: false,
    series4: false,
    series5: false,
    series6: false,
    series7: false,
    series8: false,
    series9: false,
  });
  const [prevSeries, setPrevSeries] = useState("");

  const { t } = useTranslation();

  const toggleSeries = (series) => {
    setVisibleSeries((prev) => ({
      ...prev,
      [prevSeries]: false,
      [series]: !prev[series],
    }));
    setPrevSeries(prevSeries == series ? "" : series);
  };

  const getval = (top, bottom, v) => {
    return Math.round(((v - bottom) * 100) / (top - bottom));
  };
  const toPhaseData = () => {
    if (!elements) return [];
    const { life, self, balance } = elements;
    const res = [];
    life.forEach((v) => {
      res.push({
        date: v.date.split("/")[0],
        top: 100,
        bottom: 0,
        value: getval(balance.self.top, balance.self.bottom, v.self),
        es0: getval(balance.es[0].top, balance.es[0].bottom, v.es[0] ?? 0),
        es1: getval(balance.es[1].top, balance.es[1].bottom, v.es[1] ?? 0),
        es2: getval(balance.es[2].top, balance.es[2].bottom, v.es[2] ?? 0),
        es3: getval(balance.es[3].top, balance.es[3].bottom, v.es[3] ?? 0),
        es4: getval(balance.es[4].top, balance.es[4].bottom, v.es[4] ?? 0),
        es5: getval(balance.es[5].top, balance.es[5].bottom, v.es[5] ?? 0),
        es6: getval(balance.es[6].top, balance.es[6].bottom, v.es[6] ?? 0),
        es7: getval(balance.es[7].top, balance.es[7].bottom, v.es[7] ?? 0),
        es8: getval(balance.es[8].top, balance.es[8].bottom, v.es[8] ?? 0),
        es9: getval(balance.es[9].top, balance.es[9].bottom, v.es[9] ?? 0),
      });
    });
    return res;
  };
  const getGod = (id) => {
    if (!elements) return "";
    return t(
      `life-god-10-${((id >> 1) - (elements.self.e >> 1) + 5) % 5}-${
        elements.self.e % 2 === id % 2 ? 0 : 1
      }`
    );
  };
  const godDesc = () => {
    const id = +prevSeries.substring("series".length);
    return (
      prevSeries && (
        <p>
          {t(
            `life-goddesc-10-${((id >> 1) - (elements.self.e >> 1) + 5) % 5}-${
              elements.self.e % 2 === id % 2 ? 0 : 1
            }`
          )}
        </p>
      )
    );
  };
  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="life-tooltip">
          <h4>{label}</h4>
        </div>
      );
    }
    return null;
  };

  return (
    <div>
      <div className="phase-card next">
        <button
          className={`life-20-tag ${visibleSeries.series0 && "active"}`}
          onClick={() => toggleSeries("series0")}
        >
          {getGod(0)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series1 && "active"}`}
          onClick={() => toggleSeries("series1")}
        >
          {getGod(1)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series2 && "active"}`}
          onClick={() => toggleSeries("series2")}
        >
          {getGod(2)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series3 && "active"}`}
          onClick={() => toggleSeries("series3")}
        >
          {getGod(3)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series4 && "active"}`}
          onClick={() => toggleSeries("series4")}
        >
          {getGod(4)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series5 && "active"}`}
          onClick={() => toggleSeries("series5")}
        >
          {getGod(5)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series6 && "active"}`}
          onClick={() => toggleSeries("series6")}
        >
          {getGod(6)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series7 && "active"}`}
          onClick={() => toggleSeries("series7")}
        >
          {getGod(7)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series8 && "active"}`}
          onClick={() => toggleSeries("series8")}
        >
          {getGod(8)}
        </button>
        <button
          className={`life-20-tag ${visibleSeries.series9 && "active"}`}
          onClick={() => toggleSeries("series9")}
        >
          {getGod(9)}
        </button>
        {godDesc()}
      </div>
      <div className="life-phase-chart" style={{ height: "min(400px,70vw)" }}>
        <ResponsiveContainer width="100%" height="100%">
          <LineChart
            data={toPhaseData()}
            margin={{
              top: 5,
              right: 30,
              left: 20,
              bottom: 5,
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <Tooltip content={<CustomTooltip />} />
            <Line
              type="monotone"
              dataKey="value"
              stroke="#8884d8"
              activeDot={{ r: 6 }}
              strokeWidth={2}
            />
            <Line
              type="monotone"
              dataKey="top"
              stroke="red"
              dot={false}
              activeDot={{ r: 1 }}
              strokeDasharray="5 5"
              strokeWidth={2}
            />
            <Line
              type="monotone"
              dataKey="bottom"
              stroke="red"
              dot={false}
              activeDot={{ r: 1 }}
              strokeDasharray="5 5"
              strokeWidth={2}
            />
            {visibleSeries.series0 && (
              <Line
                type="monotone"
                dataKey="es0"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series1 && (
              <Line
                type="monotone"
                dataKey="es1"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series2 && (
              <Line
                type="monotone"
                dataKey="es2"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series3 && (
              <Line
                type="monotone"
                dataKey="es3"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series4 && (
              <Line
                type="monotone"
                dataKey="es4"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series5 && (
              <Line
                type="monotone"
                dataKey="es5"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series6 && (
              <Line
                type="monotone"
                dataKey="es6"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series7 && (
              <Line
                type="monotone"
                dataKey="es7"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series8 && (
              <Line
                type="monotone"
                dataKey="es8"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}
            {visibleSeries.series9 && (
              <Line
                type="monotone"
                dataKey="es9"
                stroke="var(--secondary-color)"
                activeDot={{ r: 8 }}
                strokeWidth={2}
              />
            )}{" "}
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default Life20y;
