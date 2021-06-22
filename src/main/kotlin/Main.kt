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


val client = Client()
    .setEndpoint("https://demo.appwrite.io/v1")
    .setProject("608fa1dd20ef0")
    .setKey("YOUR_KEY")
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
        val res = database.createCollection(name = "Movies", read = arrayListOf("*"), write = arrayListOf(
            "*")
        , rules =
        arrayListOf(    mapOf(
                "label" to "Name",
                "key" to "name",
                "type" to "text",
                "default" to "Empty Name",
                "required" to true,
                "array" to false
            ),
            mapOf(
                "label" to "release_year",
                "key" to "release_year",
                "type" to "numeric",
                "default" to 1970,
                "required" to true,
                "array" to false
            )
        ))
        var jsonString = res.body?.string() ?: ""
        val je: JsonElement = JsonParser.parseString(jsonString)
        collectionId = je.asJsonObject.get("\$id").asString
        println(jsonString)
    } catch (e:AppwriteException) {
        println(e.message);
    }
}

suspend fun listCollection() {
    val database = Database(client);
    println("Running list collection API")
    try {
        val res = database.listCollections()
        var jsonString = res.body?.string() ?: ""
        val je: JsonElement = JsonParser.parseString(jsonString)
        val collection = je.asJsonObject.get("collections").asJsonArray[0]
        println(collection)
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
            data = mapOf("name" to "Spider Man", "release_year" to 1920),
            read = arrayListOf("*"),
            write = arrayListOf("*"))
        println(res.body?.string() ?: "")
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listDoc() {
    val database = Database(client)
    println("Running List Document API")
    try {
        val response = database.listDocuments(collectionId = collectionId);
        println(response.body?.string() ?: "")
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
            file = file, //multipart file
            read = arrayListOf("*"),
            write = arrayListOf(),
        )
        val jsonString = response.body?.string() ?: ""
        val je: JsonElement = JsonParser.parseString(jsonString)
        fileId = je.asJsonObject.get("\$id").asString
        println(jsonString)
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
            name = "test function", execute = arrayListOf(), env = "dart-2.12")
        var jsonString = res.body?.string() ?: ""
        val je: JsonElement = JsonParser.parseString(jsonString)
        functionId = je.asJsonObject.get("\$id").asString
        println(jsonString)
    } catch (e:AppwriteException) {
        println(e.message)
    }
}

suspend fun listFunctions() {
    val functions = Functions(client)
    println("Running List Functions API")
    try {
        val res = functions.list()
        println(res.body?.string() ?: "")
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
        val response = users.list(
        )
        var jsonString = response.body?.string() ?: ""
        val gson = GsonBuilder().setPrettyPrinting().create()
        val je: JsonElement = JsonParser.parseString(jsonString)
        println(gson.toJson(je))
    } catch (e: AppwriteException) {
        println(e)
    }
}

suspend fun createUser(email: String, password: String, name: String) {
    println("Running create user API")
    val users = Users(client)
    try {
        val response = users.create(
            email = "email@example.com",
            password = "password",
        )
        var jsonString = response.body?.string() ?: ""
        val gson = GsonBuilder().setPrettyPrinting().create()
        val je: JsonElement = JsonParser.parseString(jsonString)
        userId = je.asJsonObject.get("\$id").asString
        println(gson.toJson(je))
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
        var jsonString = response.body?.string() ?: ""
        val gson = GsonBuilder().setPrettyPrinting().create()
        val je: JsonElement = JsonParser.parseString(jsonString)
        println(gson.toJson(je))
    } catch (e: AppwriteException) {
        println(e)
    }
}
