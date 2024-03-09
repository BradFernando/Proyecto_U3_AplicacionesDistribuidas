package grupo2.pasteurizadora.back_pasteurizadora.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean // Esta anotación indica que el método devuelve un bean que debe ser gestionado por el contenedor de Spring
    public CachingConnectionFactory connectionFactory() {
        // Crea una nueva conexión a RabbitMQ en localhost con el usuario y contraseña "guest"
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost");
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");
        return cachingConnectionFactory;
    }

    @Bean // Esta anotación indica que el método devuelve un bean que debe ser gestionado por el contenedor de Spring
    public RabbitTemplate rabbitTemplate() {
        // Crea un nuevo template de RabbitMQ
        return new RabbitTemplate(connectionFactory());
    }

    @Bean // Esta anotación indica que el método devuelve un bean que debe ser gestionado por el contenedor de Spring
    public Queue myQueue1() {
        return new Queue("CLIENTE_QUEUE");
    }

    @Bean // Esta anotación indica que el método devuelve un bean que debe ser gestionado por el contenedor de Spring
    public Queue myQueue2() {
        return new Queue("PEDIDO_QUEUE");
    }

}
