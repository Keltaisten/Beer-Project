package service;

import beercatalog.Beer;

import java.util.List;

public interface FileManager {
    List<Beer> readJsonFile(String path);
}
