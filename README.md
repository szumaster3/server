<div align="center">
  <img src="https://imgur.com/yGvSk2X.png" alt="Project banner" />
</div>

<div align="center">
  A fork of <a href="https://gitlab.com/2009scape/2009scape">2009Scape</a> licensed under <strong>AGPL-3.0</strong>.
</div>

<div align="center">
  <a href="#prerequisites">Prerequisites</a> •
  <a href="#fork--clone">Fork & Clone</a> •
  <a href="#import-project-in-intellij">Import Project</a> •
  <a href="#setup-git--ssh">Git & SSH</a> •
  <a href="#build-project">Build</a> •
  <a href="#run-project">Run</a> •
  <a href="#contributing">Contributing</a> •
  <a href="#license">License</a>
</div>

___

> [!TIP]
> **Play the game**
>
> The entire project is fully built and ready to play.
>
> [Click here](https://github.com/szumaster3/windows)

___

### Prerequisites

Before setting up the project, make sure you have the following installed:

- **Java 11**
    - https://adoptium.net/temurin/releases/?version=11
    - https://www.oracle.com/java/technologies/downloads/#java11

- **IntelliJ IDEA**
    - https://www.jetbrains.com/idea/download/

> **Windows users:** Enable **Developer Mode** before continuing.

---

### Fork & Clone

1. Fork the repository on GitLab.
2. Clone your fork:

```bash
git clone <your-fork-ssh-or-https-url>
```

3. Enter the project directory:

```bash
cd <your-project-folder>
```

---

### Import Project in IntelliJ

1. Open IntelliJ IDEA.
2. Select **File → Open…** and choose the project root.
3. IntelliJ will detect `pom.xml` and import the Maven project.
4. Set the **Project SDK** to **Java 11** or newer.

---

### Setup Git & SSH

Generate an SSH key if needed:

```bash
ssh-keygen -t ed25519 -C "example@example.eu"
```

Configure Git:

```bash
git config --global user.name "Your Name"
git config --global user.email "example@example.eu"
```

---

### Build Project

```bash
mvn clean install
```

---

### Run Project

```bash
mvn exec:java -f pom.xml
```

---

### Contributing

```bash
git checkout -b feature/my-feature
git commit -am "Describe your changes"
git push
```

---

### Troubleshooting

- Java version:
```bash
java -version
```

- Maven version:
```bash
mvn -version
```

---

### License

AGPL-3.0  
See [`LICENSE`](LICENSE) or https://www.gnu.org/licenses/agpl-3.0.html
