import lunisolar from "lunisolar";
const { Lunisolar, SolarTerm, SB } = lunisolar;
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

      switch ((ey - ex + 5) % 5) {
        case 1:
          a -= c;
          b += d;
          break;
        case 2:
          a -= c;
          b -= d;
          break;
        case 3:
          b -= c;
          a -= d;
          break;
        case 4:
          b -= c;
          a += d;
          break;
      }

      wx[i] = a < 0 ? 0 : a;
      wy[j] = b < 0 ? 0 : b;
    });
  });
};

const updateWeight = (weight, pillars, seq, distance) => {
  // branch
  seq.forEach((s) => {
    五行生克数(
      pillars[s[0]][1],
      pillars[s[1]][1],
      weight[s[0]][1],
      weight[s[1]][1],
      distance ?? Math.abs(s[0] - s[1])
    );
  });
  // stem - branch
  seq.forEach((s) => {
    五行生克数(
      pillars[s[0]][0],
      pillars[s[1]][1],
      weight[s[0]][0],
      weight[s[1]][1],
      distance ?? Math.abs(s[0] - s[1]) + 1
    );
    五行生克数(
      pillars[s[0]][1],
      pillars[s[1]][0],
      weight[s[0]][1],
      weight[s[1]][0],
      distance ?? Math.abs(s[0] - s[1]) + 1
    );
  });
  // stem
  seq.forEach((s) => {
    五行生克数(
      pillars[s[0]][0],
      pillars[s[1]][0],
      weight[s[0]][0],
      weight[s[1]][0],
      distance ?? Math.abs(s[0] - s[1])
    );
  });
};

const 基础权重 = 100.0;
const 月提权重 = 基础权重 * 3;
const 时权 = 基础权重 / 2;
const 藏干权重 = [[1.0], [0.7, 0.3], [0.65, 0.25, 0.1]];

const initPillarWeight = (pillar, sWeight, bWeight) => {
  const branchW = [];
  藏干权重[pillar[1].length - 1].forEach((w) => {
    branchW.push(bWeight * w);
  });
  return [[sWeight], branchW];
};

const initWeight = (pillars, monthId, timeId) => {
  const weight = [];
  pillars.forEach((pillar, id) => {
    const w = initPillarWeight(
      pillar,
      id === timeId ? 时权 : 基础权重,
      id === monthId ? 月提权重 : id === timeId ? 时权 : 基础权重
    );
    // native stem - branch
    五行生克数(pillar[0], pillar[1], w[0], w[1], 0);
    weight.push(w);
  });
  return weight;
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
  return es;
};

// ===============================================
// 大運流年
const 年序 = 0;
const 月序 = 年序 + 1;
const 日序 = 月序 + 1;
const 时序 = 日序 + 1;
const 大运序 = 时序 + 1;
const 流年序 = 大运序 + 1;

const 太岁权重 = 140.0;
const 大运流年干支权和 = 220.0;
const 大运生发 = 20.0;

const 顺行 = (lunisolar, gender) => {
  const 年干 = lunisolar.char8.year.stem.value;
  return gender ? 年干 % 2 == 1 : 年干 % 2 == 0;
};

/**
 * 由出生日的下一日起，数至下月立节的日、时为止； 三日折一年，一日折四月，一时折十天。
 *
 * 简单算法：
 *
 * 根据年干分阴阳；
 *
 * 阳男阴女，由出生之当天数起至下一个节，3日为一岁。
 *
 * 阴男阳女，由出生之当天倒数至上一个节，3日为一岁。
 *
 * @param lunisolar
 * @param gender
 * @return 起运日期（约）
 */
const 起运交脱 = (lunisolar, gender) => {
  const isForward = 顺行(lunisolar, gender);
  const day = SolarTerm.getMonthTerms(lunisolar.year, lunisolar.month)[0];
  const t = { y: lunisolar.year, m: lunisolar.month };
  if (isForward) {
    if (t.m == 12) {
      t.y++;
      t.m = 1;
    } else {
      t.m++;
    }
  } else {
    if (t.m == 1) {
      t.y--;
      t.m = 12;
    } else {
      t.m--;
    }
  }
  const delta = Math.abs(
    (day >= lunisolar.day && isForward) || (day <= lunisolar.day && !isForward)
      ? day - lunisolar.day
      : lunisolar.diff(
          `${t.y}/${t.m}/${SolarTerm.getMonthTerms(t.y, t.m)[0]}`,
          "d"
        )
  );
  return lunisolar.add((delta * 365) / 3, "d");
};

