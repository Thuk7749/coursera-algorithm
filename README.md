# Algorithm, part I (and soon, part II)

## Introduction

- This repository of mine (_Phan Hong Thuc_), is to record my learning progress about algorithms, specifically, from the course [Algorithm, part I](https://www.coursera.org/learn/algorithms-part1) and [Algorithm, part II](https://www.coursera.org/learn/algorithms-part2) (soon, hope so) on Coursera.

- This repository is not meant to be a complete/perfect solution for the course (or for others to reference/consult), but rather a collection of _**my notes**_, code implementations, and exercises that I find useful during my learning journey.

## Prerequisites

- Visual Studio Code with "Extension Pack for Java" installed
- JDK 8 or higher (with proper `JAVA_HOME` set in your system environment)
- Git (for cloning the repository)

## Quick Start

1. Clone this repository [here](https://github.com/Thuk7749/coursera-algorithm.git): `git clone https://github.com/Thuk7749/coursera-algorithm.git`
2. Open the project in Visual Studio Code `code coursera-algorithm`
3. Ensure Java extension recognizes the project structure
4. Try running a simple example: `cd part-one/01_hello-world; .\run_local.bat` (if you are using Windows PowerShell) or `cd part-one/01_hello-world && run_local.bat` (if you are using Windows Command Prompt).

## Repository Structure

```
coursera-algorithm/
├── .vscode/
│   ├── launch.json*
│   └── settings.json
├── class*/
│   └── ...
├── part-one/
│   ├── run_all.bat
│   ├── 00_topic-template/
│   │   ├── test/
│   │   |   └── MainTemplateTest.java
|   |   ├── MainTemplate.java
|   |   ├── run_local.bat
│   ├── 01_hello-world/
|   |   ├── test/
│   │   |   └── HelloWorldTest.java
│   │   ├── HelloWorld.java
│   │   └── run_local.bat
│   └── ...
├── slides/
├── .gitignore
├── README.md
```

(_The file/folder marked with \* should be generated while working with Visual Studio Code, and be ignored by Git_)

- The `.vscode` directory contains configuration files for Visual Studio Code, including `launch.json` for debugging configurations (you should create it yourself) and `settings.json` for Java project settings.

- The `class` directory is where the compiled `.class` files are stored (if you compile the `.java` files), used to be run with VS Code "Run | Debug" button or "Run and Debug" feature.

- The `part-one` directory contains the code for the first part of the course, organized into "topic folders" (e.g., `01_hello-world`, `02_data-types`, etc.). Each topic folder contains:

  - A `test` folder with test files (if applicable).
  - A `.java` file with the main code implementation.
  - A `run_local.bat` file to run the code easily.

- The `slides` directory contains the slides for the course.

## Running the Code

- All the `.java` files here are in the default (unnamed) package (because the submit instructions on Coursera require this).

### Method 1: VS Code Run Button (Simplest)

- Open any `.java` file with a `main` method
- Click the "Run" button above `public static void main`
- **Limitation**: Cannot pass command-line arguments easily

### Method 2: VS Code Debug Configuration (Recommended for complex runs)

- Create/edit `.vscode/launch.json` with your specific configurations
- Use "Run and Debug" panel (Ctrl+Shift+D)
- **Advantage**: Full control over arguments and debugging

---

- Configuration example:

  ```json
  {
    "version": "0.2.0",
    "configurations": [
      {
        "type": "java",
        "name": "Debug BCP Client",
        "request": "launch",
        "mainClass": "BCPClient",
        "projectName": "coursera-algorithm_4cc8b159", // Project name may vary
        "args": "part-one/06_mergesort/input8.txt"
      }
    ]
  }
  ```

### Method 3: Batch Scripts (Most Flexible)

#### Using `run_all.bat`:

```cmd
cd part-one
run_all.bat
# Follow prompts to select folder and file
```

> - First, you'll select the "topic folder", then pick the `.java` file you want to run.
> - The script will compile and run your chosen `.java` file. You can enter command-line arguments if needed.
> - If a test file exists in the `test` subfolder with the same name as your `.java` file plus `Test` (for example, `HelloWorldTest.java` for `HelloWorld.java`), you can ask the script to also run those test cases automatically.

#### Using `run_local.bat`:

```cmd
cd part-one/01_hello-world
run_local.bat
# Runs files in current directory
```

> - This method makes it easier to include file names as command-line arguments, since you don't need to type the full file path.

## Extension guide

### To add a new topic folder

- Create a new folder in the `part-*` directory.
- Add the folder to the Java _classpath_, by add the folder name to the `settings.json` file in the `.vscode` folder, under the `java.project.sourcePaths` property.

### To add a `.jar` file to the project

- Just place the `.jar` file in the `part-*` directory, under the `lib` folder (e.g., `part-one/lib/your-library.jar`).

### Example `settings.json` file

```json
{
  "java.project.referencedLibraries": ["part-*/lib/*.jar"],
  "java.project.sourcePaths": [
    "part-one/00_topic-template",
    "part-one/00_topic-template/test", // The test folder should be listed here, too
    "part-one/01_hello-world",
    "part-one/01_hello-world/test",
    "part-one/02_union-find",
    "part-one/02_union-find/test",
    "part-one/03_algorithm-analysis",
    "part-one/03_algorithm-analysis/test",
    "part-one/04_stack-and-queue",
    "part-one/04_stack-and-queue/test",
    "part-one/05_elementary-sorts",
    "part-one/05_elementary-sorts/test",
    "part-one/06_mergesort",
    "part-one/06_mergesort/test"
  ],
  "java.project.outputPath": "class"
}
```
