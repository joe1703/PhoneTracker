# GitHub Setup Instructions

Follow these steps to push the project to GitHub and get automated APK builds:

## Step 1: Create a GitHub Repository

1. Go to [github.com](https://github.com)
2. Sign in (or create an account if you don't have one)
3. Click **"+" icon → New repository**
4. Name it: `phone-usage-tracker`
5. Choose "Public" (so you can download releases easily)
6. Click **"Create repository"**

## Step 2: Push Code to GitHub

Open terminal/command prompt and run these commands from the `phone_usage_tracker` folder:

```bash
# Configure git with your GitHub username and email
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Add all files to git
git add .

# Create initial commit
git commit -m "Initial commit: Phone Usage Tracker app"

# Add remote repository (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/phone-usage-tracker.git

# Rename branch to main (if needed)
git branch -M main

# Push to GitHub
git push -u origin main
```

## Step 3: Download APK

After pushing:

1. Go to your repository on GitHub: `https://github.com/YOUR_USERNAME/phone-usage-tracker`
2. Click on **"Actions"** tab
3. Wait for the build to complete (green checkmark)
4. Click on the completed workflow
5. Scroll down and download **"app-debug.apk"**

## Step 4: Install on Phone

1. Download the APK to your phone (or computer then transfer)
2. Open file manager on your phone
3. Tap the APK file
4. Tap **"Install"**
5. Grant any permissions requested
6. Done!

## Important Notes

- The first build might take 5-10 minutes
- GitHub Actions is free for public repositories
- Every time you push code, a new build is created
- To download, go to Actions → latest workflow → app-debug artifact

---

**Need help?** Check the GitHub Actions workflow at `.github/workflows/build.yml`
