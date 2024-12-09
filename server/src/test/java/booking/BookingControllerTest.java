package booking;

import YandexPracticium.ShareItServerApplication;
import YandexPracticium.booking.BookingController;
import YandexPracticium.booking.dto.BookingDto;
import YandexPracticium.booking.service.BookingService;
import YandexPracticium.enums.Statuses;
import YandexPracticium.item.dto.ItemDto;
import YandexPracticium.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;


import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
@ContextConfiguration(classes = ShareItServerApplication.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper mapper;

    private final String urlTemplate = "/bookings";
    private final String headerUserId = "X-Sharer-User-Id";

    private ItemDto makeItemDto(Long id, String name, String description, Boolean available, Long requestId) {
        ItemDto dto = new ItemDto();
        dto.setId(id);
        dto.setName(name);
        dto.setDescription(description);
        dto.setAvailable(available);
        dto.setRequestId(requestId);
        return dto;
    }

    private UserDto makeUserDto(Long id, String email, String name) {
        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(email);
        dto.setName(name);
        return dto;
    }

    private BookingDto makeBookingDto(Long id, LocalDateTime start, LocalDateTime end,
                                      Statuses status, UserDto booker, ItemDto item) {
        BookingDto dto = new BookingDto();
        dto.setId(id);
        dto.setStart(start);
        dto.setEnd(end);
        dto.setStatus(status);
        dto.setBooker(booker);
        dto.setItem(item);
        return dto;
    }

    @Test
    void createTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 20, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.create(anyLong(), any())).thenReturn(requestDto);

        mvc.perform(post(urlTemplate)
                        .content(mapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(headerUserId, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(requestDto.getId().intValue())))
                .andExpect(jsonPath("$.item.id").value(itemDto.getId().intValue()))
                .andExpect(jsonPath("$.booker.id").value(userDto.getId().intValue()))
                .andExpect(jsonPath("$.status").value(Statuses.APPROVED.toString()));
    }

    @Test
    void findBookingTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 20, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.findBooking(anyLong(), anyLong())).thenReturn(requestDto);

        mvc.perform(get(urlTemplate + "/" + requestDto.getId())
                        .header(headerUserId, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId().intValue()))
                .andExpect(jsonPath("$.item.id").value(itemDto.getId().intValue()))
                .andExpect(jsonPath("$.booker.id").value(userDto.getId().intValue()))
                .andExpect(jsonPath("$.status").value(Statuses.APPROVED.toString()));
    }

    @Test
    void findAllBookingsByUserTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");
        BookingDto requestDto1 = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 20, 30, 1), Statuses.APPROVED, userDto, itemDto);
        BookingDto requestDto2 = makeBookingDto(2L, LocalDateTime.of(2022, 7, 4, 19, 30, 1),
                LocalDateTime.of(2022, 7, 4, 20, 30, 1), Statuses.REJECTED, userDto, itemDto);

        List<BookingDto> newRequests = List.of(requestDto1, requestDto2);

        when(bookingService.findAllBookingsByUser(anyLong(), any())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate)
                        .header(headerUserId, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void findAllBookingsByOwnerItemsTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");
        BookingDto requestDto1 = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 20, 30, 1), Statuses.APPROVED, userDto, itemDto);
        BookingDto requestDto2 = makeBookingDto(2L, LocalDateTime.of(2022, 7, 4, 19, 30, 1),
                LocalDateTime.of(2022, 7, 4, 20, 30, 1), Statuses.REJECTED, userDto, itemDto);

        List<BookingDto> newRequests = List.of(requestDto1, requestDto2);

        when(bookingService.findAllBookingsByOwnerItems(anyLong(), any())).thenReturn(newRequests);

        mvc.perform(get(urlTemplate + "/owner")
                        .header(headerUserId, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void updateTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 20, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.update(anyLong(), any())).thenReturn(requestDto);

        mvc.perform(put(urlTemplate)
                        .content(mapper.writeValueAsString(requestDto))
                        .header(headerUserId, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId().intValue()))
                .andExpect(jsonPath("$.status").value(Statuses.APPROVED.toString()));
    }

    @Test
    void deleteTest() throws Exception {
        mvc.perform(delete(urlTemplate + "/1"))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).delete(1L);
    }

    @Test
    void approveBookingTest() throws Exception {
        ItemDto itemDto = makeItemDto(1L, "name", "description", Boolean.TRUE, 1L);
        UserDto userDto = makeUserDto(1L, "john.doe@mail.com", "John Doe");
        BookingDto requestDto = makeBookingDto(1L, LocalDateTime.of(2022, 7, 3, 19, 30, 1),
                LocalDateTime.of(2022, 7, 3, 20, 30, 1), Statuses.APPROVED, userDto, itemDto);

        when(bookingService.approveBooking(anyLong(), anyLong(), any())).thenReturn(requestDto);

        mvc.perform(patch(urlTemplate + "/1?approved=true")
                        .content(mapper.writeValueAsString(requestDto))
                        .header(headerUserId, 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestDto.getId().intValue()))
                .andExpect(jsonPath("$.status").value(Statuses.APPROVED.toString()));
    }
}
