# Phone Usage Tracker - Push to GitHub Script (PowerShell)
# This script automates pushing your project to GitHub

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Phone Usage Tracker - GitHub Setup" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Check if git is installed
try {
    git --version | Out-Null
} catch {
    Write-Host "ERROR: Git is not installed." -ForegroundColor Red
    Write-Host "Please install Git from https://git-scm.com" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "GitHub Setup Instructions:" -ForegroundColor Green
Write-Host ""
Write-Host "BEFORE running this script, you need to:"
Write-Host "1. Go to https://github.com and sign in"
Write-Host "2. Click + (top right) > New repository"
Write-Host "3. Name it: phone-usage-tracker"
Write-Host "4. Choose Public"
Write-Host "5. Click Create repository"
Write-Host ""
Write-Host "Copy your repository URL"
Write-Host "(looks like: https://github.com/YOUR_USERNAME/phone-usage-tracker.git)"
Write-Host ""

# Get user input
$github_username = Read-Host "Enter your GitHub username"
$repo_url = Read-Host "Enter your repository URL (or press Enter to auto-generate)"

if ([string]::IsNullOrWhiteSpace($repo_url)) {
    $repo_url = "https://github.com/$github_username/phone-usage-tracker.git"
}

$git_name = Read-Host "Enter your name (for git config)"
$git_email = Read-Host "Enter your email (for git config)"

Write-Host ""
Write-Host "Setting up Git configuration..." -ForegroundColor Yellow
git config --global user.name "$git_name"
git config --global user.email "$git_email"

Write-Host "[OK] Git configured" -ForegroundColor Green
Write-Host ""
Write-Host "Adding files to repository..." -ForegroundColor Yellow
git add .

Write-Host "[OK] Files added" -ForegroundColor Green
Write-Host ""
Write-Host "Creating initial commit..." -ForegroundColor Yellow
git commit -m "Initial commit: Phone Usage Tracker app" --no-verify

Write-Host "[OK] Commit created" -ForegroundColor Green
Write-Host ""
Write-Host "Adding remote repository..." -ForegroundColor Yellow
git remote add origin "$repo_url" 2>$null
if ($LASTEXITCODE -ne 0) {
    git remote set-url origin "$repo_url"
}

Write-Host "[OK] Remote added" -ForegroundColor Green
Write-Host ""
Write-Host "Renaming branch to 'main'..." -ForegroundColor Yellow
git branch -M main

Write-Host "[OK] Branch renamed" -ForegroundColor Green
Write-Host ""
Write-Host "Pushing to GitHub (this may take a moment)..." -ForegroundColor Yellow
git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "==========================================" -ForegroundColor Green
    Write-Host "SUCCESS!" -ForegroundColor Green
    Write-Host "==========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Your code has been pushed to GitHub!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Next steps:" -ForegroundColor Cyan
    Write-Host "1. Go to: https://github.com/$github_username/phone-usage-tracker"
    Write-Host "2. Click the 'Actions' tab"
    Write-Host "3. Wait for the build to complete (5-10 minutes)"
    Write-Host "4. Click the workflow and download 'app-debug.apk'"
    Write-Host "5. Transfer to your phone and install"
    Write-Host ""
    Write-Host "==========================================" -ForegroundColor Green
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "ERROR: Failed to push to GitHub" -ForegroundColor Red
    Write-Host "Please check your repository URL and try again" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Read-Host "Press Enter to exit"
