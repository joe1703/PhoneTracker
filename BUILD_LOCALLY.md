# Building Locally vs GitHub Actions

## 🎯 Recommended: Use GitHub Actions (No Setup Required!)

The easiest way to get your APK is to **let GitHub build it for you**:

1. Push code to GitHub
2. Go to Actions tab
3. Wait for build to complete
4. Download APK

**No software installation needed!** ✅

---

## 💻 Alternative: Build Locally (Requires Setup)

If you want to build on your computer, you need to install these first:

### **Step 1: Install Java 11+**

**Windows:**
- Download from: https://www.oracle.com/java/technologies/downloads/
- Or use: `choco install openjdk11`

**Mac:**
- Use: `brew install openjdk@11`

**Linux:**
- Use: `sudo apt-get install openjdk-11-jdk`

Verify:
```bash
java -version
```

### **Step 2: Install Gradle**

**Windows:**
- Download from: https://gradle.org/releases/ (latest version)
- Extract and add to PATH
- Or use: `choco install gradle`

**Mac:**
- Use: `brew install gradle`

**Linux:**
- Use: `sudo apt-get install gradle`

Verify:
```bash
gradle -version
```

### **Step 3: Setup Gradle Wrapper**

```bash
bash setup-gradle.sh
```

This will generate the gradle wrapper files.

### **Step 4: Build the APK**

```bash
./gradlew assembleDebug
```

Your APK will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## ⚠️ Troubleshooting

**"java command not found"**
- Install Java (see Step 1 above)

**"gradle command not found"**
- Install Gradle (see Step 2 above)

**Build still fails**
- Use GitHub Actions instead (no issues!)
- Go to your repository and check Actions tab

---

## 🏆 Best Practice

1. **First time?** Use GitHub Actions
2. **Want to develop?** Install Java + Gradle, then build locally
3. **Ready to release?** GitHub Actions creates APK automatically on each push

---

## Quick Reference

```bash
# Setup (one time)
bash setup-gradle.sh

# Build
./gradlew assembleDebug

# Clean build
./gradlew clean assembleDebug

# View build output
./gradlew assembleDebug --stacktrace
```

---

**Questions?** Check the QUICK_START.md for GitHub Actions guide!
