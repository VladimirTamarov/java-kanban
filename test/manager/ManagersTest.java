package manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void getDefault() {

        Assertions.assertNotNull(Managers.getDefault(), "Менеджер не проинициализирован");
    }

    @Test
    void getDefaultHistory() {
        Assertions.assertNotNull(Managers.getDefaultHistory(), "Менеджер истории не проинициализирован");
    }
}