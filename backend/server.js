const axios = require("axios");
const express = require("express");
const cors = require("cors");
const bodyParser = require("body-parser");
const fs = require("fs");
const path = require("path");
const dotenv = require("dotenv");

dotenv.config();

const app = express();
const PORT = 4000;

// Middleware
app.use(cors());
app.use(bodyParser.json());

// File Path
const fateFilePath = path.join(__dirname, "data", "fate.json");

// Helper Functions
const readFate = () => JSON.parse(fs.readFileSync(fateFilePath, "utf8"));
const writeFate = (data) =>
  fs.writeFileSync(fateFilePath, JSON.stringify(data, null, 2));


// API Routes
app.get("/api/fate/:gender/:year/:month/:day/:hour", async (req, res) => {
  const { gender, year, month, day, hour } = req.params;

  const { parseSelf } = await import("./fate/index.mjs");
  res.json(parseSelf(gender, year, month, day, hour));

  // const response = await axios.post(
  //   "https://iching.infoecos.com/destiny/json.php",
  //   `y=${year}&m=${month}&d=${day}&h=${hour}&i=0&gender=${gender}&timezone=CCT`
  // );
  // res.json(response.data);
});

const FATE = [];
const fateConfPath = path.join(__dirname, "data", "res", "fate");
try {
  const files = fs.readdirSync(fateConfPath);
  files.forEach((file) => {
    const filePath = path.join(fateConfPath, file);
    const stat = fs.statSync(filePath);
    if (stat.isDirectory()) {
      loadFate(file, filePath);
      FATE.push(file);
    }
  });
} catch (err) {
  console.error("Error reading directory:", err.message);
}
app.get("/api/fate-types", (req, res) => {
  res.json(FATE);
});

// ========================================================
// Visual
const visualResPath = path.join(__dirname, "data", "res");
app.use("/res", express.static(visualResPath));

// ========================================================
// Start Server
app.listen(PORT, () => {
  console.log(`Server is running on http://localhost:${PORT}`);
});
