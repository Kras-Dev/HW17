package extensions;

import allureSteps.AllureSteps;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllureExtensions implements AfterTestExecutionCallback {
    AllureSteps allureSteps = new AllureSteps();

    // Метод, вызываемый после завершения выполнения каждого теста
    @Override
    public void afterTestExecution(ExtensionContext context) {
        // Проверяем, было ли исключение во время выполнения теста
        if (context.getExecutionException().isPresent())
            // Если исключение было, делаем снимок экрана и добавляем его в отчет Allure
            allureSteps.captureScreenshotSpoiler();
    }
}
