# Playground for Kotlin

Simple examples that help you get started with Appwrite + Kotlin (=❤️)

This is Appwrite server side integration with Kotlin. For Android integration please look at our [Android playground](https://github.com/appwrite/playground-for-android) and [Android SDK](https://github.com/appwrite/sdk-for-android)

### Work in progress

Appwrite playground is a simple way to explore the Appwrite API and Appwrite Kotlin SDK. Use the source code of this page to learn how to use different Appwrite Kotlin SDK features.

## Get Started
This playground doesn't include any Kotlin best practices, but rather intended to show some of the most simple examples and use cases of using the Appwrite API in your Kotlin application.

## System Requirements
* A system with Kotlin 1.4+ or Docker installed.
* An Appwrite instance.
* An Appwrite project created in the console.
* An Appwrite API key created in the console.

### Installation
1. Clone this repository.
2. `cd` into to repository.
3. Open the `src/main/kotlin/io/appwrite/playgroundforkotlin/Main.kt` file found in the cloned repository.
4. Copy your Project ID, endpoint and API key from Appwrite console into `Main.kt`
5. Run the playground:
   Kotlin:
   - Execute the command `sh gradlew run`
   Docker:
   - Execute the command `docker compose up`
6. You will see the JSON response in the console.

> When connecting to Appwrite from within Docker, localhost is the hostname for the container and not your local Appwrite instance. You should replace localhost with your private IP as the Appwrite endpoint's hostname. You can also use a service like ngrok to proxy the Appwrite API.

### API Covered in Playground:

- Database
    * Create Collection
    * List Collections
    * Add Document
    * List Documents
    * Delete Document
    * Delete Collection

- Storage
    * Create Bucket
    * List Buckets
    * Upload File
    * List Files
    * Delete File
    * Delete Bucket

- Users
    * Create User
    * List Users
    * Delete User

- Functions
    * Create Function
    * List Functions
    * Delete Function

## Contributing

All code contributions - including those of people having commit access - must go through a pull request and approved by a core developer before being merged. This is to ensure proper review of all the code.

We truly ❤️ pull requests! If you wish to help, you can learn more about how you can contribute to this project in the [contribution guide](https://github.com/appwrite/appwrite/blob/master/CONTRIBUTING.md).

## Security

For security issues, kindly email us [security@appwrite.io](mailto:security@appwrite.io) instead of posting a public issue in GitHub.

## Follow Us

Join our growing community around the world! Follow us on [Twitter](https://twitter.com/appwrite), [Facebook Page](https://www.facebook.com/appwrite.io), [Facebook Group](https://www.facebook.com/groups/appwrite.developers/) or join our [Discord Server](https://appwrite.io/discord) for more help, ideas and discussions.

