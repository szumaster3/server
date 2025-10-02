<div align="center">
A fork of <a href="https://gitlab.com/2009scape/2009scape">2009Scape</a> with AGPL-3.0 licensing.
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

# Prerequisites

Before setting up the project, ensure the following:

- **Java 11** – Download from [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [Adoptium](https://adoptium.net/temurin/releases/?version=11)
- **IntelliJ IDEA** – Download from [JetBrains](https://www.jetbrains.com/idea/download/)

> Windows users: Enable **Developer Mode** before proceeding.

# Fork & Clone

1. Fork the repository on GitLab.
2. Clone your fork:

```bash
git clone <your-fork-ssh-or-https-url>
```

3. Navigate into the folder:

```bash
cd <your-project-folder>
```

# Import Project in IntelliJ

1. Open IntelliJ IDEA.
2. Select `File` > `Open...` and choose the project root.
3. IntelliJ should detect `pom.xml` and import the Maven project automatically.
4. Set Project SDK to **Java 11** or higher.

# Setup Git & SSH

- Generate SSH key if you don't have one:

```bash
ssh-keygen -t ed25519 -C "<example@example.eu>"
```

- Add your public key to GitLab.
- Configure Git:

```bash
git config --global user.name "example"
git config --global user.email "example@example.eu"
```

# Build Project

Run from the project root:

```bash
mvn clean install
```

This compiles and packages all files.

# Run Project

```bash
mvn exec:java -f pom.xml
```

> Tip: Run via IntelliJ by right-clicking `pom.xml` > `Run 'exec:java'`.

# Contributing

1. Fork the repository.
2. Create a feature branch:

```bash
git checkout -b feature/my-feature
```

3. Commit changes:

```bash
git commit -am "Changes"
```

4. Push and open a merge request.

# Troubleshooting

- **Java version mismatch**: `java -version` should be 11+.
- **Maven issues**: Check with `mvn -version`.
- **IDE errors**: Reimport Maven project or invalidate caches (`File > Invalidate Caches / Restart`).
- **SSH issues**: Ensure public key is added to GitLab.

# License

This project is licensed under **AGPL-3.0**. See [LICENSE](./LICENSE) or [gnu.org](https://www.gnu.org/licenses/agpl-3.0.html).
