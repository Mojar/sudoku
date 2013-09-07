Sudoku Solver
=============
Originally created for Comp 250 assignment 5

Run the program:

1. Compile SudokuSolver.java
2. type:   java SudokuSolver [puzzle path]

Changelog:
- March. 25, 2013
The 3x3 solve was completed in 2 days (80% one evening, the remaining here and there the next day).

Bugs:
- SudokuSolver.java must be run as "java SudokuSolver [puzzle path]" (ideally puzzle path could be entered afterwards and multiple puzzles solved at once).

Todo: 
- Experiment with Heuristics for 4x4 and 5x5 solves
- Add an interface so the awkward .txt files aren't necessary (choose between loading .txt file or inputting numbers in a displayed grid)
- Perhaps allow someone to create a sudoku in the displayed grid and it can determine the difficulty level (Sudoku generator)
- Refactor into separate methods (rather than one giant solve method)
