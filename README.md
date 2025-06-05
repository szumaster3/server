This project is a fork of [2009scape](https://gitlab.com/2009scape/2009scape).

Download: The latest [single-player](https://github.com/szumaster3/530-windows/zipball/releases) zipball.

___

#### Requirements

- **Java 11**: You can download it from [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
  or [AdoptOpenJDK](https://adoptium.net/temurin/releases/?version=11).
- **IntelliJ IDEA**: Download and install from [JetBrains](https://www.jetbrains.com/idea/download/).

_For Windows users_ - Enable developer mode in Windows settings first.

#### Fork & Clone

1. Fork the repository via GitLab.
2. Clone your fork:
   ```
   git clone <your-fork-url>
   ```
3. Import in IntelliJ IDEA:
   File > New > Project from Version Control > Git > Paste URL > Clone.

#### GitLab Setup

- Generate SSH key if needed:
  ```
  ssh-keygen -t ed25519 -C "<comment>"
  ```
- Add SSH key to GitLab account.
- Configure Git:
  ```
  git config --global user.name "Your Name"
  git config --global user.email "your_email@example.com"
  ```

#### Build

From the project root:
```
mvn clean install
```

#### IntelliJ Configuration

- Ensure Maven is set up correctly.
- Set Project SDK to Java 11 or higher.
- Refresh Maven projects.

#### Run

Use Maven exec plugin:
```
mvn exec:java -f pom.xml
```

#### License

This project is under the **AGPL-3.0**. See the [LICENSE](./LICENSE) file or [gnu.org](https://www.gnu.org/licenses/agpl-3.0.html) for details.