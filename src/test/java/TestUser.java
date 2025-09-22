// Pacote 

// Bibliotecas
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import com.google.gson.Gson;

// Classe
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ativa a ordenação
public class TestUser {
    // Atributos fixos
    static String ct = "application/json";
    static String uriUser = "https://petstore.swagger.io/v2/user";

    static int cadUserId = 968486201;
    static String cadUserName = "ideias";
    static String cadUserFirstName = "platao";
    static String cadUserLastName = "ensinando";
    static String cadUserEmail = "platao@alunophilo.com";
    static String cadUserPassword = "cavernain";
    static String cadUserPhone = "62999558476";
    static int cadUserStatus = 0;

    // POST - Cria usuário
    @Test
    @Order(1)
    public void testPostUser() {
        String jsonBody = "{"
                + "\"id\": " + cadUserId + ","
                + "\"username\": \"" + cadUserName + "\","
                + "\"firstName\": \"" + cadUserFirstName + "\","
                + "\"lastName\": \"" + cadUserLastName + "\","
                + "\"email\": \"" + cadUserEmail + "\","
                + "\"password\": \"" + cadUserPassword + "\","
                + "\"phone\": \"" + cadUserPhone + "\","
                + "\"userStatus\": " + cadUserStatus
                + "}";

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .post(uriUser)
        .then()
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(cadUserId)));
    }

    // GET - Consulta usuário
    @Test
    @Order(2)
    public void testGetUser() {
        given()
            .contentType(ct)
            .log().all()
        .when()
            .get(uriUser + "/" + cadUserName)
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(cadUserId))
            .body("username", is(cadUserName))
            .body("firstName", is(cadUserFirstName))
            .body("lastName", is(cadUserLastName))
            .body("email", is(cadUserEmail))
            .body("password", is(cadUserPassword))
            .body("phone", is(cadUserPhone))
            .body("userStatus", is(cadUserStatus));
    }

    // PUT - Atualiza usuário
    @Test
    @Order(3)
    public void testPutUser() {
        String jsonBody = "{"
                + "\"id\": " + cadUserId + ","
                + "\"username\": \"" + cadUserName + "\","
                + "\"firstName\": \"novoNome\","
                + "\"lastName\": \"" + cadUserLastName + "\","
                + "\"email\": \"" + cadUserEmail + "\","
                + "\"password\": \"" + cadUserPassword + "\","
                + "\"phone\": \"" + cadUserPhone + "\","
                + "\"userStatus\": " + cadUserStatus
                + "}";

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .put(uriUser + "/" + cadUserName)
        .then()
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(cadUserId)));
    }

    // DELETE - Exclui usuário
    @Test
    @Order(4)
    public void testDeleteUser() {
        given()
            .contentType(ct)
            .log().all()
        .when()
            .delete(uriUser + "/" + cadUserName)
        .then()
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(cadUserName))); // API retorna o username
    }

    // POST DDT - Criação com massa de dados CSV
    @ParameterizedTest
    @Order(5)
    @CsvFileSource(resources = "/csv/userMassa.csv", numLinesToSkip = 1, delimiter = ',')
    public void testPostUserDDT(
        int cadUserId,
        String cadUserName,
        String cadUserFirstName,
        String cadUserLastName,
        String cadUserEmail,
        String cadUserPassword,
        String cadUserPhone,
        int cadUserStatus
    ) {
        User user = new User();
        user.setId(cadUserId);
        user.setUsername(cadUserName);
        user.setFirstName(cadUserFirstName);
        user.setLastName(cadUserLastName);
        user.setEmail(cadUserEmail);
        user.setPassword(cadUserPassword);
        user.setPhone(cadUserPhone);
        user.setUserStatus(cadUserStatus);

        Gson gson = new Gson();
        String jsonBody = gson.toJson(user);

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .post(uriUser)
        .then()
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(cadUserId)));
    }
}
