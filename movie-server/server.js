import express from "express";
import fs from "node:fs";
import path from "node:path";
import Database from "better-sqlite3";

const app = express();
const PORT = process.env.PORT || 8080;
const ROOT = process.env.MOVIE_ROOT || path.resolve("./storage");
const DB_PATH = process.env.MOVIE_DB || path.resolve("./movies.db");

fs.mkdirSync(ROOT, { recursive: true });

const db = new Database(DB_PATH);
db.exec(`
  CREATE TABLE IF NOT EXISTS movies (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT DEFAULT '',
    category TEXT DEFAULT 'Movie',
    language TEXT DEFAULT '',
    poster_url TEXT DEFAULT '',
    file_name TEXT NOT NULL,
    created_at TEXT DEFAULT CURRENT_TIMESTAMP
  )
`);

app.use(express.json({ limit: "1mb" }));

app.get("/api/health", (_req, res) => {
  res.json({ ok: true, service: "BiswaTube Movie Server" });
});

app.get("/api/movies", (_req, res) => {
  const movies = db.prepare("SELECT id,title,description,category,language,poster_url,created_at FROM movies ORDER BY id DESC").all();
  res.json(movies);
});

app.get("/api/movies/:id", (req, res) => {
  const movie = db.prepare("SELECT * FROM movies WHERE id = ?").get(req.params.id);
  if (!movie) return res.status(404).json({ error: "Movie not found" });
  res.json(movie);
});

app.get("/stream/:id", (req, res) => {
  const movie = db.prepare("SELECT file_name FROM movies WHERE id = ?").get(req.params.id);
  if (!movie) return res.status(404).send("Movie not found");

  const filePath = path.resolve(ROOT, movie.file_name);
  if (!filePath.startsWith(path.resolve(ROOT) + path.sep) || !fs.existsSync(filePath)) {
    return res.status(404).send("Video file not found");
  }

  const stat = fs.statSync(filePath);
  const range = req.headers.range;
  const contentType = "video/mp4";

  if (!range) {
    res.writeHead(200, {
      "Content-Length": stat.size,
      "Content-Type": contentType,
      "Accept-Ranges": "bytes"
    });
    return fs.createReadStream(filePath).pipe(res);
  }

  const [startText, endText] = range.replace("bytes=", "").split("-");
  const start = Number(startText);
  const end = endText ? Number(endText) : stat.size - 1;
  if (!Number.isInteger(start) || start < 0 || start >= stat.size || end < start) {
    return res.status(416).set("Content-Range", `bytes */${stat.size}`).end();
  }

  const safeEnd = Math.min(end, stat.size - 1);
  res.writeHead(206, {
    "Content-Range": `bytes ${start}-${safeEnd}/${stat.size}`,
    "Accept-Ranges": "bytes",
    "Content-Length": safeEnd - start + 1,
    "Content-Type": contentType
  });

  fs.createReadStream(filePath, { start, end: safeEnd }).pipe(res);
});

app.listen(PORT, () => {
  console.log(`BiswaTube Movie Server running on port ${PORT}`);
  console.log(`Movie files: ${ROOT}`);
});
