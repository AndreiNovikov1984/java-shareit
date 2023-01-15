package ru.practicum.shareit.support;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Responce {
    private String error;

    public Responce(String error) {
        this.error = error;
    }
}

