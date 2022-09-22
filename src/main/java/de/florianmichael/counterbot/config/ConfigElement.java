package de.florianmichael.counterbot.config;

import com.google.gson.JsonObject;

public class ConfigElement<T> {
    private final String name;
    private final T defaultValue;

    private T value;

    public ConfigElement(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;

        this.setValue(this.defaultValue);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T get() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    public void load(final JsonObject node) {
        if (this.defaultValue instanceof String)
            this.setValue((T) node.get(this.name).getAsString());
        else if (this.defaultValue instanceof Number)
            this.setValue((T) node.get(this.name).getAsNumber());
        else if (this.defaultValue instanceof Boolean) {
            final Boolean boolValue = node.get(this.name).getAsBoolean();

            this.setValue((T) boolValue);
        } else
            throw new RuntimeException("T isn't a valid Json value");
    }

    public void save(final JsonObject node) {
        if (this.value instanceof String stringValue)
            node.addProperty(this.name, stringValue);
        else if (this.value instanceof Number numberValue)
            node.addProperty(this.name, numberValue);
        else if (this.value instanceof Boolean booleanValue)
            node.addProperty(this.name, booleanValue);
        else
            throw new RuntimeException("T isn't a valid Json value");
    }
}
