#!/bin/bash

# Phone Usage Tracker - Build Validation Script
# This script checks for common build issues before pushing to GitHub

echo "=========================================="
echo "Build Validation Script"
echo "=========================================="
echo ""

ISSUES=0
WARNINGS=0

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo "[1/7] Checking Java version..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep -oE 'version "[0-9.]*' | grep -oE '[0-9]*' | head -1)
    echo "  Java version: $JAVA_VERSION"

    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo -e "${RED}  ❌ ERROR: Java 17+ required (you have Java $JAVA_VERSION)${NC}"
        ISSUES=$((ISSUES + 1))
    else
        echo -e "${GREEN}  ✓ Java version OK${NC}"
    fi
else
    echo -e "${RED}  ❌ ERROR: Java not found${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

echo "[2/7] Checking Gradle..."
if [ ! -f "gradlew" ]; then
    echo -e "${YELLOW}  ⚠ gradlew not found, trying to generate...${NC}"
    if command -v gradle &> /dev/null; then
        gradle wrapper --gradle-version 8.4 > /dev/null 2>&1
        if [ -f "gradlew" ]; then
            echo -e "${GREEN}  ✓ Gradle wrapper generated${NC}"
        else
            echo -e "${RED}  ❌ ERROR: Failed to generate gradle wrapper${NC}"
            ISSUES=$((ISSUES + 1))
        fi
    else
        echo -e "${RED}  ❌ ERROR: Gradle not found${NC}"
        ISSUES=$((ISSUES + 1))
    fi
else
    echo -e "${GREEN}  ✓ gradlew found${NC}"
fi
echo ""

echo "[3/7] Checking build.gradle.kts files..."
if [ -f "app/build.gradle.kts" ]; then
    if grep -q "JavaVersion.VERSION_21" app/build.gradle.kts; then
        echo -e "${GREEN}  ✓ Java 21 configured${NC}"
    else
        echo -e "${YELLOW}  ⚠ WARNING: Java version not set to 21${NC}"
        WARNINGS=$((WARNINGS + 1))
    fi

    if grep -q "jvmTarget = \"21\"" app/build.gradle.kts; then
        echo -e "${GREEN}  ✓ Kotlin JVM target set to 21${NC}"
    else
        echo -e "${YELLOW}  ⚠ WARNING: Kotlin jvmTarget not set to 21${NC}"
        WARNINGS=$((WARNINGS + 1))
    fi
else
    echo -e "${RED}  ❌ ERROR: app/build.gradle.kts not found${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

echo "[4/7] Checking gradle.properties..."
if [ -f "gradle.properties" ]; then
    if grep -q "MaxPermSize" gradle.properties; then
        echo -e "${RED}  ❌ ERROR: MaxPermSize found in gradle.properties (not supported in Java 21)${NC}"
        ISSUES=$((ISSUES + 1))
    else
        echo -e "${GREEN}  ✓ No unsupported JVM options${NC}"
    fi
else
    echo -e "${RED}  ❌ ERROR: gradle.properties not found${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

echo "[5/7] Checking source files..."
if [ -f "app/src/main/java/com/example/phoneusagetracker/MainActivity.kt" ]; then
    echo -e "${GREEN}  ✓ MainActivity found${NC}"
else
    echo -e "${RED}  ❌ ERROR: MainActivity not found${NC}"
    ISSUES=$((ISSUES + 1))
fi

if [ -f "app/src/main/AndroidManifest.xml" ]; then
    echo -e "${GREEN}  ✓ AndroidManifest found${NC}"
else
    echo -e "${RED}  ❌ ERROR: AndroidManifest not found${NC}"
    ISSUES=$((ISSUES + 1))
fi

if [ -f "app/src/main/res/layout/activity_main.xml" ]; then
    echo -e "${GREEN}  ✓ Layout files found${NC}"
else
    echo -e "${RED}  ❌ ERROR: Layout files not found${NC}"
    ISSUES=$((ISSUES + 1))
fi
echo ""

echo "[6/7] Checking Git status..."
if git rev-parse --git-dir > /dev/null 2>&1; then
    UNCOMMITTED=$(git status --porcelain | wc -l)
    if [ "$UNCOMMITTED" -gt 0 ]; then
        echo -e "${YELLOW}  ⚠ WARNING: You have $UNCOMMITTED uncommitted changes${NC}"
        WARNINGS=$((WARNINGS + 1))
        git status --short
    else
        echo -e "${GREEN}  ✓ All changes committed${NC}"
    fi
else
    echo -e "${YELLOW}  ⚠ Not a git repository${NC}"
fi
echo ""

echo "[7/7] Checking Android SDK compatibility..."
if grep -q "compileSdk = 34" app/build.gradle.kts; then
    echo -e "${GREEN}  ✓ SDK 34 configured${NC}"
else
    echo -e "${YELLOW}  ⚠ WARNING: compileSdk not set to 34${NC}"
    WARNINGS=$((WARNINGS + 1))
fi

if grep -q "minSdk = 24" app/build.gradle.kts; then
    echo -e "${GREEN}  ✓ Min SDK 24 configured${NC}"
else
    echo -e "${YELLOW}  ⚠ WARNING: minSdk not set to 24${NC}"
    WARNINGS=$((WARNINGS + 1))
fi
echo ""

echo "=========================================="
echo "Validation Results"
echo "=========================================="
echo ""

if [ "$ISSUES" -eq 0 ]; then
    echo -e "${GREEN}✅ No critical issues found!${NC}"
else
    echo -e "${RED}❌ Found $ISSUES critical issue(s)${NC}"
fi

if [ "$WARNINGS" -gt 0 ]; then
    echo -e "${YELLOW}⚠ Found $WARNINGS warning(s)${NC}"
fi

echo ""
if [ "$ISSUES" -gt 0 ]; then
    echo -e "${RED}Fix the issues above before building${NC}"
    exit 1
else
    echo -e "${GREEN}Ready to build! You can now run:${NC}"
    echo "  ./gradlew assembleDebug"
    echo ""
    echo "Or push to GitHub:"
    echo "  git push origin main"
    exit 0
fi
