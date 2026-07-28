#!/bin/bash

# Setup Gradle Wrapper for Phone Usage Tracker

echo "=========================================="
echo "Setting up Gradle Wrapper"
echo "=========================================="
echo ""

# Check if gradle is installed
if ! command -v gradle &> /dev/null; then
    echo "❌ Gradle is not installed."
    echo ""
    echo "You have two options:"
    echo ""
    echo "Option 1: Install Gradle"
    echo "  - Download from: https://gradle.org/releases/"
    echo "  - Or use: brew install gradle (Mac)"
    echo "  - Or use: choco install gradle (Windows)"
    echo "  - Or use: apt-get install gradle (Linux)"
    echo ""
    echo "Option 2: Use GitHub Actions (Recommended)"
    echo "  - Push to GitHub and let GitHub Actions build the APK"
    echo "  - Go to: https://github.com/YOUR_USERNAME/phone-usage-tracker"
    echo "  - Click 'Actions' tab to see the build"
    echo "  - Download APK from Artifacts"
    echo ""
    exit 1
fi

echo "✓ Gradle found"
echo ""
echo "Generating gradle wrapper..."
gradle wrapper --gradle-version 8.4

if [ -f "gradlew" ]; then
    echo "✓ Gradle wrapper generated successfully!"
    echo ""
    echo "You can now build with:"
    echo "  ./gradlew assembleDebug"
    echo ""
else
    echo "❌ Failed to generate gradle wrapper"
    exit 1
fi
