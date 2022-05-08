# :chess_pawn: Tablut Challenge 2022 - Beluga :cat:

Project for the AI Challenge in Foundaments of Artificial Intelligence M at University of Bologna.
Link for the competition [@Tablut Competition](http://ai.unibo.it/games/boardgamecompetition/tablut).
The aim of the project is to create a player for the game of Tablut.


## Requirements to run the player

The project requires a working Java Environment. You need to have JDK >= 11.
From Ubuntu/Debian console, you can install it with these commands:
```bash
sudo apt update
sudo apt install openjdk-11-jdk -y
```

## Usage

You can run the player with:
```bash
cd Executables
java -jar Beluga.jar <white|black> [<timeout>] [<server_address>]
```

If not specified, the default values are **60** for `timeout` and **localhost** for `server_address`.

Alternatively, you can run the player with the bash script provided:

```bash
./runmyplayer  <white|black> [<timeout>] [<server_address>]
```
With the same default values written above.

For convenience, we provide the Server. You can find information about it in the [main repository](https://github.com/AGalassi/TablutCompetition)

## Authors
Lorenzo Di Palma, Giovanni Gheriglio

<img align="center" width="300" height="300" src="src/it/unibo/ai/didattica/competition/tablut/gui/resources/Beluga.jpeg">
