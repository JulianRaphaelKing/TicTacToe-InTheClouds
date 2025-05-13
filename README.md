
# ☁  Tic-Tac-Toe in the Clouds | README

A cozy **JavaFX Tic-Tac-Toe game** that elevates the classic game with calm aesthetics, hover effects,
and human-like AI that simulates the creators tic-tac-toe strategy.

---

## Features
-  **2 Ways to play:** Player vs Player or Player vs Computer 
-  Hover previews and cozy cloud backgrounds.
-  Background music with mute toggle and fast-mode when hovering start.
-  Score tracker with export to `scores_log.txt`.
-  Minimalist frameless window with drag support.
-  Dynamic win screens and tie screen with custom visuals.

---

##  Tech Stack
- **Java 17+**
- **JavaFX 20+**
- **Maven (for building)**
- **IntelliJ IDEA (recommended IDE)**

---

##  Project Structure

```
src/
 └── main/
     ├── README.md
     ├── LICENSE
     ├── java/
     │    └── com.example.portfilioproject/
     │         ├── FileAssets.java       # Manages and preloads all image assets (images).
     │         └── TicTacToeApp.java     # Core logic, UI, and game loop.
     └── resources/
         └── com.example.portfilioproject/
              └── images/
                   ├── bkgMusic.wav
                   ├── bkgMusicSpeed.wav
                   ├── backgrounds/
                   │     ├── bkgClouds.gif
                   │     ├── bkgCloudsHover.gif
                   │     ├── bkgDay.png
                   │     ├── bkgDraw.png
                   │     ├── bkgGridLayout.png
                   │     ├── bkgImage675x800.png
                   │     ├── bkgNight.png
                   │     ├── cloudsBlurred.png
                   │     ├── Grid.png
                   │     ├── Line.png
                   │     └── MenuStageBkg.png
                   └── buttons/
                         ├── btnClose.png
                         ├── btnCloseHover.png
                         ├── btnExportScores.png
                         ├── btnExportScores_Hovered.png
                         ├── btnMenu.png
                         ├── btnMenuClose.png
                         ├── btnMenuClose_Hovered.png
                         ├── btnMenuHover.png
                         ├── btnMin.png
                         ├── btnMinHover.png
                         ├── btnMute.png
                         ├── btnMuted.png
                         ├── btnNull.png
                         ├── btn0.png
                         ├── btnPlayerVSPlayer.png
                         ├── btnResetScores.png
                         ├── btnResetScores_Hovered.png
                         ├── btnRestart.png
                         ├── btnRestartHover.png
                         ├── btnStart.png
                         ├── btnStartHover.png
                         └── btnVSComputer.png
```

---

## Running the Application

### Prerequisites
- Java SDK 17 or later
- JavaFX SDK configured in your IDE (with VM options set up properly)
- Maven installed (optional if you just want to run in IntelliJ)

### How to Run
1. Clone or download the project.
2. Open in IntelliJ IDEA.
3. Ensure JavaFX SDK is correctly configured.
4. Run the `TicTacToeApp` class.

#### IntelliJ VM Options Example:
```
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml,javafx.media
```

---
##  Notes
- Computer AI includes random imperfections to feel more human.
- For the smoothest experience, keep your JavaFX libraries updated.

---

##  Controls

| Action               | Description                                    |
|----------------------|------------------------------------------------|
| **Hover over a cell**     | Preview X or O                                |
| **Click Start**           | Begin the game                                |
| **Click Menu**            | View scores, toggle game mode, export scores  |
| **Click Export Scores**   | Save score log with timestamp to `scores_log.txt` |
| **Click Change Mode**     | Toggle between PvP and Player vs AI mode      |
| **Click Mute**            | Toggle music on/off                           |

---

##  Score Export Format

```
X Wins: 3
O Wins: 2
Timestamp: 2025-05-12 04:42 PM
----------------------------
```
---

##  Assets Overview

Managed through the **FileAssets** class and neatly organized into:
- **Backgrounds** (`backgrounds/`)
- **Buttons & Icons** (`buttons/`)
- **Text Labels** (`texts/`)

---

## Limitations
- The Computer's AI is basic and relies on hardcoded human-like patterns, which can become predictable over time.

- The UI is fixed to a set resolution (675x800) and doesn't dynamically scale to different screen sizes.

- The score is session-based, while it can be exported to scores_log.txt, it's not persistent or saved automatically.

---

## Future Plans
| Feature                         | Description                                                                                                          |
|---------------------------------|----------------------------------------------------------------------------------------------------------------------|
| **Dynamic UI Scaling**              | **Make** the game fully responsive, resizeable, and scalable for different screens.                                      |
| **Score Persistence & Leaderboard** | **Implement** automatic score saving and add an in-app leaderboard to track lifetime wins and stats.                     |
| **Custom Soundtrack Picker**        | **Allow** players to select their own music to personalize the game experience.                                          |
| **Smarter AI**      | **Upgrade** AI and explore ML-based models for adaptive, human-like play. Also containing a variety of difficulty levels |
| **Dynamic Themes**          | **Introduce** multiple UI skins, including different aesthetics.                                                         |

---

## License

This project is licensed under the [MIT License](LICENSE). Feel free to use, modify, and distribute it for personal or commercial purposes. See the full license text in the `LICENSE` file for more details.

---

## Author - Raphael King

**Student developer**

I’m a student developer passionate about learning and creating as many creative projects as I can. I’m always working on a side project, exploring new skills and bringing creative ideas to life through code.
Tic-Tac-Toe in the Clouds is part of my journey exploring game design, UI/UX, and creative coding—bringing classic games to life with a cozy, personal touch.

---