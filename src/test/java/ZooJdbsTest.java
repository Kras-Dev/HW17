import database.Animal;
import database.CRUDUtils;
import database.DatabaseUtils;
import database.Places;
import database.jdbc.DatabaseConnection;
import io.qameta.allure.Story;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Story("Database JDBS")
@Tag("Database")
public class ZooJdbsTest {
    @BeforeAll
    static void init(){
        DatabaseUtils.createData();
    }

    @AfterAll
    static void tearDown(){
        DatabaseConnection.closeConnection();
    }
    /**
     * В таблице public.animal ровно 10 записей
     */

    @Test
    @DisplayName("Number of entities Test")
    void checkRowCountForAnimal() throws SQLException {
        int actualAnimalCountRow = CRUDUtils.getRowCountByTable("animal");
        assertEquals(10, actualAnimalCountRow);
    }

    static Stream<Animal> animalProvider() {
        List<Animal> animals = new ArrayList<>();
        for (int id = 1; id <= 10; id++) {
            Animal animal = new Animal();
            animal.setId(id);
            animal.setName("Sharik");
            animal.setAge(10);
            animal.setType(1);
            animal.setSex(1);
            animal.setPlace(1);
            animals.add(animal);
        }
        return animals.stream();
    }

    /**
     * В таблицу public.animal нельзя добавить строку с индексом от 1 до 10 включительно
     */
    @ParameterizedTest
    @MethodSource("animalProvider")
    @DisplayName("Cannot add an object with an existing ID Test")
    void insertIndexAnimal(Animal animal) {
        int countBefore = CRUDUtils.getAnimalCountRow();
        Throwable exception = assertThrows(Exception.class, () -> CRUDUtils.insertAnimalData(animal));
        assertThat(exception.getMessage()).contains("PRIMARY KEY ON PUBLIC.ANIMAL(ID)");
        int countAfter = CRUDUtils.getAnimalCountRow();
        assertEquals(countBefore, countAfter);
    }
    /**
     * В таблицу public.workman нельзя добавить строку с name = null
     */
    @Test
    @DisplayName("Cannot add a workman object with name = null Test")
    void insertNullToWorkman() {
        Assertions.assertFalse(CRUDUtils.insertWorkmanData());
        System.out.println(CRUDUtils.insertWorkmanData());
    }
    /**
     * Если в таблицу public.places добавить еще одну строку, то в ней будет 6 строк
     */
    @Test
    @DisplayName("Inserting a new row increases places count to 6 Test")
    void insertPlacesCountRow() throws SQLException {
        Places place = new Places(6, 1, 185, "Загон 1");
        CRUDUtils.getPlacesCountRow();
        System.out.println(CRUDUtils.insertPlacesData(place));
        Assertions.assertEquals(6, CRUDUtils.getPlacesCountRow());
    }
    /**
     * В таблице public.zoo всего три записи с name 'Центральный', 'Северный', 'Западный'
     */
    @Test
    @DisplayName("Verify the count of rows in zoo table and names Test")
    void countRowZoo() throws SQLException {
        List<String> expectedNames = Arrays.asList("Центральный", "Северный", "Западный");

        int actualZooCountRow = CRUDUtils.getRowCountByTable("zoo");
        Assertions.assertEquals(3, actualZooCountRow);

        List<String> actualNames = CRUDUtils.getZooNameData();

        assertThat(actualNames, containsInAnyOrder(expectedNames.toArray()));
    }
    /**
     * Обновление информации о животном
     */
    @Test
    @DisplayName("Update animal data by Id Test")
    void updateAnimalData() throws SQLException{
        Animal animal = new Animal(1,"New_name",5,1,1,1);
        CRUDUtils.updateAnimalData(animal);
        String query = "SELECT * FROM animal WHERE id=1";
        Animal updatedAnimal = CRUDUtils.getAnimalData(query).get(0);
        assertEquals("New_name", updatedAnimal.getName());
    }
    /**
     * Удаление информации о животном
     */
    @Test
    @DisplayName("Delete animal data by Id Test")
    void deleteAnimalData() throws SQLException {
        Animal animal = new Animal(12, "Elephant", 10, 2, 1, 2);
        CRUDUtils.insertAnimalData(animal);
        int countBefore = CRUDUtils.getAnimalCountRow();
        CRUDUtils.deleteAnimalData(12);
        int countAfter = CRUDUtils.getAnimalCountRow();
        assertEquals(countBefore-1, countAfter);
    }
}
