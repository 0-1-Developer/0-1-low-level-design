package com.example.state.persistent;

import java.io.*;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class StateStore {
    private final Path storageDir;
    private final Map<String, StateSnapshot> inMemoryCache = new ConcurrentHashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public StateStore(String storageDirectory) {
        this.storageDir = Paths.get(storageDirectory);
        try {
            Files.createDirectories(storageDir);
        } catch (IOException e) {
            System.err.println("Failed to create storage directory: " + e.getMessage());
        }
    }
    
    public void saveSnapshot(StateSnapshot snapshot) {
        lock.writeLock().lock();
        try {
            String key = snapshot.getWorkflowId() + "_" + snapshot.getVersion();
            inMemoryCache.put(key, snapshot);
            
            Path filePath = storageDir.resolve(key + ".state");
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(filePath.toFile()))) {
                oos.writeObject(snapshot);
                System.out.println("Saved state snapshot: " + key);
            } catch (IOException e) {
                System.err.println("Failed to persist snapshot: " + e.getMessage());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public StateSnapshot loadSnapshot(String workflowId, int version) {
        lock.readLock().lock();
        try {
            String key = workflowId + "_" + version;
            
            StateSnapshot cached = inMemoryCache.get(key);
            if (cached != null) {
                System.out.println("Loaded from cache: " + key);
                return cached;
            }
            
            Path filePath = storageDir.resolve(key + ".state");
            if (Files.exists(filePath)) {
                try (ObjectInputStream ois = new ObjectInputStream(
                        new FileInputStream(filePath.toFile()))) {
                    StateSnapshot snapshot = (StateSnapshot) ois.readObject();
                    inMemoryCache.put(key, snapshot);
                    System.out.println("Loaded from disk: " + key);
                    return snapshot;
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Failed to load snapshot: " + e.getMessage());
                }
            }
            
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public StateSnapshot loadLatest(String workflowId) {
        lock.readLock().lock();
        try {
            final int[] maxVersion = {-1};
            final StateSnapshot[] latest = {null};
            
            try {
                Files.list(storageDir)
                    .filter(path -> path.getFileName().toString().startsWith(workflowId + "_"))
                    .forEach(path -> {
                        String fileName = path.getFileName().toString();
                        String versionStr = fileName.substring(
                            fileName.lastIndexOf('_') + 1, 
                            fileName.lastIndexOf('.')
                        );
                        try {
                            int version = Integer.parseInt(versionStr);
                            if (version > maxVersion[0]) {
                                maxVersion[0] = version;
                                StateSnapshot snapshot = loadSnapshot(workflowId, version);
                                if (snapshot != null) {
                                    latest[0] = snapshot;
                                    inMemoryCache.put(workflowId + "_" + version, snapshot);
                                }
                            }
                        } catch (NumberFormatException e) {
                        }
                    });
            } catch (IOException e) {
                System.err.println("Error listing snapshots: " + e.getMessage());
            }
            
            for (Map.Entry<String, StateSnapshot> entry : inMemoryCache.entrySet()) {
                if (entry.getKey().startsWith(workflowId + "_")) {
                    StateSnapshot snapshot = entry.getValue();
                    if (snapshot.getVersion() > maxVersion[0]) {
                        maxVersion[0] = snapshot.getVersion();
                        latest[0] = snapshot;
                    }
                }
            }
            
            return latest[0];
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<StateSnapshot> getHistory(String workflowId) {
        List<StateSnapshot> history = new ArrayList<>();
        lock.readLock().lock();
        try {
            Files.list(storageDir)
                .filter(path -> path.getFileName().toString().startsWith(workflowId + "_"))
                .sorted()
                .forEach(path -> {
                    String fileName = path.getFileName().toString();
                    String versionStr = fileName.substring(
                        fileName.lastIndexOf('_') + 1,
                        fileName.lastIndexOf('.')
                    );
                    try {
                        int version = Integer.parseInt(versionStr);
                        StateSnapshot snapshot = loadSnapshot(workflowId, version);
                        if (snapshot != null) {
                            history.add(snapshot);
                        }
                    } catch (NumberFormatException e) {
                    }
                });
        } catch (IOException e) {
            System.err.println("Error getting history: " + e.getMessage());
        } finally {
            lock.readLock().unlock();
        }
        return history;
    }
    
    public void cleanup(String workflowId, int keepVersions) {
        lock.writeLock().lock();
        try {
            List<Path> files = new ArrayList<>();
            Files.list(storageDir)
                .filter(path -> path.getFileName().toString().startsWith(workflowId + "_"))
                .sorted(Comparator.reverseOrder())
                .skip(keepVersions)
                .forEach(files::add);
            
            for (Path file : files) {
                try {
                    Files.delete(file);
                    String key = file.getFileName().toString().replace(".state", "");
                    inMemoryCache.remove(key);
                    System.out.println("Cleaned up old snapshot: " + key);
                } catch (IOException e) {
                    System.err.println("Failed to delete: " + file);
                }
            }
        } catch (IOException e) {
            System.err.println("Cleanup error: " + e.getMessage());
        } finally {
            lock.writeLock().unlock();
        }
    }
}