#!/bin/bash

# Phone Usage Tracker - Push to GitHub Script
# This script automates pushing your project to GitHub

echo "=========================================="
echo "Phone Usage Tracker - GitHub Setup"
echo "=========================================="
echo ""

# Check if git is installed
if ! command -v git &> /dev/null; then
    echo "❌ Git is not installed. Please install Git from https://git-scm.com"
    exit 1
fi

echo "📝 GitHub Setup Instructions:"
echo ""
echo "BEFORE running this script, you need to:"
echo "1. Go to https://github.com and sign in"
echo "2. Click + (top right) → New repository"
echo "3. Name it: phone-usage-tracker"
echo "4. Choose Public"
echo "5. Click Create repository"
echo ""
echo "Copy your repository URL (looks like: https://github.com/YOUR_USERNAME/phone-usage-tracker.git)"
echo ""

# Get user input
read -p "Enter your GitHub username: " github_username
read -p "Enter your repository URL (or press Enter to auto-generate): " repo_url

if [ -z "$repo_url" ]; then
    repo_url="https://github.com/$github_username/phone-usage-tracker.git"
fi

read -p "Enter your name (for git config): " git_name
read -p "Enter your email (for git config): " git_email

echo ""
echo "Setting up Git configuration..."
git config --global user.name "$git_name"
git config --global user.email "$git_email"

echo "✓ Git configured"
echo ""
echo "Adding files to repository..."
git add .

echo "✓ Files added"
echo ""
echo "Creating initial commit..."
git commit -m "Initial commit: Phone Usage Tracker app" --no-verify

echo "✓ Commit created"
echo ""
echo "Adding remote repository..."
git remote add origin "$repo_url" 2>/dev/null || git remote set-url origin "$repo_url"

echo "✓ Remote added"
echo ""
echo "Renaming branch to 'main'..."
git branch -M main

echo "✓ Branch renamed"
echo ""
echo "Pushing to GitHub (this may take a moment)..."
git push -u origin main

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ SUCCESS!"
    echo "=========================================="
    echo ""
    echo "Your code has been pushed to GitHub!"
    echo ""
    echo "📱 Next steps:"
    echo "1. Go to: https://github.com/$github_username/phone-usage-tracker"
    echo "2. Click the 'Actions' tab"
    echo "3. Wait for the build to complete (5-10 minutes)"
    echo "4. Click the workflow and download 'app-debug.apk'"
    echo "5. Transfer to your phone and install"
    echo ""
    echo "=========================================="
else
    echo ""
    echo "❌ Failed to push to GitHub"
    echo "Please check your repository URL and try again"
    exit 1
fi
