import io.appwrite.Client
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Database
import io.appwrite.services.Functions
import io.appwrite.services.Storage
import io.appwrite.services.Users
import java.io.File

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
lateinit var userId: String
lateinit var functionId: String

suspend fun main() {
    createUser("kotlinplayground@appwrite.io", "user@123", "Kotlin Player")
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

    uploadFile()
    listFiles()
    deleteFile()
}

suspend fun createUser(email: String, password: String, name: String) {
    println("Running create user API")

    try {
        val response = users.create(
            userId = "unique()",
            email,
            password,
            name
        )
        userId = response.id
        println(response.toMap())
    } catch (e: AppwriteException) {
        println(e)
    }
}

suspend fun listUsers() {
    println("Running list users API")

    try {
        val response = users.list()
        println(response.users.first().toMap())
    } catch (e: AppwriteException) {
        println(e)
    }
}

suspend fun deleteUser() {
    println("Running delete user API")

    try {
        users.delete(userId)
        println("User deleted")
    } catch (e: AppwriteException) {
        println(e)
    }
}

suspend fun createCollection() {
    println("Running create collection API")

    try {
        val res = database.createCollection(
            collectionId = "movies",
            name = "Movies",
            permission = "document",
            read = arrayListOf("role:all"),
            write = arrayListOf("role:all")
        )

        collectionId = res.id

        var res2 = database.createStringAttribute(
            collectionId = collectionId,
            key = "name",
            size = 255,
            required = true,
            default = "",
            array = false
        )

        var res3 = database.createIntegerAttribute(
            collectionId = collectionId,
            key = "release_year",
            required = true,
            min = 0,
            max = 9999,
            array = false
        )

        var res4 = database.createFloatAttribute(
            collectionId = collectionId,
            key = "rating",
            required = true,
            min = 0.0,
            max = 99.99,
            array = false
        )

        var res5 = database.createBooleanAttribute(
            collectionId = collectionId,
            key = "kids",
            required = true,
            array = false
        )

        var res6 = database.createEmailAttribute(
            collectionId = collectionId,
            key = "email",
            required = false,
            default = "",
            array = false
        )

        var res7 = database.createIndex(
            collectionId = collectionId,
            key = "name_email_idx",
            type = "fulltext",
            attributes = listOf("name", "email")
        )

        println(res.toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listCollections() {
    println("Running list collection API")

    try {
        val res = database.listCollections()
        println(res.collections.first().toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun deleteCollection() {
    println("Running delete collection API")

    try {
        database.deleteCollection(collectionId)
        println("collection deleted")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun createDocument() {
    println("Running Add Document API")

    try {
        val res = database.createDocument(
            collectionId = collectionId,
            documentId = "unique()",
            data = mapOf(
                "name" to "Spider Man",
                "release_year" to 1920,
                "rating" to 99.5,
                "kids" to false
            ),
            read = listOf("role:all"),
            write = listOf("role:all")
        )

        documentId = res.id

        println(res.toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listDocuments() {
    println("Running List Document API")

    try {
        val response = database.listDocuments(collectionId)
        println(response.documents.first().toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun deleteDocument() {
    println("Running Delete Document API")

    try {
        database.deleteDocument(collectionId, documentId)
        println("Document deleted")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun createFunction() {
    println("Running Create Function API")
    try {
        val res = functions.create(
            functionId = "unique()",
            name = "Test Function",
            execute = listOf("role:all"),
            runtime = "dart-2.14"
        )
        functionId = res.id
        println(res.toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listFunctions() {
    println("Running List Functions API")

    try {
        val res = functions.list()
        println(res.functions.first().toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun deleteFunction() {
    println("Running Delete Function API")

    try {
        functions.delete(functionId)
        println("Function deleted")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun uploadFile() {
    println("Running Upload File API")

    val file = File("./nature.jpg")
    try {
        val response = storage.createFile(
            fileId = "unique()",
            file = file, // Multipart file
            read = listOf("role:all"),
            write = listOf(),
        )
        fileId = response.id
        println(response.toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listFiles() {
    println("Running List File API")

    try {
        val res = storage.listFiles()
        println(res.files.first().toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun deleteFile() {
    println("Running Delete File API")

    try {
        storage.deleteFile(fileId)
        println("File deleted")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}
