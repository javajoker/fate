import lunisolar from "lunisolar";
const { Lunisolar } = lunisolar;
import moment from "moment-timezone";

const getStems = (sb) => {
  const s = [];
  s.push([sb.stem.value]);
  const bs = [];
  sb.branch.hiddenStems.forEach((stem) => {
    bs.push(stem.value);
  });
  s.push(bs);
  return s;
};

const 基础权重 = 100.0;
const 月提权重 = 基础权重 * 3;
const 藏干权重 = [[1.0], [0.7, 0.3], [0.65, 0.25, 0.1]];
const initWeight = (pillars, monthId) => {
  const weight = [];
  pillars.forEach((pillar, id) => {
    const branchW = [];
    藏干权重[pillar[1].length - 1].forEach((w) => {
      branchW.push((id === monthId ? 月提权重 : 基础权重) * w);
    });
    weight.push([[基础权重], branchW]);
  });
  return weight;
};

const 生主泄气 = 1.0 / 4;
const 生客得气 = 2.0 / 4;
const 克主耗气 = 2.0 / 4;
const 克客失气 = 3.0 / 4;

const 异性衰减 = 3.0 / 4;

const 五行生克数 = (x, y, wx, wy, 柱距) => {
  x.forEach((vx, i) => {
    y.forEach((vy, j) => {
      const ex = vx >> 1,
        ey = vy >> 1;
      if (ex == ey) return;
      let a = wx[i],
        b = wy[j],
        c = 0,
        d = 0;
      if (Math.abs(ex - ey) % 3 == 1) {
        c = d = a > b ? b : a;
        c *= 生主泄气;
        d *= 生客得气;
      } else {
        c = d = a > b ? b : a;
        c *= 克主耗气;
        d *= 克客失气;
      }
      if (vx % 2 !== vy % 2) {
        c *= 异性衰减;
        d *= 异性衰减;
      }
      --柱距;
      const 衰减 = 1 / Math.pow(2, 柱距 <= 0 ? 0 : 柱距);
      c *= 衰减;
      d *= 衰减;

      if ((ex < ey ? ex + 5 : ex) - ey == 1) {
        b -= c;
        a += d;
      } else if ((ey < ex ? ey + 5 : ey) - ex == 1) {
        a -= c;
        b += d;
      } else if ((ex < ey ? ex + 5 : ex) - ey == 2) {
        b -= c;
        a -= d;
      } else if ((ey < ex ? ey + 5 : ey) - ex == 2) {
        a -= c;
        b -= d;
      }
      wx[i] = a < 0 ? 0 : a;
      wy[j] = b < 0 ? 0 : b;
    });
  });
};

const updateWeight = (weight, pillars, seq) => {
  // branch
  seq.forEach((s) => {
    五行生克数(
      pillars[s[0]][1],
      pillars[s[1]][1],
      weight[s[0]][1],
      weight[s[1]][1],
      Math.abs(s[0] - s[1])
    );
  });
  // native stem - branch
  pillars.forEach((p, i) => {
    五行生克数(p[0], p[1], weight[i][0], weight[i][1], 0);
  });
  // stem - branch
  seq.forEach((s) => {
    五行生克数(
      pillars[s[0]][0],
      pillars[s[1]][1],
      weight[s[0]][0],
      weight[s[1]][1],
      Math.abs(s[0] - s[1]) + 1
    );
    五行生克数(
      pillars[s[0]][1],
      pillars[s[1]][0],
      weight[s[0]][1],
      weight[s[1]][0],
      Math.abs(s[0] - s[1]) + 1
    );
  });
  // stem
  seq.forEach((s) => {
    五行生克数(
      pillars[s[0]][0],
      pillars[s[1]][0],
      weight[s[0]][0],
      weight[s[1]][0],
      Math.abs(s[0] - s[1])
    );
  });
};
const getElements = (weight, pillars) => {
  const es = [];
  pillars.forEach((p, i) => {
    p[0].forEach((s, j) => {
      if (!es[s]) es[s] = 0;
      es[s] += weight[i][0][j];
    });
    p[1].forEach((s, j) => {
      if (!es[s]) es[s] = 0;
      es[s] += weight[i][1][j];
    });
  });
  return {
    yang: {
      wood: Math.round(es[0] ?? 0),
      fire: Math.round(es[2] ?? 0),
      earth: Math.round(es[4] ?? 0),
      metal: Math.round(es[6] ?? 0),
      water: Math.round(es[8] ?? 0),
    },
    yin: {
      wood: Math.round(es[1] ?? 0),
      fire: Math.round(es[3] ?? 0),
      earth: Math.round(es[5] ?? 0),
      metal: Math.round(es[7] ?? 0),
      water: Math.round(es[9] ?? 0),
    },
  };
};

const parseSelf = (gender, year, month, day, hour) => {
  const lunisolar = new Lunisolar(
    moment.tz([year, month - 1, day, hour, 0], "Asia/Shanghai")
  );
  const pillars = [];
  pillars.push(getStems(lunisolar.char8.year));
  pillars.push(getStems(lunisolar.char8.month));
  pillars.push(getStems(lunisolar.char8.day));
  pillars.push(getStems(lunisolar.char8.hour));
  const weight = initWeight(pillars, 1);
  const seq = [];
  for (let i = 0; i < pillars.length - 1; ++i) {
    for (let j = i + 1; j < pillars.length; ++j) {
      seq.push([i, j]);
    }
  }
  updateWeight(weight, pillars, seq);
  const ele = getElements(weight, pillars);
  const host = pillars[2][0][0],
    elements = ["wood", "fire", "earth", "metal", "water"];
  ele.self = {
    yy: host % 2 == 0 ? "yang" : "yin",
    ele: elements[host >> 1],
    val: Math.round(weight[2][0][0]),
  };
  return ele;
};

export { parseSelf };
