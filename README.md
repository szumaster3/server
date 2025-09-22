This project is a **fork** of [2009Scape](https://gitlab.com/2009scape/2009scape).

This fork is licensed under the **GNU Affero General Public License v3.0 (AGPL-3.0)**.  
You are free to use, modify, and distribute this software under the terms of the AGPL-3.0.

All changes made in this fork are also licensed under the AGPL-3.0.  
For the full license text, see the [LICENSE](./LICENSE) file or visit [gnu.org](https://www.gnu.org/licenses/agpl-3.0.html).

***

### Prerequisites

Before setting up the project, make sure you have the following installed:

* **Java 11**: Download from [Oracle](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
  or [AdoptOpenJDK](https://adoptium.net/temurin/releases/?version=11).
* **IntelliJ IDEA**: Download from [JetBrains](https://www.jetbrains.com/idea/download/).

> *Windows users:* Enable **Developer Mode** in Windows settings before proceeding.

---

### Fork & Clone

1. Fork the repository on GitLab to your own account.
2. Clone **your forked repository** to your local machine:

```bash
git clone <your-fork-ssh-or-https-url>
```

3. Navigate to the cloned directory:

```bash
cd <your-project-folder>
```

---

### Import Project in IntelliJ

1. Open IntelliJ IDEA.
2. Select `File` > `Open...`.
3. Navigate to the folder where you cloned the repository (project root).
4. Click `OK` to open the project.
5. IntelliJ should detect the `pom.xml` file and import the Maven project automatically.
6. Set **Project SDK** to Java 11 or higher if not set automatically.

---

### Setup Git & SSH

* Generate a new SSH key if you don't have one:

```bash
ssh-keygen -t ed25519 -C "<your-email-or-comment>"
```

* Add your SSH public key to your GitLab account.
* Configure your Git identity:

```bash
git config --global user.name "user"
git config --global user.email "your_email@example.com"
```

---

### Build Project

From the root of the project directory, run:

```bash
mvn clean install
```

This will compile the project and package all necessary files.

---

### Run Project

Run the server/client using the Maven exec plugin:

```bash
mvn exec:java -f pom.xml
```

> Tip: If you want to run from IntelliJ, right-click `pom.xml` > `Run 'exec:java'`.

---

### Contributing

We welcome contributions! Please follow these steps:

1. Fork the repository.
2. Create a feature branch:

```bash
git checkout -b feature/my-feature
```

3. Make your changes and commit:

```bash
git commit -am "text"
```

4. Push your branch and open a merge request.

---

### Troubleshooting

* **Java version mismatch**: Ensure `java -version` returns **11** or higher.
* **Maven issues**: Run `mvn -version` to verify Maven is installed correctly.
* **IDE errors**: Reimport the Maven project or invalidate IntelliJ caches (`File > Invalidate Caches / Restart`).
* **SSH issues**: Make sure your public key is correctly added to GitLab.

---

### License

This project is licensed under the **AGPL-3.0**. See the [LICENSE](./LICENSE) file
or [gnu.org](https://www.gnu.org/licenses/agpl-3.0.html) for details.
