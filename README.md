# 🎮 Pixel Smash

## Overview
**Pixel Smash** is a 2D brawler game inspired by classics like **Super Smash Bros.** 
It was developed for the course "Interfacce grafiche e programmazione ad eventi" @ Università della Calabria by me and two other colleagues. It was previously hosted on Gitlab and I uploaded it here too.
The objective is to push your opponents out of the arena while inflicting as much damage as possible. The player who has the fewest deaths and the least damage when the timer expires is declared the winner.

The game features both **Single Player** and **Multiplayer** modes and includes a **Map Editor** to create custom arenas.

## ✨ Game Modes

### 1. **Single Player**
The single-player mode offers two gameplay options:
- **Brawl**: Select your opponents and the game map.
- **Boss Rush**: Face off in a sequence of 3 one-on-one duels against stronger AI opponents across 3 different maps.

The AI employs a dynamic algorithm to determine the best platform to move to and the character to target, alternating between aggressive attacks and defensive behavior.

### 2. **Multiplayer**
- **Host**: Create a game and share your IP address with your friend to join. You can select the map, character, and control the game time.
- **Join**: Enter the host's IP address to join the game, choose your character, and wait for the connection.

### 3. **Map Editor**
Create your custom arenas by dragging platforms and selecting backgrounds. The custom map will be available under the "Custom" option in the game mode selection screen.

---

## 🎮 Characters

- **Guy**: Long-range attacker with average armor and speed.
- **Naigt**: Close-range fighter with high armor but low speed.
- **Ningià**: Close-range fighter with high speed but low armor.
- **Gerry**: Long-range attacker with low armor and medium-high speed.

### Controls & Gameplay
- **Movement**: Move left, right, jump, double jump, and wall-jump.
- **Attack**: Perform simple and charged attacks (hold the attack button to unleash a heavy attack).
- **Shield**: Use a shield to minimize incoming damage.
- **Power-Ups**: Collect power-ups like Health, Smash, Speed, and Immunity during gameplay.

---

## 🛠️ Installation and Setup

### Prerequisites
- **Java Development Kit (JDK) 8 or above**: [Install JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
- **Maven**: [Install Maven](https://maven.apache.org/install.html)

### Clone the Repository
```bash
git clone https://github.com/lanor-97/Pixel-Smash.git
cd Pixel-Smash
```

### Build the project
```bash
mvn clean install
```

### Running the game
```bash
java -jar target/pixelsmash.jar
```

