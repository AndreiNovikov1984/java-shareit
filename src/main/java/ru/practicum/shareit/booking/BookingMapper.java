package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingLastNextDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.UserMapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class, TypeStatusMapper.class})
public interface BookingMapper {

    @Mapping(target = "itemId", ignore = true)
    @Mapping(source = "start", target = "start", qualifiedByName = "convertTimestampToLocalDate")
    @Mapping(source = "end", target = "end", qualifiedByName = "convertTimestampToLocalDate")
    BookingDto convertBookingToDto(Booking booking);             // метод преобразования Booking в BookingDto

    @Mapping(source = "start", target = "start", qualifiedByName = "convertLocalDateToTimestamp")
    @Mapping(source = "end", target = "end", qualifiedByName = "convertLocalDateToTimestamp")
    Booking convertDtoToBooking(BookingDto bookingDto);             // метод преобразования BookingDto в Booking

    @Mapping(target = "bookerId", source = "booker.id")
    @Mapping(source = "start", target = "start", qualifiedByName = "convertTimestampToLocalDate")
    @Mapping(source = "end", target = "end", qualifiedByName = "convertTimestampToLocalDate")
    BookingLastNextDto convertBookingToLNDto(Booking booking);  // метод преобразования Booking в BookingLastNextDto

    @Named("convertLocalDateToTimestamp")
    default Timestamp convertLocalDateToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    @Named("convertTimestampToLocalDate")
    default LocalDateTime convertTimestampToLocalDate(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }
}
