package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.dto.ItemDtoGate;
import ru.practicum.shareit.user.dto.UserDtoGate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class BookingDtoGate {
    public long id;
    @FutureOrPresent
    public LocalDateTime start;
    @Future
    public LocalDateTime end;
    public long itemId;
    public ItemDtoGate item;
    public UserDtoGate booker;
    public TypeStatusDtoGate status;


}
