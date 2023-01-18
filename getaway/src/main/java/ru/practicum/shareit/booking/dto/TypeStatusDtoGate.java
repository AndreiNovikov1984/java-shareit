package ru.practicum.shareit.booking.dto;

public enum TypeStatusDtoGate {
    // Все
    ALL,
    // Текущие
    CURRENT,
    // Будущие
    FUTURE,
    // Завершенные
    PAST,
    // Подтвержденные
    APPROVED,
    // Отклоненные
    REJECTED,
    // Ожидающие подтверждения
    WAITING,
    // Отмененные
    CANCELED;
}
