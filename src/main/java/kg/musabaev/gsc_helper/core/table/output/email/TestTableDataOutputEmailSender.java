package kg.musabaev.gsc_helper.core.table.output.email;

import kg.musabaev.gsc_helper.api.table.output.OutputProcessorConfig;
import kg.musabaev.gsc_helper.api.table.output.TableDataOutputProcessor;

import java.util.Objects;

/**
 * Тест
 */
public class TestTableDataOutputEmailSender implements TableDataOutputProcessor {

    private final Config config;

    public TestTableDataOutputEmailSender(Config config) {
        this.config = config;
    }

    @Override
    public void process(byte[] data) {
        // отправляет байтовый массив в указанную почту
    }

    @Override
    public OutputProcessorConfig getConfig() {
        return config;
    }

    public static class Config implements OutputProcessorConfig {

        private final String emailTo;

        public Config(String emailTo) {
            this.emailTo = emailTo;
        }

        public String emailTo() {
            return emailTo;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "emailTo='" + emailTo + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Config that = (Config) o;
            return Objects.equals(emailTo, that.emailTo);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(emailTo);
        }
    }
}
