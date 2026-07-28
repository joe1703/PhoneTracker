# Quick Start Guide

## TL;DR - Get APK in 5 Minutes

### Step 1: Create GitHub Account (if you don't have one)
- Go to [github.com](https://github.com)
- Sign up (free)

### Step 2: Create Repository on GitHub
1. Click **+** → **New repository**
2. Name: `phone-usage-tracker`
3. Choose **Public**
4. Click **Create repository**

### Step 3: Push Code to GitHub

Copy and paste these commands in terminal/PowerShell (replace YOUR_USERNAME):

```bash
cd phone_usage_tracker

git config --global user.name "Your Name"
git config --global user.email "your@email.com"

git add .
git commit -m "Initial: Phone Usage Tracker"
git remote add origin https://github.com/YOUR_USERNAME/phone-usage-tracker.git
git branch -M main
git push -u origin main
```

### Step 4: Wait for Build (5-10 minutes)
- Go to: `https://github.com/YOUR_USERNAME/phone-usage-tracker`
- Click **Actions** tab
- Watch for green checkmark ✓

### Step 5: Download APK
1. Click the completed workflow
2. Scroll to **Artifacts**
3. Click **app-debug** to download
4. Transfer to your phone and tap to install

---

## Optional: Build Locally (Requires Android Studio or Gradle)

If you want to build on your computer:

```bash
chmod +x gradlew          # On Mac/Linux only

./gradlew assembleDebug   # Build APK
# or on Windows:
gradlew.bat assembleDebug
```

APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

---

## Troubleshooting

**"git not found"**
- Install Git from [git-scm.com](https://git-scm.com)

**GitHub Action fails**
- Check Actions tab for error logs
- Make sure `build.gradle.kts` is in the `app/` folder

**Can't install APK on phone**
- Enable "Unknown sources" in phone settings
- Make sure Android version is 6.0+

---

**Questions?** Open an issue on your GitHub repository!
