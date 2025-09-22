// Pacote

// Bibliotecas

import static io.restassured.RestAssured.given; 
import static org.hamcrest.Matchers.is;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import com.google.gson.Gson;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Ativa a ordenação
public class TestStore {
    static String ct = "application/json";
    static String uriStore = "https://petstore.swagger.io/v2/store/order";
    static int cadOrderId = 7;
    int cadOrderPetId = 968486301;
    int cadOrderQuantity = 1;
    String cadOrderShipDate = "2025-03-21T15:12:55.489+0000"; // Valor fixo para shipDate (com Z)
    String cadOrderStatus = "approved";
    Boolean cadOrderComplete = true;

    // Método para ler o arquivo JSON
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // Métodos de teste
    @Test @Order(1)
    public void testPostStore() throws IOException {
        // Configura
        String jsonBody = lerArquivoJson("src/test/resources/json/store1.json");

        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)
        .when()
            .post(uriStore)
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(cadOrderId))
            .body("petId", is(cadOrderPetId))
            .body("quantity", is(cadOrderQuantity))
            .body("shipDate", is(String.valueOf("2025-03-21T15:12:55.489+0000"))) // Normaliza a data antes de validar
            .body("status", is(cadOrderStatus))
            .body("complete", is(cadOrderComplete))
        ;
    }

    @Test @Order(2)
    public void testGetStore() {
        given()
            .contentType(ct)
            .log().all()
        .when()
            .get(uriStore + "/" + cadOrderId)
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(cadOrderId))
            .body("petId", is(cadOrderPetId))
            .body("quantity", is(cadOrderQuantity))
            .body("shipDate", is(String.valueOf("2025-03-21T15:12:55.489+0000"))) // Normaliza a data antes de validar
            .body("status", is(cadOrderStatus))
            .body("complete", is(cadOrderComplete))
        ;
    }
    @Test @Order(3)
    public void testDeleteStore(){
        //configura
        given()
            .contentType(ct)
            .log().all()
        .when()
            .delete(uriStore + "/" + cadOrderId)
        .then()
            .log().all()
            .statusCode(200)
            .body("code", is(200))
            .body("type", is("unknown"))
            .body("message", is(String.valueOf(cadOrderId)))
        ; //fim do given           
        }
             
            @ParameterizedTest
            @Order(4)
            @CsvFileSource(resources = "csv/storeMassa.csv", numLinesToSkip = 1, delimiter = ',')
            public void testPostStoreDDT(
                int id,
                int petId,
                int quantity,
                String shipDate,
                String status,
                boolean complete
            ) {
                // Criar a classe Store para receber os dados do CSV
                Store store = new Store(); // Instancia a classe Store
        
                // Atribuir valores aos campos da classe Store usando os métodos setters
                store.setId(id);
                store.setPetId(petId);
                store.setQuantity(quantity);
                store.setShipDate(shipDate);
                store.setStatus(status);
                store.setComplete(complete);
        
                // Criar um JSON para o Body a ser enviado a partir da classe Store e do CSV
                Gson gson = new Gson(); // Instancia a classe Gson como o objeto gson
                String jsonBody = gson.toJson(store);
        
                given()                // Dado que
                    .contentType(ct)   // o tipo do conteúdo é
                    .log().all()       // mostre tudo na ida
                    .body(jsonBody)    // envie o corpo da requisição
                .when()               // Quando
                    .post(uriStore)   // Chamamos o endpoint fazendo post
                .then()               // Então
                    .log().all()      // Mostre tudo na volta
                    .statusCode(200)  // Verifica o status code
                    .body("id", is(id)) // Valida o campo id
                    .body("petId", is(petId)) // Valida o campo petId
                    .body("quantity", is(quantity)) // Valida o campo quantity
                    .body("shipDate", is(shipDate)) // Valida o campo shipDate
                    .body("status", is(status)) // Valida o campo status
                    .body("complete", is(complete)) // Valida o campo complete
                ; // fim do given
            }
        }