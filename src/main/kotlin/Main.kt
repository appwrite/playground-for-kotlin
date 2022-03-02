import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Database
import io.appwrite.services.Functions
import io.appwrite.services.Storage
import io.appwrite.services.Users
import kotlinx.coroutines.delay
import java.io.File
import kotlin.system.exitProcess

val client = Client()
    .setEndpoint("http://localhost/v1")
    .setProject("YOUR PROJECT ID")
    .setKey("YOUR API KEY")
    // .setJWT("jwt") // Enable this to authenticate with JWT created using client SDK

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
    uploadFile()
    listFiles()
    deleteFile()

    println("Ran playground successfully!")
    exitProcess(0)
}

suspend fun createUser(email: String, password: String, name: String) {
    println("Running create user API")

    val response = users.create(
        userId = "unique()",
        email,
        password,
        name
    )
    userId = response.id
    println(response.toMap())
}

suspend fun listUsers() {
    println("Running list users API")

    val response = users.list()
    println(response.users.first().toMap())
}

suspend fun deleteUser() {
    println("Running delete user API")

    users.delete(userId)
    println("User deleted")
}

suspend fun createCollection() {
    println("Running create collection API")

    val res = database.createCollection(
        collectionId = "movies",
        name = "Movies",
        permission = "document",
        read = arrayListOf("role:all"),
        write = arrayListOf("role:all")
    )

    collectionId = res.id
    println(res.toMap())
    println("\tRunning create string attribute")
    var res2 = database.createStringAttribute(
        collectionId = collectionId,
        key = "name",
        size = 255,
        required = true,
        default = "",
        array = false
    )

    println("\tRunning create integer attribute")
    var res3 = database.createIntegerAttribute(
        collectionId = collectionId,
        key = "release_year",
        required = true,
        min = 0,
        max = 9999
    )

    println("\tRunning create float attribute")
    var res4 = database.createFloatAttribute(
        collectionId = collectionId,
        key = "rating",
        required = true,
        min = 0.0,
        max = 99.99
    )

    println("\tRunning create boolean attribute")
    var res5 = database.createBooleanAttribute(
        collectionId = collectionId,
        key = "kids",
        required = true
    )

    println("\tRunning create email attribute")
    var res6 = database.createEmailAttribute(
        collectionId = collectionId,
        key = "email",
        required = false,
        default = ""
    )

    delay(3000)

    println("\tRunning create index")
    var res7 = database.createIndex(
        collectionId = collectionId,
        key = "name_email_idx",
        type = "fulltext",
        attributes = listOf("name", "email")
    )

    println(res.toMap())
}

suspend fun listCollections() {
    println("Running list collection API")

    val res = database.listCollections()
    println(res.collections.first().toMap())
}

suspend fun deleteCollection() {
    println("Running delete collection API")

    database.deleteCollection(collectionId)
    println("collection deleted")
}

suspend fun createDocument() {
    println("Running Add Document API")

    val res = database.createDocument(
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
    documentId = res.id
    println(res.toMap())
}

suspend fun listDocuments() {
    println("Running List Document API")

    val response = database.listDocuments(collectionId)
    println(response.documents.firstOrNull()?.toMap())
}

suspend fun deleteDocument() {
    println("Running Delete Document API")

    database.deleteDocument(collectionId, documentId)
    println("Document deleted")
}

suspend fun createFunction() {
    println("Running Create Function API")

    val res = functions.create(
        functionId = "unique()",
        name = "Test Function",
        execute = listOf("role:all"),
        runtime = "dart-2.14"
    )
    functionId = res.id
    println(res.toMap())
}

suspend fun listFunctions() {
    println("Running List Functions API")

    val res = functions.list()
    println(res.functions.first().toMap())
}

suspend fun deleteFunction() {
    println("Running Delete Function API")

    functions.delete(functionId)
    println("Function deleted")
}

suspend fun uploadFile() {
    println("Running Upload File API")

    val file = File("./nature.jpg")
    val response = storage.createFile(
        bucketId = bucketId,
        fileId = "unique()",
        file = file, // Multipart file
        read = listOf("role:all"),
        write = listOf(),
    )
    fileId = response.id
    println(response.toMap())
}

suspend fun createBucket() {
    println("Running Create Bucket API")

    val res = storage.createBucket("unique()", "Name", "bucket")
    bucketId = res.id
    println(res.toMap())
}

suspend fun listFiles() {
    println("Running List File API")

    val res = storage.listFiles(bucketId)
    println(res.files.first().toMap())
}

suspend fun deleteFile() {
    println("Running Delete File API")

    storage.deleteFile(bucketId, fileId)
    println("File deleted")
}
