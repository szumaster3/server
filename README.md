# 2009scape Fork

This project is a fork of [2009scape](https://gitlab.com/2009scape/2009scape).

---

### Requirements

- **Java 11**: Download from [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or [AdoptOpenJDK](https://adoptium.net/temurin/releases/?version=11).
- **IntelliJ IDEA**: Download from [JetBrains](https://www.jetbrains.com/idea/download/).

> _For Windows users:_ Enable Developer Mode in Windows settings before proceeding.

---

### Fork & Clone

1. Fork the repository on GitLab to your own account.
2. Clone **your forked repository** to your local machine:
   ```bash
   git clone <your-fork-ssh-or-https-url>
   ```

---

### Import Project in IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Select `File` > `Open...`
3. Navigate to the folder where you cloned the repository (project root).
4. Click `OK` to open the project.
5. IntelliJ should detect the `pom.xml` file and import the Maven project automatically.
6. Set **Project SDK** to Java 11 or higher if not set automatically.

---

### GitLab Setup

- If you don’t have an SSH key yet, generate one:
  ```bash
  ssh-keygen -t ed25519 -C "<your-email-or-comment>"
  ```
- Add your SSH public key to your GitLab account.
- Configure your Git identity:
  ```bash
  git config --global user.name "Your Name"
  git config --global user.email "your_email@example.com"
  ```

---

### Build

From the root of the project directory, run:

```bash
mvn clean install
```

---

### Run

Run the project using the Maven exec plugin:

```bash
mvn exec:java -f pom.xml
```

---

### License

This project is licensed under the **AGPL-3.0**. See the [LICENSE](./LICENSE) file or [gnu.org](https://www.gnu.org/licenses/agpl-3.0.html) for details.
