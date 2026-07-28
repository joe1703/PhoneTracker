@echo off
REM Phone Usage Tracker - Push to GitHub Script (Windows)
REM This script automates pushing your project to GitHub

echo.
echo ==========================================
echo Phone Usage Tracker - GitHub Setup
echo ==========================================
echo.

REM Check if git is installed
git --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Git is not installed.
    echo Please install Git from https://git-scm.com
    pause
    exit /b 1
)

echo GitHub Setup Instructions:
echo.
echo BEFORE running this script, you need to:
echo 1. Go to https://github.com and sign in
echo 2. Click + (top right) ^> New repository
echo 3. Name it: phone-usage-tracker
echo 4. Choose Public
echo 5. Click Create repository
echo.
echo Copy your repository URL
echo (looks like: https://github.com/YOUR_USERNAME/phone-usage-tracker.git)
echo.

REM Get user input
set /p github_username="Enter your GitHub username: "
set /p repo_url="Enter your repository URL (or press Enter to auto-generate): "

if "%repo_url%"=="" (
    set "repo_url=https://github.com/%github_username%/phone-usage-tracker.git"
)

set /p git_name="Enter your name (for git config): "
set /p git_email="Enter your email (for git config): "

echo.
echo Setting up Git configuration...
git config --global user.name "%git_name%"
git config --global user.email "%git_email%"

echo [OK] Git configured
echo.
echo Adding files to repository...
git add .

echo [OK] Files added
echo.
echo Creating initial commit...
git commit -m "Initial commit: Phone Usage Tracker app" --no-verify

echo [OK] Commit created
echo.
echo Adding remote repository...
git remote add origin "%repo_url%" 2>nul || git remote set-url origin "%repo_url%"

echo [OK] Remote added
echo.
echo Renaming branch to 'main'...
git branch -M main

echo [OK] Branch renamed
echo.
echo Pushing to GitHub (this may take a moment)...
git push -u origin main

if errorlevel 1 (
    echo.
    echo ERROR: Failed to push to GitHub
    echo Please check your repository URL and try again
    pause
    exit /b 1
)

echo.
echo ==========================================
echo SUCCESS!
echo ==========================================
echo.
echo Your code has been pushed to GitHub!
echo.
echo Next steps:
echo 1. Go to: https://github.com/%github_username%/phone-usage-tracker
echo 2. Click the 'Actions' tab
echo 3. Wait for the build to complete (5-10 minutes)
echo 4. Click the workflow and download 'app-debug.apk'
echo 5. Transfer to your phone and install
echo.
echo ==========================================
echo.

pause
