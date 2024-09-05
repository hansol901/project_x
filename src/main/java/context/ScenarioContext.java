package context;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ScenarioContext {

    private final Map<String, Object> scenarioContext;

    public ScenarioContext() {
        scenarioContext = new HashMap<>();
    }

    public void save(Key key, Object value) {
        scenarioContext.put(key.toString(), value);
    }

    public Object get(Key key) {
        return scenarioContext.get(key.toString());
    }

    public Boolean contains(Key key) {
        return scenarioContext.containsKey(key.toString());
    }

    public void clear() {
        scenarioContext.clear();
    }
}