const 大运 = (lunisolar, gender) => {
  const isForward = 顺行(lunisolar, gender);
  let m = lunisolar.char8.month.value;
  const ret = [];
  for (let i = 0; i < 12; ++i) {
    ret.push(SB.create(isForward ? ++m : --m));
  }
  return ret;
};

const 大运经年 = (lunisolar, gender, date) => {
  const ydate = 起运交脱(lunisolar, gender);
  const days = ydate.diff(date, "d");
  if (days < 0) {
    console.log("未起运");
    return -1;
  }
  const diff = Math.floor(ydate.diff(date, "y"));
  return { sid: Math.floor(diff / 10), y: diff % 10 };
};

const initWeight2 = (pillars, weight, extraPillars, 大运经年) => {
  extraPillars.forEach((p, id) => {
    const pillar = getStems(p);
    pillars.push(pillar);
    const w = initPillarWeight(
      pillar,
      id === 0
        ? 大运流年干支权和 - (大运经年 + 1) * 大运生发
        : 大运流年干支权和 - 太岁权重,
      id === 0 ? (大运经年 + 1) * 大运生发 : 太岁权重
    );

    五行生克数(pillar[0], pillar[1], w[0], w[1], 0);
    weight.push(w);
  });
};

const getMargin = (weights) => {
  const avg =
    weights.reduce((sum, val) => sum + (val ?? 0), 0) / weights.length;
  const variance =
    weights.reduce((sum, val) => sum + Math.pow((val ?? 0) - avg, 2), 0) /
    weights.length;
  const balance = Math.sqrt(variance);
  return { top: avg + balance, bottom: avg - balance };
};
const parseSelf = (gen, year, month, day, hour) => {
  const lunisolar = new Lunisolar(
    moment.tz([year, month - 1, day, hour < 0 ? 0 : hour, 0], "Asia/Shanghai")
  );
  const gender = gen == 1;
  console.log(lunisolar.format("cY cM cD cH"), gender ? "坤造" : "乾造");

  const pillars = [];
  pillars.push(getStems(lunisolar.char8.year));
  pillars.push(getStems(lunisolar.char8.month));
  pillars.push(getStems(lunisolar.char8.day));
  if (hour >= 0) pillars.push(getStems(lunisolar.char8.hour));
  const weight = initWeight(pillars, 1, 3);
  const seq = [];
  for (let i = 0; i < pillars.length - 1; ++i) {
    for (let j = i + 1; j < pillars.length; ++j) {
      seq.push([i, j]);
    }
  }
  updateWeight(weight, pillars, seq);
  const es = getElements(weight, pillars);

  // start life cast
  const now = new Date();
  const life = [];
  let ydate = 起运交脱(lunisolar, gender),
    y = lunisolar.diff(ydate, "y") + 1,
    currentY = now.getFullYear() - ydate.toDate().getFullYear();
  const sb = 大运(lunisolar, gender);
  const seq2 = [];
  for (let j = 0; j < 2; ++j) {
    for (let i = 0; i < pillars.length; ++i) seq2.push([pillars.length + j, i]);
  }
  // calculate 80 years
  const sWeight = [],
    eWeight = [],
    运 = [];

  for (let i = 0; i < 80; ++i) {
    const p = JSON.parse(JSON.stringify(pillars)),
      w = JSON.parse(JSON.stringify(weight)),
      yid = Math.floor(i / 10);
    initWeight2(p, w, [sb[yid], ydate.char8.year], i % 10);
    updateWeight(w, p, seq2, 1);
    const es2 = getElements(w, p);
    life.push({
      date: ydate.format("YYYY/MM/DD"),
      es: es2,
      self: w[2][0][0],
    });
    sWeight.push(w[2][0][0]);
    if (!运[yid]) 运[yid] = { e: getStems(sb[yid])[0][0], v: 0 };
    运[yid].v += w[2][0][0];
    es2.forEach((v, i) => {
      if (!eWeight[i]) eWeight[i] = [];
      eWeight[i].push(i == pillars[2][0][0] ? (v ?? 0) - w[2][0][0] : v ?? 0);
    });
    ydate = ydate.add(1, "y");
  }
  const esb = [];
  eWeight.forEach((v) => esb.push(getMargin(v)));

  const start = 10 * Math.floor(currentY / 10),
    life20 = life.slice(start - 10, start + 10);
  return {
    es,
    self: { e: pillars[2][0][0], v: weight[2][0][0] },
    life: life20,
    balance: { self: getMargin(sWeight), es: esb },
    y,
    y10: 运,
  };
};

export { parseSelf };
