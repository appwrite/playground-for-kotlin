import io.appwrite.Client
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import io.appwrite.exceptions.AppwriteException
import io.appwrite.services.Database
import io.appwrite.services.Functions
import io.appwrite.services.Storage
import io.appwrite.services.Users
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.math.min


val client = Client()
    .setEndpoint("https://localhost/v1")
    .setProject("playgrounds")
    .setKey("YOUR API KEY")
    // .setJWT("jwt") // Enable this to authenticate with JWT created using client SDK

lateinit var collectionId: String
lateinit var fileId: String
lateinit var userId: String
lateinit var functionId: String

suspend fun main() {
    runBlocking {
        createUser("kotlinplayground@appwrite.io", "user@123", "Kotlin Player")
        createCollection()
        uploadFile()
        createFunction()
    }
    listUsers()
    deleteUser()
    listCollection()
    addDoc()
    listDoc()
    deleteCollection()
    deleteFile()

    listFunctions()
    deleteFunction()
}

suspend fun createCollection() {
    val database = Database(client)
    println("Running create collection API")
    try {
        val res = database.createCollection(collectionId = "movies", name = "Movies", permission = "document", read = arrayListOf("role:all"), write = arrayListOf(
            "role:all"))
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
        println(res.toMap())
    } catch (e:AppwriteException) {
        println(e.message);
    }
}

suspend fun listCollection() {
    val database = Database(client);
    println("Running list collection API")
    try {
        val res = database.listCollections()
        println(res.collections.first().toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun deleteCollection() {
    val database = Database(client)
    println("Running delete collection API")
    try {
        database.deleteCollection(collectionId = collectionId)
        println("collection deleted")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun addDoc() {
    val database = Database(client);
    println("Running Add Document API")
    try {
        val res = database.createDocument(
            collectionId = collectionId,
            documentId = "unique()",
            data = mapOf("name" to "Spider Man", "release_year" to 1920),
            read = arrayListOf("role:all"),
            write = arrayListOf("role:all"))
        println(res.toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listDoc() {
    val database = Database(client)
    println("Running List Document API")
    try {
        val response = database.listDocuments(collectionId = collectionId);
        println(response.documents.first().toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun uploadFile() {
    val storage = Storage(client);
    println("Running Upload File API")
    val file =
        File("./nature.jpg")
    try {
        val response = storage.createFile(
            fileId = "unique()",
            file = file, //multipart file
            read = arrayListOf("*"),
            write = arrayListOf(),
        )
        fileId = response.id
        println(response.toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun deleteFile() {
    val storage = Storage(client)
    println("Running Delete File API")
    try {
        storage.deleteFile(fileId = fileId)
        println("File deleted")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun createFunction() {
    val functions = Functions(client)
    println("Running Create Function API")
    try {
        val res = functions.create(
            functionId = "unique()",
            name = "test function", execute = arrayListOf(), runtime = "dart-2.12")
        functionId = res.id
        println(res.toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listFunctions() {
    val functions = Functions(client)
    println("Running List Functions API")
    try {
        val res = functions.list()
        println(res.functions.first().toMap())
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun deleteFunction() {
    val functions = Functions(client);
    println("Running Delete Function API")
    try {
        functions.delete(functionId = functionId)
        println("Function deleted")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listUsers() {
    println("Running list users API")
    val users = Users(client)
    try {
        val response = users.list()
        println(response.users.first().toMap())
    } catch (e: AppwriteException) {
        println(e)
    }
}

suspend fun createUser(email: String, password: String, name: String) {
    println("Running create user API")
    val users = Users(client)
    try {
        val response = users.create(
            userId = "unique()",
            email = "email@example.com",
            password = "password",
        )
        userId = response.id
        println(response.toMap())
    } catch (e: AppwriteException) {
        println(e)
    }
}

suspend fun deleteUser() {
    var users = Users(client)
    println("Running delete user API")
    try {
        val response = users.delete(
            userId = userId        )
        println("user deleted")
    } catch (e: AppwriteException) {
        println(e)
    }
}
