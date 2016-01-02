-------------------------------------
      CONNECT-4 AI AND SOLVER
-------------------------------------

Abstract
============
This is an old school project, from early 2014. The goal was to program an AI capable of predicting the winner of a two-player perfect information game with finite horizon, from any starting board, as well as to implement an interactive automated player. In this case, it is applied to the game Connect-4, as some game knowledge is necessary (e.g. to evaluate an heuristic on on state nodes, or to determine if there is a winner). 
Most of the class and variable names are in french.


Code organization
============

The source code is organized as follows:
- Jeu.java holds the class that handles the game board, and all the associated methods
- Solver.java holds various algorithms, of increasing difficulty, to solve the game (minmax, alpha-beta, iterative deepening, etc.)
- Puiss4.java is the main class, and holds various tests
- the other files are utility classes


Data organization
============

The data is contained in boards/ and consists of text files representing starting boards, from which to determine the winner.