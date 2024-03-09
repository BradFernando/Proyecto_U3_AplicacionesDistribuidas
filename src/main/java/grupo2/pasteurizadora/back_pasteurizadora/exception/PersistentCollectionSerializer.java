package grupo2.pasteurizadora.back_pasteurizadora.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Collection;

public class PersistentCollectionSerializer extends JsonSerializer<Collection<?>> {

    @Override
    public void serialize(Collection<?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeObject(value);
    }
}