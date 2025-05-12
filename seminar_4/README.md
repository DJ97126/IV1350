## Getting Started

This project was created using VS Code, without Maven and Gradle. The unit testing is implemented in similar way (i.e. parallel folder structures). The `lib` folder requires the necessary library - [`JUnit 5 (1.12.2)`](https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.12.2/junit-platform-console-standalone-1.12.2.jar) to work with `Test Runner for Java`. `Test Runner for Java` is a VS Code extension that's included in `Extension Pack for Java`.

## Folder Structure

The workspace contains four folders, where:

- `.vscode`: maintains some project settings
- `src`: maintains source files
- `test`: maintains unit tests
- `lib` (local): maintains dependencies (put JUnit 5 `.jar` file here)
