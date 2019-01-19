package com.cmb.domain.loader;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;

public class YamlLoader<T> {

    private Yaml yaml;

    public YamlLoader(Class<T> t) {

        final Constructor constructor = new Constructor(t);

        constructor.setPropertyUtils(new PropertyUtils() {
            @Override
            public Property getProperty(Class<? extends Object> type, String name) {
                if (name.indexOf('-') > -1) {
                    name = toCamelCase(name);
                }
                return super.getProperty(type, name);
            }
        });

        yaml = new Yaml(constructor);

    }

    public T parse(Optional<Path> manifests) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(manifests.get().toFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return (T) yaml.load(inputStream);
    }


    private String toCamelCase(final String init) {
        if (init == null) {
            return null;
        }

        final StringBuilder ret = new StringBuilder(init.length());

        final String[] words = init.split("-");
        ret.append(words[0]);

        for (int i = 1; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                ret.append(words[i].substring(0, 1).toUpperCase());
                ret.append(words[i].substring(1).toLowerCase());
            }
        }

        return ret.toString();
    }
}
