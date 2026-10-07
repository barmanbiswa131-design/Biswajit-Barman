# BiswaTube — Self-Hosted Movie Server

This is the first version of BiswaTube's own movie backend.

## What it does

- Uses a local SQLite database instead of Firebase/Supabase.
- Stores movie metadata in the database.
- Keeps the actual movie files in a separate storage folder.
- Streams MP4 files with HTTP Range support, so a 2GB/8GB movie does not need to be downloaded all at once.
- Exposes a simple API that BiswaTube can use later.

## Folder layout

```
movie-server/
  server.js
  package.json
  README.md
  storage/       # put your own movie files here on the server
  movies.db      # created automatically
```

## Important

GitHub is only the code repository. It is **not** the 24/7 video server and should not be used to store multi-GB movie files.

To make this available 24/7, run this server on a VPS/cloud server or another always-on machine and attach enough disk storage.

Only upload movies that you own or have permission to host.

## Run

```bash
npm install
npm start
```

The health endpoint is:

```
GET /api/health
```

Movie streaming endpoint:

```
GET /stream/<movie-id>
```
