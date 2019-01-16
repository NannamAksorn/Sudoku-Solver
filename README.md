# Sudoku-Solver
This is a rule-based Sudoku Solver project, built on java, which can solve most problem with unique solution. It supported text file input with multiple problem, and output graphical solution with performance report. The project is designed to be simple and clean. There are still some remaining solving techniques left to implement.

![solution](https://user-images.githubusercontent.com/44284362/51250098-f91b1a00-19c7-11e9-8d28-2d6cd96d8689.JPG)
## Getting Started
If you do not have a Java Development Kit, you need to Download and install a compatible [JDK](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
### Setting JDK path
- Change  ```java.home``` in [settings.json](.vscode/settings.json) in case of using VS Code
- Set ```JDK_HOME```  environment variable
- Set ```JAVA_HOME``` environment variable
### Usage
You can change the configuration in [Solver.java](src/Solver.java)
```
public class Solver{	

  public static final boolean SHOW_GUI = true;

	public static final boolean SHOW_STATUS = false;

	public static final boolean TYPE_INPUT = false;
	public static final boolean RANDOM_FILE = false;
  
	public static final String FILE_NAME = "testdata" + ".txt";
```
With ```TYPE_INPUT``` set to ```True``` you need to type input in the console

datasets are located in [dataset](dataset) folder
## Running test
 Just run in your favorite IDE
### Sample Input
 - The first line is the total number of problem. In this case there are 3 puzzles, so just type ```3```
 - Next, there are 10 lines for each problem.
 - First line is the ```ID of the problem```, followed by the 9 lines of ```content of the puzzle```.
 - The content consists of digits from 1 to 9 and ```-``` for a blank cell. 
```
3
1
-6---54-8
-8147----
5----8729
2-9354---
--8---1--
-7486----
----4-39-
-36--9--7
---12-6-5
2
7-1-----3
-56-7--9-
--8461---
-49---87-
-6--4----
27-95-46-
-3-8-5--7
----1-52-
--2--6-8-
3
1--7354--
8-41---3-
-56---9--
----23--6
-12--4-9-
--38---7-
2--5-6---
7---9--81
-984--3-5
```
### Sample Output
 - First line of output is the ```ID of problem```
 - Second line is the ```number of blank cell``` remain unsolve.
 - Next 9 line is the solved ```solution``` for the problem.
 - If the program run into any error, it will just print ```Error```
```
...
2
0
721589643
456273198
398461752
149632875
865147239
273958461
634825917
987314526
512796384
total_error = 0
total_blank = 0
total_solve = 2/3
3
0
...
```
## Some knwon issues
X-Wing function can sometime causes wrong answer when call multiple times. But usually it is needed for just once or twice per problem.

## Acknowledgements
Special thanks to [Kyubyong Park](https://www.kaggle.com/bryanpark/sudoku/home), [Timo Mantere & Janne Koljonen](http://lipas.uwasa.fi/~timan/sudoku/) for the datasets

## License
This project is licensed under the [MIT](https://opensource.org/licenses/MIT) License


  

