package io.appwrite.playgroundforkotlin

import io.appwrite.Client
import io.appwrite.ID
import io.appwrite.Permission
import io.appwrite.Role
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.toJson
import io.appwrite.models.InputFile
import io.appwrite.services.*
import kotlinx.coroutines.delay
import java.io.File
import kotlin.system.exitProcess

val client = Client()
    .setEndpoint("YOUR_ENDPOINT")
    .setProject("YOUR_PROJECT_ID")
    .setKey("YOUR_API_KEY")

val databases = Databases(client)
val storage = Storage(client)
val functions = Functions(client)
val users = Users(client)

lateinit var databaseId: String
lateinit var collectionId: String
lateinit var documentId: String
lateinit var fileId: String
lateinit var bucketId: String
lateinit var userId: String
lateinit var functionId: String


suspend fun main() {
    createUser("${Math.random()}@appwrite.io", "user@123", "Kotlin Player")
    listUsers()
    deleteUser()

    createDatabase()
    createCollection()
    listCollections()
    createDocument()
    listDocuments()
    deleteDocument()
    deleteCollection()
    deleteDatabase()

    createFunction()
    listFunctions()
    deleteFunction()

    createBucket()
    listBuckets()
    uploadFile()
    listFiles()
    deleteFile()

    println("Ran playground successfully!")
    exitProcess(0)
}

suspend fun createUser(email: String, password: String, name: String) {
    println("Running create user API")
    val user = users.create(
        userId = ID.unique(),
        email,
        null,
        password,
        name
    )
    userId = user.id
    println(user.toJson())
}

suspend fun listUsers() {
    println("Running list users API")
    val users = users.list()
    println(users.toJson())
}

suspend fun deleteUser() {
    println("Running delete user API")
    users.delete(userId)
    println("User deleted")
}

suspend fun createDatabase() {
    println("Running create database API")
    val database = databases.create(ID.unique(), "Movies")
    databaseId = database.id
    println(database.toJson())
}

suspend fun deleteDatabase() {
    println("Running delete database API")
    databases.delete(databaseId)
    println("Database deleted")
}

suspend fun createCollection() {
    println("Running create collection API")

    val collection = databases.createCollection(
        databaseId,
        collectionId = "movies",
        name = "Movies",
        permissions = listOf(
            Permission.create(Role.users()),
            Permission.read(Role.users()),
            Permission.update(Role.users()),
            Permission.delete(Role.users()),
        ),
        documentSecurity = true,
    )
    collectionId = collection.id
    println(collection.toJson())

    println("Running create string attribute")
    val str = databases.createStringAttribute(
        databaseId,
        collectionId,
        key = "name",
        size = 255,
        required = true,
        default = "",
        array = false
    )
    println(str.toJson())

    println("Running create integer attribute")
    val int = databases.createIntegerAttribute(
        databaseId,
        collectionId,
        key = "release_year",
        required = true,
        min = 0,
        max = 9999
    )
    println(int.toJson())

    println("Running create float attribute")
    val float = databases.createFloatAttribute(
        databaseId,
        collectionId,
        key = "rating",
        required = true,
        min = 0.0,
        max = 99.99
    )
    println(float.toJson())

    println("Running create boolean attribute")
    val bool = databases.createBooleanAttribute(
        databaseId,
        collectionId,
        key = "kids",
        required = true
    )
    println(bool.toJson())

    println("Running create email attribute")
    val email = databases.createEmailAttribute(
        databaseId,
        collectionId,
        key = "email",
        required = false,
        default = ""
    )
    println(email.toJson())

    delay(3000)

    println("Running create index")
    val index = databases.createIndex(
        databaseId,
        collectionId,
        key = "name_email_idx",
        type = "fulltext",
        attributes = listOf("name", "email")
    )
    println(index.toJson())
}

suspend fun listCollections() {
    println("Running list collection API")
    val collections = databases.listCollections(databaseId)
    println(collections.toJson())
}

suspend fun deleteCollection() {
    println("Running delete collection API")
    databases.deleteCollection(databaseId, collectionId)
    println("Collection Deleted")
}

suspend fun createDocument() {
    println("Running Add Document API")

    val document = databases.createDocument(
        databaseId,
        collectionId,
        documentId = ID.unique(),
        data = mapOf(
            "name" to "Spider Man",
            "release_year" to 1920,
            "rating" to 98.5,
            "kids" to false
        ),
        permissions = listOf(
            Permission.read(Role.users()),
            Permission.update(Role.users()),
            Permission.delete(Role.users()),
        )
    )
    documentId = document.id
    println(document.toJson())
}

suspend fun listDocuments() {
    println("Running List Document API")
    val documents = databases.listDocuments(
        databaseId,
        collectionId
    )
    println(documents.toJson())
}

suspend fun deleteDocument() {
    println("Running Delete Document API")
    databases.deleteDocument(
        databaseId,
        collectionId,
        documentId
    )
    println("Document Deleted")
}

suspend fun createFunction() {
    println("Running Create Function API")
    val function = functions.create(
        functionId = ID.unique(),
        name = "Test Function",
        execute = listOf(Role.any()),
        runtime = "php-8.0",
    )

    functionId = function.id

    val variable = functions.createVariable(
        functionId,
        key = "ENV",
        value = "value"
    )

    println(function.toJson())
    println(variable.toJson())
}

suspend fun listFunctions() {
    println("Running List Functions API")
    val functions = functions.list()
    println(functions.toJson())
}

suspend fun deleteFunction() {
    println("Running Delete Function API")
    functions.delete(functionId)
    println("Function Deleted")
}

suspend fun uploadFile() {
    println("Running Upload File API")

    val file = InputFile.fromPath("./nature.jpg")
    val storageFile = storage.createFile(
        bucketId = bucketId,
        fileId = ID.unique(),
        file = file,
        permissions = listOf(
            Permission.update(Role.any())
        )
    )
    fileId = storageFile.id
    println(storageFile.toJson())
}

suspend fun createBucket() {
    println("Running Create Bucket API")
    val bucket = storage.createBucket(
        bucketId = ID.unique(),
        name = "Name",
        permissions = listOf(
            Permission.read(Role.any()),
            Permission.create(Role.users()),
            Permission.update(Role.users()),
            Permission.delete(Role.users()),
        ),
        fileSecurity = true
    )
    bucketId = bucket.id
    println(bucket.toJson())
}

suspend fun listBuckets() {
    println("Running List Buckets API")
    val buckets = storage.listBuckets()
    println(buckets.toJson())
}

suspend fun listFiles() {
    println("Running List File API")
    val files = storage.listFiles(bucketId)
    println(files.toJson())
}

suspend fun deleteFile() {
    println("Running Delete File API")
    storage.deleteFile(bucketId, fileId)
    println("File Deleted")
}
