# 🚀 Automated GitHub Setup Scripts

Choose your operating system below and run the script. It will do everything for you!

---

## 🐧 **Linux / Mac Users**

### Step 1: Open Terminal
- **Mac**: Press `Cmd + Space`, type `Terminal`, press Enter
- **Linux**: Press `Ctrl + Alt + T` or open Terminal from applications

### Step 2: Navigate to Project Folder
```bash
cd phone_usage_tracker
```

### Step 3: Run the Script
```bash
bash push-to-github.sh
```

### Step 4: Follow the Prompts
The script will ask for:
- Your GitHub username
- Repository URL (you can leave blank - it will auto-generate)
- Your name
- Your email

**Done!** ✅

---

## 🪟 **Windows Users (3 Options)**

### **Option 1: Batch Script (Simplest)**

1. Open `File Explorer` and navigate to the `phone_usage_tracker` folder
2. Right-click in the empty space → **Open in Terminal** (or **Open in PowerShell**)
3. Run this command:
```bash
push-to-github.bat
```

4. Follow the prompts

---

### **Option 2: PowerShell Script (Modern)**

1. Open PowerShell (search "PowerShell" in Start menu)
2. Navigate to the project:
```powershell
cd C:\Users\YourUsername\phone_usage_tracker
```

3. Run this command:
```powershell
Set-ExecutionPolicy -ExecutionPolicy Bypass -Scope Process
.\push-to-github.ps1
```

4. Follow the prompts

---

### **Option 3: Command Prompt**

1. Open Command Prompt (`Win + R`, type `cmd`, press Enter)
2. Navigate to the project:
```bash
cd C:\Users\YourUsername\phone_usage_tracker
```

3. Run:
```bash
push-to-github.bat
```

4. Follow the prompts

---

## 📋 **Before You Run Any Script**

You **MUST** do this first:

1. Go to [github.com](https://github.com)
2. Sign in (or create free account)
3. Click **+** (top right) → **New repository**
4. Name: `phone-usage-tracker`
5. Choose **Public**
6. Click **Create repository**
7. **Copy the repository URL** (you'll paste it in the script)

---

## ✅ **After Script Completes**

1. Go to: `https://github.com/YOUR_USERNAME/phone-usage-tracker`
2. Click **Actions** tab
3. Watch for green checkmark ✓ (takes 5-10 minutes)
4. Click completed workflow
5. Download **app-debug.apk** from Artifacts
6. Install on your Android phone

---

## ❌ **Troubleshooting**

### "Command not found: git"
- Install Git from https://git-scm.com/download

### "Permission denied" (Mac/Linux)
- Make sure script is executable:
```bash
chmod +x push-to-github.sh
```

### "Git is not configured"
- Make sure you provided your name and email when the script asked

### "Repository already exists"
- The script tried to create a remote that already exists
- This is OK - it will update the existing remote

### Build Failed on GitHub Actions
- Check the Actions tab for error messages
- Make sure repository is PUBLIC

---

## 🎯 **That's It!**

Just run the script, answer the questions, and wait for your APK to be built! 🎉

Questions? Check the README.md or QUICK_START.md files.
