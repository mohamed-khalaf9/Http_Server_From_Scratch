# Http_Server_From_Scratch
## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Future Features and Improvements](#future-features-and-improvements)
- [Endpoints Documentation](#endpoints-documentation)
- [Technical Details](#technical-details)
- [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)


## Overview

A multi-threaded HTTP server, built from scratch in Java, efficiently handles **concurrent client** requests while supporting modern web optimizations. It features **GZip compression** for reduced response sizes, **range requests** for partial content retrieval, and **ETag caching** to minimize redundant transfers. **Rate limiting** prevents abuse, **keep-alive connections** enhance efficiency, and **logging** provides detailed request tracking. Designed for performance and scalability, this lightweight server is ideal for learning and testing HTTP internals. 

## Features

### 🔗 HTTP Endpoints & Routing  
- **File Handling**: Supports **creating**, **retrieving**, and **updating** files via HTTP requests.  
- **Ping Endpoint**: `/ping` endpoint responds with "pong" for testing purposes.  
- **Router**: Efficiently routes requests to the correct handlers for structured request processing.  

### ⚡ Performance Optimizations  
- **GZip Compression**: Reduces **Requested resource size** using (`Accept-Encoding`) header , lowering **bandwidth usage** and improving **transfer time**, especially for large text-based content.  
- **Range Requests**: Enables partial content retrieval using (`Range`) header, reducing **transfer time** and **bandwidth consumption** by allowing clients to request only specific portions of a file instead of the entire content. Useful for streaming and resuming downloads.  
- **ETag Caching**: Implements entity tags (`ETag`) with `If-None-Match` headers to avoid redundant data transfers. This optimizes **bandwidth usage**, decreases **latency**, and improves **transfer speed** by ensuring unchanged files are not re-sent.  

### 🔒 Security & Traffic Control  
- **Rate Limiting**: Restricts the number of requests per client within a specified time frame, preventing abuse and ensuring fair resource distribution. This helps maintain **low latency** by avoiding server overload.  

### 📜 Logging & Monitoring  
- **Log4j2 Integration**: Provides detailed logging of requests and responses, aiding in debugging, monitoring, and analytics.
---

## Future Features and Improvements
- Authentication: Implement authentication mechanisms for secure access.
- CORS Support: Enable Cross-Origin Resource Sharing (CORS) for client-side integrations.
- Multiplexing: Improve request handling efficiency by implementing HTTP multiplexing.
- Unit and Integration Tests: Write comprehensive test cases to ensure application reliability.
- Exception Logging: Implement better error handling and structured logging for debugging.

---
## Endpoints Documentation 

   ###   **1. Create a File**  
 ```http
 POST /create/{fileName}
```
### **2. Get a File**  
```http
GET /file/{fileName}
```
### **3. Update a File**  
```http
UPDATE /update/{fileName}
body: contnet to append
```
### **4. Ping**   
```http
GET /ping
body: contnet to append
```
---

## Technical Details:

### **UML Class Diagram:**   [HTTP_SERVER.drawio (2).pdf](https://github.com/user-attachments/files/19511259/HTTP_SERVER.drawio.2.pdf)
![HTTP_SERVER drawio](https://github.com/user-attachments/assets/09e1405d-27b4-4db6-858d-2ab10e17fec6)



### **📂 Project Structure:** 

##### `src/main/java`
- **`Main.java`** - Entry point of the application, initializes the HTTP server.
- **`HttpServer.java`** - Manages server startup, executes client requests using `ExecutorService`.
- **`ClientHandler.java`** - Handles client requests, parses HTTP requests, applies rate limiting, and logs requests/responses.
- **`Handler.java`** - Defines request handlers for `GET`, `POST` (create), `UPDATE`, and `ping`.
- **`Router.java`** - Routes incoming requests to the appropriate handlers.
- **`HeadersDetector.java`** - Detects and processes headers like `Persistent-Connection`, `Range`, `Accept-Encoding`, and `If-None-Match`.
- **`HttpRequest.java`** - Represents an HTTP request.
- **`HttpResponse.java`** - Represents an HTTP response.
- **`HttpRequestLog.java`** - Stores structured request log data.
- **`HttpResponseLog.java`** - Stores structured response log data.
- **`LoggingService.java`** - Handles request and response logging using Log4j2 and Gson.
- **`RateLimiter.java`** - Implements rate limiting to control excessive requests from clients.
- **`ETagManager.java`** - Manages file ETags for caching and validation.
- **`GZipCompressor.java`** - Compresses responses using GZip to optimize bandwidth usage.
- **`RangeRequestsHandler.java`** - Handles `Range` requests for efficient partial content delivery.
- **`Utilities.java`** - Provides utility functions like timestamp formatting, processing time calculation, and response sending.

---


## Installation

### Prerequisites  
Before running the HTTP server, ensure you have the following installed:  

##### 1. Install Java (JDK 17 or later)  

###### Windows  
- Download the JDK from [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) or [Adoptium](https://adoptium.net/).  
- Run the installer and follow the setup instructions.  
- Verify installation:  
  ```sh
  java -version
  ```  

###### Linux (Ubuntu/Debian)  
```sh
sudo apt update  
sudo apt install openjdk-17-jdk -y  
java -version  
```  

###### macOS  
```sh
brew install openjdk@17  
java -version  
```  

##### 2. Install Apache Maven  

###### Windows  
- Download the latest Maven from the [official website](https://maven.apache.org/download.cgi).  
- Extract the downloaded ZIP file to a directory (e.g., `C:\Program Files\Apache\Maven`).  
- Add the `bin` directory to your system `PATH`:  
  - Open **System Properties** → **Advanced** → **Environment Variables**.  
  - Edit the `Path` variable and add:  
    ```sh
    C:\Program Files\Apache\Maven\bin
    ```
- Verify installation:  
  ```sh
  mvn -version
  ```  

###### Linux (Ubuntu/Debian)  
```sh
sudo apt update  
sudo apt install maven -y  
mvn -version  
```  

###### macOS (Using Homebrew)  
```sh
brew install maven  
mvn -version  
```  

##### 3. Install Git  

###### Windows  
- Download and install Git from [Git for Windows](https://git-scm.com/downloads).  
- Verify installation:  
  ```sh
  git --version
  ```  

###### Linux (Ubuntu/Debian)  
```sh
sudo apt update  
sudo apt install git -y  
git --version  
```  

###### macOS  
```sh
brew install git  
git --version  
```  

##### 4. Install curl  

###### Windows  
- `curl` is pre-installed on Windows 10 and later.  
- To verify installation:  
  ```sh
  curl --version
  ```  
- If not installed, download it from the [curl official website](https://curl.se/download.html) and add it to the `PATH`.  

###### Linux (Ubuntu/Debian)  
```sh
sudo apt update  
sudo apt install curl -y  
curl --version  
```  

###### macOS  
```sh
brew install curl  
curl --version  
```  

##### 5. Install ncat (Netcat)  

###### Windows  
- Download [Nmap](https://nmap.org/download.html).  
- Install it and add the installation path (e.g., `C:\Program Files (x86)\Nmap`) to the system `PATH`.  
- Verify installation:  
  ```sh
  ncat --version
  ```  

###### Linux (Ubuntu/Debian)  
```sh
sudo apt update  
sudo apt install netcat -y  
ncat --version  
```  

###### macOS  
```sh
brew install nmap  
ncat --version  
```  

---

##### Installation Steps  

Once the prerequisites are installed, follow these steps to set up and run the server:  

###### 1. Clone the repository  
```sh
git clone https://github.com/your-username/your-repo.git  
cd your-repo  
```  

###### 2. Build the project  
```sh
mvn clean package  
```  

###### 3. Run the server  
```sh
java -jar target/your-http-server.jar  
```
