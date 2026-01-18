# Build and Run Instructions

## Prerequisites

Before building and running the Hotel Reservation System, ensure you have installed:

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify installation: `java -version` and `javac -version`

2. **Apache Maven 3.6 or higher**
   - Download from: https://maven.apache.org/download.cgi
   - Extract and add to PATH
   - Verify installation: `mvn --version`

3. **Git (for version control)**
   - Download from: https://git-scm.com/downloads
   - Verify installation: `git --version`

## Building the Project

### Using Maven

Navigate to the project root directory and execute:

```bash
mvn clean install
```

This command will:
- Clean any previous builds
- Compile all source files
- Run all unit tests
- Package the application

### Expected Output

After successful build, you should see:
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXX s
[INFO] Finished at: YYYY-MM-DDTHH:MM:SS
```

## Running Tests

### Run All Tests

```bash
mvn test
```

This will run all unit tests in the project. Expected output shows:
```
[INFO] Tests run: 156, Failures: 0, Errors: 0, Skipped: 0
```

### Run Specific Test Class

```bash
mvn test -Dtest=HotelTest
```

### Run Tests with Coverage Report

```bash
mvn test jacoco:report
```

The coverage report will be generated in: `target/site/jacoco/index.html`

## Running the Application

### Option 1: Using Maven Exec Plugin

```bash
mvn exec:java -Dexec.mainClass="com.hotel.HotelReservationApp"
```

### Option 2: Direct Execution

After building, execute the JAR file (if created):

```bash
java -jar target/HotelReservationSystem-1.0.0.jar
```

### Expected Output

The application will display:
- Hotel chain initialization
- Hotel and room creation
- Guest creation
- Reservation operations (creation, check-in, check-out, cancellation)
- Availability checking
- Query results
- Defensive programming demonstrations

## Project Structure

```
HotelReservationSystem/
├── .git/                    (Git repository)
├── .gitignore              (Git ignore file)
├── README.md               (Project documentation)
├── pom.xml                 (Maven configuration)
├── src/
│   ├── main/
│   │   └── java/com/hotel/
│   │       ├── HotelReservationApp.java      (Main entry point)
│   │       ├── domain/                       (Domain classes)
│   │       │   ├── Guest.java
│   │       │   ├── Hotel.java
│   │       │   ├── HotelChain.java
│   │       │   ├── Reservation.java
│   │       │   ├── ReservationManager.java
│   │       │   ├── Room.java
│   │       │   └── RoomType.java
│   │       └── util/                         (Utility/Value objects)
│   │           ├── Address.java
│   │           ├── CreditCard.java
│   │           ├── Identity.java
│   │           ├── Money.java
│   │           └── Name.java
│   └── test/
│       └── java/com/hotel/
│           ├── domain/                       (Domain tests)
│           │   ├── GuestTest.java
│           │   ├── HotelChainTest.java
│           │   ├── HotelTest.java
│           │   ├── ReservationManagerTest.java
│           │   ├── ReservationTest.java
│           │   ├── RoomTest.java
│           │   └── RoomTypeTest.java
│           └── util/                         (Utility tests)
│               └── MoneyTest.java
└── target/                  (Build output - generated)
```

## Troubleshooting

### Problem: Maven command not found

**Solution**: Ensure Maven is installed and added to PATH
```bash
# On Windows, add to System Environment Variables
# On macOS/Linux, add to ~/.bash_profile or ~/.bashrc
export PATH=$PATH:/path/to/maven/bin
```

### Problem: Java not found

**Solution**: Ensure JDK is installed and JAVA_HOME is set
```bash
# On Windows
set JAVA_HOME=C:\Program Files\Java\jdk-11.0.x

# On macOS/Linux
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-11.0.x.jdk/Contents/Home
```

### Problem: Tests fail during build

**Solution**: 
1. Ensure all dependencies are downloaded: `mvn dependency:resolve`
2. Check that Java version is 11 or higher
3. Review test output: `mvn test -X` for debug information

### Problem: Git commands fail

**Solution**: Ensure Git is installed and configured
```bash
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"
```

## Git Commands Reference

### View Commit History

```bash
# Show all commits with dates
git log --oneline --all

# Show detailed commit information
git log --pretty=format:"%h %ad %s" --date=short

# Show commits with author
git log --oneline --all --author="Developer"
```

### Current Repository Status

```bash
# Check current branch and status
git status

# View remote repositories (after GitHub setup)
git remote -v
```

### Create GitHub Repository

1. Go to GitHub.com and sign in
2. Click "New Repository"
3. Name: HotelReservationSystem
4. Add description: "A comprehensive hotel reservation system implementing UML design with clean code and comprehensive testing"
5. Click "Create Repository"
6. Follow GitHub instructions to push local repository:

```bash
git remote add origin https://github.com/YOUR_USERNAME/HotelReservationSystem.git
git branch -M main
git push -u origin main
```

## Performance Notes

- Full build with tests: ~30-45 seconds
- Unit tests: ~10-15 seconds
- Application startup: <1 second

## System Requirements

- Minimum: 512MB RAM, 100MB disk space
- Recommended: 2GB RAM, 500MB disk space

## IDE Integration

### IntelliJ IDEA
1. File → Open → Select project directory
2. Maven should auto-import
3. Right-click pom.xml → Maven → Reload Project

### Eclipse
1. File → Import → Maven → Existing Maven Projects
2. Browse to project directory
3. Click Finish

### VS Code
1. Install extension: Extension Pack for Java
2. Open project folder
3. Create Maven project configuration if needed

## Contact and Support

For issues or questions about the build process:
1. Review the README.md for detailed documentation
2. Check the pom.xml for Maven configuration
3. Review test classes for usage examples
4. Check Javadoc comments in domain classes

## Success Indicators

After successful build and test run, you should see:
- ✓ All 156+ unit tests passing
- ✓ No compilation errors
- ✓ Main application runs without exceptions
- ✓ Git history shows 5+ meaningful commits
- ✓ All classes properly structured and documented
