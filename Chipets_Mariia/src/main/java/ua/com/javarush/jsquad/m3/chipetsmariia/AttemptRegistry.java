package ua.com.javarush.jsquad.m3.chipetsmariia;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class AttemptRegistry implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Map<String, Integer> attemptsByCandidate = new HashMap<>();

    public synchronized int countFor(String candidateName) {
        return attemptsByCandidate.getOrDefault(candidateName, 0);
    }

    public synchronized int beginAttempt(String candidateName) {
        if (candidateName == null || candidateName.isBlank()) {
            throw new IllegalArgumentException("Ім'я кандидата не може бути порожнім");
        }
        return attemptsByCandidate.merge(candidateName, 1, Integer::sum);
    }
}
