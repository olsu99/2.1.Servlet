package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

// Stub
public class PostRepository {
    private final Map<Long, Post> concurrentRepository = new ConcurrentHashMap<>();
    private final AtomicLong atomicCounter = new AtomicLong();

    public List<Post> all() {
        return new ArrayList<>(concurrentRepository.values());
    }

    public Post getById(long id) {
        if (!concurrentRepository.containsKey(id)) {
            throw new NotFoundException();
        } else {
            return concurrentRepository.get(id);
        }
    }

    public Post save(Post post) {
        long counter = atomicCounter.get();
        if (post.getId() == 0) {
            concurrentRepository.put(counter, post);
            post.setId(counter);
            atomicCounter.incrementAndGet();
        } else if (post.getId() != 0 && concurrentRepository.containsKey(post.getId())) {
            concurrentRepository.replace(post.getId(), post);
        } else {
            throw new NotFoundException();
        }
        return post;
    }

    public void removeById(long id) {
        if (concurrentRepository.containsKey(id)) {
            concurrentRepository.remove(id);
        } else {
            throw new NotFoundException();
        }
    }
}
