package ru.practicum.shareit.booking.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemDtoGate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class BookingLastNextDtoGate {
    public long id;
    @FutureOrPresent
    public LocalDateTime start;
    @Future
    public LocalDateTime end;
    public long itemId;
    public ItemDtoGate item;
    public long bookerId;
    public TypeStatusDtoGate status;
}
