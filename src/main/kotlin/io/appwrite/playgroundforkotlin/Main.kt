package io.appwrite.playgroundforkotlin

import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.extensions.toJson
import io.appwrite.services.Database
import io.appwrite.services.Functions
import io.appwrite.services.Storage
import io.appwrite.services.Users
import kotlinx.coroutines.delay
import java.io.File
import kotlin.system.exitProcess

val client = Client()
    .setEndpoint("http://192.168.4.23/v1")
    .setProject("test")
    .setKey("2d1670e9a828bd47d9b27356d56fadf7ceea4c815abbabe644eb3195b88f3b3b495a5f530785b70ef705d9f164f751cc2cf10546ed31b71c62eb151fd2bb96053917731dad2a84f439cb0628dcde59f1311c3ba2be0433d0b73ecb700cadaa07b215d810ecc9fb3c092e178b0dd4fa7bda5819ebbd1ab4917f949e3e936518e1")
    .setSelfSigned(true)

val database = Database(client)
val storage = Storage(client)
val functions = Functions(client)
val users = Users(client)

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

    createCollection()
    listCollections()
    createDocument()
    listDocuments()
    deleteDocument()
    deleteCollection()

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
        userId = "unique()",
        email,
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

suspend fun createCollection() {
    println("Running create collection API")

    val collection = database.createCollection(
        collectionId = "movies",
        name = "Movies",
        permission = "document",
        read = arrayListOf("role:all"),
        write = arrayListOf("role:all")
    )
    collectionId = collection.id
    println(collection.toJson())

    println("Running create string attribute")
    val str = database.createStringAttribute(
        collectionId = collectionId,
        key = "name",
        size = 255,
        required = true,
        default = "",
        array = false
    )
    println(str.toJson())

    println("Running create integer attribute")
    val int = database.createIntegerAttribute(
        collectionId = collectionId,
        key = "release_year",
        required = true,
        min = 0,
        max = 9999
    )
    println(int.toJson())

    println("Running create float attribute")
    val float = database.createFloatAttribute(
        collectionId = collectionId,
        key = "rating",
        required = true,
        min = 0.0,
        max = 99.99
    )
    println(float.toJson())

    println("Running create boolean attribute")
    val bool = database.createBooleanAttribute(
        collectionId = collectionId,
        key = "kids",
        required = true
    )
    println(bool.toJson())

    println("Running create email attribute")
    val email = database.createEmailAttribute(
        collectionId = collectionId,
        key = "email",
        required = false,
        default = ""
    )
    println(email.toJson())

    delay(3000)

    println("Running create index")
    val index = database.createIndex(
        collectionId = collectionId,
        key = "name_email_idx",
        type = "fulltext",
        attributes = listOf("name", "email")
    )
    println(index.toJson())
}

suspend fun listCollections() {
    println("Running list collection API")
    val collections = database.listCollections()
    println(collections.toJson())
}

suspend fun deleteCollection() {
    println("Running delete collection API")
    database.deleteCollection(collectionId)
    println("Collection Deleted")
}

suspend fun createDocument() {
    println("Running Add Document API")

    val document = database.createDocument(
        collectionId = collectionId,
        documentId = "unique()",
        data = mapOf(
            "name" to "Spider Man",
            "release_year" to 1920,
            "rating" to 98.5,
            "kids" to false
        ),
        read = listOf("role:all"),
        write = listOf("role:all")
    )
    documentId = document.id
    println(document.toJson())
}

suspend fun listDocuments() {
    println("Running List Document API")
    val documents = database.listDocuments(collectionId)
    println(documents.toJson())
}

suspend fun deleteDocument() {
    println("Running Delete Document API")
    database.deleteDocument(collectionId, documentId)
    println("Document Deleted")
}

suspend fun createFunction() {
    println("Running Create Function API")
    val function = functions.create(
        functionId = "unique()",
        name = "Test Function",
        execute = listOf("role:all"),
        runtime = "dart-2.14",
        vars = mapOf("ENV" to "value")
    )
    functionId = function.id
    println(function.toJson())
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

    val file = File("./nature.jpg")
    val storageFile = storage.createFile(
        bucketId = bucketId,
        fileId = "unique()",
        file = file,
        read = listOf("role:all")
    )
    fileId = storageFile.id
    println(storageFile.toJson())
}

suspend fun createBucket() {
    println("Running Create Bucket API")
    val bucket = storage.createBucket(
        bucketId = "unique()",
        name = "Name",
        permission = "bucket"
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
