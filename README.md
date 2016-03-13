# PinochleAI
Card game AI uses Monte Carlo methods to optimize Pinochle trick play

Server written in java allows players to play pinochle against AI and Human players.

Options:
Human Players
Random AI
Monte Carlo AI
Genetic AI


**** ORDER OF COMMUNICATIONS WITH SERVER ****

::CONNECTING TO THE GAME::
Connect to server
Send server 'PlayerProfile' (JSON) to "log in"
Server sends active 'game' list (JSON) Game[]
Client sends Server 'PlayerProfile' with gameID set
Server Sends 'Game' (JSON) of game connected to

::WAITING FOR GAME TO START::
Server sends game status.
Client must wait til game status is "ready"

::PLAYING OUT THE HAND::
Server sends a 'Hand' (JSON) of dealt cards
Client sends a Max Bid (int-JSON) indicating Max bid
Server responds with bid winner (int-JSON)
Bid winner reponds with trump suit (int-JSON)
Server responds with 'GameState' indicating bid winner and Trump
LOOP:
--Server queries next player for play
--Client responds with Card played 
--Server updates all players with Card played

::POST-ROUND::
If WINNING_SCORE has not been reached, 'Hands' are re-dealt
Otherwise players are given the chance to RESTART
