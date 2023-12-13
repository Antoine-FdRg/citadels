# projet2-ps-23-24-citadels-2024-b

## Description

projet2-ps-23-24-citadels-2024-b is a console-based card game implemented in Java and based on
the [Citadel card game](https://www.trictrac.net/jeu-de-societe/citadelles-1). The game is designed for multiple
players, each represented by a `Player` object.

The game revolves around the concept of districts, represented by the `District` class. Each player can place a district
card during their turn, which costs them a certain amount of gold. The state of the game, including the players' gold
and the districts they have placed, is displayed in the console.

The game is initiated and controlled by the `Game` class, which is created and run in the `Launcher` class's main
method. The number of players is set at the start of the game, as seen in the `Game` constructor.

The `IView` interface and its implementation `Cli` are responsible for displaying the game's state in the console. They
display the current round, the district a player places, the remaining gold of the player, and the winner of the game
with their score.

## Installation

This project uses Maven for dependency management. To install the project, follow these steps:

1. Clone the repository: `git clone https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-b.git`
2. Navigate to the project directory: `cd projet2-ps-23-24-citadels-2024-b`
3. Install the dependencies: `mvn install`

## Usage

To run the project, follow these steps:

1. Navigate to the project directory: `cd projet2-ps-23-24-citadels-2024-b`
2. Run the project: `mvn exec:java`

## Contributing

We welcome contributions! Please see our [Contributing Guide](CONTRIBUTING.md) for more details.

## Contact

If you have any questions, feel free to reach out any contributors of this repository.