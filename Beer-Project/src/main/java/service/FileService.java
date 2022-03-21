package service;

import beercatalog.Beer;

import java.util.List;

public interface FileService {
    List<Beer> readJsonFile(String path);
}
