package cz.upce.fei.nnptp.em.nnptp.energymonitor.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import cz.upce.fei.nnptp.em.nnptp.energymonitor.State;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class Persistence {
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    public static void saveState(State state, File file) throws IOException {
        createObjectMapper().writeValue(file, state);
    }

    public static State loadState(File file) throws IOException {
        return createObjectMapper().readValue(file, State.class);
    }
}
