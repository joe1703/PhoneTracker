# Build Validation Guide

Before pushing to GitHub or building locally, run the validation script to catch issues early!

## 🚀 Quick Start

### Mac / Linux:
```bash
bash validate-build.sh
```

### Windows:
```bash
validate-build.bat
```

---

## What Does It Check?

The validation script checks:

1. **Java Version** - Ensures Java 17+ is installed
2. **Gradle** - Verifies gradle wrapper exists
3. **build.gradle.kts** - Checks Java/Kotlin versions are set to 21
4. **gradle.properties** - Ensures no unsupported JVM options (like MaxPermSize)
5. **Source Files** - Verifies all required files exist
6. **Git Status** - Checks for uncommitted changes
7. **Android SDK** - Confirms SDK levels are properly configured

---

## Workflow

```
1. Make changes to code
   ↓
2. Run validation script
   ↓
3. If issues found → Fix them
   ↓
4. If validation passes → Build or push
```

---

## Example Output

### ✅ Success:
```
✅ No critical issues found!
Ready to build! You can now run:
  ./gradlew assembleDebug
```

### ❌ Error:
```
❌ Found 1 critical issue(s)
ERROR: Java 17+ required (you have Java 11)
Fix the issues above before building
```

---

## Exit Codes

- **0** = Success (no critical issues)
- **1** = Failure (critical issues found)

---

## Integration with Git

You can run validation automatically before committing:

```bash
# Run validation before every commit
bash validate-build.sh && git push origin main
```

---

## Troubleshooting

**Script says "Java not found"**
- Install Java 17+ (see BUILD_LOCALLY.md)

**Script says "Gradle not found"**
- Run: `bash setup-gradle.sh`

**Script says "MaxPermSize found"**
- Run: `git pull origin main` (should be fixed)

---

## Next Steps

If validation passes:

### Option 1: Build Locally
```bash
./gradlew assembleDebug
```
APK will be at: `app/build/outputs/apk/debug/app-debug.apk`

### Option 2: Push to GitHub
```bash
git push origin main
```
GitHub Actions will build automatically

---

**Questions?** Check the main README.md or BUILD_LOCALLY.md!
