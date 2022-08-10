package bg.softuni.kickboxing.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

@Configuration
public class ApplicationBeanConfiguration {

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    public PrintWriter printWriter() throws FileNotFoundException {
        return new PrintWriter("logg.txt");
    }
}
