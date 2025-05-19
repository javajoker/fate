import React from "react";
import "./LifePhases.css";
import {
  LineChart,
  Line,
  XAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from "recharts";

const LifePhases = ({ elements }) => {
  const getval = (top, bottom, v) => {
    return Math.round((v - bottom) * 100 / (top - bottom))
  }
  const toPhaseData = (elements) => {
    if (!elements) return [];
    const { life, self, balance } = elements;
    const res = [];
    life.forEach((v) => {
      res.push({
        date: v.date.split('/')[0],
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
      })
    });
    return res;
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
    <div className="life-phase-chart" style={{ height: "min(400px,70vw)" }}>
      <ResponsiveContainer width="100%" height="100%">
        <LineChart
          data={toPhaseData(elements)}
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
            stroke="#8884d8"
            dot={false} activeDot={{ r: 1 }}
            strokeDasharray="5 5"
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="bottom"
            stroke="#8884d8"
            dot={false} activeDot={{ r: 1 }}
            strokeDasharray="5 5"
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es0"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es1"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es2"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es3"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es4"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es5"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es6"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es7"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es8"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
          <Line
            type="monotone"
            dataKey="es9"
            stroke="#8884d8"
            activeDot={{ r: 8 }}
            strokeWidth={2}
          />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default LifePhases;
