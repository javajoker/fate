import React from "react";
import "./FiveRadar.css"
import {
  Radar,
  PolarAngleAxis,
  PolarGrid,
  PolarRadiusAxis,
  RadarChart,
  ResponsiveContainer,
  Tooltip,
} from "recharts";

const FiveRadar = ({ elements }) => {
  const toRadarData = (elements) => {
    const self = elements.self,
      yy = self.e % 2;
    const es = elements.es;
    const res = [
      {
        subject: "🪵🌱",
        all: (es[0] ?? 0) + (es[1] ?? 0),
        host: es[0 + yy] ?? 0,
        self: self.e >> 1 === 0 ? self.v : 0,
        fullMark: 100,
      },
      {
        subject: "🌋🔥",
        all: (es[2] ?? 0) + (es[3] ?? 0),
        host: es[2 + yy] ?? 0,
        self: self.e >> 1 === 1 ? self.v : 0,
        fullMark: 100,
      },
      {
        subject: "⛰️🛤",
        all: (es[4] ?? 0) + (es[5] ?? 0),
        host: es[4 + yy] ?? 0,
        self: self.e >> 1 === 2 ? self.v : 0,
        fullMark: 100,
      },
      {
        subject: "🗡️🧈",
        all: (es[6] ?? 0) + (es[7] ?? 0),
        host: es[6 + yy] ?? 0,
        self: self.e >> 1 === 3 ? self.v : 0,
        fullMark: 100,
      },
      {
        subject: "🌊💦",
        all: (es[8] ?? 0) + (es[9] ?? 0),
        host: es[8 + yy] ?? 0,
        self: self.e >> 1 === 4 ? self.v : 0,
        fullMark: 100,
      },
    ];
    res.forEach((v) => {
      v.all = Math.round(100 * Math.sqrt(v.all / self.v));
      v.host = Math.round(100 * Math.sqrt(v.host / self.v));
      if (v.self) v.self = 100;
    });
    return res;
  };

  const CustomTooltip = ({ active, payload, label }) => {
    if (active && payload && payload.length) {
      return (
        <div className="radar-tooltip">
          <h4>{label}</h4>
          {(payload[0].value ?? 0) > 0 ? <p style={{ color: '#8884d8' }}>{`${payload[1].name == "🌜" ? "🌞" : "🌜"}: ${payload[0].value - payload[1].value}`}</p> : ''}
          {(payload[1].value ?? 0) > 0 ? <p style={{ color: '#82ca9d' }}>{`${payload[1].name}: ${payload[1].value}`}</p> : ''}
          {(payload[2].value ?? 0) > 0 ? (<p style={{ color: 'red' }}>{`${payload[2].name}: ${payload[2].value}`}</p>) : ''}
        </div>
      );
    }
    return null;
  };

  return (
    <div className="radar-chart" style={{ height: "min(400px,70vw)" }}>
      <ResponsiveContainer width="100%" height="100%">
        <RadarChart
          cx="50%"
          cy="50%"
          outerRadius="80%"
          data={toRadarData(elements)}
        >
          <PolarGrid />
          <PolarAngleAxis dataKey="subject" />
          <PolarRadiusAxis angle={54} domain={[0, "dataMax"]} />
          <Tooltip content={<CustomTooltip />} />
          <Radar
            name="ALL"
            dataKey="all"
            stroke="#8884d8"
            fill="#8884d8"
            fillOpacity={0.6}
          />
          <Radar
            name={`${elements.self.e % 2 === 1 ? "🌜" : "🌞"}`}
            dataKey="host"
            stroke="#82ca9d"
            fill="#82ca9d"
            fillOpacity={0.6}
          />
          <Radar
            name="👤"
            dataKey="self"
            stroke="red"
            fill="red"
            fillOpacity={0.6}
          />
        </RadarChart>
      </ResponsiveContainer>
    </div>
  );
};

export default FiveRadar;
