# GitHub Pages + Release Setup Guide

This guide walks you through getting `maze-program-apcsa-project` live on GitHub Pages with the `.exe` available for download.

**Final result:**
- Website: `https://vivaandudeja.github.io/maze-program-apcsa-project/`
- Download link in the site automatically pulls from your latest GitHub Release

---

## How this works (architecture)

GitHub Pages serves the website (the `docs/index.html` file). But the `.exe` installer is too big to live nicely inside a Pages repo, and Pages bandwidth is limited. So we use the standard split:

- **GitHub Pages** hosts the website (the download page)
- **GitHub Releases** hosts the `.exe` file (free, unlimited, designed for binaries up to 2 GB)
- The download button on the website points at:
  `https://github.com/vivaandudeja/maze-program-apcsa-project/releases/latest/download/MazeProgram-1.0.0.exe`
- That URL automatically resolves to whatever your latest Release tags as `MazeProgram-1.0.0.exe`

---

## One-time setup

### Step 1 — Build your installer (if you haven't already)

Open PowerShell 7 in this folder and run:

```powershell
.\build-installer.bat
```

This produces `dist\MazeProgram-1.0.0.exe`. Also optionally make a portable zip:

```powershell
.\build.bat
Compress-Archive -Path dist\MazeProgram -DestinationPath dist\MazeProgram-portable.zip
```

### Step 2 — Create the GitHub repo

1. Go to https://github.com/new
2. Repository name: **`maze-program-apcsa-project`**
3. Set it to **Public** (required for free GitHub Pages)
4. **Don't** check "Add a README" or "Add a .gitignore" (you already have those)
5. Click **Create repository**

### Step 3 — Push your code to GitHub

From this folder in PowerShell 7:

```powershell
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/vivaandudeja/maze-program-apcsa-project.git
git push -u origin main
```

If git isn't installed: download from https://git-scm.com/download/win

If git asks for credentials, use a Personal Access Token (not your password):
1. Go to https://github.com/settings/tokens
2. Generate new token (classic) → check **repo** scope → Generate
3. Copy the token and paste it as your password when git prompts

### Step 4 — Enable GitHub Pages

1. Go to your repo: https://github.com/vivaandudeja/maze-program-apcsa-project
2. Click **Settings** (top nav)
3. In the left sidebar, click **Pages**
4. Under **Build and deployment** → **Source**, leave it as **Deploy from a branch**
5. Under **Branch**, pick **`main`** and folder **`/docs`**
6. Click **Save**
7. Wait ~1 minute. GitHub shows a green banner with your live URL:
   `https://vivaandudeja.github.io/maze-program-apcsa-project/`

The site is live, but the download button will 404 until you create a Release with the `.exe`.

### Step 5 — Create a Release with the .exe

1. Go to your repo's main page
2. On the right sidebar, click **Releases** → **Create a new release**
   (Or go directly to: https://github.com/vivaandudeja/maze-program-apcsa-project/releases/new)
3. Click **Choose a tag** → type **`v1.0.0`** → click **Create new tag: v1.0.0 on publish**
4. Release title: **`MazeProgram v1.0.0`**
5. Description (optional): a short note like "First public release. Generate and solve mazes."
6. **Attach binaries:** drag-and-drop these two files into the attachment area:
   - `dist\MazeProgram-1.0.0.exe`
   - `dist\MazeProgram-portable.zip` (if you made one)

   ⚠️ **The filenames must match EXACTLY** — the download buttons on the site point at these specific names:
     - `MazeProgram-1.0.0.exe`
     - `MazeProgram-portable.zip`

7. Click **Publish release**

Done. Your download links work now. Visit your Pages URL and click Download — it should pull the `.exe`.

---

## Pushing future updates

### Updating just the website

```powershell
# edit docs\index.html
git add docs\index.html
git commit -m "Update download page"
git push
```

Pages rebuilds within ~1 minute.

### Releasing a new version of the app

1. Edit `.java` files, rebuild:
   ```powershell
   javac *.java
   jar cfe MazeProgram.jar MazeRunner *.class
   .\build-installer.bat
   ```
2. Commit any source changes:
   ```powershell
   git add .
   git commit -m "v1.1.0: <what changed>"
   git push
   ```
3. Create a **new** Release on GitHub:
   - New tag: `v1.1.0`
   - Upload the new `MazeProgram-1.1.0.exe`
4. Update `docs\index.html` to point at the new version (find-and-replace `1.0.0` with `1.1.0`):
   ```powershell
   git add docs\index.html
   git commit -m "Bump download links to v1.1.0"
   git push
   ```

Alternatively: always keep the URL as `MazeProgram-latest.exe` and re-upload with that name each release — then you never have to edit the HTML. Trade-off: the URL doesn't reveal what version you'll get.

---

## Custom domain (optional)

If you want `mazeprogram.com` or similar instead of `vivaandudeja.github.io/...`:

1. Buy a domain (Namecheap, Porkbun, Cloudflare Registrar are good)
2. In your repo: **Settings → Pages → Custom domain** → enter your domain
3. At your domain registrar, add the DNS records GitHub shows you
4. Wait for DNS to propagate (up to a few hours)

---

## Troubleshooting

**"git push" asks for password and rejects my GitHub password**
GitHub disabled password auth in 2021. Use a Personal Access Token (see Step 3) or set up SSH keys.

**Pages URL shows 404**
- Make sure Settings → Pages says it's deployed from `main` branch, `/docs` folder
- Make sure `docs/index.html` actually exists in the `main` branch on GitHub
- Wait a full 2 minutes after enabling — first build is slow
- Try a hard refresh (Ctrl+Shift+R)

**Download button gives 404**
- Make sure you created a Release (not just a tag)
- Make sure the uploaded filename matches exactly: `MazeProgram-1.0.0.exe`
- Check the release is "Published," not "Draft"

**Pages site shows old version after update**
- Hard refresh (Ctrl+Shift+R) — your browser may have cached it
- Check Actions tab on GitHub to confirm the Pages build succeeded

**"Repository too large"**
- Check that `.gitignore` is excluding `myRuntime/`, `dist/`, `build-input/`, and `*.class`
- If you accidentally committed them, remove them with:
  ```powershell
  git rm -r --cached myRuntime dist build-input
  git rm --cached *.class
  git commit -m "Remove build artifacts from history"
  git push
  ```

---

## File layout (what's in the repo)

```
maze-program-apcsa-project/
├── .gitignore
├── BUILD_README.txt
├── GITHUB_SETUP.md          <- this file
├── Cell.java
├── Maze.java
├── MazeRunner.java
├── MazeSolver.java
├── MazeProgram.jar          <- optional; kept for convenience
├── build.bat                <- portable folder build
├── build-installer.bat      <- installer build (uses WiX)
└── docs/
    └── index.html           <- the GitHub Pages site
```

`myRuntime/`, `dist/`, `build-input/`, and `*.class` are intentionally NOT in the repo — they're build artifacts that anyone can regenerate by running the build scripts.
