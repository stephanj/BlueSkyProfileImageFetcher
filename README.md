# BlueSky Profile Image Fetcher

A Java application that fetches profile images from BlueSky social network using their API. This project demonstrates how to interact with BlueSky's API securely and efficiently using modern Java features.

## 🚀 Features

- Fetch profile images from BlueSky accounts
- Modern Java 23 implementation using HTTP/2
- Secure credential management using environment variables
- Efficient HTTP client with connection pooling
- Robust error handling and logging

## 📋 Prerequisites

- Java 23 or higher
- Maven 3.8+ or Gradle 8.0+
- A BlueSky account with an App Password
- Git (for version control)

## 🛠️ Installation

1. Clone the repository:

```bash
git clone https://github.com/yourusername/bluesky-profile-image-fetcher.git
cd bluesky-profile-image-fetcher
```

2. Install dependencies:
```bash
mvn install
```

3. Set up environment variables:
```bash
# Copy the example environment file
cp .env.example .env

# Edit .env with your credentials
nano .env
```

## ⚙️ Configuration

### Environment Variables

Create a `.env` file in the project root with the following variables:

```env
BLUESKY_USERNAME=your-username.bsky.social
BLUESKY_APP_PASSWORD=your-app-password
```

### Getting Your BlueSky App Password

1. Log in to your BlueSky account
2. Go to Settings
3. Select "App Passwords"
4. Create a new App Password
5. Copy the generated password to your `.env` file

## 📦 Dependencies

Add these dependencies to your `pom.xml`:

```xml
<dependencies>
    <!-- JSON Processing -->
    <dependency>
        <groupId>org.json</groupId>
        <artifactId>json</artifactId>
        <version>20231013</version>
    </dependency>

    <!-- Environment Variable Management -->
    <dependency>
        <groupId>io.github.cdimascio</groupId>
        <artifactId>dotenv-java</artifactId>
        <version>3.0.0</version>
    </dependency>
</dependencies>
```

## 🔨 Usage

### Basic Usage

```java
import com.yourpackage.com.devoxx.BlueSkyProfileImageFetcher;

public class Example {
    public static void main(String[] args) {
        try {
            String handle = "user.bsky.social";
            String imageUrl = com.devoxx.BlueSkyProfileImageFetcher.getProfileImage(handle);
            
            if (imageUrl != null) {
                System.out.println("Profile image URL: " + imageUrl);
            } else {
                System.out.println("No profile image found");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

### Advanced Usage

```java
import com.yourpackage.com.devoxx.BlueSkyConfig;
import com.yourpackage.com.devoxx.BlueSkyProfileImageFetcher;

public class AdvancedExample {
    public static void main(String[] args) {
        try {
            // Verify configuration
            com.devoxx.BlueSkyConfig config = com.devoxx.BlueSkyConfig.getInstance();
            System.out.println("Using account: " + config.getUsername());

            // Fetch and verify image URL
            String handle = "user.bsky.social";
            String imageUrl = com.devoxx.BlueSkyProfileImageFetcher.getProfileImage(handle);
            
            if (imageUrl != null) {
                // Process the image URL
                // Example: Download the image, verify it, etc.
            }
        } catch (IllegalStateException e) {
            System.err.println("Configuration error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

## 🔒 Security Considerations

- Never commit your `.env` file to version control
- Use App Passwords instead of your main account password
- Regularly rotate your App Passwords
- Keep your dependencies updated
- Monitor API rate limits

## 🔍 Error Handling

The application handles several types of errors:

- Configuration errors (missing environment variables)
- Network errors (connection timeouts, API issues)
- Authentication errors (invalid credentials)
- Rate limiting errors
- Invalid handle errors

## Testing

To run the tests, you need to set up your BlueSky credentials:

1. Copy `.env.example` to `.env`
2. Fill in your BlueSky credentials in the `.env` file:

```bash
mvn test
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.

## Acknowledgments

- Thank You BlueSky for keeping the sky BlUE 🦋

## 📚 Additional Resources

- [BlueSky API Documentation](https://github.com/bluesky-social/atproto/tree/main/packages/api)
- [Java HTTP Client Guide](https://docs.oracle.com/en/java/javase/23/docs/api/java.net.http/java/net/http/HttpClient.html)
- [Environment Variables Best Practices](https://12factor.net/config)

## 🔄 Version History

- 1.0.0
    - Initial release
    - Basic profile image fetching
    - Environment variable support
